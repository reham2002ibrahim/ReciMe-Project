package com.example.recimeproject.ui.searchScreen;

import com.example.recimeproject.DataLayer.model.Area;
import com.example.recimeproject.DataLayer.model.Category;
import com.example.recimeproject.DataLayer.model.Ingredient;

import java.util.List;

public interface SearchInterface {
    void showAllCategories(List<Category> categories);
    void showAllIngredients(List<Ingredient> ingredients);
    void showAllAreas(List<Area> ingredients);


  //  void showMeals(List<Meal> meals);
    void showError(String message);


}
