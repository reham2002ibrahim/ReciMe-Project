package com.example.recimeproject.ui.calenderScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.recimeproject.DataLayer.local.LocalDataSource;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.remote.RemoteDataSource;
import com.example.recimeproject.DataLayer.repo.Repository;
import com.example.recimeproject.R;
import com.example.recimeproject.ui.inspirationScreen.Inspiration;
import com.example.recimeproject.ui.loginScreen.Login;
import com.example.recimeproject.ui.profileScreen.profile;
import com.example.recimeproject.ui.savedScreen.SavedMeals;
import com.example.recimeproject.ui.searchScreen.Search;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CalenderMeals extends AppCompatActivity implements CalenderMealsInterface {
    private CalenderPresenter presenter;
    private RecyclerView recyclerView;
    private CalendredAdapter adapter;
    private Button btnBack;
    ImageView profileImg ;
    private Date date;
    LottieAnimationView lottieAnimationView ;

    private CompositeDisposable disposables = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_meals);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        findViewById(R.id.btn_home).setOnClickListener(v -> {
                startActivity(new Intent(CalenderMeals.this, Inspiration.class));
        });
        findViewById(R.id.btn_favorite).setOnClickListener(v -> {
            startActivity(new Intent(CalenderMeals.this, SavedMeals.class));
        });
        findViewById(R.id.btn_search).setOnClickListener(v -> {
            startActivity(new Intent(CalenderMeals.this, Search.class));
        });
        findViewById(R.id.btn_calendar).setOnClickListener(v -> {
           // startActivity(new Intent(CalenderMeals.this, CalenderMeals.class));
        });





        presenter = new CalenderPresenter(Repository.getInstance(LocalDataSource.getInstance(this), new RemoteDataSource()));

        CalendarView calendarView = findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long curDate = calendar.getTimeInMillis();
        date = calendar.getTime();
        Log.d("CalenderMeals", "Initialized Date: " + date); // تسجيل تاريخ التهيئة

        calendar.add(Calendar.DAY_OF_YEAR, 6);
        long nxtDate = calendar.getTimeInMillis();
        calendarView.setMinDate(curDate);
        calendarView.setMaxDate(nxtDate);
        calendarView.setDate(curDate);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar chosenDate = Calendar.getInstance();
            chosenDate.set(year, month, dayOfMonth);
            chosenDate.set(Calendar.HOUR_OF_DAY, 0);
            chosenDate.set(Calendar.MINUTE, 0);
            chosenDate.set(Calendar.SECOND, 0);
            chosenDate.set(Calendar.MILLISECOND, 0);
            date = chosenDate.getTime();
            disposables.add(presenter.getMealsCalendered(date)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            this::showCalenderMeals,
                            throwable -> Log.e("CalenderMeals", "Error in لثسس " + throwable.getMessage())
                    ));
        });

        recyclerView = findViewById(R.id.rvFavMeals);
        btnBack = findViewById(R.id.btnBack);
        profileImg = findViewById(R.id.profileImg);
        lottieAnimationView = findViewById(R.id.lottieAnimationView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapter = new CalendredAdapter(this, new ArrayList<>(), meal -> {
            if (date != null) {
                Log.d("CalenderMeals", "MealID = " + meal.getIdMeal());
                Log.d("CalenderMeals", "MealDate =  " + date);

                disposables.add(presenter.deleteCalendredMeal(meal.getIdMeal(), date)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    Log.d("CalenderMeals", "Meal deleted successfully");
                                    int position = adapter.getMealList().indexOf(meal);
                                    if (position != -1) {
                                        adapter.removeMeal(position);
                                    }
                                },
                                throwable -> Log.e("CalenderMeals", "erreoe in delete" + throwable.getMessage())
                        ));
            } else {
                Log.e("CalenderMeals", " date = null");
            }
        });

        recyclerView.setAdapter(adapter);
        disposables.add(presenter.getMealsCalendered(new Date(curDate))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showCalenderMeals,
                        throwable -> Log.e("CalenderMeals", "error fetching meals: " + throwable.getMessage())
                ));

        btnBack.setOnClickListener(v -> finish());
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalenderMeals.this, profile.class));

            }
        });
    }

    @Override
    public void showCalenderMeals(List<Meal> meals) {
        adapter.setMealList(meals);
        Log.i("calenderMeals ", "showCalenderMeals: meals are ready for you");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

}
