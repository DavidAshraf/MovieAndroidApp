package com.example.david.movieapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.david.movieapp.Adapters.ImageAdapter;
import com.example.david.movieapp.data.FetchMoviesTask;
import com.example.david.movieapp.data.MovieDBHelper;
import com.example.david.movieapp.models.MoviesModel;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {


    public MainActivityFragment() {
    }

    public GridView gridView;
    public List<MoviesModel> moviesModelList;
    private final String TAG = "FETCH_TASK";
    public MovieListener mListener;
   public MovieDBHelper movieDBHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) rootView.findViewById(R.id.grid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mListener.onMovieSelected(moviesModelList.get(position));

            }
        });


        update_data("/popular");


        return rootView;
    }


    public void setMovieListener(MovieListener movieListener) {
        mListener = movieListener;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // Actions according to the item selected
        if (id == R.id.most_popular) {
            update_data("/popular");
        } else if (id == R.id.top_rated) {
            update_data("/top_rated");

        } else if (id == R.id.favorites) {
           show_favorites();
        }


        return super.onOptionsItemSelected(item);
    }



    public void update_data(String s) {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(this);
        fetchMoviesTask.execute(s);

    }


    public void show_favorites(){
        movieDBHelper = new MovieDBHelper(getContext());
        moviesModelList=movieDBHelper.getFavorites();
        ImageAdapter imageAdapter = new ImageAdapter(moviesModelList,getContext());
        movieDBHelper.close();
        gridView.setAdapter(imageAdapter);

    }
}







