package com.example.recimeproject.ui.calenderScreen;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recimeproject.DataLayer.local.LocalDataSource;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.remote.RemoteDataSource;
import com.example.recimeproject.DataLayer.repo.Repository;
import com.example.recimeproject.R;

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
    private Date date;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_meals);
        presenter = new CalenderPresenter(Repository.getInstance(LocalDataSource.getInstance(this), new RemoteDataSource()));

        CalendarView calendarView = findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long curDate = calendar.getTimeInMillis();

        calendar.add(Calendar.DAY_OF_YEAR, 6);
        long nxtDate = calendar.getTimeInMillis();
        calendarView.setMinDate(curDate);
        calendarView.setMaxDate(nxtDate);
        calendarView.setDate(curDate);

        calendarView.setOnDateChangeListener((view, year, month,dayOfMonth) -> {
            Calendar chosenDate=Calendar.getInstance();
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
                            throwable -> Log.e("CalenderMeals", "Errorin  fetching meals " + throwable.getMessage())
                    ));
        });
        recyclerView = findViewById(R.id.rvFavMeals);
        btnBack = findViewById(R.id.btnBack);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapter = new CalendredAdapter(this, new ArrayList<>(), meal -> {
            disposables.add(presenter.deleteCalendredMeal(meal.getIdMeal(), date)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> Log.d("CalenderMeals", "Meal deleted successfully"),
                            throwable -> Log.e("CalenderMeals", "Error deleting meal " + throwable.getMessage())
                    ));
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
    }

    @Override
    public void showCalenderMeals(List<Meal> meals) {
        adapter.setMealList(meals);
        Log.i("calenderMeals ", "showCalenderMeals:meals is ready for you ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}


