package com.example.recimeproject.ui.savedScreen;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.repo.Repository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Presenter implements PresenterInterface{

   // private final SavedMeals view;
    private final Repository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public Presenter(Repository repo ) {
      //  this.view = view;
        this.repository = repo ;
    }
    public Flowable<List<Meal>> getFavMeals() {
        return repository.getFavMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void deleteFavMeal(Meal meal) {
        disposables.add(repository.deleteFromFav(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Log.d("Presenter", "Meal deleted from fav " + meal.getStrMeal());
                        },
                        throwable -> {
                            Log.e("Presenter", "can't deleting meal from fav " + throwable.getMessage());
                        }
                ));
    }

    public void onDestroy() {
        disposables.clear();
    }

}
