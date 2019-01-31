package com.nite.projecteuropa.Views;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nite.projecteuropa.DTO.Flick;
import com.nite.projecteuropa.R;
import com.nite.projecteuropa.Tools.Controller;
import com.nite.projecteuropa.Tools.HandyCallback;
import com.squareup.picasso.Picasso;

import cz.msebera.android.httpclient.Header;

public class Watcher extends AppCompatActivity {

    Flick flick = null;
    String imdbId = "";
    String url = "http://juble.xyz:8185/";
    private Menu menu;
    private Boolean liked = true;
    private Boolean seen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watcher);


        Intent intent = getIntent();
        final String imdbId = intent.getStringExtra("imdbId");
        final Watcher me = this;
        this.imdbId = imdbId;
        flick = new Flick(imdbId, new HandyCallback() {
            @Override
            public void success(String details) {
                // main
                me.setTitle(flick.getTitle());
                // Title
                TextView txtTitle = (TextView) me.findViewById(R.id.title);
                txtTitle.setText(flick.getTitle());

                // Released Mini
                TextView txtReleasedMini = (TextView) me.findViewById(R.id.released_mini);
                txtReleasedMini.setText(Html.fromHtml("<b>"+flick.getReleased()+"</b>"));

                // Image
                ImageView thumbnail = (ImageView) me.findViewById(R.id.flick_img);
                if(flick.getPoster() != "") {
                    Picasso.with(me).load(flick.getPoster()).into(thumbnail);
                }

                // Rating Imdb Stars
                RatingBar ratingImdb  = (RatingBar) me.findViewById(R.id.imdbrating);
                ratingImdb.setRating(flick.getImdbRating().equals("N/A") ? 0 : Float.valueOf(flick.getImdbRating()));

                // Rating Imdb Init Text
                TextView ratingImdbInit  = (TextView) me.findViewById(R.id.imdbratingText1);
                ratingImdbInit.setText(Html.fromHtml("<b>IMBb</b>"));
                Typeface type = Typeface.createFromAsset(me.getAssets(),"imdbfont.ttf");
                ratingImdbInit.setTypeface(type);
                ratingImdbInit.setTextColor(Color.parseColor("#B4A000"));

                // Rating Imdb Literal Text
                TextView ratingImdbLiteralText  = (TextView) me.findViewById(R.id.imdbratingText2);
                ratingImdbLiteralText.setText(Html.fromHtml("<b>"+flick.getImdbRating()+" /10 ("+flick.getImdbVotes()+" VOTES)</b>"));

                // Metascore
                TextView txtMetascore = (TextView) me.findViewById(R.id.metascore);
                txtMetascore.setText(Html.fromHtml("<b>METASCORE</b> "+flick.getMetascore()));
//
//                // Released
//                TextView txtReleased = (TextView) me.findViewById(R.id.released);
//                txtReleased.setText(Html.fromHtml("<b>RELEASED</b> "+flick.getReleased()));

                // Genre
                TextView txtGenre = (TextView) me.findViewById(R.id.genre);
                txtGenre.setText(Html.fromHtml("<b>GENRES</b> "+flick.getGenre()));

                // Runtime
                TextView txtRuntime = (TextView) me.findViewById(R.id.runtime);
                txtRuntime.setText(Html.fromHtml("<b>RUNTIME</b> "+flick.getRuntime()));

                // Description
                TextView txtDescription = (TextView) me.findViewById(R.id.description);
                txtDescription.setText(Html.fromHtml("<b>PLOT</b> "+flick.getPlotFull()));

                // Actors
                TextView txtActors = (TextView) me.findViewById(R.id.actors);
                txtActors.setText(Html.fromHtml("<b>ACTOR(s)</b> "+flick.getActors()));

                // Director
                TextView txtDirector = (TextView) me.findViewById(R.id.director);
                txtDirector.setText(Html.fromHtml("<b>DIRECTOR(s)</b> "+flick.getDirector()));

                // Writer
                TextView txtWriter = (TextView) me.findViewById(R.id.writer);
                txtWriter.setText(Html.fromHtml("<b>WRITER(s)</b> "+flick.getWriter()));

                // Country
                TextView txtCountry = (TextView) me.findViewById(R.id.country);
                txtCountry.setText(Html.fromHtml("<b>COUNTRY</b> "+flick.getCountry()));

                // Awards
                TextView txtAwards = (TextView) me.findViewById(R.id.awards);
                txtAwards.setText(Html.fromHtml("<b>AWARD(s)</b> "+flick.getAwards()));

                // Type
                TextView txtType = (TextView) me.findViewById(R.id.type);
                txtType.setText(Html.fromHtml("<b>"+flick.getType()+"</b>"));

                // Box Office
                TextView txtBoxOffice = (TextView) me.findViewById(R.id.boxoffice);
                txtBoxOffice.setText(Html.fromHtml("<b>BOX OFFICE</b> "+flick.getBoxOffice()));

                // Production
                TextView txtProduction = (TextView) me.findViewById(R.id.production);
                txtProduction.setText(Html.fromHtml("<b>PRODUCTION</b> "+flick.getProduction()));

                // Rotten Tomatoes
                TextView txtRottenTomatoes = (TextView) me.findViewById(R.id.rottenTomatoes);
                txtRottenTomatoes.setText(Html.fromHtml("<b>ROTTEN TOMATOES</b> "+flick.getRottenTomatoes()));


            }

            @Override
            public void failure(String details) {

            }
        });

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#990040"));
        setTitle("");
