package com.ka.task1;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.ka.task1.adpter.PhotoAdapter;
import com.ka.task1.model.Photo;
import com.ka.task1.network.RetroRepository;
import com.ka.task1.network.RetroServiceInterface;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private MutableLiveData<PagedList<Photo>> recentPhotos;
    public static final String BASE_URL = "https://api.flickr.com/";
    public static final String API_KEY = "6f102c62f41998d151e5a1b48713cf13"; // Replace with your actual API key
    public static PhotoAdapter photoAdapter;
    private RetroRepository repository;
    private Snackbar retrySnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
        setRecyclerView();
        showRetrySnackbar();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycleimg);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        photoAdapter = new PhotoAdapter(PhotoAdapter.DIFF_CALLBACK);
        recyclerView.setAdapter(photoAdapter);
    }
    private void setRecyclerView() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetroServiceInterface service = retrofit.create(RetroServiceInterface.class);
        repository = new RetroRepository(service);

        // Create a LiveData<PagedList<Photo>> using the repository
        LiveData<PagedList<Photo>> pagedListLiveData = repository.getPagedListLiveData(API_KEY, "cat", 10);

        // Observe the result
        pagedListLiveData.observe(this, photos -> {
            Log.d("YourActivity", "Received photos: " + photos);
            photoAdapter.submitList(photos);
        });
    }


    private void showRetrySnackbar() {
        retrySnackbar = Snackbar.make(findViewById(R.id.container), "Network failure. Retry?", Snackbar.LENGTH_INDEFINITE);
        retrySnackbar.setAction("Retry", v -> {
            // Perform retry action

        });
        retrySnackbar.show();
    }
}
