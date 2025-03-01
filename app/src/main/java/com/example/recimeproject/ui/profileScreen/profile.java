package com.example.recimeproject.ui.profileScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.recimeproject.DataLayer.local.LocalDataSource;
import com.example.recimeproject.R;
import com.example.recimeproject.ui.loginScreen.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class profile extends AppCompatActivity {

    Button btnBack, btnBackup, btnLogout;
    private FirebaseAuth mAuth;
    private LocalDataSource localDataSource;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btnBack = findViewById(R.id.btnBack);
        btnBackup = findViewById(R.id.btnBackup);
        btnLogout = findViewById(R.id.btnLogout);

        mAuth = FirebaseAuth.getInstance();
        localDataSource = LocalDataSource.getInstance(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        btnBack.setOnClickListener(v -> finish());
        btnBackup.setOnClickListener(v -> backupData());
        btnLogout.setOnClickListener(v -> logoutUser());
    }

    private void backupData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        DatabaseReference userRef = databaseReference.child(userId);

        localDataSource.getFavoriteMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meals -> {
                    localDataSource.getMealDates()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(mealDates -> {
                                userRef.child("meals").setValue(meals)
                                        .addOnSuccessListener(aVoid -> {
                                            userRef.child("mealDates").setValue(mealDates)
                                                    .addOnSuccessListener(aVoid1 -> {Toast.makeText(profile.this, "Backup Successful!", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.i("profile", "backupData: Error saving meals");
                                                    });
                                        })
                                        .addOnFailureListener(e -> {Log.i("profile", "backupData: Error saving meals");

                                        });
                            }, throwable -> {Log.i("profile ", "backupData: erroe ingetching ");
                            });
                }, throwable -> {Log.i("profile", "backupData: got error ");
                });
    }

    private void logoutUser() {
        mAuth.signOut();
        Toast.makeText(profile.this, "Logout Successful!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(profile.this, Login.class);
        startActivity(intent);
        finish();
    }


}
