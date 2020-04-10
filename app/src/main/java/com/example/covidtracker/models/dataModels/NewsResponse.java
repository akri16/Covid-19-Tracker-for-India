package com.example.covidtracker.models.dataModels;

import com.example.covidtracker.models.dataModels.NewsArticle;

import java.util.List;

public class NewsResponse {

    private List<NewsArticle> articles;

    public List<NewsArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsArticle> articles) {
        this.articles = articles;
    }
}
