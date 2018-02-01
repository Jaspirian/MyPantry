package com.jraynolds.mypantry.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Jasper on 1/31/2018.
 */

public class ImageView_Square extends android.support.v7.widget.AppCompatImageView {

    public ImageView_Square(Context context) {
        super(context);
    }

    public ImageView_Square(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageView_Square(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); // Snap to width
    }
}
