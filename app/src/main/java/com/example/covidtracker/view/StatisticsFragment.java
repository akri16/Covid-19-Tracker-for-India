package com.example.covidtracker.view;

import android.Manifest;
import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.covidtracker.R;
import com.example.covidtracker.databinding.FragmentStatisticsBinding;
import com.example.covidtracker.interfaces.FetchDataCallback;
import com.example.covidtracker.models.dataModels.CoronaHistory;
import com.example.covidtracker.models.dataModels.DailyRecord;
import com.example.covidtracker.utils.DataUtils;
import com.example.covidtracker.utils.GeneralUtils;
import com.example.covidtracker.utils.LineChartHelper;
import com.example.covidtracker.viewmodels.CoronaRecordsViewModel;
import com.example.covidtracker.viewmodels.CustomViewModelFactory;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class StatisticsFragment extends Fragment {
    private static final String TAG = "StatisticsFragment";
    private final String[] LOCATION_PERMISSION = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private final int REQUEST_CODE = 1001;
    private FragmentStatisticsBinding binding;
    private int colorArray[];
    private CoronaRecordsViewModel recordsViewModel;
    private LiveData<List<CoronaHistory>> liveRecords;
    private LiveData<List<List<DailyRecord>>> liveStatewiseRecords;
    private LineChartHelper lineChartHelper;
    private int currentlocationValue = 0;
    private int currentDateValue = 0;
    private AlertDialog locationDialog;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.statsLocation.setText(DataUtils.STATE_LIST.get(currentDateValue));
        CustomViewModelFactory factory = new CustomViewModelFactory(getActivity().getApplication(),
                new FetchDataCallback() {
                    @Override
                    public void onFailure() {
                        binding.recordsSwipeRefresh.setRefreshing(false);
                        GeneralUtils.showNoInternetSnackbar(getActivity().findViewById(R.id.bottom_nav));
                    }
                    @Override
                    public void onSuccess() {
                        binding.recordsSwipeRefresh.setRefreshing(false);
                    }
                }, CustomViewModelFactory.COVID_MODEL);

        recordsViewModel = new ViewModelProvider(this, factory).get(CoronaRecordsViewModel.class);
        liveRecords = recordsViewModel.getLiveRecords();
        liveStatewiseRecords = recordsViewModel.getLiveStatewiseRecords();

        binding.recordsSwipeRefresh.setOnRefreshListener(() -> {
            recordsViewModel.refreshRecords();
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose a location");
        ArrayList<String> data = new ArrayList<>(DataUtils.STATE_LIST);
        data.add(0, "My Location");
        builder.setItems(data.toArray(new String[30]), (dialog, which) -> {
            int index = which-1;
            if(which==0){
                index=getLocationIndex();
                if(index==-1){
                    Toast.makeText(getContext(), "Location not available currently", Toast.LENGTH_SHORT).show();
                    index = 0;
                }
            }
            binding.statsLocation.setText(DataUtils.STATE_LIST.get(index));
            currentlocationValue = index;
            initGraph();
        });
        locationDialog = builder.create();

        lineChartHelper = new LineChartHelper(binding.statsLineChart, getContext());
        lineChartHelper.formatLineGraph();

        liveStatewiseRecords.observe(getViewLifecycleOwner(), lists -> {
            if(liveStatewiseRecords.getValue()!=null) {
                binding.recordsSwipeRefresh.setRefreshing(false);
                binding.statsLineChart.setOnChartValueSelectedListener(new MyChartValueSelectedListener());
                initGraph();
                Log.d(TAG, "onActivityCreated: ");
            }
        });

        binding.statsLocation.setOnClickListener(v -> {
            locationDialog.show();
        });

        binding.statsDate.setOnClickListener(v -> {
            binding.statsLineChart.setFocusable(true);
            binding.statsLineChart.requestFocus();
            binding.statsLineChart.clearFocus();
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE && GeneralUtils.checkPermission(getContext(), LOCATION_PERMISSION)){
            int locationIndex = getLocationIndex();
            if(locationIndex!=-1) {
                currentlocationValue = locationIndex;
                initGraph();
            }
        }
    }

    private void initStats()  {
        if(liveStatewiseRecords.getValue()==null){
           GeneralUtils.showNoInternetSnackbar(getActivity().findViewById(R.id.bottom_nav));
            return;
        }
        DailyRecord record = liveStatewiseRecords.getValue().get(currentlocationValue).get(currentDateValue);
        DailyRecord diffRecord = new DailyRecord(0, 0, 0, 0);
        if(currentDateValue > 0){
            DailyRecord prevRecord = liveStatewiseRecords.getValue().get(currentlocationValue).get(currentDateValue-1);
            diffRecord.setActive(record.getActive()-prevRecord.getActive());
            diffRecord.setConfirmed(record.getConfirmed()-prevRecord.getConfirmed());
            diffRecord.setDeaths(record.getDeaths()-prevRecord.getDeaths());
            diffRecord.setRecovered(record.getRecovered()-prevRecord.getRecovered());
        }
        binding.statsCircles.statsTvActive.setText(String.valueOf(record.getActive()));
        binding.statsCircles.statsTvRecovered.setText(String.valueOf(record.getRecovered()));
        binding.statsCircles.statsTvDead.setText(String.valueOf(record.getDeaths()));
        binding.statsDate.setText(GeneralUtils.formatDate(record.getDate()));
        binding.statsCircles.activeUpdateText.setText(GeneralUtils.getFormattedDiff(diffRecord.getActive()));
        binding.statsCircles.deadUpdateText.setText(GeneralUtils.getFormattedDiff(diffRecord.getDeaths()));
        binding.statsCircles.recoveredUpdateText.setText(GeneralUtils.getFormattedDiff(diffRecord.getRecovered()));
    }

    private void initGraph(){
        if(liveStatewiseRecords.getValue()==null){
            GeneralUtils.showNoInternetSnackbar(getActivity().findViewById(R.id.bottom_nav));
            return;
        }
        List<DailyRecord> records = liveStatewiseRecords.getValue().get(currentlocationValue);
        currentDateValue = records.size()-1;
        lineChartHelper.setDataset(records);
        initStats();
        lineChartHelper.animate();
    }

    private int getLocationIndex(){
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if(!GeneralUtils.checkPermission(getContext(), LOCATION_PERMISSION)) {
            ActivityCompat.requestPermissions(getActivity(), LOCATION_PERMISSION, REQUEST_CODE);
            return -1;
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        int index = -1;
        try {
            String state = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1)
                    .get(0).getAdminArea();
            Log.d(TAG, "getLocationIndex: "+state);
            index = DataUtils.getStateIndex(state);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "getLocationIndex: ", e);
        }

        return index;
    }

    private class MyChartValueSelectedListener implements OnChartValueSelectedListener{

        @Override
        public void onValueSelected(Entry e, Highlight h) {
            currentDateValue = Math.round(e.getX());
            initStats();
        }

        @Override
        public void onNothingSelected() {

        }
    }
}
