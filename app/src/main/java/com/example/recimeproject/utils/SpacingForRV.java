package com.example.recimeproject.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacingForRV extends RecyclerView.ItemDecoration {
    private final int spaceHeight;

    public SpacingForRV(int spaceHeight) {
        this.spaceHeight = spaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = spaceHeight;
    }




}
