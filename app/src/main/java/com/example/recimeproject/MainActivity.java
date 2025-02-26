package com.example.recimeproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recimeproject.ui.inspirationScreen.Inspiration;
import com.example.recimeproject.ui.signupScreen.Signup;
import com.example.recimeproject.ui.splashScreen.Splash;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, Splash.class);
        startActivity(intent);
        finish();


    }
}
