package com.example.android.sciencedaily;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class StoryLoader extends AsyncTaskLoader<List<Stories>> {
    //Tag for log messages
    private static final String LOG_TAG = StoryLoader.class.getName();

    //Query URL
    private String mUrl;

    public StoryLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.v(LOG_TAG,"onStartLoading reload");
    }

    @Override
    public List<Stories> loadInBackground() {
        if (mUrl == null) {
            Log.v(LOG_TAG, "loadInBackground reload");
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Stories> stories = QueryUtils.fetchStoryData(mUrl);
        return stories;
    }

}