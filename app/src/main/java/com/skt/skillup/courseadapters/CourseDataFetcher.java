package com.skt.skillup.courseadapters;


import com.skt.skillup.BuildConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDataFetcher {

    public static List<CourseModel> fetchCourses() {
        List<CourseModel> courseList = new ArrayList<>();

        String url = BuildConfig.API_KEY;
        String user = "your_username";
        String password = "your_password";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT Title, Description, Link FROM JavaPlaylist";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String link = resultSet.getString("Link");
                CourseModel course = new CourseModel(title, description, link);
                courseList.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courseList;
    }
}
