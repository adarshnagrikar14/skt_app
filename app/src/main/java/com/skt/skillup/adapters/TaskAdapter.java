package com.skt.skillup.adapters;

import static com.skt.skillup.SharedPreferencesHelper.saveTasks;
import static com.skt.skillup.activities.DashboardActivity.navController;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.skt.skillup.R;
import com.skt.skillup.models.TaskModel;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<TaskModel> taskList;

    public TaskAdapter(List<TaskModel> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        taskList.sort((task1, task2) -> Boolean.compare(task1.isDone(), task2.isDone()));
        TaskModel task = taskList.get(position);

        holder.icDelete.setOnClickListener(view -> {
            TaskModel taskToDelete = taskList.get(holder.getAdapterPosition());
            taskList.remove(taskToDelete);
            saveTasks(view.getContext(), taskList);
            notifyItemRemoved(holder.getAdapterPosition());
        });

        if (task.isDone()) {
            holder.checkDone.setEnabled(false);
            changeDesign(holder);
        } else {
            holder.checkDone.setEnabled(true);
            holder.checkDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
                TaskModel taskToDelete = taskList.get(holder.getAdapterPosition());
                TaskModel updatedTask = taskList.get(holder.getAdapterPosition());
                updatedTask.setDone(isChecked);

                taskList.add(updatedTask);
                saveTasks(buttonView.getContext(), taskList);

                taskList.remove(taskToDelete);
                saveTasks(buttonView.getContext(), taskList);

                while (navController.getPreviousBackStackEntry() != null && navController.getPreviousBackStackEntry().getDestination().getId() != R.id.nav_home) {
                    navController.popBackStack();
                }

                navController.popBackStack();
                navController.navigate(R.id.nav_task);
            });
        }

        holder.bindTask(task);
    }

    private void changeDesign(TaskViewHolder holder) {

        holder.textTask.setPaintFlags(holder.textTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        int textColor = ContextCompat.getColor(holder.textTask.getContext(), R.color.greyVariant);
        holder.textTask.setTextColor(textColor);

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView textTask;
        private final TextView textDate;
        private final ImageView icDelete;
        private final CheckBox checkDone;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textTask = itemView.findViewById(R.id.textTask);
            textDate = itemView.findViewById(R.id.textDate);
            checkDone = itemView.findViewById(R.id.checkDone);
            icDelete = itemView.findViewById(R.id.deleteTask);
        }

        public void bindTask(TaskModel task) {
            textTask.setText(task.getTaskName());
            textDate.setText(task.getTaskDate());
            checkDone.setChecked(task.isDone());
        }
    }
}
