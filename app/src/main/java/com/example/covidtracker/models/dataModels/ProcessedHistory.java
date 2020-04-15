package com.example.covidtracker.models.dataModels;

import java.util.ArrayList;
import java.util.List;

public class ProcessedHistory {
    //Highest index is represented as iMaxX where x is (Active, Confirmed, ...)
    private double iMaxActive;
    private double iMaxConfirmed;
    private double iMaxDead;
    private double iMaxRecovered;
    private List<ProcessedStateRecord> stateRecords;

    public ProcessedHistory(double iMaxActive, double iMaxConfirmed, double iMaxDead, double iMaxRecovered, List<ProcessedStateRecord> stateRecords) {
        this.iMaxActive = iMaxActive;
        this.iMaxConfirmed = iMaxConfirmed;
        this.iMaxDead = iMaxDead;
        this.iMaxRecovered = iMaxRecovered;
        this.stateRecords = stateRecords;
    }

    public ProcessedHistory() {
        iMaxActive=0;
        iMaxConfirmed=0;
        iMaxDead=0;
        iMaxRecovered=0;
        this.stateRecords = new ArrayList<>();
    }

    public double getiMaxActive() {
        return iMaxActive;
    }

    public void setiMaxActive(double iMaxActive) {
        this.iMaxActive = iMaxActive;
    }

    public double getiMaxConfirmed() {
        return iMaxConfirmed;
    }

    public void setiMaxConfirmed(double iMaxConfirmed) {
        this.iMaxConfirmed = iMaxConfirmed;
    }

    public double getiMaxDead() {
        return iMaxDead;
    }

    public void setiMaxDead(double iMaxDead) {
        this.iMaxDead = iMaxDead;
    }

    public double getiMaxRecovered() {
        return iMaxRecovered;
    }

    public void setiMaxRecovered(double iMaxRecovered) {
        this.iMaxRecovered = iMaxRecovered;
    }

    public List<ProcessedStateRecord> getStateRecords() {
        return stateRecords;
    }

    public void setStateRecords(List<ProcessedStateRecord> stateRecords) {
        this.stateRecords = stateRecords;
    }
}
