package com.example.covidtracker.models.dataModels;

public class DailyRecord {

    private String state;
    private String date;
    private int confirmed;
    private int recovered;
    private int active;
    private int deaths;

    public DailyRecord() {
    }

    public DailyRecord(int confirmed, int recovered, int active, int deaths) {
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.active = active;
        this.deaths = deaths;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
}
