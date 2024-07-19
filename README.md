# react-native-zds-maps

A Google Maps Native View for React Native

## Requirements

- You will need a GOOGLE MAPS API KEY. Go find yours at https://console.cloud.google.com.

- Configure your project with Java Version >= 11 <=17

## Installation

```sh
npm install react-native-zds-maps
```

## Usage

Go to your AndroidManifest.xml file and paste the following location permissions

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

Also put your GOOGLE MAPS API KEY in the manifest:

```xml
    </activity>

      <meta-data android:name="com.google.android.geo.API_KEY" android:value="myAPIKey" />

  </application>
</manifest>
```

After that you can import the package and use it.

```js
import { ZdsMapsView } from "react-native-zds-maps";

// ...

<ZdsMapsView style={{}} markers={[]} onMarkerClick={(event) => {}} />
```

| Props         | Type                  | Default   |
| ------------- | --------------------- | --------- |
| style         | ViewStyle             | undefined |
| markers       | any                   | undefined |
| onMarkerClick | ((args: any) => void) | undefined |

Markers will be typed in the following releases. For the moment you
can use the next structure:

```ts
interface Marker {
  id: string;
  title: string;
  latitude: number;
  longitude: number;
  imageSrc: string;
}
```

## Troubleshooting

If you can't see the map, consider applying Credentials restrictions to your API KEY on GCP.

You can also look at the Logcat and search for a warning message to find your API KEY and fingerprint. Copy them and set Android restrictions for your API KEY.

- Example of package name: com.testingmaplibrary
- Example of fingerprint: 5E:8F:16:06:2E:A3:CD:2C:4A:0D:54:78:76:BA:A6:F3:8C:AB:F6:23

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
