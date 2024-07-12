package com.skt.skillup.networks;

import android.os.AsyncTask;
import android.util.Log;

import com.skt.skillup.BuildConfig;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SendDataToServerTask extends AsyncTask<String, Void, Integer> {

    private final AsyncTaskCompleteListener callback;

    public SendDataToServerTask(AsyncTaskCompleteListener callback) {
        this.callback = callback;
    }

    public interface AsyncTaskCompleteListener {
        void onTaskComplete(Integer responseCode);
    }

    @Override
    protected Integer doInBackground(String... params) {
        try {
            String endpointUrl = BuildConfig.API_KEY;

            String userID = params[0];
            String userName = params[1];
            String userAndroidID = params[2];
            String userAccess = params[3];

            String jsonData = String.format("{\"userID\":\"%s\",\"userName\":\"%s\",\"userAndroidID\":\"%s\",\"userAccess\":%s}", userID, userName, userAndroidID, userAccess);

            URL url = new URL(endpointUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            connection.disconnect();

            Log.d("SendDataToServerTask", "doInBackground: " + connection.getResponseMessage());

            return responseCode;

        } catch (Exception e) {
            Log.e("SendDataToServerTask", "Error: " + e.getMessage());
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer responseCode) {
        if (callback != null) {
            callback.onTaskComplete(responseCode);
        }
    }
}
