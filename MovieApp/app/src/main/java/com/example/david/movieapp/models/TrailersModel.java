package com.example.david.movieapp.models;

/**
 * Created by David on 2016/04/22.
 */
 public class TrailersModel {

private String id ;
private String iso_639_1;
private String iso_3166_1 ;
private String key ;
private String name ;
private String site ;
private int size ;
private String type ;

public String getTrailer() {

        return "http://www.youtube.com/watch?v="+key ;

        }

public String getId() {
        return id;
        }

public void setId(String id) {
        this.id = id;
        }
        }
