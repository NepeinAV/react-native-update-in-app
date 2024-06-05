package com.reactnativeinappupdate;

import static android.content.Context.RECEIVER_NOT_EXPORTED;
import static com.reactnativeinappupdate.Constants.BROADCAST_ACTION;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.File;
import java.io.IOException;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.reactnativeinappupdate.broadcast.DownloadEndIntentHandler;
import com.reactnativeinappupdate.broadcast.DownloadErrorIntentHandler;
import com.reactnativeinappupdate.broadcast.DownloadProgressIntentHandler;
import com.reactnativeinappupdate.broadcast.DownloadStartIntentHandler;
import com.reactnativeinappupdate.update.ApkUtils;
import com.reactnativeinappupdate.update.AppUtils;
import com.reactnativeinappupdate.update.StorageUtils;
import com.reactnativeinappupdate.update.DownloadUtils;

public class AppUpdateModule extends AppUpdateSpec {
    private final ReactApplicationContext reactContext;
    private BroadcastReceiver broadcastReceiver;
    private Activity activity;

    public static final String NAME = "AppUpdate";

    public AppUpdateModule(ReactApplicationContext reactContext) {
        super(reactContext);

        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void downloadApp(String apkUrl) {
        DownloadUtils.downloadApp(activity, apkUrl);
    }

    @ReactMethod
    public void getVersionCode(Promise promise) {
        try {
            int versionCode = AppUtils.getVersionCode(activity);

            promise.resolve(versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            promise.reject(e);
        }
    }

    @ReactMethod
    public void installApp(String apkFileName, Promise promise) {
        try {
            File apkFile = new File(StorageUtils.getApkDirectory(activity), apkFileName);

            ApkUtils.installApk(activity, apkFile);

            promise.resolve(null);
        } catch (IOException e) {
            promise.reject(e);
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    public void initialize() {
        super.initialize();

        activity = getCurrentActivity();

        broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String eventName = intent.getStringExtra("eventName");

                switch (eventName) {
                    case "downloadStart": {
                        sendEvent(
                            "onDownloadProgress",
                            new DownloadStartIntentHandler().getParams(intent)
                        );

                        break;
                    }

                    case "downloadProgress": {
                        sendEvent(
                            "onDownloadProgress",
                            new DownloadProgressIntentHandler().getParams(intent)
                        );

                        break;
                    }

                    case "downloadEnd": {
                        sendEvent(
                            "onDownloadProgress",
                            new DownloadEndIntentHandler().getParams(intent)
                        );

                        break;
                    }

                    case "downloadError": {
                        sendEvent(
                            "onDownloadProgress",
                            new DownloadErrorIntentHandler().getParams(intent)
                        );

                        break;
                    }

                    default:
                        break;
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            reactContext.registerReceiver(broadcastReceiver, intentFilter, RECEIVER_NOT_EXPORTED);
        } else {
            reactContext.registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    public void invalidate() {
        reactContext.unregisterReceiver(broadcastReceiver);
    }

    private void sendEvent(String eventName, WritableMap params) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
    }

    @ReactMethod
    public void addListener(String eventName) {}

    @ReactMethod
    public void removeListeners(double count) {}
}
