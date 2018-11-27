package com.xson.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by xkai on 2018/11/23.
 */

public class AdBannerLayout extends LinearLayout {
    public AdBannerLayout(Context context) {
        super(context);
    }

    public AdBannerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdBannerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width,width/3*2);

    }
}
