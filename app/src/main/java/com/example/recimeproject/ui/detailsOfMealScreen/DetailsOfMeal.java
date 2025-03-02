package com.example.recimeproject.ui.detailsOfMealScreen;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.recimeproject.DataLayer.local.LocalDataSource;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.model.MealDate;
import com.example.recimeproject.DataLayer.remote.RemoteDataSource;
import com.example.recimeproject.DataLayer.repo.Repository;
import com.example.recimeproject.R;
import com.example.recimeproject.ui.calenderScreen.CalenderMeals;
import com.example.recimeproject.ui.inspirationScreen.Inspiration;
import com.example.recimeproject.ui.loginScreen.Login;
import com.example.recimeproject.ui.profileScreen.profile;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailsOfMeal extends AppCompatActivity implements DetailsOfMealInterface {
    private Presenter presenter;
    private ImageView mealImage, savedIcon, calenderIcon  , profileImg;
    private Button btnBack;
    private TextView mealName, mealCategory, mealArea, mealInstructions;
    private WebView webView;
    String mealId  ;
    private Meal meal;
    private FirebaseAuth auth;
    private Boolean isSaved = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_of_meal);
        auth = FirebaseAuth.getInstance();

        mealImage = findViewById(R.id.mealImageDetail);
        mealName = findViewById(R.id.mealName);
        mealCategory = findViewById(R.id.mealCategory);
        mealArea = findViewById(R.id.mealArea);
        mealInstructions = findViewById(R.id.mealInstructions);
        webView = findViewById(R.id.webView);
        btnBack = findViewById(R.id.btnBack);
        savedIcon = findViewById(R.id.savedIcon);
        calenderIcon = findViewById(R.id.calenderIcon);
        profileImg = findViewById(R.id.profileImg);

        mealId = getIntent().getStringExtra("mealId");
        presenter = new Presenter(this, Repository.getInstance(LocalDataSource.getInstance(this), new RemoteDataSource()));
        presenter.getSelectedMeal(mealId);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

      /*  savedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() != null) {
                    presenter.putSavedMeal(mealId);
                } else {
                    showLoginDialog();
                }
            }
        });
        */
isSaved = false ;
        savedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() != null) {
                    if (!isSaved) {
                        savedIcon.setImageResource(R.drawable.wightheart);
                        savedIcon.setBackgroundResource(R.drawable.wight_circle_background);
                        isSaved= true;
                    }
                    presenter.putSavedMeal(mealId);
                } else {
                    showLoginDialog();
                }
            }
        });

        calenderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() != null) {
                    showDatePickerDialog();
                } else {
                    showLoginDialog();
                }

            }
        });
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() != null) {
                    startActivity(new Intent(DetailsOfMeal.this, profile.class));
                } else {
                    showLoginDialog();
                }

            }
        });



    }
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                DetailsOfMeal.this,
                (view, year1, month1, day1) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year1, month1, day1);
                    selectedDate.set(Calendar.HOUR_OF_DAY, 0);
                    selectedDate.set(Calendar.MINUTE, 0);
                    selectedDate.set(Calendar.SECOND, 0);
                    selectedDate.set(Calendar.MILLISECOND, 0);
                    Date date = selectedDate.getTime();

                    MealDate mealDate = new MealDate(mealId, date);
                    presenter.putCalenderMeal( mealDate);
                },
                year, month, dayOfMonth
        );
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }


    // ******************* remember to ann indrediants ****************************
    @Override
    public void showSelectedMeal(Meal meal) {
        mealName.setText(meal.getStrMeal());
        mealCategory.setText("Category: " + meal.getStrCategory());
        mealArea.setText("Area: " + meal.getStrArea());
        this.meal = meal;

        String instructions = meal.getStrInstructions().trim();
        String[] steps = instructions.split("\\. ");
        StringBuilder formattedInstructions = new StringBuilder("Instructions:\n");
        for (int i = 0; i < steps.length; i++) {
            formattedInstructions.append("Step ").append(i + 1).append(": ").append(steps[i]).append(".\n");
        }
        mealInstructions.setText(formattedInstructions.toString());

        Glide.with(DetailsOfMeal.this)
                .load(meal.getStrMealThumb())
                .into(mealImage);

        showVideo(meal.getStrYoutube());
    }

    private void showVideo(String youtubeUrl) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.setWebChromeClient(new WebChromeClient());

        String videoId = extractYoutubeId(youtubeUrl);
        if (videoId != null) {
            String iframe = "<html><body style='margin:0;padding:0;'><iframe width='100%' height='100%' " +
                    "src='https://www.youtube.com/embed/" + videoId + "?autoplay=0&mute=0' " +
                    "frameborder='0' allowfullscreen></iframe></body></html>";
            webView.loadData(iframe, "text/html", "utf-8");
        } else {
            webView.setVisibility(View.GONE);
        }
    }

    private String extractYoutubeId(String youtubeUrl) {
        try {
            Uri uri = Uri.parse(youtubeUrl);
            String videoId = null;
            if (youtubeUrl.contains("watch?v=")) {
                videoId = uri.getQueryParameter("v");
            } else if (youtubeUrl.contains("youtu.be/")) {
                videoId = youtubeUrl.substring(youtubeUrl.lastIndexOf("/") + 1);
            }
            return videoId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showLoginDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_view, null);
        androidx.appcompat.app.AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();
        LottieAnimationView animationView = dialogView.findViewById(R.id.animation_view);
        animationView.setAnimation(R.raw.logindialog);
        animationView.playAnimation();
        dialogView.findViewById(R.id.btnLogin).setOnClickListener(view -> {
            Intent intent = new Intent(DetailsOfMeal.this, Login.class);
            startActivity(intent);
            dialog.dismiss();
        });

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }
}






