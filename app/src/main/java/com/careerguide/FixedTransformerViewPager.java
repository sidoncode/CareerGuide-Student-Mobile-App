package com.careerguide;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Gaurav Gupta(9910781299) on 22/Jan/18-Monday.
 */

public class FixedTransformerViewPager extends ViewPager {

    PageTransformer pageTransformer;

    public FixedTransformerViewPager(Context context) {
        super(context);
    }

    public FixedTransformerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        this.pageTransformer = transformer;
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);
        fixedPageScrolled(position, offset, offsetPixels);
    }

    protected void fixedPageScrolled(int position, float offset, int offsetPixels) {

        int clientWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

        if (pageTransformer != null) {
            final int scrollX = getScrollX();
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = getChildAt(i);
                final ViewPager.LayoutParams lp = (ViewPager.LayoutParams) child.getLayoutParams();

                if (lp.isDecor) continue;
                //note the getPaddingLeft() that now exists
                final float transformPos = (float) (child.getLeft() - getPaddingLeft() - scrollX) / clientWidth;
                pageTransformer.transformPage(child, transformPos);
            }
        }
    }
}
