package com.example.recimeproject.ui.searchScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recimeproject.DataLayer.model.Area;
import com.example.recimeproject.R;

import java.util.List;
public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaViewHolder> {
    private Context context;
    private List<Area> areaList;
    private OnItemClickListener listener;
    public AreaAdapter(Context context, List<Area> areaList) {
        this.context = context;
        this.areaList = areaList;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_area, parent, false);
        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        Area area = areaList.get(position);
        holder.txtAreaName.setText(area.getName());
        Glide.with(context)
                .load(area.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.detete_icon)
                .into(holder.imgAreaThumb);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(area.getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }

    public static class AreaViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAreaThumb;
        TextView txtAreaName;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAreaThumb = itemView.findViewById(R.id.areaImage);
            txtAreaName = itemView.findViewById(R.id.areaName);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(String areaName);
    }
}