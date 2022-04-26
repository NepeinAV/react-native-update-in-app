package com.reactnativeinappupdate;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.Activity;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.IOException;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
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

public class AppUpdateModule extends ReactContextBaseJavaModule  {
    private final ReactApplicationContext reactContext;
    private BroadcastReceiver broadcastReceiver;
    private Activity activity;

    public final static String BROADCAST_ACTION = "AppUpdateAction";

    public AppUpdateModule(ReactApplicationContext reactContext) {
        super(reactContext);

        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "AppUpdate";
    }

    @ReactMethod
    public void updateApp(String apkUrl) {
        DownloadUtils.updateApp(activity, apkUrl);
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

        reactContext.registerReceiver(broadcastReceiver, intentFilter);
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
    public void removeListeners(Integer count) {}
}
