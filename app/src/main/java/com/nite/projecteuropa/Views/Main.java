package com.nite.projecteuropa.Views;

import android.app.SearchManager;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nite.projecteuropa.DTO.Flick;
import com.nite.projecteuropa.Tools.Controller;

import com.nite.projecteuropa.Customs.FlickAdapter;
import com.nite.projecteuropa.R;
import com.nite.projecteuropa.Tools.HandyCallback;

import java.lang.reflect.Field;
import java.util.ArrayList;


import cz.msebera.android.httpclient.Header;


public class Main extends AppCompatActivity {

    FlickAdapter flickAdapter;
    ArrayList<Flick> topFlicks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Main me = this;
        final String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        final ListView topFlickListView = (ListView)findViewById( R.id.main_list );
        flickAdapter = new FlickAdapter(topFlicks, this.getApplicationContext(), this);
        topFlickListView.setAdapter(flickAdapter);

        RequestParams params = new RequestParams();
        params.put("userId", android_id);
        params.put("method", "topFlicks");
        Controller.getClient().get(Controller.getHostEuropa(), params, new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String res) {
                    JsonObject jsonObject = new JsonParser().parse(res).getAsJsonObject();
                    JsonArray topFlicksJson = jsonObject.get("list").getAsJsonArray();
                    for(int i = 0; i < topFlicksJson.size(); ++i){
                        final String id = topFlicksJson.get(i).getAsJsonObject().get("imdbId").getAsString();
                        topFlicks.add(new Flick(id,
                            new HandyCallback() {
                                @Override
                                public void success(String details) {
                                    flickAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void failure(String details) {

                                }
                            }));
                        topFlickListView.setOnItemClickListener(new ListView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(me, Watcher.class);
                                intent.putExtra("imdbId", topFlicks.get(i).getImdbId());
                                me.startActivity(intent);
                            }
                        });
                        flickAdapter.notifyDataSetChanged();
                    }

                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {

                }
            }
        );
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.go_search:
//                Intent intent = new Intent(this, Search.class);
//                this.startActivity(intent);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.go_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        final Main me = this;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(me, Search.class);
                intent.putExtra("searchQuery", query);
                me.startActivity(intent);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
        }
        return true;
    }
}
