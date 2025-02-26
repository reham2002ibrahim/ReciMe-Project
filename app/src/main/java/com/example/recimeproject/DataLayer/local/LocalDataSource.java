package com.example.recimeproject.DataLayer.local;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.model.MealDate;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LocalDataSource {
    private final MealDao mealDao;
    private LiveData <List<Meal>> favoriteMeals;
    private LiveData <List<Meal>> calendarMeals;
    private static LocalDataSource instance  = null ;
    private LocalDataSource(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.mealDao = db.mealDao();
     //   this.favoriteMeals = mealDao.getFavMeals();
      // this.calendarMeals = new ArrayList<>();
    }

    public  static  LocalDataSource getInstance(Context context){
        if (instance == null ) {
            instance = new LocalDataSource(context) ;
        }
        return  instance ;
    }
    public Flowable<List<Meal>> getFavoriteMeals() {
        return mealDao.getFavMeals();
    }
public Completable insertToFav(@NonNull Meal meal) {
    Boolean isFav = meal.getFav();
    if (isFav == null || !isFav) {
        meal.setFav(true);
        return mealDao.insertMeal(meal)
                .doOnComplete(() -> Log.d("LocalDataSource", "successfully added meal = " + meal.getStrMeal()))
                .doOnError(throwable -> Log.e("LocalDataSource", "Failed to add meal: " + throwable.getMessage() + ", fav value: " + isFav));
    } else {
        Log.d("LocalDataSource", "already in favorites your fav: " + meal.getStrMeal());
        return Completable.complete();
    }
}
    public Completable insertToCalendar(MealDate mealDate) {
        return mealDao.insertMealDate(mealDate)
                .doOnComplete(() -> Log.d("LocalDataSource", "calenderMeal date inserted: " + mealDate.getDate()))
                .doOnError(throwable -> Log.e("LocalDataSource", "error in inserting meal dat " + throwable.getMessage()));
    }

    public Flowable<List<String>> getCalendaredDate(Date date) {
        return mealDao.getMealIdsByDate(date);
    }

    public Completable deleteFromFav(Meal meal) {
        return mealDao.deleteMeal(meal)
                .doOnComplete(() -> Log.d("LocalDataSource","Meal has been deleted from fav " + meal.getStrMeal()))
                .doOnError(throwable -> Log.e("LocalDataSource","Can't delete it " + throwable.getMessage()));
    }

    public Completable deleteFromCalendar(String mealId, Date date) {
        return mealDao.deleteCalendredMeal(mealId, date)
                .doOnComplete(() -> Log.d("LocalDataSource","Meal has been deleted from calender  " + mealId))
                .doOnError(throwable -> Log.e("LocalDataSource","Can't delete it  " + throwable.getMessage()));
    }
}
