package com.example.covidtracker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.covidtracker.interfaces.FetchDataCallback;
import com.example.covidtracker.models.dataModels.DailyRecord;
import com.example.covidtracker.models.dataModels.CoronaHistory;
import com.example.covidtracker.models.dataModels.ProcessedHistory;
import com.example.covidtracker.models.repositories.CoronaRecordsRepository;

import java.util.List;

public class CoronaRecordsViewModel extends ViewModel {

    private final CoronaRecordsRepository recordsRepository;
    private MutableLiveData<List<CoronaHistory>> liveRecords;
    private MutableLiveData<List<List<DailyRecord>>> liveStatewiseRecords;
    private FetchDataCallback callback;
    private MutableLiveData<ProcessedHistory> liveProcessedRecord;

    public LiveData<List<CoronaHistory>> getLiveRecords() {
        return liveRecords;
    }

    public LiveData<List<List<DailyRecord>>> getLiveStatewiseRecords() {
        return liveStatewiseRecords;
    }

    public MutableLiveData<ProcessedHistory> getLiveProcessedRecords() {
        return liveProcessedRecord;
    }

    public CoronaRecordsViewModel(FetchDataCallback callback) {
        this.callback = callback;
        recordsRepository = CoronaRecordsRepository.getInstance();
        liveRecords = recordsRepository.getLiveRecords();
        liveStatewiseRecords = recordsRepository.getLivestatewiseRecords();
        liveProcessedRecord = recordsRepository.getLiveProcessedRecords();
        recordsRepository.getHistoryRecords(callback);
    }

    public void refreshRecords(){
        recordsRepository.getHistoryRecords(callback);
    }

}
