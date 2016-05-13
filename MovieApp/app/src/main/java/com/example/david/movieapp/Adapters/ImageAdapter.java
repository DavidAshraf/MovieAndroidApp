package com.example.david.movieapp.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.david.movieapp.R;
import com.example.david.movieapp.models.MoviesModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 2016/04/15.
 */

//*********** Creating a Custom adapter *************//
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    public List<MoviesModel> mData = new ArrayList<MoviesModel>();

    final String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/";
    final String IMAGE_SIZE = "w342/";

    // Constructor
    public ImageAdapter( List<MoviesModel> mData,Context mContext ) {
        this.mData=mData;
        this.mContext=mContext;
    }

    public int getCount() {
        return mData.size();
    }

    public MoviesModel getItem(int position) {

        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // getView method returns a new ImageView for each item
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        // Check if the passed view object is recycled. If not set the desired properties to a new ImageView
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        } else {
            imageView = (ImageView) convertView;
        }



        // passing image source to the imageView
        Picasso.with(mContext)
                .load(BASE_URL_IMAGE + IMAGE_SIZE + mData.get(position).getPoster_path())
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);

        return imageView;
    }




}