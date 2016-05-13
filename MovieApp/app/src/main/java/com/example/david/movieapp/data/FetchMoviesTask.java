package com.example.david.movieapp.data;

/**
 * Created by David on 2016/04/20.
 */

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import com.example.david.movieapp.Adapters.ImageAdapter;
import com.example.david.movieapp.MainActivityFragment;
import com.example.david.movieapp.models.MoviesModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchMoviesTask extends AsyncTask<String,String ,List<MoviesModel>>{
    private static final String Log_TAG = "FETCH_MOVIES";

    final String BASE_URL = "http://api.themoviedb.org/3/movie";
    final String API_KEY = "cc25f55ebab88a2a9c6d69396eda8987";
    final String APP_KEY_PARAM = "api_key";
    private String FULL_URL;
    private String JsonString;
    private GridView grid;
    private MainActivityFragment mainActivityFragment;


    // Constructor
    public FetchMoviesTask(MainActivityFragment mainActivityFragment) {
       this.grid=mainActivityFragment.gridView;
       this.mainActivityFragment=mainActivityFragment;

    }


    @Override
    protected List<MoviesModel> doInBackground(String... params) {

        // Verify size of params.
        if (params.length == 0) {
            return null;
        }

        // Http Connection
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {

            // Constructing the URL
            FULL_URL =  BASE_URL + params[0];
            Uri buildUri = Uri.parse(FULL_URL).buildUpon()
                    .appendQueryParameter(APP_KEY_PARAM, API_KEY)
                    .build();
            URL url = new URL(buildUri.toString());

            // Create the request to the backend and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();

            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();

            String LINE;
            while ((LINE = reader.readLine()) != null) {
                stringBuffer.append(LINE);
            }

            // Data
            JsonString = stringBuffer.toString();

            // Getting Parent object
            JSONObject parentObject = new JSONObject(JsonString);

            // Getting Array Results which include all movies objects
            JSONArray resultsArray = parentObject.getJSONArray("results");

            List<MoviesModel> moviesModelList = new ArrayList<>();

            Gson gson = new Gson();

            // Getting each object from results array and adding it to the list
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject elementObject = resultsArray.getJSONObject(i);

                    MoviesModel moviesModel = gson.fromJson(elementObject.toString(), MoviesModel.class);
                    moviesModelList.add(moviesModel);


            }


            return moviesModelList;

        } catch (IOException e) {
            Log.e(Log_TAG, "IOException ", e);
            return null;

        } catch (JSONException e) {
            Log.e(Log_TAG, "JSONException", e);

        }  finally{

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(Log_TAG, "Error closing stream", e);
                }
            }

        }

        return null;
    }



    @Override
    protected void onPostExecute(final List<MoviesModel> results) {
        super.onPostExecute(results);

        if (results != null) {
            // Passing the list to the custom adapter
            ImageAdapter imageAdapter = new ImageAdapter(results,mainActivityFragment.getContext());
            grid.setAdapter(imageAdapter);
            mainActivityFragment.moviesModelList=results;


        }


    }


}
