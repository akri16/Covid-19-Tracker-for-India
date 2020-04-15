package com.example.covidtracker.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.covidtracker.R;
import com.example.covidtracker.databinding.FragmentMapsBinding;
import com.example.covidtracker.interfaces.FetchDataCallback;
import com.example.covidtracker.models.dataModels.DailyRecord;
import com.example.covidtracker.models.dataModels.ProcessedHistory;
import com.example.covidtracker.models.dataModels.ProcessedStateRecord;
import com.example.covidtracker.utils.DataUtils;
import com.example.covidtracker.utils.GeneralUtils;
import com.example.covidtracker.viewmodels.CoronaRecordsViewModel;
import com.example.covidtracker.viewmodels.CustomViewModelFactory;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MapsActivity";
    private static final int ERROR_DIALOG_REQUEST = 1001;
    private static final int REQUEST_CODE = 1014;
    private static final float DEFAULT_ZOOM = 50;
    private static final float MIN_ZOOM = 4;
    private static final float MAX_ZOOM = 6.5f;
    public static final String STATE_INDEX_TAG = "StateIndex";
    private static int[] PIE_COLORS;
    private static final CameraPosition INDIA_CAMERA = new CameraPosition.Builder()
            .target(DataUtils.BOUNDS_INDIA.getCenter()).zoom(MIN_ZOOM).bearing(0).tilt(0).build();

    public static final String STATE_NAME_TAG = "NAME_1";
    private GoogleMap mMap;
    private static final String[] LOCATION_PERMISSION = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private boolean locationpermissiongranted = false;
    private FusedLocationProviderClient locationProviderClient;
    private BottomSheetBehavior bottomSheetBehavior;
    private FragmentMapsBinding binding;
    private CoronaRecordsViewModel viewModel;
    private LiveData<ProcessedHistory> liveStatewisRecords;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false);
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        ((MyViewGroup)binding.getRoot()).setChildrenEnabled(false);
        binding.mapChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (mMap != null) {
                initializeOverlay();
            }

            Log.d(TAG, "onCreateView: " + group.getCheckedChipId());
        });

        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CustomViewModelFactory factory = new CustomViewModelFactory(getActivity().getApplication(),
                new FetchDataCallback() {
                    @Override
                    public void onFailure() {
                        GeneralUtils.showNoInternetSnackbar(getActivity().findViewById(R.id.bottom_nav));
                    }
                    @Override
                    public void onSuccess() {
                    }
                }, CustomViewModelFactory.COVID_MODEL);

        viewModel = new ViewModelProvider(this, factory).get(CoronaRecordsViewModel.class);
        liveStatewisRecords = viewModel.getLiveProcessedRecords();
        liveStatewisRecords.observe(getViewLifecycleOwner(), processedHistory -> {
            if (processedHistory!=null && processedHistory.getStateRecords()!=null && mMap != null) {
                initializeOverlay();
            }
            else {
                Log.d(TAG, "onActivityCreated: null records" + processedHistory.getStateRecords());
            }
        });
        liveStatewisRecords.observe(getViewLifecycleOwner(), lists -> {

        });

        PIE_COLORS = new int[]{
                ContextCompat.getColor(getContext(), R.color.color_graph_active),
                ContextCompat.getColor(getContext(), R.color.color_graph_dead),
                ContextCompat.getColor(getContext(), R.color.color_graph_recovered)};

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void initMyLocation() {
        locationpermissiongranted = GeneralUtils.checkPermission(getContext(), LOCATION_PERMISSION);
        if (!locationpermissiongranted) {
            ActivityCompat.requestPermissions(getActivity(), LOCATION_PERMISSION, REQUEST_CODE);
        } else {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d(TAG, "onMapReady: ");
        initMyLocation();

        if (liveStatewisRecords.getValue() != null && liveStatewisRecords.getValue().getStateRecords()!=null) {
            initializeOverlay();
        }

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setLatLngBoundsForCameraTarget(DataUtils.BOUNDS_INDIA);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(INDIA_CAMERA));
        mMap.setMinZoomPreference(MIN_ZOOM);
        mMap.setMaxZoomPreference(MAX_ZOOM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int res : grantResults) {
                    if (res != PackageManager.PERMISSION_GRANTED) {
                        locationpermissiongranted = false;
                        return;
                    }
                }
                locationpermissiongranted = true;
                initMyLocation();
            }
        }

    }

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    private void initializeOverlay() {
        mMap.clear();
        binding.progressBar.setVisibility(View.VISIBLE);
        ((MyViewGroup)binding.getRoot()).setChildrenEnabled(false);
        try {
            GeoJsonLayer layer = new GeoJsonLayer(mMap, R.raw.india_shape, getContext());

            String state_name;
            int index;
            for (GeoJsonFeature feature : layer.getFeatures()) {
                state_name = feature.getProperty(STATE_NAME_TAG);
                index = DataUtils.getStateIndex(state_name);

                feature.setProperty(STATE_INDEX_TAG, String.valueOf(index));
                GeoJsonPolygonStyle style = new GeoJsonPolygonStyle();
                style.setFillColor(getRequiredColorFor(index));
                feature.setPolygonStyle(style);
            }

            addGeoJsonLayerToMap(layer);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        ((MyViewGroup)binding.getRoot()).setChildrenEnabled(true);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    private void addGeoJsonLayerToMap(GeoJsonLayer layer) {

        layer.addLayerToMap();
        // Demonstrate receiving features via GeoJsonLayer clicks.
        layer.setOnFeatureClickListener((GeoJsonLayer.GeoJsonOnFeatureClickListener) feature -> {

            DailyRecord stateRecord = liveStatewisRecords.getValue().getStateRecords()
                    .get(Integer.parseInt(feature.getProperty(STATE_INDEX_TAG))).getDailyRecord();

            binding.setContext(getContext());
            binding.setRecord(stateRecord);

            setUpPieChart(stateRecord);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        });

        mMap.setOnMapClickListener(latLng -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));
    }

    private void setUpPieChart(DailyRecord record) {
        ArrayList<PieEntry> dataVals = new ArrayList<>();

        dataVals.add(new PieEntry(record.getActive(), ""));
        dataVals.add(new PieEntry(record.getDeaths(), ""));
        dataVals.add(new PieEntry(record.getRecovered(), ""));

        PieDataSet pieDataSet = new PieDataSet(dataVals, "");
        pieDataSet.setDrawValues(false);
        pieDataSet.setColors(PIE_COLORS);

        PieData pieData = new PieData(pieDataSet);
        Description description = new Description();
        description.setText("");
        binding.mapPieChart.setDescription(description);
        binding.mapPieChart.setData(pieData);
        binding.mapPieChart.getLegend().setEnabled(false);
        binding.mapPieChart.setDrawEntryLabels(false);
        binding.mapPieChart.invalidate();
        binding.mapPieChart.animateXY(500, 500);

    }

    private void addMarker(LatLng latLng, String title) {
        int height = 35;
        int width = 35;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.covid);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions
                .position(latLng)
                .title(title)
                .flat(true)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        Marker marker = mMap.addMarker(markerOptions);
        marker.setTag(title);
    }


    private void getDeviceLocation() {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        final Task location = locationProviderClient.getLastLocation();
        location.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "onComplete: found location");
                Location currentLoc = (Location) task.getResult();
                moveCamera(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()), DEFAULT_ZOOM);
            } else {
                Log.d(TAG, "onComplete: null location");
            }
        });
    }

    private int getRequiredColorFor(int stateIndex) {
        ProcessedStateRecord stateRecord = viewModel.getLiveProcessedRecords().getValue().getStateRecords().get(stateIndex);

        int endColor = Color.BLACK;
        double value = 0;
        //int percent = 0;
        switch (binding.mapChipGroup.getCheckedChipId()) {

            case R.id.map_chip_active:
                value = stateRecord.getRelativeActive();
                endColor = PIE_COLORS[0];
                break;

            case R.id.map_chip_dead:
                value = stateRecord.getRelativeDead();
                endColor = PIE_COLORS[1];
                break;

            case R.id.map_chip_recovered:
                value = stateRecord.getRelativeRecovered();
                endColor = PIE_COLORS[2];
                break;

            case R.id.map_chip_cnf:
                value = stateRecord.getRelativeConfirmed();
                endColor = Color.BLUE;
                break;
        }
        float percent = (float) (100*value);

        return GeneralUtils.getProgressColour(percent, Color.WHITE, endColor);
    }

}
