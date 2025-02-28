package com.example.recimeproject.DataLayer.remote;

import com.example.recimeproject.DataLayer.model.Area;
import com.example.recimeproject.DataLayer.model.Category;
import com.example.recimeproject.DataLayer.model.Ingredient;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.model.MealResponse;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteDataSource {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static final String IMAGE_ingredients_URL = "https://www.themealdb.com/images/ingredients/";
    private static final String IMAGE_AREA_URL = "https://flagcdn.com/64x48/";
    private static final String PLACEHOLDER_IMAGE = "https://www.themealdb.com/images/placeholder.png";
    private final ApiService apiService;
    private static final Map<String, String> COUNTRY_CODES = new HashMap<>();

    static {
        COUNTRY_CODES.put("American", "us");
        COUNTRY_CODES.put("British", "gb");
        COUNTRY_CODES.put("Canadian", "ca");
        COUNTRY_CODES.put("Chinese", "cn");
        COUNTRY_CODES.put("Croatian", "hr");
        COUNTRY_CODES.put("Dutch", "nl");
        COUNTRY_CODES.put("Egyptian", "eg");
        COUNTRY_CODES.put("Filipino", "ph");
        COUNTRY_CODES.put("French", "fr");
        COUNTRY_CODES.put("Greek", "gr");
        COUNTRY_CODES.put("Indian", "in");
        COUNTRY_CODES.put("Irish", "ie");
        COUNTRY_CODES.put("Italian", "it");
        COUNTRY_CODES.put("Jamaican", "jm");
        COUNTRY_CODES.put("Japanese", "jp");
        COUNTRY_CODES.put("Kenyan", "ke");
        COUNTRY_CODES.put("Malaysian", "my");
        COUNTRY_CODES.put("Mexican", "mx");
        COUNTRY_CODES.put("Moroccan", "ma");
        COUNTRY_CODES.put("Norwegian", "no");
        COUNTRY_CODES.put("Polish", "pl");
        COUNTRY_CODES.put("Portuguese", "pt");
        COUNTRY_CODES.put("Russian", "ru");
        COUNTRY_CODES.put("Spanish", "es");
        COUNTRY_CODES.put("Thai", "th");
        COUNTRY_CODES.put("Tunisian", "tn");
        COUNTRY_CODES.put("Turkish", "tr");
        COUNTRY_CODES.put("Ukrainian", "ua");
        COUNTRY_CODES.put("Uruguayan", "uy");
        COUNTRY_CODES.put("Vietnamese", "vn");
    }

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
                        } else ingredient.setImageUrl(PLACEHOLDER_IMAGE);
                    }
                    return ingredients;
                });
    }
    public Single<List<Area>> getAreasWithImages() {
        return apiService.getAreas()
                .subscribeOn(Schedulers.io())
                .map(areaResponse -> {
                    List<Area> areas = areaResponse.getMeals();
                    for (Area area : areas) {
                        String countryCode = COUNTRY_CODES.get(area.getName());
                        if (countryCode != null) {
                            String imageUrl = IMAGE_AREA_URL + countryCode + ".png";
                            area.setImageUrl(imageUrl);
                        }
                    }
                    return areas;
                });
    }
    public Single<MealResponse> searchMealsByName(String mealName) {
        return apiService.searchMealsByName(mealName)
                .subscribeOn(Schedulers.io());
    }

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
    public Single<MealResponse> getMealsByCategory(String category) {
        return apiService.getMealsByCategory(category)
                .subscribeOn(Schedulers.io());
    }
    public Single<MealResponse> getMealsByIngredient(String ingredient) {
        return apiService.getMealsByIngredient(ingredient)
                .subscribeOn(Schedulers.io());
    }
    public Single<MealResponse> getMealsByArea(String area) {
        return apiService.getMealsByArea(area)
                .subscribeOn(Schedulers.io());
    }

}
