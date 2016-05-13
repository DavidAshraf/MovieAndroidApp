package com.example.david.movieapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.david.movieapp.R;
import com.example.david.movieapp.models.TrailersModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 2016/04/24.
 */

/******* This Custom Expandable adapter adds reviews and trailers as groups and in DetailActivityFragment.java the rest of data is added as a header *******/

public class DetailExpandableAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    public List<String> mReviews = new ArrayList<String>();
    public List<String>  mAuthors = new ArrayList<String>();
    public List<String>  mTrailers = new ArrayList<String>();
    public List<TrailersModel> trailersModelList;


    // Constructor
    public DetailExpandableAdapter( List<String> mReviews,List<String> mAuthors,List<String> mTrailers,List<TrailersModel> trailersModelList,Context mContext ) {
        this.mTrailers=mTrailers;
        this.mContext=mContext;
        this.mAuthors=mAuthors;
        this.mReviews=mReviews;
        this.trailersModelList=trailersModelList;

    }



    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition==1)
            return mReviews.size();
        else
            return mTrailers.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        if(groupPosition==1) {
            return "Reviews";
        } else {
            return "Trailers";
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition==1 ) {
            return mReviews.get(childPosition);
        }else {
            return mTrailers.get(childPosition);
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        TextView header;

        if (convertView == null) {
            view =  LayoutInflater.from(mContext).inflate(R.layout.group_header, parent, false);
        }else {
            view =  convertView;
        }
        header=(TextView)view.findViewById(R.id.group_header);

        if (groupPosition==1)
        header.setText("Reviews");
        else
        header.setText("Trailers");

        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view;
        TextView trailer;
        TextView review;
        TextView author;
        ImageButton play;


            if(getGroupId(groupPosition)==0) {
                view = LayoutInflater.from(mContext).inflate(R.layout.list_trailer_item, parent, false);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.list_review_item, parent, false);
            }


        if(getGroupId(groupPosition)==0 && mTrailers.size()>0) {
            trailer = (TextView) view.findViewById(R.id.media_text);
            trailer.setText(mTrailers.get(childPosition));

            play = (ImageButton)view.findViewById(R.id.media);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(trailersModelList.get(childPosition).getTrailer()));
                            mContext.startActivity(intent);

                }
            });

        }else if  (getGroupId(groupPosition)==1 && mReviews.size()>0){
            review = (TextView)view.findViewById(R.id.review);
            author = (TextView)view.findViewById(R.id.author);
            review.setText("\""+mReviews.get(childPosition)+"\"");
            author.setText(mAuthors.get(childPosition));

        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
