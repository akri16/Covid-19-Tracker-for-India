package com.example.covidtracker.models.dataModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoronaHistory {

    @SerializedName("day")
    private String date;
    private DailyRecord total;
    private List<DailyRecord> statewise;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public DailyRecord getTotal() {
        return total;
    }

    public void setTotal(DailyRecord total) {
        this.total = total;
    }

    public List<DailyRecord> getStatewise() {
        return statewise;
    }

    public void setStatewise(List<DailyRecord> statewise) {
        this.statewise = statewise;
    }
}
