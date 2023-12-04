package com.example.myapplication;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkHandler {
    private static final String API_KEY = "ad5c40df65mshba2972e2b4bd264p1df5f1jsnae362888b3bb";

    private HttpURLConnection setupConnection(URL url) {
        HttpURLConnection urlConnection;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("X-RapidAPI-Key", API_KEY);
            return urlConnection;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String readResponse(HttpURLConnection connection) throws IOException {
        StringBuilder result = new StringBuilder();

        InputStream inStream = new BufferedInputStream(connection.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }
    public String performRequest(String urlString) throws IOException {
        String result;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(urlString);
            urlConnection = setupConnection(url);
            result = readResponse(urlConnection);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        Log.d("API response", result);
        return result;
    }
}
