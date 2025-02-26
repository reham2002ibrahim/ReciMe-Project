package com.example.recimeproject.DataLayer.local;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.model.MealDate;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMeal(Meal meal);

    @Delete
    Completable deleteMeal(Meal meal);

    @Query("SELECT * FROM meals")
    Flowable<List<Meal>> getAllMeals();


    @Query("SELECT * FROM meals WHERE idMeal = :mealId")
    Flowable<Meal> getMealById(String mealId);


    @Query("SELECT * FROM meals WHERE fav = true ")
    Flowable<List<Meal>> getFavMeals();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMealDate(MealDate mealDate);

    @Query("SELECT mealId from calendar WHERE date = :date")
    Flowable<List<String>> getMealIdsByDate(Date date);

    @Query("DELETE  from calendar where mealId = :mealId and date = :date")
    Completable deleteCalendredMeal(String mealId, Date date);
}
