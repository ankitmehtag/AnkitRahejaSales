package com.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.sp.R;
import com.utils.Utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadingAsync extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "Download Task";
    private Context context;
    private String file_name;
    private String downloadUrl;
    private File outputFile = null;
    private long total = 0;
    private int contentLength = 0;
    private File mFileDirectory;
    private IDownloadAttachment iDownloadAttachment;
    private String deliverMessage = null;
    private ProgressDialog dialog;

    public DownloadingAsync(Context context, String file_name, String downloadUrl, File directory) {
        this.context = context;
        this.file_name = file_name;
        this.downloadUrl = downloadUrl;
        this.mFileDirectory = directory;
        iDownloadAttachment = (IDownloadAttachment) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Downloading started, please wait.");
        dialog.show();
    }

    @Override
    protected void onPostExecute(Void result) {

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (outputFile != null) {
            iDownloadAttachment.isAttachmentDownloaded(deliverMessage);
            Utils.openAttachment(context,downloadUrl,outputFile);
        }
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        try {
            String savingPath = null;
            URL url = new URL(downloadUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            //connect
            urlConnection.connect();
            switch (urlConnection.getResponseCode()) {
                case HttpURLConnection.HTTP_OK: {
                    contentLength = urlConnection.getContentLength();

                    //If File is not present create directory
                    if (!mFileDirectory.exists()) {
                        mFileDirectory.mkdir();
                    }
                   /* File parent = directory.getParentFile();
                    if (parent != null)
                        parent.mkdirs();*/

                    savingPath = mFileDirectory + File.separator + file_name;
                    outputFile = new File(mFileDirectory, file_name);

                    /**
                     * CHECK INCOMPLETE DOWNLOADED FILE
                     * IF TRUE THE DELETE THE INCOMPLETE FILE AND START DOWNLOAD SAME FILE AGAIN
                     */
                    //**********************************************
                    File file = new File(savingPath);
                    if (file.exists()) {
                        if (file.length() != contentLength) {
                            if (outputFile.exists()) {
                                outputFile.delete();
                                System.out.println(context.getString(R.string.incomplete_download_file_deleted));
                            }
                        } else {
                            contentLength = 0;
                            deliverMessage = context.getString(R.string.file_already_downloaded);
                            return null;
                        }
                    }
                    //**********************************************
                    try {
                        outputFile.createNewFile();
                    } catch (IOException e) {
                        Log.d(TAG, "IOException " + e.getMessage());
                    }
                    try {
                        InputStream inputStream = urlConnection.getInputStream();
                        FileOutputStream outputStream = new FileOutputStream(outputFile);
                        int bytesRead = -1;
                        byte[] buffer = new byte[4096];
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            total += bytesRead;
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        outputStream.flush();
                        outputStream.close();
                        inputStream.close();

                    } catch (OutOfMemoryError e) {
                        Log.d(TAG, "OutOfMemoryError " + e.getMessage());

                    }

                    if (total == contentLength) {
                        deliverMessage = context.getString(R.string.file_download_completed);
                    }
                }
            }
        } catch (MalformedURLException e) {
            Log.d(TAG, "MalformedURLException " + e);
        } catch (IOException e) {
            Log.d(TAG, "IOException " + e);
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(TAG, "Exception " + e);
        }
        return null;
    }

    public interface IDownloadAttachment {
        void isAttachmentDownloaded(String message);
    }
}
