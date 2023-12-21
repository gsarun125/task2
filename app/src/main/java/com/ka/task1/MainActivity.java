// MainActivity.java
package com.ka.task1;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
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

    PhotoAdapter photoAdapter;
    public static final String BASE_URL = "https://api.flickr.com/";
    public static final String API_KEY = "6f102c62f41998d151e5a1b48713cf13"; // Replace with your actual API key
    private MutableLiveData<List<Photo>> recentPhotos;
    private Snackbar retrySnackbar;
    private RetroRepository repository;



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
        photoAdapter = new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);
    }

    private void setRecyclerView() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetroServiceInterface service = retrofit.create(RetroServiceInterface.class);
        recentPhotos = new MutableLiveData<>();
        repository = new RetroRepository(service);

        // Make the initial API call with the search text
        makeApiCall("cat");

        // Observe the result
        recentPhotos.observe(this, photos -> {
            Log.d("YourActivity", "Received photos: " + photos);
            if (photos != null) {
                photoAdapter.setData(photos);
            }
        });
    }

    private void makeApiCall(String searchText) {
        repository.makeAPICall(API_KEY, searchText, recentPhotos, null);
    }

    private void showRetrySnackbar() {
        retrySnackbar = Snackbar.make(findViewById(R.id.container), "Network failure. Retry?", Snackbar.LENGTH_INDEFINITE);
        retrySnackbar.setAction("Retry", v -> {
            // Perform retry action
            makeApiCall("cat");
        });
        retrySnackbar.show();
    }
}
