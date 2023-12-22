package com.ka.task1.network;

import static com.ka.task1.MainActivity.searchResultProgressBar;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

    /**
     * An interface for handling retry actions.
     */
    public interface RetryCallback {
        void onRetry();
    }

    /**
     * Set retry callback
     */

    public void setRetryCallback(RetryCallback retryCallback) {
        this.retryCallback = retryCallback;
    }

    /**
     * Method for initial data load
     *
     * @param apiKey
     * @param searchText
     * @param page
     * @param initialCallback
     */
    public void loadInitial(String apiKey, String searchText, int page, PageKeyedDataSource.LoadInitialCallback<Integer, Photo> initialCallback) {
        makeAPICall(apiKey, searchText, page, initialCallback, null, null);
    }

    /**
     * Method for subsequent data loads
     *
     * @param apiKey
     * @param searchText
     * @param page
     * @param callback
     */
    public void loadAfter(String apiKey, String searchText, int page, PageKeyedDataSource.LoadCallback<Integer, Photo> callback) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            // UI-related code should be executed on the main thread
            searchResultProgressBar.setVisibility(View.VISIBLE);

            Context context = searchResultProgressBar.getContext();
            int pageno = page - 1;
            Toast.makeText(context, "Loading Page" + pageno + " data...", Toast.LENGTH_SHORT).show();
        });

        makeAPICall(apiKey, searchText, page, null, callback, handler);
    }

    /**
     * Makes an API call to retrieve data from the server.
     *
     * @param apiKey          The API key for authentication.
     * @param searchText      The text to be searched.
     * @param page            The page number for pagination.
     * @param initialCallback Callback for initial data load (can be null for subsequent data loads).
     * @param callback        Callback for subsequent data loads (can be null for initial data load).
     * @param handler         A handler to post actions on the main thread.
     */
    private void makeAPICall(String apiKey, String searchText, int page, PageKeyedDataSource.LoadInitialCallback<Integer, Photo> initialCallback, PageKeyedDataSource.LoadCallback<Integer, Photo> callback, Handler handler) {
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


        call.enqueue(new Callback<RecyclerList>() {
            @Override
            public void onResponse(Call<RecyclerList> call, Response<RecyclerList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Photo> photos = response.body().getPhotos().getItems();
                    Log.d("YourActivity", "API Request URL: " + call.request().url());

                    if (initialCallback != null) {
                        // Load initial data
                        initialCallback.onResult(photos, null, page + 1);
                    } else if (callback != null) {
                        // Load next page
                        handler.post(() -> {  // Use Handler to post on the main thread
                            callback.onResult(photos, page + 1);
                            searchResultProgressBar.setVisibility(View.GONE);
                        });
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

    /**
     * Returns a LiveData containing a PagedList of Photo objects.
     *
     * @param apiKey     The API key for authentication.
     * @param searchText The text to be searched.
     * @param pageSize   The number of items loaded at a time in the PagedList.
     * @return A LiveData object containing the PagedList of Photo objects.
     */
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
