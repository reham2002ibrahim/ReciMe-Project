package com.example.recimeproject.ui.calenderScreen;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.R;
import com.example.recimeproject.ui.detailsOfMealScreen.DetailsOfMeal;
import com.example.recimeproject.utils.OnRemoveClickListener;

import java.util.List;

public class CalendredAdapter extends RecyclerView.Adapter<CalendredAdapter.MealViewHolder> {
    private final Context context;
    private List<Meal> mealList;
    private final OnRemoveClickListener onRemoveClickListener;

    public CalendredAdapter(Context context, List<Meal> mealList, OnRemoveClickListener onRemoveClickListener) {
        this.context = context;
        this.mealList = mealList;
        this.onRemoveClickListener = onRemoveClickListener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_calendered, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.txtMealName.setText(meal.getStrMeal());
        Glide.with(context)
                .load(meal.getStrMealThumb())
                .apply(new RequestOptions().override(200, 200))
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgMealThumb);

        holder.deleteIcon.setOnClickListener(v -> {
            if (onRemoveClickListener != null) {
                onRemoveClickListener.onRemoveClick(meal);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsOfMeal.class);
            intent.putExtra("mealId", meal.getIdMeal());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public void setMealList(List<Meal> meals) {
        this.mealList = meals;
        notifyDataSetChanged();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMealThumb;
        TextView txtMealName;
        ImageView deleteIcon;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMealThumb = itemView.findViewById(R.id.imgMealThumb);
            txtMealName = itemView.findViewById(R.id.txtMealName);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }
}