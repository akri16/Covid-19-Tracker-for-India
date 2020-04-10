package com.example.covidtracker.models.dataModels;

import com.google.gson.annotations.SerializedName;

public class NewsArticle {

    private NewsSource source;
    private String title;
    private String content;
    @SerializedName("urlToImage")
    private String imageUrl;
    private String url;
    @SerializedName("publishedAt")
    private String date;

    public NewsArticle() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public NewsSource getSource() {
        return source;
    }

    public void setSource(NewsSource source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
