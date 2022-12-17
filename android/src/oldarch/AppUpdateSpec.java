package com.reactnativeinappupdate;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.Promise;

abstract class AppUpdateSpec extends ReactContextBaseJavaModule {
    AppUpdateSpec(ReactApplicationContext context) {
        super(context);
    }

    public abstract void downloadApp(String apkUrl);

    public abstract void getVersionCode(Promise promise);

    public abstract void installApp(String apkFileName, Promise promise);

    public abstract void addListener(String eventName);
    public abstract void removeListeners(double count);
}
