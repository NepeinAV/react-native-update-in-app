import { DownloadStatus } from '../types';
import { AppUpdate } from '../AppUpdate';
import AppUpdateNativeModule from '../AppUpdateNativeModule';

jest.mock('../AppUpdateNativeModule');

describe('AppUpdate', () => {
    beforeEach(() => {
        jest.resetAllMocks();
    });

    test('should call native installApp method correctly', () => {
        AppUpdate.installApp('app-update.apk');

        expect(AppUpdateNativeModule.installApp).toBeCalledTimes(1);
        expect(AppUpdateNativeModule.installApp).toBeCalledWith(
            'app-update.apk',
        );
    });

    test('should call native downloadApp method correctly', () => {
        AppUpdate.downloadApp('https://cdn.com/app-update.apk');

        expect(AppUpdateNativeModule.downloadApp).toBeCalledTimes(1);
        expect(AppUpdateNativeModule.downloadApp).toBeCalledWith(
            'https://cdn.com/app-update.apk',
        );
    });

    test('should call native getVersionCode method correctly', async () => {
        await AppUpdate.getVersionCode();

        expect(AppUpdateNativeModule.getVersionCode).toBeCalledTimes(1);
        expect(AppUpdateNativeModule.getVersionCode).toBeCalledWith();
    });

    test('should correctly return versionCode', async () => {
        AppUpdateNativeModule.getVersionCode = jest.fn(async () => 1002);

        const versionCode = await AppUpdate.getVersionCode();

        expect(versionCode).toBe(1002);
    });

    test('should receive onDownloadProgress events', async () => {
        const callbackSpy = jest.fn();

        const unsubscribe = AppUpdate.onDownloadProgress(callbackSpy);

        AppUpdate.eventEmitter.emit('onDownloadProgress', {
            status: DownloadStatus.Start,
            progress: 0,
        });

        expect(callbackSpy).toBeCalledTimes(1);
        expect(callbackSpy).toBeCalledWith({
            status: DownloadStatus.Start,
            progress: 0,
        });

        unsubscribe();
    });

    test('should not receive onDownloadProgress events after unsubscribe', async () => {
        const callbackSpy = jest.fn();

        const unsubscribe = AppUpdate.onDownloadProgress(callbackSpy);

        unsubscribe();

        AppUpdate.eventEmitter.emit('onDownloadProgress', {
            status: DownloadStatus.Start,
            progress: 0,
        });

        expect(callbackSpy).toBeCalledTimes(0);
    });

    test('should ignore any event other than onDownloadProgress', async () => {
        const callbackSpy = jest.fn();

        const unsubscribe = AppUpdate.onDownloadProgress(callbackSpy);

        AppUpdate.eventEmitter.emit('onAnotherEvent', {
            status: DownloadStatus.Start,
            progress: 0,
        });

        expect(callbackSpy).toBeCalledTimes(0);

        unsubscribe();
    });
});
