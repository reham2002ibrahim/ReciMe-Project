package com.example.recimeproject.ui.loginScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recimeproject.R;
import com.example.recimeproject.ui.inspirationScreen.Inspiration;
import com.example.recimeproject.ui.signupScreen.Signup;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText txtEmailL, txtPassL;
    Button btnLoginL , btnSignupL ;

    String txtEmail, txtPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmailL = findViewById(R.id.txtEmailL) ;
        txtPassL = findViewById(R.id.txtPassL) ;
        btnLoginL = findViewById(R.id.btnLoginL) ;
        btnSignupL = findViewById(R.id.btnSignupL) ;

        btnSignupL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        btnLoginL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtEmail = txtEmailL.getText().toString().trim();
                txtPassword = txtPassL.getText().toString().trim();

                if (!TextUtils.isEmpty(txtEmail)) {
                    if (txtEmail.matches(emailPattern)) {
                        if (!TextUtils.isEmpty(txtPassword)) {
                            SignInUser();
                        } else {
                            txtPassL.setError("Password Field can't be empty");
                        }
                    } else {
                        txtEmailL.setError("Enter a valid Email Address");
                    }
                } else {
                    txtEmailL.setError("Email Field can't be empty");
                }
            }
        });
    }

    private void SignInUser() {
        btnLoginL.setVisibility(View.INVISIBLE);

        mAuth.signInWithEmailAndPassword(txtEmail, txtPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(Login.this, "Login Successful !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, Inspiration.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Error - " + e.getMessage(), Toast.LENGTH_SHORT).show();
              //  progressBar.setVisibility(View.INVISIBLE);
                btnLoginL.setVisibility(View.VISIBLE);
            }
        });

    }
}