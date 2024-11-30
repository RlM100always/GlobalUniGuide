package com.techtravelcoder.alluniversityinformations.pdf;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.books.BookCategoryActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader {

    public static void downloadFile(Context context, String fileUrl, String fileName, DownloadListener listener) {
        try {
            // Create a DownloadManager.Request
            Uri uri = Uri.parse(fileUrl);
            DownloadManager.Request request = new DownloadManager.Request(uri);

            // Set download preferences
            request.setTitle(fileName);
            request.setDescription("Downloading...");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            // Ensure the Downloads/Pintu directory exists
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Pintu");
            if (!directory.exists()) {
                directory.mkdirs();  // Create the directory if it doesn't exist
            }

            // Set the destination for the downloaded file
            File file = new File(directory, fileName);
            // Use setDestinationUri to specify the full path
            request.setDestinationUri(Uri.fromFile(file));

            // Allow download over mobile data and Wi-Fi, disable roaming
            request.setAllowedOverMetered(true);
            request.setAllowedOverRoaming(false); // Disable downloads during roaming

            // Enqueue the download
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            long downloadId = downloadManager.enqueue(request);

            // Monitor the download progress in a separate thread
            new Thread(() -> {
                boolean downloading = true;
                while (downloading) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor cursor = downloadManager.query(query);

                    if (cursor != null && cursor.moveToFirst()) {
                        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            downloading = false;

                            // Post download complete to the main thread
                            ((PDFShowActivity) context).runOnUiThread(() -> {
                                listener.onDownloadComplete(file);
                            });
                        } else if (status == DownloadManager.STATUS_FAILED) {
                            int reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
                            Log.e("DownloadManager", "Download failed, reason: " + reason);
                            downloading = false;

                            // Post download failed to the main thread
                            ((PDFShowActivity) context).runOnUiThread(() -> {
                                listener.onDownloadFailed(new Exception("Download failed: " + reason));
                            });
                        }

                        // Get download progress
                        int totalBytes = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        int downloadedBytes = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        if (totalBytes > 0) {
                            int progress = (int) (downloadedBytes * 100L / totalBytes);
                            // You can update the progress bar here if needed
                        }
                    }

                    if (cursor != null) {
                        cursor.close();
                    }

                    // Sleep briefly to avoid busy-waiting
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.e("DownloadTask", "Interrupted while sleeping", e);
                    }
                }
            }).start();
        } catch (Exception e) {
            Log.e("FileDownloader", "Error in downloading file", e);
            ((PDFShowActivity) context).runOnUiThread(() -> listener.onDownloadFailed(e));
        }
    }

    public interface DownloadListener {
        void onDownloadComplete(File file);
        void onDownloadFailed(Exception e);
    }


}
