package com.reactnativeinappupdate.update;

import android.app.Activity;
import android.content.Intent;

import com.reactnativeinappupdate.Constants;

public class DownloadUtils {
    public static void downloadApp(final Activity context, final String downloadUrl) {
        Intent intent = new Intent(context.getApplicationContext(), DownloadService.class);
        intent.putExtra(Constants.APK_DOWNLOAD_URL, downloadUrl);

        context.startService(intent);
    }
}
