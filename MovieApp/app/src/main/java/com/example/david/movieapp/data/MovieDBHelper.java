package com.example.david.movieapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.david.movieapp.models.MoviesModel;

import java.util.ArrayList;

/**
 * Created by David on 2016/04/22.
 */
public class MovieDBHelper extends SQLiteOpenHelper{

    ArrayList<MoviesModel> moviesModelArrayList = new ArrayList<>();


    public MovieDBHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_STATEMENT = "CREATE TABLE "
                + Constants.TABLE_NAME
                + " ("
                + Constants.PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Constants.MOVIE_ID + " INTEGER NOT NULL,"
                + Constants.POSTER_PATH + " TEXT NOT NULL,"
                + Constants.RELEASE_DATE + " TEXT NOT NULL,"
                + Constants.ORIGINAL_TITLE + " TEXT NOT NULL,"
                + Constants.OVERVIEW + " TEXT NOT NULL,"
                + Constants.VOTE_AVERAGE + " TEXT NOT NULL);"
                ;

        db.execSQL(CREATE_STATEMENT);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ Constants.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public void addMovieToFavorite (MoviesModel moviesModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.MOVIE_ID,moviesModel.getId());
        contentValues.put(Constants.POSTER_PATH,moviesModel.getPoster_path());
        contentValues.put(Constants.RELEASE_DATE,moviesModel.getRelease_date());
        contentValues.put(Constants.VOTE_AVERAGE,moviesModel.getVote_average());
        contentValues.put(Constants.ORIGINAL_TITLE,moviesModel.getOriginal_title());
        contentValues.put(Constants.OVERVIEW,moviesModel.getOverview());

        sqLiteDatabase.insert(Constants.TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();


    }

    public ArrayList<MoviesModel> getFavorites (){

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(Constants.TABLE_NAME, new String[]{Constants.MOVIE_ID , Constants.POSTER_PATH , Constants.ORIGINAL_TITLE , Constants.RELEASE_DATE , Constants.VOTE_AVERAGE, Constants.OVERVIEW},null ,null,null,null,null);

        if (cursor.moveToFirst()){
            do {
                MoviesModel movie = new MoviesModel();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.MOVIE_ID)));
                movie.setOriginal_title(cursor.getString(cursor.getColumnIndexOrThrow(Constants.ORIGINAL_TITLE)));
                movie.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(Constants.POSTER_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(Constants.OVERVIEW)));
                movie.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow(Constants.RELEASE_DATE)));
                movie.setVote_average(cursor.getString(cursor.getColumnIndexOrThrow(Constants.VOTE_AVERAGE)));

                moviesModelArrayList.add(movie);

            }while ( cursor.moveToNext());

        }

        return moviesModelArrayList;
    }

    public void deleteFromFavorite(int id ){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(Constants.TABLE_NAME,Constants.MOVIE_ID + " = ? ", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
    }

    public  boolean isFavorite(int id){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String SELECT_STATEMENT = "SELECT * FROM "+ Constants.TABLE_NAME + " WHERE "+ Constants.MOVIE_ID + " =" + String.valueOf(id);

        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_STATEMENT,null);
        if (cursor.getCount()>0){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;

    }
}
