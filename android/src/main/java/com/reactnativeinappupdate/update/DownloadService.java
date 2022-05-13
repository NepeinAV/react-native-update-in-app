package com.reactnativeinappupdate.update;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.reactnativeinappupdate.Constants;
import com.reactnativeinappupdate.broadcast.DownloadEndIntentHandler;
import com.reactnativeinappupdate.broadcast.DownloadErrorIntentHandler;
import com.reactnativeinappupdate.broadcast.DownloadProgressIntentHandler;
import com.reactnativeinappupdate.broadcast.DownloadStartIntentHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.SocketException;

public class DownloadService extends IntentService {
    private static final int BUFFER_SIZE = 10 * 1024;
    private Context mContext;

    private boolean isFirstConnection = true;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = getApplicationContext();
        isFirstConnection = true;

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String urlStr = intent.getStringExtra(Constants.APK_DOWNLOAD_URL);

        InputStream in = null;
        FileOutputStream out = null;

        try {
            while (downloadFile(urlStr, !isFirstConnection) == false) {
                isFirstConnection = false;
            }
        } catch (Exception e) {
            mContext.sendBroadcast(new DownloadErrorIntentHandler().create(e));
        }
    }

    private boolean downloadFile(String urlStr, boolean resumeDownload) throws Exception {
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

            long bytesum = 0;
            int byteread = 0;

            File dir = StorageUtils.getApkDirectory(this);
            String apkName = urlStr.substring(urlStr.lastIndexOf("/") + 1, urlStr.length());
            File apkFile = new File(dir, apkName);

            if (apkFile.exists() && resumeDownload) {
                long donwloadedBytesLength = apkFile.length();
                bytesum = donwloadedBytesLength;

                urlConnection.setRequestProperty("Range", "bytes=" + donwloadedBytesLength + "-");

                out = new FileOutputStream(apkFile, true);
            } else {
                out = new FileOutputStream(apkFile);
            }

            urlConnection.connect();

            long bytetotal = urlConnection.getContentLength() + bytesum;
            int status = urlConnection.getResponseCode();

            int oldProgress = (int) (bytesum * 100L / bytetotal);

            if (status != HttpStatusCodeConstants.REQUESTED_RANGE_NOT_SATISFIABLE) {
                in = urlConnection.getInputStream();
                byte[] buffer = new byte[BUFFER_SIZE];

                mContext.sendBroadcast(new DownloadStartIntentHandler().create(oldProgress));

                while ((byteread = in.read(buffer)) != -1) {
                    bytesum += byteread;
                    out.write(buffer, 0, byteread);

                    int progress = (int) (bytesum * 100L / bytetotal);

                    if (progress != oldProgress) {
                        mContext.sendBroadcast(new DownloadProgressIntentHandler().create(progress));
                    }

                    oldProgress = progress;
                }

            }

            mContext.sendBroadcast(new DownloadEndIntentHandler().create(apkName));

            return true;
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().contains("connection reset")) {
                return false;
            }
          
            throw e;
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
