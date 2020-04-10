package com.example.covidtracker.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Covid19Api {

    @GET("covid19-in/unofficial/covid19india.org/statewise/history")
    Call<ResponseBody> getHistoryData();
}
