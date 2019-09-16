package com.example.moviecatalogue.view.utility;

import java.util.ArrayList;
import java.util.List;

public class GenreConverter {

    public static String convertGenreIdsToAStringOfNames(List<Integer> genreIds) {
        ArrayList<String> genreNameList = convertGenreIdListToNameList(genreIds);
        return convertGenreNameListToAString(genreNameList);
    }

    private static ArrayList<String> convertGenreIdListToNameList(List<Integer> genre_ids) {
        ArrayList<String> genreNameList = new ArrayList<>();
        for (int i = 0; i < genre_ids.size(); i++) {
            String genre = convertGenreIdtoGenreName(genre_ids.get(i));
            genreNameList.add(genre);
        }
        return genreNameList;
    }

    private static String convertGenreNameListToAString(ArrayList<String> genres) {
        StringBuilder genresStringBuilder = new StringBuilder();
        for (String genre : genres) {
            genresStringBuilder = genresStringBuilder.length() > 0 ? genresStringBuilder.append(", ").append(genre) : genresStringBuilder.append(genre);
        }
        return genresStringBuilder.toString();
    }

    private static String convertGenreIdtoGenreName(Integer genreId) {
        String genre;
        switch (genreId) {
            case 28:
                genre = "Action";
                break;
            case 12:
                genre = "Adventure";
                break;
            case 16:
                genre = "Animation";
                break;
            case 35:
                genre = "Comedy";
                break;
            case 80:
                genre = "Crime";
                break;
            case 99:
                genre = "Documentary";
                break;
            case 18:
                genre = "Drama";
                break;
            case 10751:
                genre = "Family";
                break;
            case 14:
                genre = "Fantasy";
                break;
            case 36:
                genre = "History";
                break;
            case 27:
                genre = "Horror";
                break;
            case 10402:
                genre = "Music";
                break;
            case 9648:
                genre = "Mystery";
                break;
            case 10749:
                genre = "Romance";
                break;
            case 878:
                genre = "Science Fiction";
                break;
            case 10770:
                genre = "TV Movie";
                break;
            case 53:
                genre = "Thriller";
                break;
            case 10752:
                genre = "War";
                break;
            case 37:
                genre = "Western";
                break;
            case 10759:
                genre = "Action & Adventure";
                break;
            case 10765:
                genre = "Sci-Fi & Fantasy";
                break;
            default:
                genre = "Unknown";
                break;
        }
        return genre;
    }
}
