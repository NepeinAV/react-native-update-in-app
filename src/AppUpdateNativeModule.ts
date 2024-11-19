import { NativeModules } from 'react-native';
import type { Spec } from '../codegen/NativeAppUpdate';

const isTurboModuleEnabled = global.__turboModuleProxy != null;

const AppUpdateNativeModule: Spec = isTurboModuleEnabled
    ? require('../codegen/NativeAppUpdate').default
    : NativeModules.AppUpdate;

export default AppUpdateNativeModule;
