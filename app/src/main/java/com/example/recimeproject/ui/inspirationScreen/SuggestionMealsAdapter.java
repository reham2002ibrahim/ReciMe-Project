package com.example.recimeproject.ui.inspirationScreen;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.R;
import com.example.recimeproject.ui.detailsOfMealScreen.DetailsOfMeal;

import java.util.List;

public class SuggestionMealsAdapter extends RecyclerView.Adapter<SuggestionMealsAdapter.MealViewHolder> {
    private Context context;
    private List<Meal> mealList;

    public SuggestionMealsAdapter(Context context, List<Meal> mealList) {
        this.context = context;
        this.mealList = mealList;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.txtMealName.setText(meal.getStrMeal());
        Glide.with(context)
                .load(meal.getStrMealThumb())
                .apply(new RequestOptions().override(200, 200))
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgMealThumb);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsOfMeal.class);
                intent.putExtra("mealId", meal.getIdMeal());
                context.startActivity(intent);
            }


        });
        Log.d("SuggestionMealsAdapter", "insrted meal at pos  " + position + ", name: " + meal.getStrMeal());
    }


    @Override
    public int getItemCount() {
        Log.d("SuggestionMealsAdapter", "all items is  " + mealList.size());
        return mealList.size();
    }
    public static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMealThumb;
        TextView txtMealName;
        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMealThumb = itemView.findViewById(R.id.imgMealThumb);
            txtMealName = itemView.findViewById(R.id.txtMealName);
        }
    }
}
