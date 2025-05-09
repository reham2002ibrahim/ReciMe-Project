package com.example.recimeproject.ui.searchScreen;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.repo.Repository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Presenter implements PresenterInterface{
    private final SearchInterface view;
    private final Repository repository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public Presenter(SearchInterface view, Repository repository) {
        this.view = view;
        this.repository = repository;
    }


    @Override
    public void searchCategories() {
        Disposable disposable = repository.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> view.showAllCategories(categories),
                        throwable -> view.showError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }

    @Override
    public void searchIngredients() {
        Disposable disposable =repository.getIngredients ()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ingredients -> view.showAllIngredients(ingredients),
                        throwable -> view.showError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);

    }

    @Override
    public void searchAreas() {
        Disposable disposable =repository.getAreas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        areas -> view.showAllAreas(areas),
                        throwable -> view.showError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);

    }
    @Override
    public void searchByName(String mealName) {
        Disposable disposable = repository.getMealByName(mealName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealList -> {
                            if (mealList != null && !mealList.isEmpty()) {
                                view.showSearchResults(mealList);
                            } else {
                                view.showError("No data found");
                            }
                        },
                        throwable -> view.showError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }
    @Override
    public void searchMealsByCategory(String categoryName) {
        Disposable disposable = repository.getMealsByCategory(categoryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealList -> {
                            if (mealList != null && !mealList.isEmpty()) {
                                view.showMealsOfTheCategoty(mealList);
                            } else {
                                view.showError("category iss empty");
                            }
                        },
                        throwable -> view.showError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }


    public void searchMealsByIngredient(String ingrediantName) {
        Disposable disposable = repository.getMealsByIngredient(ingrediantName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealList -> {
                            if (mealList != null && !mealList.isEmpty()) {
                                view.showMealsOfTheIngredient(mealList);
                            } else {
                                view.showError("ingredians iss empty");
                            }
                        },
                        throwable -> view.showError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }
    public void searchMealsByArea(String areaName) {
        Disposable disposable = repository.getMealsByArea(areaName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealList -> {
                            if (mealList != null && !mealList.isEmpty()) {
                                view.showMealsOfTheArea(mealList);
                            } else {
                                view.showError("ingredians iss empty");
                            }
                        },
                        throwable -> view.showError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }




}
