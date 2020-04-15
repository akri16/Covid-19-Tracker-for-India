package com.example.covidtracker.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.covidtracker.interfaces.FetchDataCallback;
import com.example.covidtracker.models.dataModels.NewsResponse;
import com.example.covidtracker.utils.GeneralUtils;
import com.example.covidtracker.viewmodels.CustomViewModelFactory;
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

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        recyclerView = binding.newsRv;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.newsSwipeRefresh.setOnRefreshListener(() -> {
            viewModel.refreshData();
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CustomViewModelFactory factory = new CustomViewModelFactory(getActivity().getApplication(),
                new FetchDataCallback() {
                    @Override
                    public void onFailure() {
                        Log.d(TAG, "onFailure: ");
                        if(binding!=null) {
                            binding.newsSwipeRefresh.setRefreshing(false);
                            GeneralUtils.showNoInternetSnackbar(getActivity().findViewById(R.id.bottom_nav));
                        }
                    }

                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: ");
                        if(binding!=null) {
                            binding.newsSwipeRefresh.setRefreshing(false);
                        }
                    }
                }, CustomViewModelFactory.NEWS_MODEL);
        viewModel = new ViewModelProvider(this
                , factory).get(NewsViewModel.class);
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
