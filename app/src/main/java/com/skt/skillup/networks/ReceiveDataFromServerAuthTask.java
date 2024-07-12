package com.skt.skillup.networks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReceiveDataFromServerAuthTask extends AsyncTask<String, Void, String> {

    private static final String TAG = ReceiveDataFromServerAuthTask.class.getSimpleName();

    private OnDataReceivedListener onDataReceivedListener;
    private String userID;
    private String userAndroidID;

    public ReceiveDataFromServerAuthTask(String userID, String userAndroidID, OnDataReceivedListener onDataReceivedListener) {
        this.userID = userID;
        this.userAndroidID = userAndroidID;
        this.onDataReceivedListener = onDataReceivedListener;
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString = params[0];

        try {
            urlString += "/?userID=" + userID + "&userAndroidID=" + userAndroidID;

            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                return result.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error fetching data from server: " + e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (onDataReceivedListener != null) {
            onDataReceivedListener.onDataReceived(result);
        }
    }

    public interface OnDataReceivedListener {
        void onDataReceived(String result);
    }
}