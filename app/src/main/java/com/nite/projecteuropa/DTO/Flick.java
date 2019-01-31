package com.nite.projecteuropa.DTO;

import android.widget.BaseAdapter;

import cz.msebera.android.httpclient.Header;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nite.projecteuropa.Tools.Controller;
import com.nite.projecteuropa.Tools.HandyCallback;

public class Flick {

    private String imdbId;
    private String imdbRating;
    private String imdbVotes;
    private String title;
    private String year;
    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String writer;
    private String actors;
    private String plot;
    private String plotFull;
    private String language;
    private String country;
    private String awards;
    private String boxOffice;
    private String Production;
    private String rottenTomatoes;
    private String poster;
    private String metascore;
    private String type;

    public Flick(String imdbId, final HandyCallback callback){
        clear();
        this.get(imdbId, callback);
    }

    public void get(final String imdbId, final HandyCallback callback){
        final AsyncHttpClient client = Controller.getClient();
        RequestParams params = new RequestParams();
        params.put("i", imdbId);
        params.put("plot", "short");
        params.put("r", "json");
        this.imdbId = imdbId;
        // Full plot
        client.get(Controller.getHostOMDB(), params, new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String res) {
                    System.out.println(res);
                    JsonObject jsonObject = new JsonParser().parse(res).getAsJsonObject();
                    if(jsonObject.get("Response").getAsString().equals("False")){
                        System.out.println("ohshit");
                        return;
                    }
                    imdbRating = jsonObject.get("imdbRating").getAsString();
                    imdbVotes = jsonObject.get("imdbVotes").getAsString();
                    title = jsonObject.get("Title").getAsString();
                    year = jsonObject.get("Year").getAsString();
                    rated = jsonObject.get("Rated").getAsString();
                    released = jsonObject.get("Released").getAsString();
                    runtime = jsonObject.get("Runtime").getAsString();
                    genre = jsonObject.get("Genre").getAsString();
                    director = jsonObject.get("Director").getAsString();
                    writer = jsonObject.get("Writer").getAsString();
                    actors = jsonObject.get("Actors").getAsString();
                    plot = jsonObject.get("Plot").getAsString();
                    language = jsonObject.get("Language").getAsString();
                    country = jsonObject.get("Country").getAsString();
                    awards = jsonObject.get("Awards").getAsString();
                    poster = jsonObject.get("Poster").getAsString();
                    type = jsonObject.get("Type").getAsString();
                    switch(type){
                        case "series":
                            type = "TV Series";
                            break;
                        case "movie":
                            type = "Film";
                            break;
                        default:
                            type = "N/A";
                            break;
                    }
                    rottenTomatoes = "N/A";
                    try {
                        JsonArray specificRatings = jsonObject.get("Ratings").getAsJsonArray();
                        for (int i = 0; i < specificRatings.size(); ++i) {
                            JsonObject current = specificRatings.get(i).getAsJsonObject();
                            switch (current.get("Source").getAsString()) {
                                case "Rotten Tomatoes":
                                    rottenTomatoes = current.get("Value").getAsString();
                                    break;
                            }
                        }
                    }
                    catch(Exception ex){

                    }
                    metascore = jsonObject.get("Metascore").getAsString();
                    try {
                        boxOffice = jsonObject.get("BoxOffice").getAsString();
                    }
                    catch(Exception ex){
                        boxOffice = "N/A";
                    }
                    try {
                        Production = jsonObject.get("Production").getAsString();
                    }
                    catch(Exception ex){
                        Production = "N/A";
                    }
                    final RequestParams paramsShort = new RequestParams();
                    paramsShort.put("i", imdbId);
                    paramsShort.put("plot", "full");
                    paramsShort.put("r", "json");
                    // Short plot
                    client.get(Controller.getHostOMDB(), paramsShort, new TextHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String res) {
                                System.out.println(res);
                                JsonObject jsonObject = new JsonParser().parse(res).getAsJsonObject();
                                plotFull = jsonObject.get("Plot").getAsString();
                                if(callback != null){
                                    callback.success(null);
                                }
                            }
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                                t.printStackTrace();
                                callback.failure(null);
                            }
                        }
                    );
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    t.printStackTrace();
                    callback.failure(null);
                }
            }
        );
    }

    public void clear(){
        this.imdbId = "";
        this.imdbRating = "";
        this.imdbVotes = "";
        this.title = "";
        this.year = "";
        this.rated = "";
        this.released = "";
        this.runtime = "";
        this.genre = "";
        this.director = "";
        this.writer = "";
        this.actors = "";
        this.plot = "";
        this.language = "";
        this.country = "";
        this.awards = "";
        this.plotFull = "";
        this.poster = "";
        this.metascore = "";
        this.type = "";
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public String getProduction() {
        return Production;
    }

    public String getRottenTomatoes() {
        return rottenTomatoes;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getRated() {
        return rated;
    }

    public String getReleased() {
        return released;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getWriter() {
        return writer;
    }

    public String getActors() {
        return actors;
    }

    public String getPlot() {
        return plot;
    }

    public String getPlotFull() {
        return plotFull;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public String getAwards() {
        return awards;
    }

    public String getPoster() {
        return poster;
    }

    public String getMetascore() {
        return metascore;
    }

    public String getType() {
        return type;
    }
}
