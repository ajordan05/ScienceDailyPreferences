package com.example.android.sciencedaily;

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
    // Tag for the log messages
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    // private constructor declaration
    private QueryUtils() {
    }

    public static List<Stories> fetchStoryData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Stories> stories = extractFeatureFromJson(jsonResponse);
        // Return the list of stories
        return stories;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    //make an http request to URL and return a String
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200)
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the story JSON results.", e);
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

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Stories> extractFeatureFromJson(String storyJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(storyJSON)) {
            Log.e(LOG_TAG, "JSON empty");
            return null;
        }
        // Create an empty ArrayList that we can start adding stories to
        List<Stories> stories = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON is formatted, a JSONException exception object will be thrown.
        try {
            //Create a json object from the above sample response string
            JSONObject baseJsonResponse = new JSONObject(storyJSON);
            //Extract the array with the key "results" which contains the values we want
            JSONObject storiesObject = baseJsonResponse.getJSONObject("response");
            //Log.v(LOG_TAG,"Output of get JSON for response" + storiesObject);
            JSONArray storiesArray = storiesObject.getJSONArray("results");
            //Log.v(LOG_TAG, "Output of get JSON for results" + storiesArray);
            //for each story in our new array create an object
            for( int i = 0; i < storiesArray.length(); i++) {
                JSONObject currentStory = storiesArray.getJSONObject(i);
                JSONArray tagsArray = currentStory.getJSONArray("tags");
                JSONObject tags = tagsArray.getJSONObject(0);

                String title = currentStory.getString("webTitle");
                Log.e(LOG_TAG, + i + title );
                String section = currentStory.getString("sectionName");
                String date = currentStory.getString("webPublicationDate");
                //adding this for an onclick method to bring up page for story
                String url = currentStory.getString("webUrl");
                //get the author of the story
                String author = tags.getString("webTitle");


                Stories story = new Stories(author, title, section, date, url);
                stories.add(story);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the story JSON results", e);
        }

        return stories;
    }


}
