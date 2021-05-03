package com.example.llegarlibro;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private static final String LOG_TAG = BookLoader.class.getName();

    private final String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.e(LOG_TAG, "Starting the loader");
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        Log.e(LOG_TAG, "Loading in Background");
        if (mUrl == null) {
            return null;
        }

        return QueryUtils.fetchBookData(mUrl);
    }

}


