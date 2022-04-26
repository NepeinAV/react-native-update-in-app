package com.reactnativeinappupdate.update;

import android.content.Context;
import android.content.pm.PackageManager;

public class AppUtils {
    public static int getVersionCode(Context mContext) throws PackageManager.NameNotFoundException {
        return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
    }
}
