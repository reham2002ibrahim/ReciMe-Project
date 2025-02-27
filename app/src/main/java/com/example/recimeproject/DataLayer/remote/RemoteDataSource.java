package com.example.recimeproject.DataLayer.remote;

import android.util.Log;

import com.example.recimeproject.DataLayer.model.Category;
import com.example.recimeproject.DataLayer.model.Ingredient;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.model.MealResponse;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class RemoteDataSource {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static final String IMAGE_ingredients_URL = "https://www.themealdb.com/images/ingredients/";

    private static final String PLACEHOLDER_IMAGE = "https://www.themealdb.com/images/placeholder.png";

    private final ApiService apiService;

    public RemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        this.apiService = retrofit.create(ApiService.class);
    }
    public Single<MealResponse> getRandomMeal() {
        return apiService.getRandomMeal().subscribeOn(Schedulers.io());
    }

    public Single<List<Ingredient>> getIngredientsWithImages() {
        return apiService.getIngredients()
                .subscribeOn(Schedulers.io())
                .map(ingredientResponse -> {
                    List<Ingredient> ingredients = ingredientResponse.getMeals();
                    for (Ingredient ingredient : ingredients) {
                        if (ingredient.getName() != null && !ingredient.getName().isEmpty()) {
                            String imageUrl = IMAGE_ingredients_URL + ingredient.getName() + "-Small.png";
                            ingredient.setImageUrl(imageUrl);
                        } else {
                            // إذا كان الاسم `null` أو فارغ، استخدم صورة افتراضية
                            ingredient.setImageUrl(PLACEHOLDER_IMAGE);
                        }
                        // تسجيل البيانات القادمة من API للتأكد من القيم
                        Log.d("IngredientCheck", "Ingredient name: " + ingredient.getName() + ", Image: " + ingredient.getImageUrl());
                    }
                    return ingredients;
                });
    }

    public Single<List<Ingredient>> getIngredients() {
        return getIngredientsWithImages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


//    public Single<List<Ingredient>> getIngredientsWithImages() {
//        return apiService.getIngredients()
//                .subscribeOn(Schedulers.io())
//                .map(ingredientResponse -> {
//                    List<Ingredient> ingredients = ingredientResponse.getMeals();
//                    for (Ingredient ingredient : ingredients) {
//                        String imageUrl = IMAGE_ingredients_URL + ingredient.getName() + "-Small.png";
//
//                       // String imageUrl = IMAGE_ingredients_URL + ingredient.getName() + "-Small.png";
//                        ingredient.setImageUrl(imageUrl);
//                    }
//                    return ingredients;
//                });
//    }


    public Single<Meal> getMealById(String mealId) {
    return apiService.getMealById(mealId)
            .subscribeOn(Schedulers.io())
            .map(mealResponse -> {
                if (mealResponse.getMeals() != null && !mealResponse.getMeals().isEmpty()) {
                    return mealResponse.getMeals().get(0);
                } else {
                    throw new RuntimeException("Nomealsfound");
                }
            });
}
    public Single<List<Meal>> searchMealsByLetters() {
        String[] letters = {"M"};
        return Observable.fromArray(letters)
                .flatMapSingle(letter -> apiService.searchMealsByLetter(letter))
                .flatMapIterable(MealResponse::getMeals)
                .toList()
                .subscribeOn(Schedulers.io());
                //.observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Category>> getCategories() {
        return apiService.getCategories()
                .subscribeOn(Schedulers.io())
                .map(categoriesResponse -> {
                    if (categoriesResponse.getCategories() != null) {
                        return categoriesResponse.getCategories();
                    } else throw new RuntimeException("No results categories");

                });
    }
    public Single<MealResponse> getMealsByCategory(String categoryName) {
        return apiService.getMealsByCategory(categoryName)
                .subscribeOn(Schedulers.io());
    }

/*    public Single<List<Ingredient>> getIngredientsWithImages() {
        return apiService.getIngredients()
                .subscribeOn(Schedulers.io())
                .map(ingredientResponse -> {
                    List<Ingredient> ingredients = ingredientResponse.getMeals();
                    for (Ingredient ingredient : ingredients) {
                        String imageUrl = IMAGE_ingredients_URL + ingredient.getName() + "-Small.png";
                        ingredient.setImageUrl(imageUrl);
                    }
                    return ingredients;
                });
    }*/



}
