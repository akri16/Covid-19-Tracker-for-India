package com.example.covidtracker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.covidtracker.models.dataModels.DailyRecord;
import com.example.covidtracker.models.dataModels.CoronaHistory;
import com.example.covidtracker.models.repositories.CoronaRecordsRepository;

import java.util.List;

public class CoronaRecordsViewModel extends ViewModel {

    private final CoronaRecordsRepository recordsRepository;
    private MutableLiveData<List<CoronaHistory>> liveRecords;
    private MutableLiveData<List<List<DailyRecord>>> liveStatewiseRecords;

    public LiveData<List<CoronaHistory>> getLiveRecords() {
        return liveRecords;
    }

    public LiveData<List<List<DailyRecord>>> getLiveStatewiseRecords() {
        return liveStatewiseRecords;
    }

    public CoronaRecordsViewModel() {
        recordsRepository = CoronaRecordsRepository.getInstance();
        liveRecords = recordsRepository.getLiveRecords();
        liveStatewiseRecords = recordsRepository.getLivestatewiseRecords();
        recordsRepository.getHistoryRecords();
    }

    public void refreshRecords(){
        recordsRepository.getHistoryRecords();
    }

}
