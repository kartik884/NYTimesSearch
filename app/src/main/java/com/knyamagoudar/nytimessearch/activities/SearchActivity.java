package com.knyamagoudar.nytimessearch.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.knyamagoudar.nytimessearch.R;
import com.knyamagoudar.nytimessearch.adapters.ArticleArrayAdapter;
import com.knyamagoudar.nytimessearch.adapters.EndlessScrollListener;
import com.knyamagoudar.nytimessearch.api.ArticleClient;
import com.knyamagoudar.nytimessearch.fragments.FilterFragment;
import com.knyamagoudar.nytimessearch.models.Article;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements FilterFragment.OnFragmentInteractionListener
{

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;
    ArticleClient articleClient;
    ArrayList<Article> articles;

    ArticleArrayAdapter adapter;


    String query;
    boolean success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(isNetworkAvailable()){
            gvResults = (GridView) findViewById(R.id.gvResults);
            articles = new ArrayList<>();
            adapter = new ArticleArrayAdapter(this,articles);
            gvResults.setAdapter(adapter);
//            etQuery = (EditText) findViewById(R.id.etQuery);
//            btnSearch = (Button) findViewById(R.id.btnSearch);
//
//            btnSearch.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onArticleSearch(query);
//                }
//            });

            articleClient = new ArticleClient();

            gvResults.setOnScrollListener(new EndlessScrollListener(0,0) {
                @Override
                public boolean onLoadMore(int page, int totalItemsCount) {

                    loadPage(page);
                    return true;
                }
            });

            gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //create intent
                    Intent i = new Intent(getApplicationContext(),ArticleActivity.class);

                    //get the arrticle to display
                    Article article = articles.get(position);

                    i.putExtra("article",article);
                    //start the activity
                    startActivity(i);
                }
            });


        }else {
            Toast.makeText(getApplicationContext(), "No Internet Access", Toast.LENGTH_SHORT).show();
        }
    }

    public void setUpViews(){
//        gvResults = (GridView) findViewById(R.id.gvResults);
//        articles = new ArrayList<>();
//        adapter = new ArticleArrayAdapter(this,articles);
//        gvResults.setAdapter(adapter);
//
//        articleClient = new ArticleClient();
//        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //create intent
//                Intent i = new Intent(getApplicationContext(),ArticleActivity.class);
//
//                //get the arrticle to display
//                Article article = articles.get(position);
//
//                i.putExtra("article",article);
//                //start the activity
//                startActivity(i);
//            }
//        });
//
//        gvResults.setOnScrollListener(new EndlessScrollListener(0,0) {
//            @Override
//            public boolean onLoadMore(int page, int totalItemsCount) {
//
//                loadPage(page);
//                return true;
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        setUpSearchView(menu);
        setUpFilterView(menu);

        return true;
    }

    public void setUpFilterView(Menu menu){

        MenuItem filterItem = menu.findItem(R.id.action_filter);

        filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //Toast.makeText(getApplicationContext(), "FilterFragment Item Clicked", Toast.LENGTH_LONG).show();

                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                FilterFragment filterFragment = FilterFragment.newInstance("temp","temp");
                //editFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
                filterFragment.show(fm,"fragment_filter");
                return false;
            }
        });

    }

    public void setUpSearchView(Menu menu){

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //get the articles based on the search

                onArticleSearch(query);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }




        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(String query) {

        this.query = query;
        adapter.clear();

        articleClient.getSearchArticles(query,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("DEBUG", response.toString());
                JSONArray articleResult = null;
                try{

                    articleResult = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleResult));
                    //adapter.addAll(articles);
                    adapter.notifyDataSetChanged();
                    success = true;
                }catch(JSONException ex) {
                    ex.printStackTrace();
                    success = false;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                success = false;
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                success = false;
            }
        });
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    public void loadPage(int page){

        articleClient.getPage(query,page,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleResult = null;
                try{

                    articleResult = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleResult));
                    //adapter.addAll(articles);
                    adapter.notifyDataSetChanged();
                }catch(JSONException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
