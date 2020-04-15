package com.example.covidtracker.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieDrawable;
import com.example.covidtracker.databinding.FragmentHelpPagerBinding;
import com.example.covidtracker.models.dataModels.Guideline;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpPagerFragment extends Fragment {

    private static final String ARG_KEY = "ARG";
    private FragmentHelpPagerBinding binding;

    public HelpPagerFragment() {
        // Required empty public constructor
    }

    public static HelpPagerFragment newInstance(Guideline guideline){
        HelpPagerFragment fragment = new HelpPagerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_KEY, guideline);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHelpPagerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments().containsKey(ARG_KEY)) {
            Guideline guideline = (Guideline) getArguments().getSerializable(ARG_KEY);
            if(guideline.getImage()!=0) {
                binding.pagerItemImage.setScaleX(0.5f);
                binding.pagerItemImage.setScaleY(0.5f);
                binding.pagerItemImage.setImageResource(guideline.getImage());
            }
            if(guideline.getBody()!=0) {
                //binding.tvBodyPagerItem.setMovementMethod(LinkMovementMethod.getInstance());
                binding.tvBodyPagerItem.setText(guideline.getBody());
            }
            if(guideline.getTitle()!=0) {
                binding.tvTitlePagerItem.setText(guideline.getTitle());
            }
            if(guideline.getAnim()!=0) {
                binding.pagerItemImage.setAnimation(guideline.getAnim());
                binding.pagerItemImage.setScale(10);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.pagerItemImage.playAnimation();
        binding.pagerItemImage.setRepeatCount(LottieDrawable.INFINITE);
    }
}
