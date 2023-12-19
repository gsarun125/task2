package com.ka.task1.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ka.task1.model.Photo;
import com.ka.task1.network.RetroRepository;
import com.ka.task1.network.RetroServiceInterface;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainActivityViewModel extends ViewModel {

    MutableLiveData<List<Photo>> recentPhotos;
    RetroServiceInterface retroServiceInterface;

    @Inject
    public MainActivityViewModel(RetroServiceInterface retroServiceInterface) {
        this.retroServiceInterface = retroServiceInterface;
        recentPhotos = new MutableLiveData<>();
    }

    public MutableLiveData<List<Photo>> getLiveData() {
        return recentPhotos;
    }

    public void makeApiCall() {
        RetroRepository retroRepository = new RetroRepository(retroServiceInterface);
        retroRepository.makeAPICall("us", recentPhotos);
    }
}
