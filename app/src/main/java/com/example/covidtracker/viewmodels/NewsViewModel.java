package com.example.covidtracker.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.covidtracker.interfaces.FetchDataCallback;
import com.example.covidtracker.models.dataModels.NewsResponse;
import com.example.covidtracker.models.repositories.NewsRepository;

public class NewsViewModel extends ViewModel {

    private static final String TAG = "NewsViewModel";
    private MutableLiveData<NewsResponse> liveNewsResponse;
    private NewsRepository newsRepository;
    private FetchDataCallback callback;

    public NewsViewModel(FetchDataCallback callback) {
        this.callback = callback;
        Log.d(TAG, "NewsViewModel: ");
        newsRepository = NewsRepository.getInstance();
        liveNewsResponse = newsRepository.getLiveNewsResponse(callback);
    }

    public LiveData<NewsResponse> getLiveNewsResponse() {
        return liveNewsResponse;
    }

    public void refreshData(){
        newsRepository.refreshNewsResponse(liveNewsResponse, callback);
    }

}
