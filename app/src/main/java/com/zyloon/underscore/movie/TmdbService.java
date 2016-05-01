package com.zyloon.underscore.movie;

import java.util.List;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by _underscore on 30-04-2016.
 */
public interface TmdbService {

    @GET("3/discover/movie?api_key=f4b37cafc154ec6c25da661351601c7f")
    Call<MovieList> listMovies();
}
