package com.reactnativeinappupdate.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

public class ApkUtils {
    public static void installApk(Context context, File apkFile) throws IOException {
        Intent installAPKIntent = getApkInstallIntent(context, apkFile);

        context.startActivity(installAPKIntent);
    }

    public static Intent getApkInstallIntent(Context context, File apkFile) throws IOException {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".update.provider", apkFile);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = getApkUri(apkFile);
        }

        intent.setDataAndType(uri, "application/vnd.android.package-archive");

        return intent;
    }

    private static Uri getApkUri(File apkFile) throws IOException {
        String[] command = {"chmod", "777", apkFile.toString()};

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();

        Uri uri = Uri.fromFile(apkFile);

        return uri;
    }
}
