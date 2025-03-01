package com.example.recimeproject.DataLayer.model;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Calendar",
        indices = {@Index(value = {"mealId", "date"}, unique = true)}
)
public class MealDate {
    @PrimaryKey(autoGenerate=true)
    public int id;
    private String userId;
    @NonNull
    public String mealId;

    @NonNull
    public Date date;

    public MealDate(@NonNull String mealId, @NonNull Date date) {
        this.mealId = mealId;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public MealDate() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    @NonNull
    public String getMealId() {
        return mealId;
    }

    public void setMealId(@NonNull String mealId) {
        this.mealId = mealId;
    }
}
