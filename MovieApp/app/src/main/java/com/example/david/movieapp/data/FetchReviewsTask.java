package com.example.david.movieapp.data;

/**
 * Created by David on 2016/04/23.
 */

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.david.movieapp.DetailActivityFragment;
import com.example.david.movieapp.models.ReviewsModel;
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


/** return type string array **/
public class FetchReviewsTask extends AsyncTask<String,String ,List<ReviewsModel>> {

    private static final String Log_TAG = "FETCH_REVIEWS";

    final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    final String API_KEY = "cc25f55ebab88a2a9c6d69396eda8987";
    final String APP_KEY_PARAM = "api_key";
    private String FULL_URL;
    private String JsonString;
    private DetailActivityFragment detailActivityFragment;



    public FetchReviewsTask(DetailActivityFragment detailActivityFragment) {
        this.detailActivityFragment=detailActivityFragment;


    }

    @Override
    protected List<ReviewsModel> doInBackground(String... params) {

        // Verify size of params.
        if (params.length == 0) {
            return null;
        }

        // Http Connection
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {

            // Constructing the URL
            FULL_URL =  BASE_URL + params[0]+"/reviews";
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

            List<ReviewsModel> reviewsModelList = new ArrayList<>();

            Gson gson = new Gson();

            // Getting each object from results array and adding it to the list
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject elementObject = resultsArray.getJSONObject(i);

                ReviewsModel reviewsModel = gson.fromJson(elementObject.toString(), ReviewsModel.class);
                reviewsModelList.add(reviewsModel);


            }


            return reviewsModelList;

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
    protected void onPostExecute(final List<ReviewsModel> results) {
        super.onPostExecute(results);


            if (results != null) {
                List<String> reviewsList = new ArrayList<>();
                List<String> authorsList = new ArrayList<>();


                for (int i = 0; i < results.size(); i++) {
                    authorsList.add(results.get(i).getAuthor());
                    reviewsList.add(results.get(i).getContent());
                }


                FetchTrailersTask fetchTrailersTask = new FetchTrailersTask(detailActivityFragment,authorsList,reviewsList);
                fetchTrailersTask.execute(Integer.toString(detailActivityFragment.getArguments().getInt("id")));



        }

    }
}

