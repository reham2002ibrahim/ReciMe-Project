package com.example.recimeproject.DataLayer.remote;

import com.example.recimeproject.DataLayer.model.AreaResponse;
import com.example.recimeproject.DataLayer.model.CategoriesResponse;
import com.example.recimeproject.DataLayer.model.IngredientResponse;
import com.example.recimeproject.DataLayer.model.MealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("random.php")
    Single<MealResponse> getRandomMeal();

    @GET("lookup.php")
    Single<MealResponse> getMealById(@Query("i") String mealId);

    @GET("search.php")
    Single<MealResponse> searchMealsByLetter(@Query("f") String letter);

    @GET("categories.php")
    Single<CategoriesResponse> getCategories();

    @GET("filter.php")
    Single<MealResponse> getMealsByCategory(@Query("c") String categoryName);

    @GET("list.php?i=list")
    Single<IngredientResponse> getIngredients();

    @GET("list.php?a=list")
    Single<AreaResponse> getAreas();


}