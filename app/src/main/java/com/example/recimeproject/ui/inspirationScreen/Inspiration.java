package com.example.recimeproject.ui.inspirationScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.recimeproject.DataLayer.local.LocalDataSource;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.remote.RemoteDataSource;
import com.example.recimeproject.DataLayer.repo.Repository;
import com.example.recimeproject.R;
import com.example.recimeproject.ui.calenderScreen.CalenderMeals;
import com.example.recimeproject.ui.detailsOfMealScreen.DetailsOfMeal;
import com.example.recimeproject.ui.loginScreen.Login;
import com.example.recimeproject.ui.profileScreen.profile;
import com.example.recimeproject.ui.savedScreen.SavedMeals;
import com.example.recimeproject.ui.searchScreen.Search;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;

public class Inspiration extends AppCompatActivity implements InspirationInterface {
    private Presenter presenter;
    ImageView imgMeal, profileImg;
    TextView txtMealName;
    private String mealId;
    Button btnSaved, btnCalender, btnSearch;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inspiration);

        imgMeal = findViewById(R.id.imgMeal);
        txtMealName = findViewById(R.id.txtMealName);
        btnSaved = findViewById(R.id.btnSaved);
        btnSearch = findViewById(R.id.btnSearch);
        btnCalender = findViewById(R.id.btnCalender);
        profileImg = findViewById(R.id.profileImg);
        presenter = new Presenter(this, Repository.getInstance(LocalDataSource.getInstance(this), new RemoteDataSource()));
        presenter.getRandomMeal();
        presenter.getSuggestionMeals();
        auth = FirebaseAuth.getInstance();

        profileImg.setOnClickListener(v -> {
            Intent intent = new Intent(Inspiration.this, profile.class);
            startActivity(intent);
        });

        imgMeal.setOnClickListener(v -> {
            Intent intent = new Intent(Inspiration.this, DetailsOfMeal.class);
            if (mealId != null) {
                intent.putExtra("mealId", mealId);
                startActivity(intent);
            }
        });

        btnSaved.setOnClickListener(v -> {
            if (auth.getCurrentUser() != null) {
                Intent intent = new Intent(Inspiration.this, SavedMeals.class);
                startActivity(intent);
            } else {
                showLoginDialog();
            }
        });

        btnCalender.setOnClickListener(v -> {
            Intent intent = new Intent(Inspiration.this, CalenderMeals.class);
            startActivity(intent);
        });

        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(Inspiration.this, Search.class);
            startActivity(intent);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SuggestionMealsAdapter adapter = new SuggestionMealsAdapter(Inspiration.this, meals);
        recyclerView.setAdapter(adapter);
    }

    private void showLoginDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_view, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();
        LottieAnimationView animationView = dialogView.findViewById(R.id.animation_view);
        animationView.setAnimation(R.raw.logindialog);
        animationView.playAnimation();
        dialogView.findViewById(R.id.btnLogin).setOnClickListener(view -> {
            Intent intent = new Intent(Inspiration.this, Login.class);
            startActivity(intent);
            dialog.dismiss();
        });

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }
}
