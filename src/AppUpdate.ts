import { NativeEventEmitter, NativeModules } from 'react-native';

import type { Spec } from 'src/codegen/NativeAppUpdate';

import type { OnDownloadProgressEvent } from './types';

const isTurboModuleEnabled = global.__turboModuleProxy != null;

const AppUpdateModule: Spec = isTurboModuleEnabled
    ? require('./codegen/NativeAppUpdate').default
    : NativeModules.AppUpdate;

const AppUpdate = {
    onDownloadProgress(callback: (event: OnDownloadProgressEvent) => void) {
        const eventEmitter = new NativeEventEmitter(AppUpdateModule);

        const onDownloadProgressListener = eventEmitter.addListener(
            'onDownloadProgress',
            callback,
        );

        return onDownloadProgressListener.remove;
    },
    getVersionCode(): Promise<number> {
        return AppUpdateModule.getVersionCode();
    },
    downloadApp(apkUrl: string) {
        AppUpdateModule.updateApp(apkUrl);
    },
    installApp(apkFileName: string): Promise<null> {
        return AppUpdateModule.installApp(apkFileName);
    },
};

export { AppUpdate };
