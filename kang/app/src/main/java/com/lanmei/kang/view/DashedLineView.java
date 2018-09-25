package com.lanmei.kang.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.lanmei.kang.R;

/**
 * Created by xkai on 2017/12/29.
 * 垂直虚线
 */

public class DashedLineView extends View{

    public int colorId = R.color.colorFF7676;//默認

    public DashedLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getContext().getResources().getColor(colorId));//颜色可以自己设置
//        paint.setColor(getContext().getResources().getColor(R.color.colorFF7676));//颜色可以自己设置
        Path path = new Path();
        path.moveTo(0, 0);//起始坐标
        path.lineTo(0,500);//终点坐标
        PathEffect effects = new DashPathEffect(new float[]{8,8,8,8},1);//设置虚线的间隔和点的长度
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }

    public void setColor(int colorId){
        this.colorId = colorId;
        invalidate();
    }

}
