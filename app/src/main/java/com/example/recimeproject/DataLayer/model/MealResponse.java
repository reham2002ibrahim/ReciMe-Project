package com.example.recimeproject.DataLayer.model;

import java.util.List;

public class MealResponse {
    private List<Meal> meals;

    public MealResponse(List<Meal> meals) {
        this.meals = meals;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public Meal getMeal(int pos) {
        return meals.get(pos);
    }
}