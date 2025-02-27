package com.example.recimeproject.ui.searchScreen;

import com.example.recimeproject.DataLayer.model.Category;
import com.example.recimeproject.DataLayer.model.Meal;

import java.util.List;

public interface SearchInterface {
    void showSearchResults(List<Category> categories);
  //  void showMeals(List<Meal> meals);
    void showError(String message);


}
