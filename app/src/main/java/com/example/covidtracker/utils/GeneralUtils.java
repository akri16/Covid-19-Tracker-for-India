package com.example.covidtracker.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.example.covidtracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GeneralUtils {
    private static final String TAG = "GeneralUtils";

    public static final List<String> stateList = Arrays.asList("India", "Kerala", "Haryana", "Maharashtra", "Uttar Pradesh", "Delhi",
            "Karnataka", "Jammu and Kashmir", "Rajasthan", "Andhra Pradesh", "Punjab", "Tamil Nadu", "Telangana",
            "Assam", "Bihar", "Gujarat", "Meghalaya", "Madhya Pradesh", "Odisha", "Puducherry", "Tripura", "West Bengal",
            "Goa", "Arunachal Pradesh", "Chhattisgarh", "Himachal Pradesh", "Jharkhand", "Manipur", "Mizoram", "Nagaland",
            "Sikkim", "Uttarakhand", "Chandigarh", "Dadra and Nagar Haveli", "Daman and Diu", "Lakshadweep");

    public static String HELPLINE_NO = "1075";

    public static int getStateIndex(String state){
        for(int i=0;i<stateList.size();i++){
            if(stateList.get(i).toLowerCase().equals(state.toLowerCase())){
                Log.d(TAG, "getStateIndex: " + i);
                return i;
            }
        }
        return -1;
    }

    public static String formatDate(String inpdate){
        SimpleDateFormat givenformat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String date = null;
        try {
            date = formatter.format(givenformat.parse(inpdate.substring(0, 10)));
        } catch (ParseException e) {
            e.printStackTrace();
            formatter=givenformat;
            date = inpdate;
        }
        catch (NullPointerException e){
            return null;
        }
        String todaysDate = formatter.format(Calendar.getInstance().getTime());
        String yestDate = formatter.format(new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24)));

        if(todaysDate.equals(date)){
            date = "Today";
        }
        else if(yestDate.equals(date)){
            date = "Yesterday";
        }
        Log.d(TAG, "formatDate: " + todaysDate +" "+ yestDate + " " + date);

        return date;
    }

    public static boolean checkPermission(Context context, String[] LOCATION_PERMISSION){
        for(String perm : LOCATION_PERMISSION){
            if(ActivityCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED){
                return true;
            }
        }
        return false;
    }

    public static void loadIntoImageView(ImageView view, String uri){
        Log.d(TAG, "loadIntoImageView: "+ view+uri);
        Glide.with(view)
                .asBitmap()
                .load(uri)
                .transition(new GenericTransitionOptions<>())
                .placeholder(R.drawable.corona_news_default_img)
                .into(view);
    }

    public static String cleanTextContent(String text){
        if(text==null){
            return text;
        }

        int index = text.indexOf('[');
        if(index>-1)
            return text.substring(0, index);

        return text;
    }

}
