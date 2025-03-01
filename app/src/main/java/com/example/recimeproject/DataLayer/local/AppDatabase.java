package com.example.recimeproject.DataLayer.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.model.MealDate;

//@Database(entities = {Meal.class}, version = 1, exportSchema = false)
@Database(entities = {Meal.class, MealDate.class}, version = 3, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MealDao mealDao();
    private  static AppDatabase instance  = null ;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "meals_database")
                    .fallbackToDestructiveMigration()
                   .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                    .build();

        }
        return instance;
    }

}