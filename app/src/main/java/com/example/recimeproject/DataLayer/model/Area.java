package com.example.recimeproject.DataLayer.model;

import com.google.gson.annotations.SerializedName;

public class Area {


    @SerializedName("strArea")
    private String name;
    private String imageUrl;

    public Area(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}