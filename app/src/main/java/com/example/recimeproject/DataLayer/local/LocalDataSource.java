package com.example.recimeproject.DataLayer.local;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.model.MealDate;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class LocalDataSource {
    private final MealDao mealDao;
    private LiveData <List<Meal>> favoriteMeals;
    private LiveData <List<Meal>> calendarMeals;
    private static LocalDataSource instance  = null ;
  //  private  final Context context ;
    private LocalDataSource(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.mealDao = db.mealDao();
        //this.context = context.getApplicationContext();

        //   this.favoriteMeals = mealDao.getFavMeals();
      // this.calendarMeals = new ArrayList<>();
    }


    public  static synchronized LocalDataSource getInstance(Context context){
        if (instance == null ) {
            instance = new LocalDataSource(context) ;
        }
        return  instance ;
    }
    public Flowable<List<Meal>> getFavoriteMeals() {
        return mealDao.getFavMeals();
    }
    public Flowable<List<Meal>> getAllMeals() {
        return mealDao.getAllMeals();
    }

/*
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

*/


    public Completable insertToFav(@NonNull Meal meal) {
        Boolean isFav = meal.getFav();
        if (isFav == null || !isFav) {
            meal.setFav(true);
            return mealDao.insertMeal(meal)
                    .doOnComplete(() -> Log.d("LocalDataSource", "Mehhhhh" + meal.getStrMeal()))
                    .doOnError(throwable -> Log.e("LocalDataSource", "nnn " + throwable.getMessage()));
        } else {
            Log.d("LocalDataSource", "Meal already in favorites: " + meal.getStrMeal());
            return Completable.complete();
        }
    }
  public Completable insertToCalendar(MealDate mealDate) {
      return mealDao.insertMealDate(mealDate);
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
    // for backup
    public Completable deleteAllData() {
        return mealDao.deleteAllMeals()
                .andThen(mealDao.deleteAllMealDates())
                .doOnComplete(() -> Log.d("LocalDataSource", "all data deleted"))
                .doOnError(throwable -> Log.e("LocalDataSource", "error in deleting data " + throwable.getMessage()));
    }

    public Completable insertMealDates(List<MealDate> mealDates) {
        return mealDao.insertMealDates(mealDates)
                .doOnComplete(() -> Log.d("LocalDataSource", "insert mealDate"))
                .doOnError(throwable -> Log.e("LocalDataSource", "error in insert" + throwable.getMessage()));
    }

    public Completable insertMeals(List<Meal> meals) {
        return mealDao.insertMeals(meals)
                .doOnComplete(() -> Log.d("LocalDataSource", "Meals inserted"))
                .doOnError(throwable -> Log.e("LocalDataSource", "rrror in serting " + throwable.getMessage()));
    } public Completable insertOneMeal(Meal meal) {
        return mealDao.insertMeal(meal)
                .doOnComplete(() -> Log.d("LocalDataSource", "insert  one meal"))
                .doOnError(throwable -> Log.e("LocalDataSource", "rrror in serting  " + throwable.getMessage()));
    }
    public Flowable<List<MealDate>> getMealDates() {
        return mealDao.getMealDates()
                .doOnError(throwable -> Log.e("LocalDataSource", "rrror in serting  " + throwable.getMessage()));
    }
/*
    public void closeDatabase() {
        AppDatabase db = AppDatabase.getInstance(context);
        if (db.isOpen()) {
            db.close();
            Log.d("LocalDataSource", "Database closed successfully");
        }
    }
*/


}
