package com.example.recimeproject.ui.calenderScreen;

import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.repo.Repository;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CalenderPresenter implements CalenderPresenterInterface {
    // private final SavedMeals view;
    private final Repository repository;

    public CalenderPresenter(Repository repo) {
        //  this.view = view;
        this.repository = repo;
    }

    public Flowable<List<Meal>> getMealsCalendered(Date date) {
        return repository.getMealsCalendared(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteCalendredMeal(String mealId, Date date) {
        return repository.deleteFromCalender(mealId, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
