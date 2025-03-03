package com.example.recimeproject.ui.detailsOfMealScreen;

import android.content.Context;
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
import com.example.recimeproject.DataLayer.model.IngredientAndM;
import com.example.recimeproject.R;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private Context context;
    private List<IngredientAndM> ingredientList;

    public IngredientAdapter(Context context, List<IngredientAndM> ingredientList) {
        this.context = context;
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ingredientsview, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        IngredientAndM ingredient = ingredientList.get(position);

        holder.txtIngredientName.setText(ingredient.getName());
        holder.txtIngredientDetails.setText("Quantity:"+ ingredient.getMeasure());

        Glide.with(context)
                .load(ingredient.getImageUrl())
                .apply(new RequestOptions().override(100, 100))
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.detete_icon)
                .into(holder.imgIngredientThumb);
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIngredientThumb;
        TextView txtIngredientName, txtIngredientDetails;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIngredientThumb = itemView.findViewById(R.id.ingredientImage);
            txtIngredientName = itemView.findViewById(R.id.ingredientName);
            txtIngredientDetails = itemView.findViewById(R.id.ingredientDetails);
        }
    }
}