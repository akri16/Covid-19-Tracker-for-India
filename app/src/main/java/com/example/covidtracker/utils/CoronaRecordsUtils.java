package com.example.covidtracker.utils;

import android.util.Log;

import com.airbnb.lottie.L;
import com.example.covidtracker.models.dataModels.CoronaHistory;
import com.example.covidtracker.models.dataModels.DailyRecord;
import com.example.covidtracker.models.dataModels.ProcessedHistory;
import com.example.covidtracker.models.dataModels.ProcessedStateRecord;

import java.util.ArrayList;
import java.util.List;

public class CoronaRecordsUtils {
    private static final String TAG = "CoronaRecordsUtils";
    public static ProcessedHistory processRecord(ArrayList<CoronaHistory> coronaHistories){
        List<List<DailyRecord>> statewise = makeStatewiseRecord(coronaHistories);
        ProcessedHistory history = getProcessedHistory(statewise);
        if(history!=null && history.getStateRecords()!=null) {
            Log.d(TAG, "processRecord: ");
            setRelativeIndices(history);
        }
        return history;
    }

    public static ProcessedHistory processRecord(List<List<DailyRecord>> stateRecord){
        ProcessedHistory history = getProcessedHistory(stateRecord);
        if(history!=null && history.getStateRecords()!=null) {
            setRelativeIndices(history);
        }
        return history;
    }

    private static void setRelativeIndices(ProcessedHistory history){
        for (ProcessedStateRecord record : history.getStateRecords()){
            record.setRelativeRecovered(record.getiRecovered()/history.getiMaxRecovered());
            record.setRelativeActive(record.getiActive()/history.getiMaxActive());
            record.setRelativeDead(record.getiDead()/history.getiMaxDead());
            record.setRelativeConfirmed(record.getiConfirmed()/history.getiMaxConfirmed());
            Log.d(TAG, "setRelativeIndices: "+ record.getDailyRecord().getState()+" "+record.getRelativeActive()+" "+record.getiActive());
        }
    }

    private static ProcessedHistory getProcessedHistory(List<List<DailyRecord>> coronaHistories) {
        ProcessedHistory history = new ProcessedHistory();
        for(int i=0;i<coronaHistories.size();i++){
            ProcessedStateRecord processedStateRecord = new ProcessedStateRecord();
            int size = coronaHistories.get(i).size();
            if(size==0){
                Log.d(TAG, "getProcessedHistory: size of records 0" );
                return null;
            }
            DailyRecord record = coronaHistories.get(i).get(size-1);
            processedStateRecord.setDailyRecord(record);
            int population = DataUtils.STATE_POPULATION_LIST.get(i);
            processedStateRecord.setiActive(((double)record.getActive()));
            processedStateRecord.setiConfirmed(((double)record.getConfirmed())/population);
            processedStateRecord.setiDead(((double)record.getDeaths()));
            processedStateRecord.setiRecovered(((double)record.getRecovered())/record.getConfirmed());
            //Log.d(TAG, "getProcessedHistory: " + record.getState() + " " + processedStateRecord.getiRecovered());

            if(i!=0) {
                if(history.getiMaxActive()<processedStateRecord.getiActive()){
                    history.setiMaxActive(processedStateRecord.getiActive());
                }
                if (history.getiMaxConfirmed() < processedStateRecord.getiConfirmed()) {
                    history.setiMaxConfirmed(processedStateRecord.getiConfirmed());
                }
                if (history.getiMaxDead() < processedStateRecord.getiDead()) {
                    history.setiMaxDead(processedStateRecord.getiDead());
                }
                if (history.getiMaxRecovered() < processedStateRecord.getiRecovered()) {
                    history.setiMaxRecovered(processedStateRecord.getiRecovered());
                }
            }
            history.getStateRecords().add(processedStateRecord);
        }

        return history;
    }

    public static List<List<DailyRecord>> makeStatewiseRecord(List<CoronaHistory> record) {
        List<List<DailyRecord>> stateWise = new ArrayList<>();
        for (int i = 0; i< DataUtils.STATE_LIST.size(); i++) {
            String state = DataUtils.STATE_LIST.get(i);
            stateWise.add(new ArrayList<>());
            for (CoronaHistory it1 : record) {
                if(i==0){
                    it1.getTotal().setDate(it1.getDate());
                    stateWise.get(i).add(it1.getTotal());
                    continue;
                }
                for (DailyRecord it2 : it1.getStatewise()) {
                    if (state.equals(it2.getState())) {
                        it2.setDate(it1.getDate());
                        stateWise.get(i).add(it2);
                    }
                }
            }
        }
        return stateWise;
    }



}
