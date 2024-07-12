package com.skt.skillup;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ImageUploader {

    private final Context context;
    private final String serverAddress;
    private final String username;
    private final String password;
    private final String remotePath;
    private final Bitmap bitmap;

    public ImageUploader(Context context, String serverAddress, String username, String password, String remotePath, Bitmap bitmap) {
        this.context = context;
        this.serverAddress = serverAddress;
        this.username = username;
        this.password = password;
        this.remotePath = remotePath;
        this.bitmap = bitmap;
    }

    public void uploadImage() {
        new UploadImageTask().execute();
    }

    public class UploadImageTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                FTPClient ftpClient = new FTPClient();
                ftpClient.connect(serverAddress);

                if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                    ftpClient.disconnect();
                    return false;
                }

                if (ftpClient.login(username, password)) {
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                    byte[] imageBytes = Base64.decode(encodedImage, Base64.DEFAULT);

                    ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);

                    if (ftpClient.storeFile(remotePath, inputStream)) {
                        return true;
                    } else {
                        Log.e("FTP-ERROR", "Failed to store file");
                    }
                }
            } catch (IOException e) {
                Log.e("FTP-ERROR", Objects.requireNonNull(e.getMessage()));
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
