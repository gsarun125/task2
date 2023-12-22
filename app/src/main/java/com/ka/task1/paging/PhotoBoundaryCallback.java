package com.ka.task1.paging;

import static android.app.ProgressDialog.show;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;

import com.ka.task1.MainActivity;
import com.ka.task1.model.Photo;
import com.ka.task1.network.RetroRepository;

public class PhotoBoundaryCallback extends PagedList.BoundaryCallback<Photo> {

    private final RetroRepository repository;
    private final String apiKey;
    private final String searchText;

    public PhotoBoundaryCallback(RetroRepository repository, String apiKey, String searchText) {
        this.repository = repository;
        this.apiKey = apiKey;
        this.searchText = searchText;
    }

    @Override
    public void onZeroItemsLoaded() {
        // Called when the PagedList is empty, typically at the beginning.
        // You can start loading the initial data here.
        repository.loadInitial(apiKey, searchText, 1, null);
    }

    @Override
    public void onItemAtEndLoaded(@NonNull Photo itemAtEnd) {

        // Called when the last item in the PagedList has been loaded.
        // You can start loading the next page of data here.
        repository.loadAfter(apiKey, searchText, itemAtEnd.getPage() + 1, null);
    }

    @Override
    public void onItemAtFrontLoaded(@NonNull Photo itemAtFront) {
        // Called when the first item in the PagedList has been loaded.
        // You can start loading the previous page of data here if needed.
    }
}
