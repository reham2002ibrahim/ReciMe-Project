package com.example.recimeproject.DataLayer.repo;

import android.util.Log;

import com.example.recimeproject.DataLayer.local.LocalDataSource;
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
        return remoteDataSource.searchMealsByLetters();
    }

    public Single<Meal> getMealById(String mealId) {
        return remoteDataSource.getMealById(mealId);
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
                                .flatMapSingle(remoteDataSource::getMealById) // تحويل كل mealId إلى Meal
                                .toList() // جمع النتائج في List<Meal>
                                .toFlowable(); // تحويل Single<List<Meal>> إلى Flowable<List<Meal>>
                    } else {
                        return Flowable.just(new ArrayList<>()); // إرجاع قائمة فارغة إذا لم توجد mealIds
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


/*    public LiveData<List<Meal>> getAllMeals() {
        return localDataSource.getAllMeals();
    }*/
/*public Single<MealResponse> fetchRandomMeal() {
    return remoteDataSource.getRandomMeal()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
}

  public Single<List<Meal>> searchMealsByLetters() {
      return remoteDataSource.searchMealsByLetters();
  }
    public Single<Meal> getMealById(String mealId) {
        return remoteDataSource.getMealById(mealId);
    }
    // detalis meals


    public void inserTotFav(String mealId) {
        getMealById(mealId, new NetworkCallback<Meal>() {
            @Override
            public void onSuccess(Meal meal) {
                if (meal != null) {
                    Log.d("NetworkCallback", "Retrieved meal object is not null: " + meal.getStrMeal());
                    localDataSource.inserTotFav(meal);
                    Log.d("fav meal inserted", "onSuccess: donee");
                } else {
                    Log.e("Error", "Retrieved meal object is null");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("Error", "Failed to retrieve meal: " + errorMessage);
            }
        });
    }

    public void deleteFromFav (Meal meal) {
        localDataSource.deleteMeal(meal);
    }

    public void deleteFromCalender (String mealId , Date date) {
        localDataSource.deleteFromCalender(mealId , date);
    }



    public void inserTotCalendar(String mealId, Date date) {
        MealDate mealDate = new MealDate(mealId, date);
        localDataSource.inserTotCalendar(mealDate);
        Log.d("calendar meal inserted", "onSuccess: doneeeeeeeeeeeeeeeee");
    }
    public LiveData<List<Meal>> getFavMeals() {
        return localDataSource.getFavoriteMeals() ;
    }

    public LiveData<List<Meal>> getMealsCalendered(Date date) {
        MediatorLiveData<List<Meal>> result = new MediatorLiveData<>();
        LiveData<List<String>> allMeals = localDataSource.getCalenderedDate(date);

        result.addSource(allMeals, mealIds -> {
            if (mealIds != null && !mealIds.isEmpty()) {
                List<Meal> meals = new ArrayList<>();
                for (String it : mealIds) {
                    Log.d("Repository", "Fetching mealId: " + it);
                    remoteDataSource.getMealById(it, new NetworkCallback<Meal>() {
                        @Override
                        public void onSuccess(Meal meal) {
                            Log.d("Repository", "Fetched meal: " + meal.getStrMeal());
                            meals.add(meal);
                            result.setValue(meals);
                        }
                        @Override
                        public void onFailure(String errorMessage) {
                            Log.e("Repository", "Error fetching meal");
                        }
                    });
                }
            } else {result.setValue(new ArrayList<>());}
        });
        return result;
    }
    public LiveData<List<String>>getCalenderedDate(Date date){
        return  localDataSource.getCalenderedDate(date) ;
    }
}*/

  /*  public void inserTotCalendar(String mealId, Date date) {
        getMealById(mealId, new NetworkCallback<Meal>() {
            @Override
            public void onSuccess(Meal meal) {
                MealDate mealDate = new MealDate(mealId, date);
                localDataSource.inserTotCalendar(mealDate);
                Log.d("calendar meal inserted", "onSuccess: doneeeeeeeeeeeeeeeee");
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("Error", "Failed to retrieve meal: " + errorMessage);
            }
        });
    }*/


    // saved screen




    // repo get list from local and pass it to remote to get info about each meal
//    public LiveData<List<Meal>> getCalenderdMeals(Date date) {
//        LiveData<List<String>> allMeals =   localDataSource.getCalenderedDate(date) ;
//        LiveData<List<Meal>> ans ;
//        for ( String it : allMeals) {
//            remoteDataSource.getMealById(it, NetworkCallback<Meal> callback);
//        }
//
//
//    }



/*
    public void getCalenderMeals(){
        localDataSource.getCalendarMeals() ;
    }*/


 /*   public  LiveData<List<Meal>> getMeals() {

        return localDataSource.getAllMeals() ;
    }*/



/*  public void fetchRandomMeal(NetworkCallback<Meal> callback) {
        remoteDataSource.getRandomMeal(new NetworkCallback<Meal>() {
            @Override
            public void onSuccess(Meal meal) {
            //    localDataSource.insertMeal(meal);
                callback.onSuccess(meal);
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }*/
/*    public void searchMealsByLetters(NetworkCallback<List<Meal>> callback) {
        remoteDataSource.searchMealsByLetters(new NetworkCallback<List<Meal>>() {
            @Override
            public void onSuccess(List<Meal> meals) {
             *//*   for (Meal meal : meals) {
                    localDataSource.insertMeal(meal);
                }*//*
                callback.onSuccess(meals);
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }*/
