package com.example.recimeproject.DataLayer.model;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("strIngredient")
    private String name;
    private String imageUrl;
    public Ingredient(String name, String imageUrl) {
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