package com.example.covidtracker.models.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.covidtracker.models.dataModels.NewsResponse;
import com.example.covidtracker.interfaces.NewsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsRepository {
    private static final String TAG = "NewsRepository";

    private final String BASE_URL = "https://newsapi.org/";
    private static NewsRepository instance;
    private NewsApi newsApi;

    private NewsRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newsApi = retrofit.create(NewsApi.class);
    }

    public static NewsRepository getInstance(){
        if(instance==null){
            instance = new NewsRepository();
        }
        return instance;
    }

    public MutableLiveData<NewsResponse> getLiveNewsResponse() {
        MutableLiveData<NewsResponse> liveNewsResponse = new MutableLiveData<>();
        Call<NewsResponse> response = newsApi.getNews(NewsApi.QUERY_KEYWORD, NewsApi.NEWS_API_KEY,
                NewsApi.NO_OF_ARTICLES, NewsApi.SORT_BY, NewsApi.LANGUAGE);
        response.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                liveNewsResponse.postValue(response.body());
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
        return liveNewsResponse;
    }

}
