package com.reactnativeinappupdate.reactnative.broadcast;

import android.content.Intent;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import com.reactnativeinappupdate.reactnative.AppUpdateModule;

public class DownloadStartIntentHandler {
    public Intent create() {
        Intent downloadStartIntent = new Intent(AppUpdateModule.BROADCAST_ACTION);

        downloadStartIntent.putExtra("eventName", "downloadStart");
        downloadStartIntent.putExtra("status", "start");

        return downloadStartIntent;
    }

    public WritableMap getParams(Intent intent) {
        WritableMap params = Arguments.createMap();
        params.putString("status", "start");
        params.putInt("progress", 0);

        return params;
    }
}
