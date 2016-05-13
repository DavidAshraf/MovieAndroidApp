package com.example.david.movieapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.movieapp.data.FetchReviewsTask;
import com.example.david.movieapp.data.MovieDBHelper;
import com.example.david.movieapp.models.MoviesModel;
import com.squareup.picasso.Picasso;


public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    final String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/";
    final String IMAGE_SIZE = "w780/";
    MovieDBHelper movieDBHelper;

    public ExpandableListView detailListView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflating expandable listView
        View rootView =inflater.inflate(R.layout.detail_list_view, container, false);

        // the layout including poster , description , rating , date and favorite button is added as a header to the expandable listView
        View header = inflater.inflate(R.layout.fragment_detail, null);


        ImageView imageView= (ImageView)header.findViewById(R.id.poster);
        TextView titleView = (TextView)header.findViewById(R.id.title);
        final TextView releaseDateView = (TextView)header.findViewById(R.id.releasedate);
        TextView overviewView = (TextView)header.findViewById(R.id.overview);
        TextView voteView = (TextView)header.findViewById(R.id.vote_averge);
        final ImageButton favoriteButton = (ImageButton)header.findViewById(R.id.favorite_button);
        detailListView = (ExpandableListView)rootView.findViewById(R.id.detail_list_view);

        // adding the header view ( poster , description , ..)
        detailListView.addHeaderView(header);

        movieDBHelper = new MovieDBHelper(getContext());

        // Getting data and setting the views to it
        Picasso.with(getContext()).load(BASE_URL_IMAGE + IMAGE_SIZE + getArguments().getString("poster_path")).into(imageView);
        titleView.setText(getArguments().getString("title"));
        overviewView.setText(getArguments().getString("overview"));
        voteView.setText(getArguments().getString("vote_average"));
        releaseDateView.setText(getArguments().getString("release_date"));

        // fetching reviews and trailers
        FetchReviewsTask fetchReviewsTask = new FetchReviewsTask(this);
        fetchReviewsTask.execute(Integer.toString(getArguments().getInt("id")));




        if (movieDBHelper.isFavorite(getArguments().getInt("id"))) {
            favoriteButton.setActivated(true);
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // To UnFavorite Movie
                if (favoriteButton.isActivated()) {
                    favoriteButton.setActivated(false);
                    movieDBHelper.deleteFromFavorite(getArguments().getInt("id"));


                // To Add Movie to Favorites
                } else {
                    favoriteButton.setActivated(true);
                    MoviesModel movie = new MoviesModel();
                    movie.setPoster_path(getArguments().getString("poster_path"));
                    movie.setRelease_date(getArguments().getString("release_date"));
                    movie.setVote_average(getArguments().getString("vote_average"));
                    movie.setId(getArguments().getInt("id"));
                    movie.setOverview(getArguments().getString("overview"));
                    movie.setOriginal_title(getArguments().getString("title"));

                    movieDBHelper.addMovieToFavorite(movie);
                    movieDBHelper.close();
                }

            }
        });


        return rootView;

    }



}
