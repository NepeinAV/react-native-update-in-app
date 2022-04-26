package com.reactnativeinappupdate.update;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;

final public class StorageUtils {
    public static File getApkDirectory(@NonNull Context context) {
        File appCacheDir = context.getCacheDir();

        if (appCacheDir == null) {
            Log.w("StorageUtils", "Can't define system cache directory! The app should be re-installed.");
        }

        return appCacheDir;
    }
}
