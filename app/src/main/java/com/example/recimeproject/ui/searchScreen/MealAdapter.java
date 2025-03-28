package com.example.recimeproject.ui.searchScreen;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recimeproject.DataLayer.model.Meal;
import com.example.recimeproject.R;
import com.example.recimeproject.ui.detailsOfMealScreen.DetailsOfMeal;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private Context context;
    private List<Meal> mealList;
    private OnItemClickListener listener;
    public MealAdapter(Context context, List<Meal> mealList) {
        this.context = context;
        this.mealList = mealList;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgMealThumb);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsOfMeal.class);
                intent.putExtra("mealId", meal.getIdMeal());
                context.startActivity(intent);
            }


        });

    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }
    public void setMealList(List<Meal> newMealList) {
        this.mealList = newMealList;
        notifyDataSetChanged();
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

    public interface OnItemClickListener {
        void onItemClick(String mealName);
    }
}