package com.alif.moviecatalogue.view.utility;

import android.net.Uri;
import android.util.Log;

import com.alif.moviecatalogue.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class Networking {
    // Default: http://api.themoviedb.org/3/movie/popular
    // Alternative: http://api.themoviedb.org/3/discover/movie?sort_by=...&api_key=...
    // https://api.themoviedb.org/3/discover/movie?api_key={API_KEY}&primary_release_date.gte={TODAY_DATE}&primary_release_date.lte={TODAY_DATE}

    public Networking() {

    }

    public static URL buildURL(String todayDate) {
        Uri uri = Uri.parse("https://api.themoviedb.org/3/discover/movie").buildUpon()
                .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY) // Use your own API key
                .appendQueryParameter("primary_release_date.gte", todayDate)
                .appendQueryParameter("primary_release_date.lte", todayDate)
                .build();
        Log.v("uri = ", uri.toString());
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getHttpResponse(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else
                return null;
        } finally {
            httpURLConnection.disconnect();
        }
    }
}
