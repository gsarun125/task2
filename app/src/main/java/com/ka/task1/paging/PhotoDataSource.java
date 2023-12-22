package com.ka.task1.paging;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.ka.task1.model.Photo;
import com.ka.task1.network.RetroRepository;

public class PhotoDataSource extends PageKeyedDataSource<Integer, Photo> {

    private final RetroRepository repository;
    private final String apiKey;
    private final String searchText;

    public PhotoDataSource(RetroRepository repository, String apiKey, String searchText) {
        this.repository = repository;
        this.apiKey = apiKey;
        this.searchText = searchText;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Photo> callback) {
        // Call the loadInitial method from RetroRepository
        repository.loadInitial(apiKey, searchText, 1, callback);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Photo> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Photo> callback) {
        // Call the loadAfter method from RetroRepository
        repository.loadAfter(apiKey, searchText, params.key, callback);
    }

}

