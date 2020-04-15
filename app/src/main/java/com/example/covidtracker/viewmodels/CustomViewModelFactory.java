package com.example.covidtracker.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.covidtracker.interfaces.FetchDataCallback;

public class CustomViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    public static final int NEWS_MODEL=0;
    public static final int COVID_MODEL=1;
    private Application application;
    private final FetchDataCallback callback;
    private final int modelChoice;

    public CustomViewModelFactory(Application application, FetchDataCallback callback, int modelChoice) {
        this.application = application;
        this.callback = callback;
        this.modelChoice = modelChoice;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if(modelChoice==NEWS_MODEL){
            return (T) new NewsViewModel(callback);
        }
        else {
            return (T) new CoronaRecordsViewModel(callback);
        }

    }
}
