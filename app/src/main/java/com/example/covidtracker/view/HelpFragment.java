package com.example.covidtracker.view;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.covidtracker.R;
import com.example.covidtracker.adapters.GuidelinesPagerAdapter;
import com.example.covidtracker.databinding.FragmentHelpBinding;
import com.example.covidtracker.utils.DataUtils;
import com.example.covidtracker.utils.GeneralUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    private static final int REQUEST_CODE = 1002;
    private FragmentHelpBinding binding;
    private final String[] CALL_PERMISION = {Manifest.permission.CALL_PHONE};
    private ViewPager.OnPageChangeListener listener;

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

        binding.callHelplineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.guidelinesPager.setAdapter(new GuidelinesPagerAdapter(
                getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

        binding.fabHelpForward.setOnClickListener(v -> {
            int newPos = binding.guidelinesPager.getCurrentItem() + 1;
            if (newPos < GuidelinesPagerAdapter.SIZE) {
                binding.guidelinesPager.setCurrentItem(newPos, true);
            }
        });

        binding.fabHelpBack.setOnClickListener(v -> {
            int newPos = binding.guidelinesPager.getCurrentItem() - 1;
            if (newPos > -1) {
                binding.guidelinesPager.setCurrentItem(newPos, true);
            }
        });

        binding.pagerProgress.setProgress(100 / GuidelinesPagerAdapter.SIZE);

        listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.fabHelpBack.show();
                binding.fabHelpForward.show();
                if (position == 0) {
                    binding.fabHelpBack.hide();
                } else if (position == GuidelinesPagerAdapter.SIZE - 1) {
                    binding.fabHelpForward.hide();
                }
                int progress = 100 * (position + 1) / GuidelinesPagerAdapter.SIZE;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.pagerProgress.setProgress(progress, true);
                } else {
                    binding.pagerProgress.setProgress(progress);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        binding.guidelinesPager.addOnPageChangeListener(listener);
    }

    private void makeCall() {
        if (!GeneralUtils.checkPermission(getContext(), CALL_PERMISION)) {
            ActivityCompat.requestPermissions(getActivity(), CALL_PERMISION, REQUEST_CODE);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + DataUtils.HELPLINE_NO));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && GeneralUtils.checkPermission(getContext(), CALL_PERMISION)) {
            makeCall();
        }
        Toast.makeText(getContext(), R.string.call_perm_on_rejected_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.guidelinesPager.removeOnPageChangeListener(listener);
    }
}
