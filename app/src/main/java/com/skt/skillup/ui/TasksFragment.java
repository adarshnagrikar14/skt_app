package com.skt.skillup.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skt.skillup.BottomSheets.TaskBottomSheetFragment;
import com.skt.skillup.R;
import com.skt.skillup.SharedPreferencesHelper;
import com.skt.skillup.adapters.TaskAdapter;
import com.skt.skillup.models.TaskModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class TasksFragment extends Fragment implements TaskBottomSheetFragment.BottomSheetListener {

    private List<TaskModel> tasks = new ArrayList<>();
    private TaskAdapter taskAdapter;
    RecyclerView recyclerView;

    ImageView taskUnavailable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        tasks = SharedPreferencesHelper.getTasks(requireContext());
        taskUnavailable = view.findViewById(R.id.taskUnavailable);
        recyclerView = view.findViewById(R.id.recyclerViewTask);

        if (tasks.size() == 0) {
            taskUnavailable.setVisibility(View.VISIBLE);
        }

        taskAdapter = new TaskAdapter(tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(taskAdapter);

        FloatingActionButton fabAddTask = view.findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(v -> showAddTaskDialog());

        return view;
    }

    private void showAddTaskDialog() {
        TaskBottomSheetFragment bottomSheetFragment = new TaskBottomSheetFragment();
        bottomSheetFragment.setBottomSheetListener(this);
        bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onAddButtonClicked(String inputText) {

        Date currentDate = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(currentDate);

        if (inputText == null || inputText.isEmpty()) {
            Toast.makeText(requireContext(), "Add something...", Toast.LENGTH_SHORT).show();
        } else {
            TaskModel newTask = new TaskModel(inputText, formattedDate, false);
            tasks.add(newTask);
            SharedPreferencesHelper.saveTasks(requireContext(), tasks);
            taskAdapter.notifyDataSetChanged();
            Objects.requireNonNull(requireActivity()).recreate();
        }
    }
}