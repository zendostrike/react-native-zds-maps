package com.zdsmaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.android.gms.maps.GoogleMapOptions;

import java.util.Map;

public class ZdsMapsViewManager extends ViewGroupManager<ZdsMapsView> {
  public static final String REACT_CLASS = "ZdsMapsView";

  ReactApplicationContext reactContext;

  public ZdsMapsViewManager(ReactApplicationContext context) {
    this.reactContext = context;
  }

  @NonNull
  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @NonNull
  @Override
  protected ZdsMapsView createViewInstance(@NonNull ThemedReactContext themedReactContext) {
    GoogleMapOptions googleMapOptions = new GoogleMapOptions();

    return new ZdsMapsView(themedReactContext, googleMapOptions, this);
  }

  @ReactProp(name = "markers")
  public void setMarkers(ZdsMapsView view, @Nullable ReadableArray markers) {
    view.markers = markers;
  }

  @Override
  public Map getExportedCustomBubblingEventTypeConstants() {
    return MapBuilder.builder().put(
      "onMarkerTouch",
      MapBuilder.of(
        "phasedRegistrationNames",
        MapBuilder.of("bubbled", "onMarkerClick")
      )
    ).build();
  }
}
