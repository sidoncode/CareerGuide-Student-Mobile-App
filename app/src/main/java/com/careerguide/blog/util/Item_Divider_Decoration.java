package com.careerguide.blog.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.careerguide.R;

import org.jetbrains.annotations.NotNull;

public class Item_Divider_Decoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public Item_Divider_Decoration(Context context) {
        mDivider = context.getResources().getDrawable(R.drawable.bg_divider);
    }

    @Override
    public void onDrawOver(@NotNull Canvas c, RecyclerView parent, @NotNull RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}