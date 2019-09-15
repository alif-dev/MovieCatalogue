package com.example.moviecatalogue.view.utility;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {

    @SuppressLint("SimpleDateFormat")
    public static String formatDateToLocal(String dateString) {
        Date date = null;
        String formattedDate = "N/A";

        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            formattedDate = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(date);
        }

        return formattedDate;
    }
}
