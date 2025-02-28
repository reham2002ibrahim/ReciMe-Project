package com.example.recimeproject.ui.searchScreen;
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
import com.example.recimeproject.DataLayer.model.Category;
import com.example.recimeproject.R;
//import com.example.recimeproject.ui.categoryDetails.CategoryDetailsActivity;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private List<Category> categoryList;
    /*private OnItemClickListener listener;*/
    private OnItemClickListener listener;


    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.txtCategoryName.setText(category.getStrCategory());
        Glide.with(context)
                .load(category.getStrCategoryThumb())
                .apply(new RequestOptions().override(100, 100))
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.detete_icon)
                .into(holder.imgCategoryThumb);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(category.getStrCategory());
            }
        });

        Log.d("CategoryAdapter", "done  name: " + category.getStrCategory());
    }


    @Override
    public int getItemCount() {
          return categoryList.size();
    }
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategoryThumb;
        TextView txtCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategoryThumb = itemView.findViewById(R.id.categoryImage);
            txtCategoryName = itemView.findViewById(R.id.categoryName);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String categoryName);
    }
}