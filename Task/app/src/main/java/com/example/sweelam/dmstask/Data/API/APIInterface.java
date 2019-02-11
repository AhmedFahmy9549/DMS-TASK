package com.example.sweelam.dmstask.Data.API;

import com.example.sweelam.dmstask.Models.Module;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("users/square/repos?")
     Call <List<Module>> getTopRatedMovies(
            @Query("page") int page,
            @Query("per_page") int per_page,
            @Query("acess_token") String acess_token


    );
}

