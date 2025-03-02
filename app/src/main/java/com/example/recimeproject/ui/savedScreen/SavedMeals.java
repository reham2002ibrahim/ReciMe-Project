package com.example.recimeproject.ui.savedScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recimeproject.DataLayer.local.LocalDataSource;
import com.example.recimeproject.DataLayer.remote.RemoteDataSource;
import com.example.recimeproject.DataLayer.repo.Repository;
import com.example.recimeproject.R;
import com.example.recimeproject.ui.calenderScreen.CalenderMeals;
import com.example.recimeproject.ui.inspirationScreen.Inspiration;
import com.example.recimeproject.ui.profileScreen.profile;
import com.example.recimeproject.ui.searchScreen.Search;
import com.example.recimeproject.utils.SpacingForRV;
import java.util.ArrayList;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SavedMeals extends AppCompatActivity {
    private Presenter presenter;
    private RecyclerView recyclerView;
    private FavMealsAdapter adapter;
    private Button btnBack;
    ImageView profileImg ;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved_meals);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        findViewById(R.id.btn_home).setOnClickListener(v -> {
            startActivity(new Intent(SavedMeals.this, Inspiration.class));
        });
        findViewById(R.id.btn_favorite).setOnClickListener(v -> {
            //startActivity(new Intent(SavedMeals.this, SavedMeals.class));
        });
        findViewById(R.id.btn_search).setOnClickListener(v -> {
            startActivity(new Intent(SavedMeals.this, Search.class));
        });
        findViewById(R.id.btn_calendar).setOnClickListener(v -> {
            startActivity(new Intent(SavedMeals.this, CalenderMeals.class));
        });

        recyclerView = findViewById(R.id.rvFavMeals);
        btnBack = findViewById(R.id.btnBack);
        profileImg = findViewById(R.id.profileImg);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new FavMealsAdapter(SavedMeals.this, new ArrayList<>(), meal -> {
            presenter.deleteFavMeal(meal);
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacingForRV(50));

        presenter = new Presenter(Repository.getInstance(LocalDataSource.getInstance(this), new RemoteDataSource()));
        disposables.add(presenter.getFavMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> adapter.setMealList(meals),
                        throwable -> Log.e("SavedMeals", "Error fetching favorite meals " + throwable.getMessage())
                ));


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SavedMeals.this, profile.class));
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
