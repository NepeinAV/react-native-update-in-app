package com.reactnativeinappupdate.reactnative.broadcast;

import android.content.Intent;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.reactnativeinappupdate.reactnative.AppUpdateModule;

public class DownloadErrorIntentHandler {
    public Intent create(Exception e) {
        Intent downloadErrorIntent = new Intent(AppUpdateModule.BROADCAST_ACTION);

        downloadErrorIntent.putExtra("eventName", "downloadError");
        downloadErrorIntent.putExtra("status", "error");
        downloadErrorIntent.putExtra("errorMessage", e.getMessage());

        return downloadErrorIntent;
    }

    public WritableMap getParams(Intent intent) {
        String errorMessage = intent.getStringExtra("errorMessage");

        WritableMap params = Arguments.createMap();
        params.putString("status", "error");
        params.putString("errorMessage", errorMessage);

        return params;
    }
}
