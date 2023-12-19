package com.ka.task1.network;

import com.ka.task1.model.RecyclerList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetroServiceInterface {
    @GET("services/rest")
    Call<RecyclerList> getRecentPhotos(
            @Query("method") String method,
            @Query("api_key") String apiKey,
            @Query("per_page") int perPage,
            @Query("page") int page,
            @Query("format") String format,
            @Query("nojsoncallback") int noJsonCallback,
            @Query("extras") String extras
    );
}
