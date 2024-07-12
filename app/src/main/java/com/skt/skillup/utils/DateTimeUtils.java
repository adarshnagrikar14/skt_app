package com.skt.skillup.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    public static long calculateTimestamp(String selectedDate, String selectedTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        Date dateTime = null;

        try {
            dateTime = dateFormat.parse(selectedDate + " " + selectedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (dateTime != null) {
            return dateTime.getTime();
        }

        return -1;
    }
}
