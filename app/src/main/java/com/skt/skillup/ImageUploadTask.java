package com.skt.skillup;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageUploadTask {
    private Context context;
    private Bitmap bitmap;
    private String serverUrl;

    public ImageUploadTask(Context context, Bitmap bitmap, String serverUrl) {
        this.context = context;
        this.bitmap = bitmap;
        this.serverUrl = serverUrl;
    }

    public void uploadImage() {
        // Convert Bitmap to Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        final String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(context);

        // Define the POST request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Handle the response here (success or failure)
                if (response.equals("success")) {
                    Toast.makeText(context, "Image upload successful", Toast.LENGTH_SHORT).show();
                    // You can handle success actions here
                } else {
                    Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
                    // You can handle failure actions here
                    Log.d("RESPONSE_IMAGE", "onResponse: " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle errors here
                error.printStackTrace();
                Toast.makeText(context, "Image upload failed 2", Toast.LENGTH_SHORT).show();
                // You can handle failure actions here
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Set POST parameters here (if needed)
                Map<String, String> params = new HashMap<>();
                params.put("image", encodedImage);
                return params;
            }
        };

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }
}
