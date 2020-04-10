package com.example.covidtracker.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.covidtracker.models.dataModels.NewsResponse;
import com.example.covidtracker.models.repositories.NewsRepository;

public class NewsViewModel extends AndroidViewModel {

    private static final String TAG = "NewsViewModel";
    private MutableLiveData<NewsResponse> liveNewsResponse;
    private NewsRepository newsRepository;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "NewsViewModel: ");
        newsRepository = NewsRepository.getInstance();
        liveNewsResponse = newsRepository.getLiveNewsResponse();
    }

    public LiveData<NewsResponse> getLiveNewsResponse() {
        return liveNewsResponse;
    }
}
