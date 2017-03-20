package com.knyamagoudar.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by knyamagoudar on 3/14/17.
 */

public class Article implements Parcelable {

    String webUrl;
    String headLine;
    String thumbNail;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadLine() {
        return headLine;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public Article(JSONObject jsonObject){
        try{
            this.webUrl = jsonObject.getString("web_url");
            this.headLine = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if(multimedia.length() > 0){
                JSONObject multimediaObj = multimedia.getJSONObject(0);
                this.thumbNail = "http://www.nytimes.com/"+multimediaObj.getString("url");
            }else{
                this.thumbNail = "";
            }
        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray jsonArray){

        ArrayList<Article> results = new ArrayList<>();

        for(int i=0;i<jsonArray.length();i++){
            try{
                 results.add(new Article(jsonArray.getJSONObject(i)));
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }
        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.webUrl);
        dest.writeString(this.thumbNail);
        dest.writeString(this.headLine);
    }

    protected Article(Parcel in) {
        this.webUrl = in.readString();
        this.thumbNail = in.readString();
        this.headLine = in.readString();
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
