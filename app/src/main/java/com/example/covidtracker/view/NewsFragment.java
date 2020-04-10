package com.example.covidtracker.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.covidtracker.models.dataModels.NewsResponse;
import com.example.covidtracker.viewmodels.NewsViewModel;
import com.example.covidtracker.R;
import com.example.covidtracker.adapters.NewsAdapter;
import com.example.covidtracker.databinding.FragmentNewsBinding;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    private static final String TAG = "NewsFragment";
    private FragmentNewsBinding binding;
    private LiveData<NewsResponse> liveNewsResponse;
    private NewsAdapter adapter;
    private RecyclerView recyclerView;
    private NewsViewModel viewModel;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = view.findViewById(R.id.news_rv);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(NewsViewModel.class);
        liveNewsResponse = viewModel.getLiveNewsResponse();

        NewsAdapter adapter = new NewsAdapter(new ArrayList<>());
        liveNewsResponse.observe(getViewLifecycleOwner(), newsResponse -> {
            adapter.setNewsArticles(newsResponse.getArticles());
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        Log.d(TAG, "onCreateView: "+recyclerView.getAdapter());

    }
}
