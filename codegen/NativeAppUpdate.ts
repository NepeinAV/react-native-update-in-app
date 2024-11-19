import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
    getVersionCode(): Promise<number>;
    downloadApp(apkUrl: string): void;
    installApp(apkFileName: string): Promise<null>;

    addListener(eventName: string): void;
    removeListeners(count: number): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('AppUpdate');
