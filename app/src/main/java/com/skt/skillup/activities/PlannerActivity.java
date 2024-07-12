package com.skt.skillup.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skt.skillup.BottomSheets.PlanBottomSheetFragment;
import com.skt.skillup.R;
import com.skt.skillup.SharedPreferencesHelper;
import com.skt.skillup.adapters.PlanAdapter;
import com.skt.skillup.models.PlanModel;

import java.util.ArrayList;

public class PlannerActivity extends AppCompatActivity implements PlanBottomSheetFragment.BottomSheetListener {

    RecyclerView recyclerView;
    ImageView taskUnavailable;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        toolbar.setNavigationOnClickListener(v -> this.finish());

        checkNotificationPermission();

        findViewById(R.id.fabAddPlan).setOnClickListener(v -> showAddTaskDialog());
        taskUnavailable = findViewById(R.id.taskUnavailable);

        recyclerView = findViewById(R.id.recyclerViewPlanner);

        ArrayList<PlanModel> planModels = SharedPreferencesHelper.getPlans(this);
        PlanAdapter planAdapter = new PlanAdapter(planModels, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(planAdapter);

        planAdapter.notifyDataSetChanged();

        if (planModels.isEmpty()) {
            taskUnavailable.setVisibility(View.VISIBLE);
        }

    }

    private void showAddTaskDialog() {
        PlanBottomSheetFragment bottomSheetFragment = new PlanBottomSheetFragment();
        bottomSheetFragment.setCancelable(false);
        bottomSheetFragment.setBottomSheetListener(this);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public void onAddButtonClicked(String inputText) {
        if (!inputText.isEmpty()) {
            recreate();
        }
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                Toast.makeText(this, "Please grant notification permission", Toast.LENGTH_SHORT).show();
                this.finish();
                Intent settingsIntent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName()).putExtra(Settings.EXTRA_CHANNEL_ID, 1);
                startActivity(settingsIntent);
            }
        }
    }
}