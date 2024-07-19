package com.zdsmaps;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ZdsMapsView extends MapView implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
  private static final float ZOOM_LVL = 14f;
  private static final LatLng MAP_LOCATION_FALLBACK = new LatLng(-12.0694283, -76.9374733);

  public GoogleMap googleMap;
  public ReadableArray markers;
  private final ZdsMapsViewManager manager;
  private final ThemedReactContext context;

  public ZdsMapsView(@NonNull ThemedReactContext context, @Nullable GoogleMapOptions options, ZdsMapsViewManager manager) {
    super(context, options);

    this.manager = manager;
    this.context = context;

    MapsInitializer.initialize(context, MapsInitializer.Renderer.LATEST, renderer -> Log.d("Callback fn", renderer.toString()));

    super.onCreate(null);
    super.onResume();
    super.getMapAsync(this);
  }

  @Override
  public void onMapReady(@NonNull GoogleMap googleMap) {
    this.googleMap = googleMap;

    LatLng cameraPosition = MAP_LOCATION_FALLBACK;

    if (markers != null && markers.size() > 0) {
      cameraPosition = renderMarkers();
    }

    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(cameraPosition, ZOOM_LVL);

    // Move camera to center of markers or default position
    this.googleMap.animateCamera(cameraUpdate);

    // Better styles for map
    applyMapStyle(R.raw.map_style);
  }

  // Look for styles or print error log
  private void applyMapStyle(int styleResourceId) {
    try {
      MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this.context, styleResourceId);

      this.googleMap.setMapStyle(mapStyleOptions);
    } catch (Resources.NotFoundException e) {
      Log.e("MapStyle", "Map style resource not found: " + styleResourceId, e);
    } catch (Exception e) {
      Log.e("MapStyle", "Error applying map style", e);
    }
  }

  private LatLng renderMarkers() {
    List<LatLng> locations = new ArrayList<>();

    for (int index = 0; index < markers.size(); index++) {
      ReadableType readableType = markers.getType(index);

      if (readableType == ReadableType.Map) {
        ReadableMap transformedMarkers = markers.getMap(index);

        String id = transformedMarkers.getString("id");
        String title = transformedMarkers.getString("title");
        Double longitude = transformedMarkers.getDouble("longitude");
        Double latitude = transformedMarkers.getDouble("latitude");

        LatLng latLng = new LatLng(latitude, longitude);

        locations.add(latLng);

        Marker marker = this.googleMap.addMarker(new MarkerOptions().position(latLng).title(title));
        marker.setTag(id);
      }
    }

    // Setup Listeners whenever a User clicks on a Marker
    this.googleMap.setOnMarkerClickListener(this);

    // Return center from locations
    return this.getCenterFromLocationsList(locations);
  }

  private LatLng getCenterFromLocationsList(List<LatLng> locations) {
    if (locations == null || locations.isEmpty()) {
      throw new IllegalArgumentException("The locations list must not be null or empty");
    }

    double totalLatitude = 0;
    double totalLongitude = 0;

    for (LatLng location : locations) {
      totalLatitude += location.latitude;
      totalLongitude += location.longitude;
    }

    int numberOfLocations = locations.size();
    double averageLatitude = totalLatitude / numberOfLocations;
    double averageLongitude = totalLongitude / numberOfLocations;

    return new LatLng(averageLatitude, averageLongitude);
  }

  @Override
  public boolean onMarkerClick(@NonNull Marker marker) {
    String idFromRN = getIdFromMarker(marker);

    if (idFromRN != null) {
      WritableMap event = createEvent(idFromRN);
      sendEventToReactNative("onMarkerTouch", event);
    } else {
      // Handle the case where the marker tag is null, if necessary
      Log.w("onMarkerClick", "Marker tag is null");
    }

    return false;
  }

  private String getIdFromMarker(@NonNull Marker marker) {
    Object tag = marker.getTag();

    if (tag instanceof String) {
      return (String) tag;
    }

    return null;
  }

  private WritableMap createEvent(String message) {
    WritableMap event = Arguments.createMap();
    event.putString("message", message);
    return event;
  }

  private void sendEventToReactNative(String eventName, WritableMap event) {
    if (this.context != null) {
      this.context
        .getJSModule(RCTEventEmitter.class)
        .receiveEvent(getId(), eventName, event);
    } else {
      Log.e("sendEventToReactNative", "ReactContext is null");
    }
  }
}
