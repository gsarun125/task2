package com.ka.task1.paging;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.ka.task1.model.Photo;
import com.ka.task1.network.RetroRepository;

public class PhotoDataSourceFactory extends DataSource.Factory<Integer, Photo> {

    private final RetroRepository repository;
    private final String apiKey;
    private final String searchText;
    private MutableLiveData<PhotoDataSource> dataSourceLiveData;

    public PhotoDataSourceFactory(RetroRepository repository, String apiKey, String searchText) {
        this.repository = repository;
        this.apiKey = apiKey;
        this.searchText = searchText;
        dataSourceLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Integer, Photo> create() {
        PhotoDataSource dataSource = new PhotoDataSource(repository, apiKey, searchText);
        dataSourceLiveData.postValue(dataSource);
        return dataSource;
    }

    public MutableLiveData<PhotoDataSource> getDataSourceLiveData() {
        return dataSourceLiveData;
    }
}
