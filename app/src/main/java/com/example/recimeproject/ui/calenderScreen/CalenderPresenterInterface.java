package com.example.recimeproject.ui.calenderScreen;

import com.example.recimeproject.DataLayer.model.Meal;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface CalenderPresenterInterface {


  //  void getCalenderMeals() ;
    Flowable<List<Meal>> getMealsCalendered(Date date) ;


    Completable deleteCalendredMeal(String mealId , Date date) ;


}
