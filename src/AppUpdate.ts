import { NativeEventEmitter, NativeModules } from 'react-native';

import type { OnDownloadProgressEvent } from './types';

const { AppUpdate: AppUpdateNative } = NativeModules;

const AppUpdate = {
    onDownloadProgress(callback: (event: OnDownloadProgressEvent) => void) {
        const eventEmitter = new NativeEventEmitter(AppUpdateNative);

        const onDownloadProgressListener = eventEmitter.addListener(
            'onDownloadProgress',
            callback,
        );

        return onDownloadProgressListener.remove;
    },
    getVersionCode(): Promise<number> {
        return AppUpdateNative.getVersionCode();
    },
    updateApp(apkUrl: string) {
        AppUpdateNative.updateApp(apkUrl);
    },
    installApp(apkFileName: string): Promise<null> {
        return AppUpdateNative.installApp(apkFileName);
    },
};

export { AppUpdate };
