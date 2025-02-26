package com.example.recimeproject.ui.detailsOfMealScreen;

import android.util.Log;

import com.example.recimeproject.DataLayer.repo.Repository;

import java.util.Date;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Presenter implements PresenterInterface{
    private final DetailsOfMeal view;
    private final Repository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();


    public Presenter(DetailsOfMeal view,Repository repo) {
        this.view = view;
        this.repository =repo ;
    }

/*    @Override
    public void getSelectedMeal(String mealId) {

    }*/

    @Override
    public void getSelectedMeal(String mealId) {
        disposables.add(repository.getMealById(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meal -> view.showSelectedMeal(meal),
                        throwable -> {
                            Log.e("Presenter", "Error in get meal: " + throwable.getMessage());
                        }
                ));
    }
    @Override
    public void putSavedMeal(String mealId) {
        disposables.add(repository.insertToFav(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Log.d("Presenter", "susseccfuly fav meal added  " + mealId);
                        },
                        throwable -> {
                            Log.e("Presenter", "Error  in saving " + throwable.getMessage());
                        }
                ));
    }

    public void putCalenderMeal(String mealId, Date date) {
        disposables.add(repository.insertToCalendar(mealId, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("Presenter", "Meal saved to u calendar: " + mealId),
                        throwable -> Log.e("Presenter", "Error saving meal to calendar: " + throwable.getMessage())
                ));
    }


}
