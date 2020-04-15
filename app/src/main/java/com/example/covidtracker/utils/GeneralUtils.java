package com.example.covidtracker.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.example.covidtracker.R;
import com.example.covidtracker.models.dataModels.Guideline;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GeneralUtils {
    private static final String TAG = "GeneralUtils";

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

    public static boolean checkPermission(Context context, final String[] PERMISSIONS){
        for(String perm : PERMISSIONS){
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

    public static int getProgressColour(float percent, int c1, int c2) {
        float[] rgb1 = getRGB(c1);
        float[] rgb2 = getRGB(c2);
        int[] rgb = new int[3];
        float prg = percent / 100;


        for (int i = 0; i < 3; i++) {
            rgb[i] = (int) (rgb1[i] + prg * (rgb2[i] - rgb1[i]));
        }

        String hexColour = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
        Log.d(TAG, "getProgressColour: "+prg+" "+hexColour+"  " +rgb[0]+" "+rgb[1]+" "+rgb[2]);
        return Color.parseColor(hexColour);
    }

    private static float[] getRGB(int color) {
        float red = (color >> 16) & 0xFF;
        float green = (color >> 8) & 0xFF;
        float blue = (color) & 0xFF;

        return new float[]{red, green, blue};
    }

    private LatLng getPlaceLatLng(Context context, String name) {
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> addresses = geocoder.getFromLocationName(name, 1);
            if (!addresses.isEmpty()) {
                return new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getString (Context context, int c, int value){
        String s = String.valueOf(value);
        if(context==null){
            return "";
        }

        switch (c){
            case 0:
                return context.getString(R.string.active)+s;
            case 1:
                return context.getString(R.string.dead)+s;
            case 2:
                return context.getString(R.string.recovered)+s;
            case 3:
                return context.getString(R.string.total)+s;
        }
        return s;
    }

    public static String getFormattedDiff(int val){
        if(val>=0){
            return String.format("+%d", val);
        }
        else{
            return String.valueOf(val);
        }
    }

    public static void showNoInternetSnackbar(View root){
        Snackbar snackbar = Snackbar
                .make(root, "No Internet Connection", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(root.getContext(), R.color.white))
                .setAnchorView(root);
        snackbar.show();
    }

}
