package com.ka.task1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.ka.task1.adpter.PhotoAdapter;
import com.ka.task1.model.Photo;
import com.ka.task1.network.RetroRepository;
import com.ka.task1.network.RetroServiceInterface;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    PhotoAdapter photoAdapter;
    private static final String BASE_URL = "https://api.flickr.com/";
    private static final String API_KEY = "6f102c62f41998d151e5a1b48713cf13"; // Replace with your actual API key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNav();
        initRecycleView();
        setRecycleView();

    }
    private  void initNav(){
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_home) {

            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

    }
    private void initRecycleView() {
        RecyclerView recyclerView = findViewById(R.id.recycleimg);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        photoAdapter = new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);
    }

    private void  setRecycleView(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetroServiceInterface service = retrofit.create(RetroServiceInterface.class);
        MutableLiveData<List<Photo>> recentPhotos = new MutableLiveData<>();
        RetroRepository repository = new RetroRepository(service);
        repository.makeAPICall(API_KEY, recentPhotos);
        recentPhotos.observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
                if (photos != null) {
                    photoAdapter.setData(photos);
                }
            }
        });
    }

}
