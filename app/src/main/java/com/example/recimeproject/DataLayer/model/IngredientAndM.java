package com.example.recimeproject.DataLayer.model;

public class IngredientAndM {
    private String name;
    private String measure;
    private String imageUrl;

    public IngredientAndM(String name, String measure, String imageUrl) {
        this.name = name;
        this.measure = measure;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}