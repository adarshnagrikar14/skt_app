package com.skt.skillup.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.skt.skillup.R;
import com.skt.skillup.activities.DashboardActivity;
import com.skt.skillup.activities.PlannerActivity;
import com.skt.skillup.activities.VideoListActivity;
import com.skt.skillup.utils.ImagePagerAdapter;

public class HomeFragment extends Fragment {

    @SuppressLint("ScheduleExactAlarm")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        view.findViewById(R.id.fabAddTask).setOnClickListener(view1 -> DashboardActivity.navController.navigate(R.id.nav_task));

        view.findViewById(R.id.linearLayoutBookmark).setOnClickListener(view1 -> {
            startActivity(new Intent(requireContext(), PlannerActivity.class));
            requireActivity().overridePendingTransition(R.anim.scale_in_simple, 0);
        });

        view.findViewById(R.id.open_learnings).setOnClickListener(view1 -> {
            startActivity(new Intent(requireContext(), VideoListActivity.class));
            requireActivity().overridePendingTransition(R.anim.scale_in_simple, 0);
        });

        return view;
    }
}