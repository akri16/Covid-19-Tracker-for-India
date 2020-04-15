package com.example.covidtracker.models.dataModels;

public class ProcessedStateRecord {
    private double relativeActive;
    private double relativeConfirmed;
    private double relativeDead;
    private double relativeRecovered;
    private double iActive;
    private double iConfirmed;
    private double iDead;
    private double iRecovered;
    private DailyRecord dailyRecord;

    public ProcessedStateRecord() {
    }

    public DailyRecord getDailyRecord() {
        return dailyRecord;
    }

    public void setDailyRecord(DailyRecord dailyRecord) {
        this.dailyRecord = dailyRecord;
    }

    public double getRelativeActive() {
        return relativeActive;
    }

    public void setRelativeActive(double relativeActive) {
        this.relativeActive = relativeActive;
    }

    public double getRelativeConfirmed() {
        return relativeConfirmed;
    }

    public void setRelativeConfirmed(double relativeConfirmed) {
        this.relativeConfirmed = relativeConfirmed;
    }

    public double getRelativeDead() {
        return relativeDead;
    }

    public void setRelativeDead(double relativeDead) {
        this.relativeDead = relativeDead;
    }

    public double getRelativeRecovered() {
        return relativeRecovered;
    }

    public void setRelativeRecovered(double relativeRecovered) {
        this.relativeRecovered = relativeRecovered;
    }

    public double getiActive() {
        return iActive;
    }

    public void setiActive(double iActive) {
        this.iActive = iActive;
    }

    public double getiConfirmed() {
        return iConfirmed;
    }

    public void setiConfirmed(double iConfirmed) {
        this.iConfirmed = iConfirmed;
    }

    public double getiDead() {
        return iDead;
    }

    public void setiDead(double iDead) {
        this.iDead = iDead;
    }

    public double getiRecovered() {
        return iRecovered;
    }

    public void setiRecovered(double iRecovered) {
        this.iRecovered = iRecovered;
    }
}
