package com.skt.skillup.models;

public class TaskModel {

    private final String taskName;
    private final String taskDate;
    boolean isDone;

    public TaskModel(String taskName, String taskDate, boolean isDone) {
        this.taskName = taskName;
        this.taskDate = taskDate;
        this.isDone = isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public boolean isDone() {
        return isDone;
    }


}
