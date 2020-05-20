package com.careerguide.newsfeed;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.prof.rssparser.Article;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeedRepo {

    public static final String PREFS_NAME = "NEWS_FEED_PREF";


    public FeedRepo() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFeeds(Context context, List<Article> favorites, String cacheFileName) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(cacheFileName, jsonFavorites);

        editor.apply();
    }



    public ArrayList<Article> getFeeds(Context context, String cacheFile) {
        SharedPreferences settings;
        List<Article> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(cacheFile)) {
            String jsonFavorites = settings.getString(cacheFile, null);
            Gson gson = new Gson();
            Article[] favoriteItems = gson.fromJson(jsonFavorites,
                    Article[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Article>(favorites);
        } else
            return null;

        return (ArrayList<Article>) favorites;
    }
}