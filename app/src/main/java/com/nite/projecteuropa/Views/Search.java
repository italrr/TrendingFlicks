package com.nite.projecteuropa.Views;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nite.projecteuropa.DTO.Flick;
import com.nite.projecteuropa.Tools.Controller;
import com.nite.projecteuropa.Customs.FlickAdapter;
import com.nite.projecteuropa.R;
import com.nite.projecteuropa.Tools.HandyCallback;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Search extends AppCompatActivity {

    private String searchQuery = "";
    FlickAdapter flickAdapter;
    ArrayList<Flick> topFlicks = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        searchQuery = intent.getStringExtra("searchQuery");

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#990040"));
        setTitle("\""+searchQuery+"\"");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        final Search me = this;
        ListView flickListView = (ListView)findViewById( R.id.main_list );
        flickAdapter = new FlickAdapter(topFlicks, this.getApplicationContext(), this);
        flickListView.setAdapter(flickAdapter);


        RequestParams params = new RequestParams();
        params.put("s", searchQuery);
        params.put("r", "json");

        Controller.getClient().get(Controller.getHostOMDB(), params, new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String res) {
                    JsonObject jsonObject = new JsonParser().parse(res).getAsJsonObject();
                    JsonArray array = jsonObject.getAsJsonArray("Search");
                    for(int i = 0; i < array.size(); ++i){
                        final String imdbId = array.get(i).getAsJsonObject().get("imdbID").getAsString();
                        topFlicks.add(new Flick(imdbId, new HandyCallback() {
                            @Override
                            public void success(String details) {
                                flickAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void failure(String details) {

                            }
                        }));
                    }
                    flickAdapter.notifyDataSetChanged();
                    Toast.makeText(me, String.valueOf(array.size())+" flick(s) found.", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    ;
                }
            }
        );

        flickListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(me, Watcher.class);
                intent.putExtra("imdbId", topFlicks.get(i).getImdbId());
                me.startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
