package com.example.recimeproject.DataLayer.repo;

import android.util.Log;

import com.example.recimeproject.DataLayer.local.LocalDataSource;
import com.example.recimeproject.DataLayer.model.Area;
import com.example.recimeproject.DataLayer.model.Category;
import com.example.recimeproject.DataLayer.model.Ingredient;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.model.MealDate;
import com.example.recimeproject.DataLayer.model.MealResponse;
import com.example.recimeproject.DataLayer.remote.RemoteDataSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Repository {
    private static Repository instance;
    private final LocalDataSource localDataSource;
    private final RemoteDataSource remoteDataSource;

    private Repository(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public static synchronized Repository getInstance(LocalDataSource local , RemoteDataSource remote) {
        if (instance == null) {
            instance = new Repository(local , remote);
        }
        return instance;
    }
    public Single<MealResponse> fetchRandomMeal() {
        return remoteDataSource.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Meal>> searchMealsByLetters() {
        return remoteDataSource.searchMealsByLetters().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Meal> getMealById(String mealId) {
        return remoteDataSource.getMealById(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    // searchSvreen
    public Single<List<Category>> getCategories() {
        return remoteDataSource.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Single<List<Ingredient>> getIngredients() {
        return remoteDataSource.getIngredientsWithImages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Single<List<Area>> getAreas() {
        return remoteDataSource.getAreasWithImages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Meal>> getMealByName(String mealName) {
        return remoteDataSource.searchMealsByName(mealName)
                .map(MealResponse::getMeals)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }



    public Single<MealResponse> getMealsByCategory(String categoryName) {
        return remoteDataSource.getMealsByCategory(categoryName);
    }

    public Completable insertToFav(String mealId) {
        return remoteDataSource.getMealById(mealId)
                .flatMapCompletable(meal -> {
                    if (meal != null) {
                        Log.d("Repository", "awsame meal  " + meal.getStrMeal());
                        return localDataSource.insertToFav(meal);
                    } else {
                        Log.e("Repository", "obss got null");
                        return Completable.error(new NullPointerException("obs Meal object is null"));
                    }
                })
                .doOnComplete(() -> Log.d("Repository", "fav meal inserted: " + mealId))
                .doOnError(throwable -> Log.e("Repository", "can'e  inserting favorite meal: " + throwable.getMessage()));
    }

    public Completable insertToCalendar(String mealId, Date date) {
        MealDate mealDate = new MealDate(mealId, date);
        return localDataSource.insertToCalendar(mealDate)
                .doOnComplete(() -> Log.d("Repository", "Meal inserted to calender " + mealId))
                .doOnError(throwable -> Log.e("Repository", "can't insert calendar meal " + throwable.getMessage()));
    }

    public Flowable<List<Meal>> getFavMeals() {
        return localDataSource.getFavoriteMeals();
    }

    public Completable deleteFromFav(Meal meal) {
        return localDataSource.deleteFromFav(meal);
    }

    public Flowable<List<Meal>> getMealsCalendared(Date date) {
        return localDataSource.getCalendaredDate(date)
                .flatMap(mealIds -> {
                    if (mealIds != null && !mealIds.isEmpty()) {
                        return Flowable.fromIterable(mealIds)
                                .flatMapSingle(remoteDataSource::getMealById)
                                .toList()
                                .toFlowable();
                    } else {
                        return Flowable.just(new ArrayList<>());
                    }
                });
    }
    public Flowable<List<String>> getCalendaredDate(Date date) {
        return localDataSource.getCalendaredDate(date);
    }

    public Completable deleteFromCalender(String mealId, Date date) {
        return localDataSource.deleteFromCalendar(mealId, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
