import { StyleSheet, View } from 'react-native';
import { ZdsMapsView } from 'react-native-zds-maps';

import { exampleData } from './exampleData';

export default function App() {
  const handleMarkerTouch = (event: any) => {
    console.log(`${event.nativeEvent.message} touched.`);
  };

  return (
    <View style={styles.container}>
      <ZdsMapsView
        style={styles.map}
        markers={exampleData}
        onMarkerClick={handleMarkerTouch}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  map: {
    width: '100%',
    height: '100%',
  },
});
