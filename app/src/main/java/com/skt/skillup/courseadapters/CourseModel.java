package com.skt.skillup.courseadapters;

public class CourseModel {
    private String title;
    private String description;

    private String link;

    public CourseModel(String title, String description, String link) {
        this.title = title;
        this.description = description;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}
