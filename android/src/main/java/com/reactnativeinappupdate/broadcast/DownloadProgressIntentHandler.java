package com.reactnativeinappupdate.broadcast;

import android.content.Intent;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import com.reactnativeinappupdate.Constants;

public class DownloadProgressIntentHandler {
    public Intent create(int progress) {
        Intent downloadProgressIntent = new Intent(Constants.BROADCAST_ACTION);

        downloadProgressIntent.putExtra("eventName", "downloadProgress");
        downloadProgressIntent.putExtra("status", "downloading");
        downloadProgressIntent.putExtra("progress", progress);

        return downloadProgressIntent;
    }

    public WritableMap getParams(Intent intent) {
        int progress = intent.getIntExtra("progress", 0);

        WritableMap params = Arguments.createMap();
        params.putString("status", "downloading");
        params.putInt("progress", progress);

        return params;
    }
}
