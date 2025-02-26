package com.example.recimeproject.ui.inspirationScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recimeproject.DataLayer.local.LocalDataSource;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.remote.RemoteDataSource;
import com.example.recimeproject.DataLayer.repo.Repository;
import com.example.recimeproject.R;
import com.example.recimeproject.ui.calenderScreen.CalenderMeals;
import com.example.recimeproject.ui.detailsOfMealScreen.DetailsOfMeal;
import com.example.recimeproject.ui.profileScreen.profile;
import com.example.recimeproject.ui.savedScreen.SavedMeals;

import java.util.Calendar;
import java.util.List;


public class Inspiration extends AppCompatActivity implements InspirationInterface {
    private Presenter presenter;
    ImageView imgMeal , profileImg;
    TextView txtMealName;
    private String mealId;
    private String mealName;
    Button btnSaved , btnCalender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inspiration);
        imgMeal = findViewById(R.id.imgMeal);
        txtMealName = findViewById(R.id.txtMealName);
        btnSaved = findViewById(R.id.btnSaved);
        btnCalender = findViewById(R.id.btnCalender);
        profileImg = findViewById(R.id.profileImg);
        presenter = new Presenter(this, Repository.getInstance(LocalDataSource.getInstance(this) , new RemoteDataSource()));
        presenter.getRandomMeal();
        presenter.getSuggestionMeals();

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inspiration.this, profile.class);
                    startActivity(intent);
            }
        });



        // navigation
        imgMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inspiration.this, DetailsOfMeal.class);
                if (mealId != null) {
                    intent.putExtra("mealId", mealId);
                    startActivity(intent);
                }
            }
        });

        btnSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inspiration.this, SavedMeals.class);
                startActivity(intent);

            }
        });
        btnCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inspiration.this, CalenderMeals.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void showRandomMeal(Meal meal) {
        mealId = meal.getIdMeal();
        txtMealName.setText(meal.getStrMeal());
        Glide.with(this).load(meal.getStrMealThumb()).into(imgMeal);

    }
    @Override
    public void showSuggestionMeals(List<Meal> meals) {
        RecyclerView recyclerView = findViewById(R.id.rvSuggestionMeals);
       // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SuggestionMealsAdapter adapter = new SuggestionMealsAdapter(Inspiration.this, meals);
        recyclerView.setAdapter(adapter);
    }






}