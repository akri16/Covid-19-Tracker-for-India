package com.example.covidtracker.models.dataModels;

import java.io.Serializable;

public class Guideline implements Serializable {
    private int image;
    private int title;
    private int body;
    private int anim;

    public Guideline(int image, int title, int body, int anim) {
        this.image = image;
        this.title = title;
        this.body = body;
        this.anim = anim;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getBody() {
        return body;
    }

    public void setBody(int body) {
        this.body = body;
    }

    public int getAnim() {
        return anim;
    }

    public void setAnim(int anim) {
        this.anim = anim;
    }
}
