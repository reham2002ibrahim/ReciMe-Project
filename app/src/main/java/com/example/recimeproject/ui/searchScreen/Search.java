package com.example.recimeproject.ui.searchScreen;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recimeproject.DataLayer.model.Area;
import com.example.recimeproject.DataLayer.model.Category;
import com.example.recimeproject.DataLayer.model.Ingredient;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.DataLayer.repo.Repository;
import com.example.recimeproject.R;
import com.example.recimeproject.DataLayer.local.LocalDataSource;
import com.example.recimeproject.DataLayer.remote.RemoteDataSource;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.example.recimeproject.ui.searchScreen.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity implements SearchInterface {
    private ChipGroup chipGroup;
    private Chip chip, chip2, chip3;
    private RecyclerView recyclerView;
    ImageView imgDelete ;
    private Presenter presenter;
    private CategoryAdapter categoryAdapter;
    private MealAdapter mealAdapter;
    private IngredientAdapter ingredientAdapter ;
    private AreaAdapter areaAdapter ;
    private EditText editTextSearch;
    private boolean isSearching = false;



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
        editTextSearch = findViewById(R.id.editTextSearch);
        imgDelete = findViewById(R.id.imgDelete);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        Repository repository = Repository.getInstance(
                LocalDataSource.getInstance(this),
                new RemoteDataSource()
        );
        presenter = new Presenter(this, repository);
        setupChipListeners();

        mealAdapter = new MealAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(mealAdapter);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();

                if (!query.isEmpty()) {
                    isSearching = true;
                    presenter.searchByName(query);
                } else {
                    isSearching = false;
                    restoreChipSelection();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSearch.setText("");

            }
        });
    }
    @Override
    public void showSearchResults(List<Meal> mealList) {

        if (isSearching) {
            mealAdapter = new MealAdapter(this, mealList);
            recyclerView.setAdapter(mealAdapter);
            mealAdapter.notifyDataSetChanged();
            }
    }

    @Override
    public void showMealsOfTheCategoty(List<Meal> mealList) {
        isSearching = true;
        mealAdapter = new MealAdapter(this, mealList);
        recyclerView.setAdapter(mealAdapter);
        mealAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMealsOfTheIngredient(List<Meal> mealList) {
        isSearching = true;
        mealAdapter = new MealAdapter(this, mealList);
        recyclerView.setAdapter(mealAdapter);
        mealAdapter.notifyDataSetChanged();
    }
   @Override
    public void showMealsOfTheArea(List<Meal> mealList) {
        isSearching = true;
        mealAdapter = new MealAdapter(this, mealList);
        recyclerView.setAdapter(mealAdapter);
        mealAdapter.notifyDataSetChanged();
    }

    private void setupChipListeners() {
        chipGroup.setSingleSelection(true);

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (mealAdapter != null) {
                mealAdapter.setMealList(new ArrayList<>());
                mealAdapter.notifyDataSetChanged();
            }
            isSearching = false;
            if (checkedId == R.id.chip) {
                presenter.searchCategories();
            } else if (checkedId == R.id.chip2) {
                presenter.searchAreas();
            } else if (checkedId == R.id.chip3) {
                presenter.searchIngredients();
            }
        });
    }

    private void restoreChipSelection() {
        int checkedId = chipGroup.getCheckedChipId();
        if (checkedId == R.id.chip) {
            presenter.searchCategories();
        } else if (checkedId == R.id.chip2) {
            presenter.searchAreas();
        } else if (checkedId == R.id.chip3) {
            presenter.searchIngredients();
        }
    }


    public void showAllCategories(List<Category> categories) {
        if (!isSearching) {
            categoryAdapter = new CategoryAdapter(this, categories);
            recyclerView.setAdapter(categoryAdapter);
            categoryAdapter.notifyDataSetChanged();
            categoryAdapter.setOnItemClickListener(categoryName -> {
                isSearching = true;
                presenter.searchMealsByCategory(categoryName);
            });
        }
    }
    @Override
    public void showAllIngredients(List<Ingredient> ingredients) {
        if (!isSearching) {
            ingredientAdapter = new IngredientAdapter(this, ingredients);
            recyclerView.setAdapter(ingredientAdapter);
            ingredientAdapter.notifyDataSetChanged();
            ingredientAdapter.setOnItemClickListener(ingredientName -> {
                isSearching = true;
                Log.i("search", "showAllIngredientsnameee: " + ingredientName);
                presenter.searchMealsByIngredient(ingredientName);
            });
        }

    }

    @Override
    public void showAllAreas(List<Area> areas) {
        if (!isSearching) {
            areaAdapter = new AreaAdapter(this, areas);
            recyclerView.setAdapter(areaAdapter);
            areaAdapter.notifyDataSetChanged();
            areaAdapter.setOnItemClickListener(areaName -> {
                isSearching = true;
                Log.i("search", "showAllIngredientsnameee: " + areaName);
                presenter.searchMealsByArea(areaName);
            });
        }
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