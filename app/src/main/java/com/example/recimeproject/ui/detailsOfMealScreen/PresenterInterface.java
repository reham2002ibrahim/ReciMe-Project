package com.example.recimeproject.ui.detailsOfMealScreen;

import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.model.MealDate;

import java.util.Date;

public interface PresenterInterface {
    void getSelectedMeal(String mealId) ;

    void putSavedMeal(String mealId) ;

    void putCalenderMeal(Meal meal , MealDate mealDate) ;


}
