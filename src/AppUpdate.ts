import { NativeEventEmitter } from 'react-native';

import AppUpdateNativeModule from './AppUpdateNativeModule';

import type { OnDownloadProgressEvent } from './types';

let appUpdateEventEmitter: NativeEventEmitter;

const AppUpdate = {
    onDownloadProgress(callback: (event: OnDownloadProgressEvent) => void) {
        const onDownloadProgressListener = AppUpdate.eventEmitter.addListener(
            'onDownloadProgress',
            callback,
        );

        return onDownloadProgressListener.remove;
    },
    getVersionCode(): Promise<number> {
        return AppUpdateNativeModule.getVersionCode();
    },
    downloadApp(apkUrl: string) {
        AppUpdateNativeModule.downloadApp(apkUrl);
    },
    installApp(apkFileName: string): Promise<null> {
        return AppUpdateNativeModule.installApp(apkFileName);
    },

    get eventEmitter() {
        if (!appUpdateEventEmitter) {
            appUpdateEventEmitter = new NativeEventEmitter(
                AppUpdateNativeModule,
            );
        }

        return appUpdateEventEmitter;
    },
};

export { AppUpdate };
