package com.example.recimeproject.ui.searchScreen;

public interface PresenterInterface {

    void searchCategories();
    void searchIngredients();

    void searchAreas();
    void searchByName(String mealName);
    void searchMealsByCategory(String categoryName);
}