//
//        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        this.menu = menu;
        final Menu currentMenu = menu;
        inflater.inflate(R.menu.watcher, menu);

        final String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        final Watcher me = this;

        RequestParams paramsLiked = new RequestParams();
        paramsLiked.put("userId", android_id);
        paramsLiked.put("imdbId", imdbId);
        paramsLiked.put("method", "isLikedFlick");
        Controller.getClient().get(Controller.getHostEuropa(), paramsLiked, new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String res) {
                    JsonObject jsonObject = new JsonParser().parse(res).getAsJsonObject();
                    Boolean liked = jsonObject.get("liked").getAsBoolean();
                    MenuItem button = currentMenu.findItem(R.id.liked_button);
                    if(liked){
                        me.liked = true;
                        button.setIcon(getResources().getDrawable(R.drawable.ic_liked));
                    }else{
                        me.liked = false;
                        button.setIcon(getResources().getDrawable(R.drawable.ic_dislike));
                    }

                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    ;
                }
            }
        );

        RequestParams paramsSeen = new RequestParams();
        paramsSeen.put("userId", android_id);
        paramsSeen.put("imdbId", imdbId);
        paramsSeen.put("method", "isSeenFlick");
        Controller.getClient().get(Controller.getHostEuropa(), paramsSeen, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String res) {
                        JsonObject jsonObject = new JsonParser().parse(res).getAsJsonObject();
                        Boolean seen = jsonObject.get("seen").getAsBoolean();
                        MenuItem button = currentMenu.findItem(R.id.seen_button);
                        if(seen){
                            me.seen = true;
                            button.setIcon(getResources().getDrawable(R.drawable.ic_seen));
                        }else{
                            me.seen = false;
                            button.setIcon(getResources().getDrawable(R.drawable.ic_unseen));
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        ;
                    }
                }
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        final Watcher me = this;
        final MenuItem finalItem = item;
        switch (item.getItemId()) {
            case R.id.liked_button:
                final Boolean liked = me.liked;
                RequestParams paramsLike = new RequestParams();
                paramsLike.put("userId", android_id);
                paramsLike.put("imdbId", imdbId);
                paramsLike.put("method", liked ? "unlikeFlick" : "likeFlick");
                Controller.getClient().get(Controller.getHostEuropa(), paramsLike, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            if(liked) {
                                finalItem.setIcon(getResources().getDrawable(R.drawable.ic_dislike));
                            }else{
                                finalItem.setIcon(getResources().getDrawable(R.drawable.ic_liked));
                            }
                            me.liked = !me.liked;
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {

                        }
                    }
                );
                return true;
            case R.id.seen_button:
                final Boolean seen = me.seen;

                RequestParams paramsSeen = new RequestParams();
                paramsSeen.put("userId", android_id);
                paramsSeen.put("imdbId", imdbId);
                paramsSeen.put("method", seen ? "unlikeFlick" : "likeFlick");
                Controller.getClient().get(Controller.getHostEuropa(), paramsSeen, new TextHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String res) {
                                if(seen) {
                                    finalItem.setIcon(getResources().getDrawable(R.drawable.ic_unseen));
                                }else{
                                    finalItem.setIcon(getResources().getDrawable(R.drawable.ic_seen));
                                }
                                me.seen = !me.seen;
                            }
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {

                            }
                        }
                );
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
