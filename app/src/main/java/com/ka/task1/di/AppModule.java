package com.ka.task1.di;

import com.ka.task1.network.RetroServiceInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    String baseURL="https://api.flickr.com/services/rest/";

    @Provides
    @Singleton
    public RetroServiceInterface getRetroServiceInterface(Retrofit retrofit){
        return retrofit.create(RetroServiceInterface.class);
    }
    @Provides
    @Singleton
    public Retrofit getRetroInstance(){
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
