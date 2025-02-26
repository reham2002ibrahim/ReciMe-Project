package com.example.recimeproject.ui.inspirationScreen;

import android.content.Context;

import com.example.recimeproject.DataLayer.model.Meal;

import com.example.recimeproject.DataLayer.repo.Repository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Presenter implements PresenterInterface {
    private final InspirationInterface view;
    private final Repository repository;

    public Presenter(Inspiration view, Repository repo) {
        this.view = view;
        this.repository = repo;
    }

    public void getRandomMeal() {
        repository.fetchRandomMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealResponse -> {
                            if (mealResponse.getMeals() != null && !mealResponse.getMeals().isEmpty()) {
                                view.showRandomMeal(mealResponse.getMeals().get(0));
                            }} );
    }

    public void getSuggestionMeals() {
        repository.searchMealsByLetters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> view.showSuggestionMeals(meals)
                );
    }
}
