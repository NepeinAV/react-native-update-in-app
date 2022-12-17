package com.reactnativeinappupdate.broadcast;

import android.content.Intent;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import com.reactnativeinappupdate.Constants;

public class DownloadEndIntentHandler {
    public Intent create(String apkName) {
        Intent downloadEndIntent = new Intent(Constants.BROADCAST_ACTION);

        downloadEndIntent.putExtra("eventName", "downloadEnd");
        downloadEndIntent.putExtra("status", "end");
        downloadEndIntent.putExtra("apkFileName", apkName);

        return downloadEndIntent;
    }

    public WritableMap getParams(Intent intent) {
        String apkFileName = intent.getStringExtra("apkFileName");

        WritableMap params = Arguments.createMap();
        params.putString("status", "end");
        params.putString("apkFileName", apkFileName);

        return params;
    }
}
