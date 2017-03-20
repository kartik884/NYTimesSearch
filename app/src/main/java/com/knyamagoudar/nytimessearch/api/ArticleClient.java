package com.knyamagoudar.nytimessearch.api;

import android.util.Log;

import com.knyamagoudar.nytimessearch.models.Filter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

/**
 * Created by knyamagoudar on 3/18/17.
 */

public class ArticleClient {

    private static final String API_BASE_URL = "https://api.nytimes.com/svc/search/v2/";
    private static final String API_KEY = "7203d4da00f94362a7a10ca1ecf92695";

    private AsyncHttpClient asyncHttpClient;

    public ArticleClient(){
        this.asyncHttpClient = new AsyncHttpClient();
    }

    public String getSearchUrl(String relativePath){
        return API_BASE_URL + relativePath;
    }

    public String parseDateString(String date){
        String[] parts = date.split("/");
        String newFormat;

        if(parts[0].length() == 1 && parts[1].length() == 1){
            newFormat= ""+parts[2]+"0"+parts[0]+"0"+parts[1];
        }else if(parts[0].length() == 1){
            newFormat= ""+parts[2]+"0"+parts[0]+parts[1];
        }else if(parts[1].length() == 1){
            newFormat= ""+parts[2]+parts[0]+"0"+parts[1];
        }else{
            newFormat= ""+parts[2]+parts[0]+parts[1];
        }

        return newFormat;
    }

    //get the articles from the search query
    public void getSearchArticles(String query, JsonHttpResponseHandler handler){

        String url = getSearchUrl("articlesearch.json");
        RequestParams reqParams = new RequestParams();
        reqParams.put("api-key",API_KEY);
        reqParams.put("q",query);
        reqParams.put("page",0);

        try{

                if(Filter.beginDate != null){
                    reqParams.put("begin_date",parseDateString(Filter.beginDate));
                }

                if(Filter.endDate != null){
                    reqParams.put("end_date",parseDateString(Filter.endDate));
                }

                if(Filter.sortOrder != null){
                    reqParams.put("sort",Filter.sortOrder);
                }

                String newsDesk = "news_desk:(";
                if(Filter.newsDeskValues != null){
                    ArrayList<String> newsDeskArray = Filter.newsDeskValues;
                    for(int i=0;i<newsDeskArray.size();i++){
                        newsDesk += "\""+newsDeskArray.get(i)+"\" ";
                    }

                    newsDesk += ")";
                    reqParams.put("fq",newsDesk);
                }

        }catch(Exception ex){
            ex.printStackTrace();
        }

        asyncHttpClient.get(url, reqParams, handler);
    }

    public void getPage(String query,int page, JsonHttpResponseHandler handler){

        String url = getSearchUrl("articlesearch.json");
        RequestParams reqParams = new RequestParams();
        reqParams.put("api-key",API_KEY);
        reqParams.put("q",query);
        reqParams.put("page",page);

        try{
                if(Filter.beginDate != null){
                    reqParams.put("begin_date",parseDateString(Filter.beginDate));
                }

                if(Filter.endDate != null){
                    reqParams.put("end_date",parseDateString(Filter.endDate));
                }

                if(Filter.sortOrder != null){
                    reqParams.put("sort",Filter.sortOrder);
                }

                String newsDesk = "news_desk:(";
                if(Filter.newsDeskValues != null){
                    ArrayList<String> newsDeskArray = Filter.newsDeskValues;
                    for(int i=0;i<newsDeskArray.size();i++){
                        newsDesk += "\""+newsDeskArray.get(i)+"\" ";
                    }

                    newsDesk += ")";
                    reqParams.put("fq",newsDesk);

                }
                Log.d("info",reqParams.toString());

        }catch(Exception ex){
            ex.printStackTrace();
        }

        asyncHttpClient.get(url, reqParams, handler);
    }
}
