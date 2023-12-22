package com.ka.task1.network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.ka.task1.model.Photo;
import com.ka.task1.model.RecyclerList;
import com.ka.task1.paging.PhotoBoundaryCallback;
import com.ka.task1.paging.PhotoDataSourceFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetroRepository {

    private RetroServiceInterface retroServiceInterface;
    private RetryCallback retryCallback;
    public RetroRepository(RetroServiceInterface retroServiceInterface) {
        this.retroServiceInterface = retroServiceInterface;
    }
    public interface RetryCallback {
        void onRetry();
    }

    // Set retry callback
    public void setRetryCallback(RetryCallback retryCallback) {
        this.retryCallback = retryCallback;
    }
    // Method for initial data load
    public void loadInitial(String apiKey, String searchText, int page, PageKeyedDataSource.LoadInitialCallback<Integer, Photo> initialCallback) {
        makeAPICall(apiKey, searchText, page, initialCallback, null);
    }

    // Method for subsequent data loads
    public void loadAfter(String apiKey, String searchText, int page, PageKeyedDataSource.LoadCallback<Integer, Photo> callback) {
        makeAPICall(apiKey, searchText, page, null, callback);
    }

    private void makeAPICall(String apiKey, String searchText, int page, PageKeyedDataSource.LoadInitialCallback<Integer, Photo> initialCallback, PageKeyedDataSource.LoadCallback<Integer, Photo> callback) {
        Call<RecyclerList> call = retroServiceInterface.getRecentPhotos(
                "flickr.photos.search",
                apiKey,
                10, // per_page
                page, // page number
                "json",
                1, // nojsoncallback
                "url_s",
                searchText
        );

        Log.d("YourActivity", "API Request URL: " + call.request().url());

        call.enqueue(new Callback<RecyclerList>() {
            @Override
            public void onResponse(Call<RecyclerList> call, Response<RecyclerList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Photo> photos = response.body().getPhotos().getItems();

                    if (initialCallback != null) {
                        // Load initial data
                        initialCallback.onResult(photos, null, page + 1);
                    } else if (callback != null) {
                        // Load next page
                        callback.onResult(photos, page + 1);
                    }
                } else {
                    if (retryCallback != null) {
                        retryCallback.onRetry();
                    }
                    Log.e("YourActivity", "API Error: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<RecyclerList> call, Throwable t) {
                Log.e("YourActivity", "API Call Failure: " + t.getMessage());

                if (retryCallback != null) {
                    retryCallback.onRetry();
                }
                Log.e("YourActivity", "API Request URL: " + call.request().url());
            }
        });
    }

    public LiveData<PagedList<Photo>> getPagedListLiveData(String apiKey, String searchText, int pageSize) {
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setEnablePlaceholders(false)
                .build();

        PhotoDataSourceFactory dataSourceFactory = new PhotoDataSourceFactory(this, apiKey, searchText);

        return new LivePagedListBuilder<>(dataSourceFactory, config)
                .setBoundaryCallback(new PhotoBoundaryCallback(this, apiKey, searchText))
                .build();
    }
}
