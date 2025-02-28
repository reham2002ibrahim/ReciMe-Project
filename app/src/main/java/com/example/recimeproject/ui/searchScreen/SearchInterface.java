package com.example.recimeproject.ui.searchScreen;

import com.example.recimeproject.DataLayer.model.Area;
import com.example.recimeproject.DataLayer.model.Category;
import com.example.recimeproject.DataLayer.model.Ingredient;
import com.example.recimeproject.DataLayer.model.Meal;

import java.util.List;

public interface SearchInterface {
    void showAllCategories(List<Category> categories);
    void showAllIngredients(List<Ingredient> ingredients);
    void showAllAreas(List<Area> ingredients);
    void showSearchResults(List<Meal> mealList);
    void showMealsOfTheCategoty(List<Meal> mealList);
    void showMealsOfTheIngredient(List<Meal> mealList);
    void showMealsOfTheArea(List<Meal> mealList);

  //  void showMeals(List<Meal> meals);
    void showError(String message);


}
