package com.example.recimeproject.DataLayer.model;

import java.util.List;

public class IngredientResponse {
    private List<Ingredient> meals;

    public List<Ingredient> getMeals() {
        return meals;
    }

    public void setMeals(List<Ingredient> meals) {
        this.meals = meals;
    }
}