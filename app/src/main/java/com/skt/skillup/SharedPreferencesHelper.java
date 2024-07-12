package com.skt.skillup;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skt.skillup.models.PlanModel;
import com.skt.skillup.models.TaskModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesHelper {

    private static final String TASK_PREFS_NAME = "TaskPreference";
    private static final String TASKS_KEY = "TaskList";

    private static final String PLAN_PREFS_NAME = "PlanPreference";
    private static final String PLANS_KEY = "PlanList";

    // Tasks
    public static void saveTasks(Context context, List<TaskModel> tasks) {
        SharedPreferences prefs = context.getSharedPreferences(TASK_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String tasksJson = gson.toJson(tasks);
        editor.putString(TASKS_KEY, tasksJson);
        editor.apply();
    }

    public static List<TaskModel> getTasks(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(TASK_PREFS_NAME, Context.MODE_PRIVATE);
        String tasksJson = prefs.getString(TASKS_KEY, null);

        if (tasksJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<TaskModel>>() {
            }.getType();
            return gson.fromJson(tasksJson, type);
        }

        return new ArrayList<>();
    }

    // Plans
    public static void savePlans(Context context, ArrayList<PlanModel> plans) {
        SharedPreferences prefs = context.getSharedPreferences(PLAN_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String plansJson = gson.toJson(plans);
        editor.putString(PLANS_KEY, plansJson);
        editor.apply();
    }

    public static ArrayList<PlanModel> getPlans(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PLAN_PREFS_NAME, Context.MODE_PRIVATE);
        String plansJson = prefs.getString(PLANS_KEY, null);

        if (plansJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<PlanModel>>() {
            }.getType();
            return gson.fromJson(plansJson, type);
        }

        return new ArrayList<>();
    }
}
