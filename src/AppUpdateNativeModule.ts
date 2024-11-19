import { NativeModules, Platform, TurboModuleRegistry } from 'react-native';
import type { Spec } from '../codegen/NativeAppUpdate';

const isTurboModuleEnabled = global.__turboModuleProxy != null;

const AppUpdateNativeModule: Spec = isTurboModuleEnabled
    ? Platform.OS === 'android'
        ? TurboModuleRegistry.getEnforcing<Spec>('AppUpdate')
        : null
    : NativeModules.AppUpdate;

export default AppUpdateNativeModule;
