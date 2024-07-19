import {
  requireNativeComponent,
  UIManager,
  Platform,
  type ViewStyle,
} from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-zds-maps' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

interface ZdsMapsViewProps {
  style: ViewStyle;
  markers?: any;
  onMarkerClick?: (args: any) => void;
}

const ComponentName = 'ZdsMapsView';

export const ZdsMapsView =
  UIManager.getViewManagerConfig(ComponentName) != null
    ? requireNativeComponent<ZdsMapsViewProps>(ComponentName)
    : () => {
        throw new Error(LINKING_ERROR);
      };
