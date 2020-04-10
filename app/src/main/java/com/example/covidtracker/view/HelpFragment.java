package com.example.covidtracker.view;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.covidtracker.R;
import com.example.covidtracker.databinding.FragmentHelpBinding;
import com.example.covidtracker.utils.GeneralUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    private static final int REQUEST_CODE = 1002;
    private FragmentHelpBinding binding;
    private final String[] CALL_PERMISION = {Manifest.permission.CALL_PHONE};

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHelpBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Glide.with(getContext())
                .asBitmap()
                .load(R.drawable.corona_symptoms)
                .into(binding.helpSymptomsImg);

    }

    public void makeCall(){
        if(!GeneralUtils.checkPermission(getContext(), CALL_PERMISION)){
            ActivityCompat.requestPermissions(getActivity(), CALL_PERMISION, REQUEST_CODE);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + GeneralUtils.HELPLINE_NO));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE && GeneralUtils.checkPermission(getContext(), CALL_PERMISION)){
            makeCall();
        }
        Toast.makeText(getContext(), R.string.call_perm_on_rejected_message, Toast.LENGTH_SHORT).show();
    }
}
