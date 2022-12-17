package com.reactnativeinappupdate;

import com.facebook.react.bridge.ReactApplicationContext;

abstract class AppUpdateSpec extends NativeAppUpdateSpec {
    AppUpdateSpec(ReactApplicationContext context) {
        super(context);
    }
}
