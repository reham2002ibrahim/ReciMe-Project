package com.example.recimeproject.ui.savedScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recimeproject.DataLayer.local.LocalDataSource;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.remote.RemoteDataSource;
import com.example.recimeproject.DataLayer.repo.Repository;
import com.example.recimeproject.R;
import com.example.recimeproject.ui.detailsOfMealScreen.DetailsOfMeal;
import com.example.recimeproject.ui.inspirationScreen.Inspiration;
import com.example.recimeproject.ui.inspirationScreen.SuggestionMealsAdapter;
import com.example.recimeproject.utils.SpacingForRV;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SavedMeals extends AppCompatActivity {
    private Presenter presenter ;
    private ImageView imgMeal, imgMealThumb;
   private  TextView txtMealName;
    RecyclerView recyclerView ;
    FavMealsAdapter adapter  ;
    Button btnBack  ;
    private CompositeDisposable disposables = new CompositeDisposable();


    LiveData<List<Meal>> liveFavData ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_meals);
        recyclerView = findViewById(R.id.rvFavMeals);
        btnBack = findViewById(R.id.btnBack);
     //   imgMealThumb = findViewById(R.id.imgMealThumb);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new FavMealsAdapter(SavedMeals.this, new ArrayList<>(), meal -> {
            presenter.deleteFavMeal(meal);
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacingForRV(50));

        // imgMeal = findViewById(R.id.imgMeal);
        // txtMealName = findViewById(R.id.txtMealName);
        presenter = new Presenter(Repository.getInstance( LocalDataSource.getInstance(this) , new RemoteDataSource() ));
        disposables.add(presenter.getFavMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> adapter.setMealList(meals),
                        throwable -> Log.e("SavedMeals", "Error fetching favorite meals: " + throwable.getMessage())
                ));


        Log.i("Favvvv", "onCreate:  donw ");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SavedMeals.this, Inspiration.class);
                startActivity(intent);
            }
        });
      /*  imgMealThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SavedMeals.this, DetailsOfMeal.class);
                if (mealId != null) {
                    intent.putExtra("mealId", mealId);
                    startActivity(intent);
                }

            }
        });*/

    }

}