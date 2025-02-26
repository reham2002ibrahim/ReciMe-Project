package com.example.recimeproject.ui.signupScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recimeproject.R;
import com.example.recimeproject.ui.inspirationScreen.Inspiration;
import com.example.recimeproject.ui.loginScreen.Login;
import com.example.recimeproject.ui.splashScreen.Splash;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    EditText  txtName , txtEmail , txtPass, txtConfirmPass ;
    Button btnLogin , btnSignup ;
    ImageView gmailIcon ;
    ProgressBar progressBar  ;
    String  name , email , pass, confirmPass ;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        txtName = findViewById(R.id.txtName) ;
        txtEmail = findViewById(R.id.txtEmail) ;
        txtPass = findViewById(R.id.txtPass) ;
        txtConfirmPass = findViewById(R.id.txtConfirmPass) ;
        btnLogin = findViewById(R.id.btnLogin) ;
        btnSignup = findViewById(R.id.btnSignup) ;
        gmailIcon = findViewById(R.id.gmailIcon) ;
        progressBar = findViewById(R.id.signUpProgressBar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
                finish();
            }
        });


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = txtName.getText().toString();
                email = txtEmail.getText().toString().trim();
                pass = txtPass.getText().toString().trim();
                confirmPass = txtConfirmPass.getText().toString().trim();

                if (!TextUtils.isEmpty(name)) {
                    if (!TextUtils.isEmpty(email)) {
                        if (email.matches(emailPattern)) {
                            if (!TextUtils.isEmpty(pass)) {
                                if (!TextUtils.isEmpty(confirmPass)) {
                                    if (confirmPass.equals(pass)) {
                                        SignUpUser();
                                    } else {
                                        txtConfirmPass.setError("Confirm Password and Password should be same.");
                                    }
                                } else {
                                    txtConfirmPass.setError("Confirm Password Field can't be empty");
                                }
                            } else {
                                txtPass.setError("Password Field can't be empty");
                            }
                        } else {
                            txtEmail.setError("Enter a valid Email Address");
                        }
                    } else {
                        txtEmail.setError("Email Field can't be empty");
                    }
                } else {
                    txtName.setError("Full Name Field can't be empty");
                }
            }
        });
    }

    private void SignUpUser() {
        progressBar.setVisibility(View.VISIBLE);
        btnSignup.setVisibility(View.INVISIBLE);

        mAuth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Map<String, Object> user = new HashMap<>();
                user.put("Name", name);
                user.put("Email", email);

                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(Signup.this, "Data Stored Successfully !", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Signup.this, Inspiration.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Signup.this, "Error - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Signup.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();

                btnSignup.setVisibility(View.VISIBLE);
            }
        });

    }
}