package com.example.covidtracker.models.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.covidtracker.interfaces.Covid19Api;
import com.example.covidtracker.interfaces.FetchDataCallback;
import com.example.covidtracker.models.dataModels.DailyRecord;
import com.example.covidtracker.models.dataModels.ProcessedHistory;
import com.example.covidtracker.utils.CoronaRecordsUtils;
import com.example.covidtracker.utils.DataUtils;
import com.example.covidtracker.models.dataModels.CoronaHistory;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CoronaRecordsRepository {

    private static CoronaRecordsRepository instance;
    private static final String TAG = "StatewiseRepository";
    private final String DATA = "data";
    private final String HISTORY = "history";
    private final String BASE_URL = "https://api.rootnet.in/";
    private final Covid19Api covid19Api;

    private MutableLiveData<List<CoronaHistory>> liveRecords;
    private MutableLiveData<List<List<DailyRecord>>> livestatewiseRecords;
    private MutableLiveData<ProcessedHistory> liveProcessedRecords;

    public MutableLiveData<ProcessedHistory> getLiveProcessedRecords() {
        return liveProcessedRecords;
    }

    private CoronaRecordsRepository() {
        liveRecords = new MutableLiveData<>();
        livestatewiseRecords = new MutableLiveData<>();
        liveProcessedRecords = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();

        covid19Api = retrofit.create(Covid19Api.class);
    }

    public static CoronaRecordsRepository getInstance() {
        if (instance == null) {
            instance = new CoronaRecordsRepository();
        }
        return instance;
    }

    public MutableLiveData<List<List<DailyRecord>>> getLivestatewiseRecords() {
        return livestatewiseRecords;
    }

    public MutableLiveData<List<CoronaHistory>> getLiveRecords() {
        return liveRecords;
    }

    public void getHistoryRecords(FetchDataCallback callback) {
        List<CoronaHistory> historyRecords = new ArrayList<>();
        Call<ResponseBody> call = covid19Api.getHistoryData();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.code());
                } else {
                    ResponseBody body = response.body();
                    Gson gson = new Gson();

                    try {
                        JSONObject main = new JSONObject(body.string());
                        JSONObject data = main.getJSONObject(DATA);
                        JSONArray history = data.getJSONArray(HISTORY);
                        for (int i = 0; i < history.length(); i++) {
                            JSONObject jsonObject = history.getJSONObject(i);
                            CoronaHistory historyObject = gson.fromJson(jsonObject.toString(), CoronaHistory.class);
                            historyRecords.add(historyObject);
                        }
                        liveRecords.setValue(historyRecords);
                        List<List<DailyRecord>> statewise = CoronaRecordsUtils.makeStatewiseRecord(historyRecords);
                        livestatewiseRecords.setValue(statewise);
                        liveProcessedRecords.setValue(CoronaRecordsUtils.processRecord(statewise));
                        callback.onSuccess();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure();
                Log.d(TAG, "onFailure: ", t);
            }
        });

    }



}
