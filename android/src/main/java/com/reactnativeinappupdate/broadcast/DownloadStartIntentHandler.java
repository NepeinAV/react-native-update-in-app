package com.reactnativeinappupdate.broadcast;

import android.content.Intent;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import com.reactnativeinappupdate.AppUpdateModule;

public class DownloadStartIntentHandler {
    public Intent create(int progress) {
        Intent downloadStartIntent = new Intent(AppUpdateModule.BROADCAST_ACTION);

        downloadStartIntent.putExtra("eventName", "downloadStart");
        downloadStartIntent.putExtra("status", "start");
        downloadStartIntent.putExtra("status", progress);

        return downloadStartIntent;
    }

    public WritableMap getParams(Intent intent) {
        int progress = intent.getIntExtra("progress", 0);

        WritableMap params = Arguments.createMap();
        params.putString("status", "start");
        params.putInt("progress", progress);

        return params;
    }
}
