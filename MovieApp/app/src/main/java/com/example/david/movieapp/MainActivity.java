package com.example.david.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.LinearLayout;

import com.example.david.movieapp.models.MoviesModel;


public class MainActivity extends AppCompatActivity implements MovieListener{


boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    LinearLayout linearLayout = (LinearLayout)findViewById(R.id.two_pane);

        if ( linearLayout == null){
            mTwoPane=false;
        }else{
            mTwoPane=true;
        }

        if (savedInstanceState==null){
             MainActivityFragment mainActivityFragment = new MainActivityFragment();
            mainActivityFragment.setMovieListener(this);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.left_side, mainActivityFragment)
                    .commit();
        }

    }

    @Override
    public void onMovieSelected(MoviesModel moviesModel) {

        if (mTwoPane==false){

                           Intent intent = new Intent(this , DetailActivity.class);
                          intent.putExtra("poster_path", moviesModel.getPoster_path())
                         .putExtra("id", moviesModel.getId())
                         .putExtra("title", moviesModel.getOriginal_title())
                         .putExtra("overview", moviesModel.getOverview())
                         .putExtra("release_date", moviesModel.getRelease_date())
                         .putExtra("vote_average", moviesModel.getVote_average())
                ;
                startActivity(intent);


        }else{
            DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
            Bundle bundle = new Bundle();
            bundle.putString("poster_path", moviesModel.getPoster_path());
            bundle.putInt("id", moviesModel.getId());
            bundle.putString("title", moviesModel.getOriginal_title());
            bundle.putString("overview", moviesModel.getOverview());
            bundle.putString("release_date", moviesModel.getRelease_date());
            bundle.putString("vote_average", moviesModel.getVote_average());


            detailActivityFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.right_side, detailActivityFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit();

        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }





}
