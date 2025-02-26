package com.example.recimeproject.ui.savedScreen;

import androidx.lifecycle.LiveData;

import com.example.recimeproject.DataLayer.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface PresenterInterface {



    Flowable<List<Meal>> getFavMeals();
    void deleteFavMeal(Meal meal);

}
