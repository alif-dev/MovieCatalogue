package com.alif.moviecatalogue.view.utility;

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
            Locale localeIndonesia = new Locale("in", "ID");
            if (Locale.getDefault().equals(localeIndonesia)) {
                formattedDate = new SimpleDateFormat("d MMMM yyyy", localeIndonesia).format(date);
            } else {
                formattedDate = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(date);
            }
        }

        return formattedDate;
    }
}
