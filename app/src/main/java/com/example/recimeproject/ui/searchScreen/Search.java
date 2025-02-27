package com.example.recimeproject.ui.searchScreen;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recimeproject.DataLayer.model.Category;
import com.example.recimeproject.DataLayer.model.Ingredient;
import com.example.recimeproject.DataLayer.repo.Repository;
import com.example.recimeproject.R;
import com.example.recimeproject.DataLayer.local.LocalDataSource;
import com.example.recimeproject.DataLayer.remote.RemoteDataSource;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.List;

public class Search extends AppCompatActivity implements SearchInterface {
    private ChipGroup chipGroup;
    private Chip chip, chip2, chip3;
    private RecyclerView recyclerView;
    private Presenter presenter;
    private SearchAdapter searchAdapter;
    private IngredientAdapter ingredientAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        chipGroup = findViewById(R.id.chipGroup);
        chip = findViewById(R.id.chip);
        chip2 = findViewById(R.id.chip2);
        chip3 = findViewById(R.id.chip3);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        Repository repository = Repository.getInstance(
                LocalDataSource.getInstance(this),
                new RemoteDataSource()
        );
        presenter = new Presenter(this, repository);
        setupChipListeners();
    }


    private void setupChipListeners() {
        chipGroup.setSingleSelection(true);

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chip) {
                presenter.searchCategories();
            } else if (checkedId == R.id.chip2) {
                //  presenter.searchIngrediant();
            } else if (checkedId == R.id.chip3) {
                 presenter.searchIngredients();
            }
        });
    }

    @Override
    public void showAllCategories(List<Category> categories) {
        searchAdapter = new SearchAdapter(this, categories);
        recyclerView.setAdapter(searchAdapter);
    }

    @Override
    public void showAllIngredients(List<Ingredient> ingredients) {
        ingredientAdapter = new IngredientAdapter(this, ingredients);
        recyclerView.setAdapter(ingredientAdapter);
    }


    @Override
    public void showError(String message) {
        Log.i("Search ", "got ewwre : ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}