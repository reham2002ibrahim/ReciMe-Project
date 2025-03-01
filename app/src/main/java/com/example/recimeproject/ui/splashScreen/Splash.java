package com.example.recimeproject.ui.splashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.recimeproject.R;
import com.example.recimeproject.ui.inspirationScreen.Inspiration;
import com.example.recimeproject.ui.loginScreen.Login;
import com.example.recimeproject.ui.signupScreen.Signup;
import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    private Handler handler ;
    private Runnable runnable ;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();

        handler = new Handler();

        runnable =new Runnable() {
            @Override
            public void run() {
                //if (mAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(Splash.this, Inspiration.class);
                    startActivity(intent);
                    finish();
              /*  } else {
                    Intent intent = new Intent(Splash.this, Login.class);
                    startActivity(intent);
                    finish();
                }*/
            }
        } ;
        handler.postDelayed(runnable , 2900) ;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
