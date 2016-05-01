package com.zyloon.underscore.movie;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{

    ListView movieListView;
    List<Movie> data;
    Spinner spinner;
    Activity activity;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        List<String> categories = new ArrayList<String>();
        categories.add("Popularity");
        categories.add("Release Date");
        categories.add("Rating");
        data = new ArrayList<Movie>();

        spinner = (Spinner)findViewById(R.id.spinner);
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        loading = new ProgressDialog(this);
        loading.setMessage("Fetching Data, Please Wait...");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setIndeterminate(true);
        loading.show();

        movieListView = (ListView) findViewById(R.id.movieList);
        movieListView.setBackgroundColor(Color.LTGRAY);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TmdbService service = retrofit.create(TmdbService.class);
        Call<MovieList> call = service.listMovies();

        final boolean[] loaded = {false};
        new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                if(loaded[0] == false) {
                    Toast.makeText(getApplicationContext(), "Unable to Fetch Data", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }.start();

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                loaded[0] = true;
                data = response.body().getResults();
                MovieAdapter adapter = new MovieAdapter(activity, R.layout.movie_row, data);
                loading.dismiss();
                movieListView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {

            }
        });


        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,SummaryPopup.class);
                intent.putExtra("Summary",data.get(position).getOverview());
                startActivity(intent);
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String item = parent.getItemAtPosition(position).toString();

                if(!data.isEmpty()){
                    Collections.sort(data, new Comparator<Movie>() {
                        @Override
                        public int compare(Movie lhs, Movie rhs) {
                           if (item == "popularity") {
                               return -lhs.getPopularity().compareTo(rhs.getPopularity());
                         } else if (item == "Release Date") {
                                return -lhs.getReleaseDate().compareTo(rhs.getReleaseDate());
                          } else if (item == "Rating") {
                            return -lhs.getVoteAverage().compareTo(rhs.getVoteAverage());
                            }
                           return 0;
                    }
                    });
                     MovieAdapter adapter = new MovieAdapter(activity, R.layout.movie_row, data);

                    movieListView.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
