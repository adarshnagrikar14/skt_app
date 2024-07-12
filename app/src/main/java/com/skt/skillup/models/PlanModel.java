package com.skt.skillup.models;

public class PlanModel {
    private String date;
    private String time;
    private String plan;

    public PlanModel(String date, String time, String plan) {
        this.date = date;
        this.time = time;
        this.plan = plan;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPlan() {
        return plan;
    }
}
