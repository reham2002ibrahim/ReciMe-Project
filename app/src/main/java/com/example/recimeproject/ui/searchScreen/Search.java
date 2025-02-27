package com.example.recimeproject.ui.searchScreen;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recimeproject.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class Search extends AppCompatActivity {
    ChipGroup chipGroup;
    Chip chip ;
    Chip chip2 ;
    Chip chip3 ;
    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        chipGroup = findViewById(R.id.chipGroup) ;
        chip = findViewById(R.id.chip) ;
        chip2 = findViewById(R.id.chip2) ;
        chip3 = findViewById(R.id.chip3) ;
        chipGroup.setSingleSelection(true) ;
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));





    }
}