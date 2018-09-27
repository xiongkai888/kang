package com.lanmei.kang.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by xson on 2017/3/2.
 * 设置高宽比的ImageView
 */

public class RatioImageView extends android.support.v7.widget.AppCompatImageView{
    private float mRatio = (float) 3/4;
    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioImageView(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.width = getWidth();
                layoutParams.height = (int)(getWidth() * mRatio); // 宽高比为4:3
                setLayoutParams(layoutParams);
                getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
    }

    /**
     * 设置宽：高 比
     */
    public void setRatio(float ratio) {
        mRatio = ratio;
    }
}
