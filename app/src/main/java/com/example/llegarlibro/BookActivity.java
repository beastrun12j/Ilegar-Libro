package com.example.llegarlibro;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


public class BookActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private BookAdapter mAdapter;

    public static final String LOG_TAG = BookActivity.class.getName();

    private static final String GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes";

    private static final int BOOK_LOADER_ID = 1;

    private TextView mEmptyStateTextView;

    private ProgressBar mLoadingIndicator;

    public String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e(LOG_TAG, "OnCreate Method is called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new BookAdapter(this, new ArrayList<>());
        ListView bookListView = findViewById(R.id.list_item);
        bookListView.setAdapter(mAdapter);

        mLoadingIndicator = findViewById(R.id.loadingIndicator);
        mLoadingIndicator.setIndeterminate(true);

        SearchView bookSearchView = findViewById(R.id.search_view);

        mEmptyStateTextView = findViewById(R.id.emptyView);
        bookListView.setEmptyView(mEmptyStateTextView);

        bookListView.setOnItemClickListener((parent, view, position, l) -> {
            Book currentBook = mAdapter.getItem(position);

            Uri bookUri = Uri.parse(currentBook.getUrl());

            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

            startActivity(websiteIntent);

        });

        bookSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                bookSearchView.clearFocus();

                mEmptyStateTextView.setText("");

                mAdapter.clear();

                searchQuery = bookSearchView.getQuery().toString();

                Log.e(LOG_TAG, searchQuery);

                mLoadingIndicator.setVisibility(View.VISIBLE);
                getLoaderManager().restartLoader(BOOK_LOADER_ID, null, BookActivity.this);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {
            Log.e(LOG_TAG, "Loader Manager Called");
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_connection);
        }

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        Log.e(LOG_TAG, "Creating the Loader");

        Uri baseUri = Uri.parse(GOOGLE_BOOKS_API_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        if (searchQuery == null) {
            uriBuilder.appendQueryParameter("q", "kajshfabfjhsbfhjbsj");
        } else {
            uriBuilder.appendQueryParameter("q", searchQuery);
            uriBuilder.appendQueryParameter("minResults", "2");
        }
        Log.e(LOG_TAG, uriBuilder.toString());
        return new BookLoader(this, uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        mLoadingIndicator.setVisibility(View.GONE);

        Log.e(LOG_TAG, "Loader Task finished");

        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        } else {
            mEmptyStateTextView.setText(R.string.empty_state_text);
            mEmptyStateTextView.setTextColor(ContextCompat.getColor(this, R.color.white));
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.e(LOG_TAG, "Resetting the Loader");
        mAdapter.clear();
    }

}
