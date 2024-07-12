package com.skt.skillup.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.skt.skillup.ImageUploadTask;
import com.skt.skillup.R;

import java.io.IOException;

public class ImageActivity extends AppCompatActivity {

    Bitmap bitmap;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = findViewById(R.id.imageUpload);
    }

    public void uploadIt(View view) {
        // String serverUrl = "http://192.168.1.4/uploadimage.php";
        String serverUrl = "https://assignme-work.000webhostapp.com/make.php";
        ImageUploadTask imageUploadTask = new ImageUploadTask(this, bitmap, serverUrl);
        imageUploadTask.uploadImage();

    }

    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), o -> {
                        if (o.getResultCode() == RESULT_OK) {

                            Intent data = o.getData();
                            assert data != null;
                            Uri uri = data.getData();

                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                imageView.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
            );

    public void pickImage(View view) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intent);

    }
}