package com.careerguide.blog.util;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.IntRange;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class Item_Space_Decoration extends RecyclerView.ItemDecoration {
    private final int columns;
    private int margin;

    public Item_Space_Decoration(@IntRange(from = 0) int margin, @IntRange(from = 0) int columns) {
        this.margin = margin;
        this.columns = columns;

    }

    @Override
    public void getItemOffsets(Rect outRect, @NotNull View view, RecyclerView parent, @NotNull RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        outRect.right = margin;
        outRect.bottom = margin;
        if (position < columns) {
            outRect.top = margin;
        }
        if (position % columns == 0) {
            outRect.left = margin;
        }
    }
}