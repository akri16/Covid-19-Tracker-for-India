package com.example.covidtracker.utils;

import android.text.Html;
import android.util.Log;

import com.example.covidtracker.R;
import com.example.covidtracker.models.dataModels.Guideline;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DataUtils {
    public static final List<String> STATEs_LIST = Arrays.asList("India", "Kerala", "Haryana", "Maharashtra", "Uttar Pradesh", "Delhi",
            "Karnataka", "Jammu and Kashmir", "Rajasthan", "Andhra Pradesh", "Punjab", "Tamil Nadu", "Telangana",
            "Assam", "Bihar", "Gujarat", "Meghalaya", "Madhya Pradesh", "Odisha", "Puducherry", "Tripura", "West Bengal",
            "Goa", "Arunachal Pradesh", "Chhattisgarh", "Himachal Pradesh", "Jharkhand", "Manipur", "Mizoram", "Nagaland",
            "Sikkim", "Uttarakhand", "Chandigarh", "Dadra and Nagar Haveli", "Daman and Diu", "Lakshadweep", "Andaman and Nicobar Islands");

    public static final List<String> STATE_LIST = Arrays.asList("India", "Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam",
            "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh",
            "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya",
            "Mizoram", "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh",
            "Uttarakhand", "West Bengal");

    public static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(0, 68.14712), new LatLng(28.20453, 97.34466));
    public static final List GUIDELINES = Arrays.asList(
            new Guideline(R.drawable.ic_alert, R.string.dialog_1, R.string.body_1, 0),
            new Guideline(R.drawable.ic_alert_less, R.string.dialog_2, R.string.body_2, 0),
            new Guideline(R.drawable.ic_alert_less, R.string.dialog_3, R.string.body_3, 0),
            new Guideline(R.drawable.ic_alert_less, R.string.dialog_4, R.string.body_4, 0),
            new Guideline(0, R.string.wash_hands, 0, R.raw.hand_sanitizer),
            new Guideline(0, R.string.cover, 0, R.raw.wear_mask),
            new Guideline(0, R.string.avoid_contact, 0, R.raw.social_distancing),
            new Guideline(0, R.string.avoid_body_touch, 0, R.raw.stay_safe_stay_home)
    );

    public static final List<Integer> STATE_POPULATION_LIST = Arrays.asList(1335140907,
            419978, 52883163,1528296,34586234,119461013,1126705,28566990,378979,220084,18345784,1542750,63907200,
            27388008,7316708,13635010,37329128,66165886,35330888,71218,82342793,120837347,3008546,3276323,1205974,2189297,45429399,
            1375592,29611935,78230816,671720,76481545,38472769,4057847,228959599, 11090425,97694960);

    public static String HELPLINE_NO = "1075";

    public static int getStateIndex(String state){
        for(int i = 0; i< STATE_LIST.size(); i++){
            if(STATE_LIST.get(i).toLowerCase().equals(state.toLowerCase())){
                return i;
            }
        }
        return -1;
    }
}
