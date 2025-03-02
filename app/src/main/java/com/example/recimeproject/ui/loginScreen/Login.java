package com.example.recimeproject.ui.loginScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recimeproject.DataLayer.local.LocalDataSource;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.model.MealDate;
import com.example.recimeproject.R;
import com.example.recimeproject.ui.inspirationScreen.Inspiration;
import com.example.recimeproject.ui.signupScreen.Signup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Login extends AppCompatActivity {
    EditText txtEmailL, txtPassL;
    Button btnLoginL, btnSignupL , btnGuest;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private LocalDataSource localDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmailL = findViewById(R.id.txtEmailL);
        txtPassL = findViewById(R.id.txtPassL);
        btnLoginL = findViewById(R.id.btnLoginL);
        btnSignupL = findViewById(R.id.btnSignupL);
        btnGuest = findViewById(R.id.btnGuest);
        progressBar =findViewById(R.id.progressBar);

        localDataSource = LocalDataSource.getInstance(this);
        mAuth = FirebaseAuth.getInstance();

        btnSignupL.setOnClickListener(view -> {
            startActivity(new Intent(Login.this, Signup.class));
            finish();
        });

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Inspiration.class));
            }
        });

        btnLoginL.setOnClickListener(view -> {
            String txtEmail = txtEmailL.getText().toString().trim();
            String txtPassword = txtPassL.getText().toString().trim();

            if (TextUtils.isEmpty(txtEmail)) {
                txtEmailL.setError("Email Field can't be empty");
                return;
            }
            if (!txtEmail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                txtEmailL.setError("Enter a valid Email Address");
                return;
            }
            if (TextUtils.isEmpty(txtPassword)) {
                txtPassL.setError("Password Field can't be empty");
                return;
            }

            loginUsre(txtEmail, txtPassword);
        });
    }

    private void loginUsre(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        btnLoginL.setVisibility(View.INVISIBLE);
        clearLocalData(() -> {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            getFirebaseData(user.getUid());
                        }
                    })
                    .addOnFailureListener(e -> {
                      //  btnLoginL.setVisibility(View.VISIBLE);
                    });
        });
    }

    private void clearLocalData(Runnable onSuccess) {
        localDataSource.deleteAllData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    onSuccess.run();
                }, throwable -> {
                    btnLoginL.setVisibility(View.VISIBLE);
                });
    }

    private void getFirebaseData(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Meal> meals = new ArrayList<>();
                    List<MealDate> mealDates = new ArrayList<>();
                    for (DataSnapshot mealSnapshot : snapshot.child("meals").getChildren()) {
                        Meal meal = mealSnapshot.getValue(Meal.class);
                        if (meal != null) meals.add(meal);
                    }
                    for (DataSnapshot dateSnapshot : snapshot.child("mealDates").getChildren()) {
                        MealDate mealDate = dateSnapshot.getValue(MealDate.class);
                        if (mealDate != null) mealDates.add(mealDate);
                    }

                    putDataToRoom(meals, mealDates);
                } else {
                    Toast.makeText(Login.this, "No data found for this user!", Toast.LENGTH_SHORT).show();
                    navigateToInspiration();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void putDataToRoom(List<Meal> meals, List<MealDate> mealDates) {
        localDataSource.insertMeals(meals)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> localDataSource.insertMealDates(mealDates)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    Toast.makeText(Login.this, "Data loaded successfully!", Toast.LENGTH_SHORT).show();
                                    navigateToInspiration();
                                }));
    }

    private void navigateToInspiration() {
        startActivity(new Intent(Login.this, Inspiration.class));
        finish();
    }
}
