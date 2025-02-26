package com.example.recimeproject.ui.detailsOfMealScreen;

import java.util.Date;

public interface PresenterInterface {
    void getSelectedMeal(String mealId) ;

    void putSavedMeal(String mealId) ;

    void putCalenderMeal(String mealId, Date date) ;


}
