package com.reactnativeinappupdate.update;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.reactnativeinappupdate.Constants;
import com.reactnativeinappupdate.reactnative.broadcast.DownloadEndIntentHandler;
import com.reactnativeinappupdate.reactnative.broadcast.DownloadErrorIntentHandler;
import com.reactnativeinappupdate.reactnative.broadcast.DownloadProgressIntentHandler;
import com.reactnativeinappupdate.reactnative.broadcast.DownloadStartIntentHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends IntentService {
    private static final int BUFFER_SIZE = 10 * 1024;
    private Context mContext;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = getApplicationContext();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String urlStr = intent.getStringExtra(Constants.APK_DOWNLOAD_URL);
        InputStream in = null;
        FileOutputStream out = null;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(false);
            urlConnection.setConnectTimeout(10 * 1000);
            urlConnection.setReadTimeout(10 * 1000);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");

            urlConnection.connect();

            long bytetotal = urlConnection.getContentLength();
            long bytesum = 0;
            int byteread = 0;

            File dir = StorageUtils.getApkDirectory(this);
            String apkName = urlStr.substring(urlStr.lastIndexOf("/") + 1, urlStr.length());
            File apkFile = new File(dir, apkName);

            in = urlConnection.getInputStream();
            out = new FileOutputStream(apkFile);
            byte[] buffer = new byte[BUFFER_SIZE];

            int oldProgress = 0;

            mContext.sendBroadcast(new DownloadStartIntentHandler().create());

            while ((byteread = in.read(buffer)) != -1) {
                bytesum += byteread;
                out.write(buffer, 0, byteread);

                int progress = (int) (bytesum * 100L / bytetotal);

                if (progress != oldProgress) {
                    mContext.sendBroadcast(new DownloadProgressIntentHandler().create(progress));
                }

                oldProgress = progress;
            }

            mContext.sendBroadcast(new DownloadEndIntentHandler().create(apkName));
        } catch (Exception e) {
            mContext.sendBroadcast(new DownloadErrorIntentHandler().create(e));
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {}
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {}
            }
        }
    }
}
