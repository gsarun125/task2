// RetroRepository.java
package com.ka.task1.network;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ka.task1.model.Photo;
import com.ka.task1.model.RecyclerList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetroRepository {

    private RetroServiceInterface retroServiceInterface;

    public RetroRepository(RetroServiceInterface retroServiceInterface) {
        this.retroServiceInterface = retroServiceInterface;
    }

    public interface RetryCallback {
        void onRetry();
    }

    public void makeAPICall(String apiKey, String searchText, MutableLiveData<List<Photo>> recentPhotos, RetryCallback retryCallback) {
        Call<RecyclerList> call = retroServiceInterface.getRecentPhotos(
                "flickr.photos.search",
                apiKey,
                10, // per_page
                1,  // page
                "json",
                1,  // nojsoncallback
                "url_s",
                searchText
        );

        Log.d("YourActivity", "API Request URL: " + call.request().url());

        call.enqueue(new Callback<RecyclerList>() {
            @Override
            public void onResponse(Call<RecyclerList> call, Response<RecyclerList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recentPhotos.postValue(response.body().getPhotos().getItems());

                } else {
                    // Handle API error
                    Log.e("YourActivity", "API Error: " + response.code() + " " + response.message());
                    recentPhotos.postValue(null);

                    // Trigger retry callback on network failure
                    if (retryCallback != null) {
                        retryCallback.onRetry();
                    }
                }
            }

            @Override
            public void onFailure(Call<RecyclerList> call, Throwable t) {
                Log.e("YourActivity", "API Call Failure: " + t.getMessage());

                // Log the request URL
                Log.e("YourActivity", "API Request URL: " + call.request().url());

                // Handle network failure
                recentPhotos.postValue(null);

                // Trigger retry callback on network failure
                if (retryCallback != null) {
                    retryCallback.onRetry();
                }
            }
        });
    }
}
