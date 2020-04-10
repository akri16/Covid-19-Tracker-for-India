package com.example.covidtracker.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidtracker.models.dataModels.NewsArticle;
import com.example.covidtracker.databinding.NewsItemBinding;
import com.example.covidtracker.utils.GeneralUtils;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private static final String TAG = "NewsAdapter";
    private List<NewsArticle> newsArticles;

    public NewsAdapter(List<NewsArticle> newsArticles) {
        this.newsArticles = newsArticles;
        Log.d(TAG, "newsAdapter: " + newsArticles.size());
    }

    public void setNewsArticles(List<NewsArticle> newsArticles) {
        this.newsArticles = newsArticles;
        notifyDataSetChanged();
        Log.d(TAG, "setNewsArticles: " + newsArticles.size());
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        NewsItemBinding binding = NewsItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new NewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsArticle article = newsArticles.get(position);
        Log.d(TAG, "onBind: ");
        GeneralUtils.loadIntoImageView(holder.binding.newsItemImg, article.getImageUrl());
        holder.binding.newsItemTitle.setText(article.getTitle());
        holder.binding.newsItemBody.setText(GeneralUtils.cleanTextContent(article.getContent()));
        holder.binding.newsItemDate.setText(GeneralUtils.formatDate(article.getDate()));
        holder.binding.newsItemSource.setText(article.getSource().getName());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+newsArticles.size());
        return newsArticles.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        public NewsItemBinding binding;

        public NewsViewHolder(@NonNull NewsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
