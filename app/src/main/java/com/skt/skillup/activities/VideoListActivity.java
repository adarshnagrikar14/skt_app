package com.skt.skillup.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.skt.skillup.BuildConfig;
import com.skt.skillup.R;
import com.skt.skillup.courseadapters.CourseAdapter;
import com.skt.skillup.courseadapters.CourseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {

    private CourseAdapter adapter;
    private List<CourseModel> courseList;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.videoListJava);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        courseList = new ArrayList<>();

        adapter = new CourseAdapter(this, courseList);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading Content");
        progressDialog.show();

        fetchCourseData();
    }

    private void fetchCourseData() {
        String url = BuildConfig.API_VIDEO;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    updateCourseList(response);
                    progressDialog.dismiss();
                },
                error -> {
                    Toast.makeText(VideoListActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });

        Volley.newRequestQueue(this).add(request);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateCourseList(JSONArray response) {
        try {
            courseList.clear();

            for (int i = 0; i < response.length(); i++) {
                JSONObject courseObject = response.getJSONObject(i);
                String title = courseObject.getString("title");
                String description = courseObject.getString("description");
                String link = courseObject.getString("link");
                CourseModel course = new CourseModel(title, description, link);
                courseList.add(course);
            }

            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

//        courseList.add(new CourseModel("2. Java Environment Setup", "Setting up the Environment required", 4));
//        courseList.add(new CourseModel("3. Classes and objects", "Java programming", 4));
//        courseList.add(new CourseModel("4. Arrays and Strings", "Java programming", 4));
//        courseList.add(new CourseModel("5. Object-Oriented programming concepts", "Java programming", 4));
//        courseList.add(new CourseModel("6. Exception Handling", "Java programming", 4));
//        courseList.add(new CourseModel("7. Collection Framework", "Java programming", 4));
//        courseList.add(new CourseModel("8. Special Important Topics", "Java additional important concepts", 4));
//        courseList.add(new CourseModel("9. Multithreading", "Java Advanced", 5));
//        courseList.add(new CourseModel("10. Java 8 Features", "Java Advanced", 4));
//        courseList.add(new CourseModel("11. Practice Task", "Practice on concepts of Java", 4));
//        courseList.add(new CourseModel("12. More Special topics", "Java additional important concepts", 5));
