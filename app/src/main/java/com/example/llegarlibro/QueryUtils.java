package com.example.llegarlibro;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    public static final String LOG_TAG = BookActivity.class.getName();

    private QueryUtils() {
    }

    public static List<Book> extractFeatureFromJSON(String bookJSON) {

        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        List<Book> books = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(bookJSON);
            JSONArray items = baseJsonResponse.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject currentBook = items.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                JSONArray authors = volumeInfo.getJSONArray("authors");
                String author = authors.getString(0);
                String publisher;
                if (volumeInfo.has("publisher")) {
                    publisher = volumeInfo.getString("publisher");
                } else {
                    publisher = "N.A.";
                }
                int averageRating;
                if (volumeInfo.has("averageRating")) {
                    averageRating = volumeInfo.getInt("averageRating");
                } else {
                    averageRating = 0;
                }
                int pageCount;
                if (volumeInfo.has("pageCount")) {
                    pageCount = volumeInfo.getInt("pageCount");
                } else {
                    pageCount = 0;
                }
                JSONArray categories = new JSONArray();
                if (volumeInfo.has("categories")) {
                    categories = volumeInfo.getJSONArray("categories");
                } else {
                    categories.put("N.A.");
                }
                String category = categories.getString(0);
                String previewLink = volumeInfo.getString("previewLink");
                Book book = new Book(title, author, publisher, averageRating, pageCount, category, previewLink);
                books.add(book);
            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the Books JSON results", e);
        }

        // Return the list of earthquakes
        return books;
    }

    public static List<Book> fetchBookData(String requestUrl) {

        Log.e(LOG_TAG, "Fetching Google Books API data");

        URL url = createURL(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        return extractFeatureFromJSON(jsonResponse);

    }


    private static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = null;

        if (url == null) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error Response Code : " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Book JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();

        try {

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Book JSON results", e);
        }

        return output.toString();
    }

}

