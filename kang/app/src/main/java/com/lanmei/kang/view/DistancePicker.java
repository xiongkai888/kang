package com.lanmei.kang.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.qqtheme.framework.picker.WheelPicker;
import cn.qqtheme.framework.util.DateUtils;
import cn.qqtheme.framework.widget.WheelView;

/**
 * Created by xkai on 2017/6/5.
 * 带小数点的公里数
 */

public class DistancePicker extends WheelPicker {


    private OnTimePickListener onTimePickListener;
    private String pointLabel = ".", kmLabel = "km";
    private String selectedInteger = "0", selectedDecimals = "00";


    /**
     * Instantiates a new Time picker.
     *
     * @param activity the activity
     */
    public DistancePicker(Activity activity) {
        super(activity);
    }

    /**
     * Sets label.
     *
     * @param pointLabel the point label
     * @param kmLabel    the km label
     */
    public void setLabel(String pointLabel, String kmLabel) {
        this.pointLabel = pointLabel;
        this.kmLabel = kmLabel;
    }
//
//    /**
//     * Sets selected item.
//     *
//     * @param hour   the hour
//     * @param minute the minute
//     */
//    public void setSelectedItem(int hour, int minute, int second) {
//        selectedHour = String.valueOf(hour);
//        selectedMinute = String.valueOf(minute);
//        selectedSecond = String.valueOf(second);
//    }

    /**
     * Sets on time pick listener.
     *
     * @param listener the listener
     */
    public void setOnTimePickListener(OnTimePickListener listener) {
        this.onTimePickListener = listener;
    }

    @Override
    @NonNull
    protected View makeCenterView() {
        //公里整数
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        WheelView integerView = new WheelView(activity);
        integerView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        integerView.setTextSize(textSize);
        integerView.setTextColor(textColorNormal, textColorFocus);
        integerView.setLineVisible(lineVisible);
        integerView.setLineColor(lineColor);
        layout.addView(integerView);
        TextView integerTextView = new TextView(activity);
        integerTextView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        integerTextView.setTextSize(textSize);
        integerTextView.setTextColor(textColorFocus);
        if (!TextUtils.isEmpty(pointLabel)) {
            integerTextView.setText(pointLabel);
        }
        layout.addView(integerTextView);
//公里小数
        WheelView decimalsView = new WheelView(activity);
        decimalsView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        decimalsView.setTextSize(textSize);
        decimalsView.setTextColor(textColorNormal, textColorFocus);
        decimalsView.setLineVisible(lineVisible);
        decimalsView.setLineColor(lineColor);
        decimalsView.setOffset(offset);
        layout.addView(decimalsView);
        TextView decimalsTextView = new TextView(activity);
        decimalsTextView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        decimalsTextView.setTextSize(textSize);
        decimalsTextView.setTextColor(textColorFocus);
        if (!TextUtils.isEmpty(kmLabel)) {
            decimalsTextView.setText(kmLabel);
        }
        layout.addView(decimalsTextView);

        //公里整数范围
        ArrayList<String> integer = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            integer.add(i+"");
        }
        integerView.setItems(integer, selectedInteger);

        //公里小数范围
        ArrayList<String> decimals = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            decimals.add(DateUtils.fillZero(i));
        }
        decimalsView.setItems(decimals, selectedDecimals);

        integerView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedInteger = item;
            }
        });
        decimalsView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedDecimals = item;
            }
        });

        return layout;
    }

    @Override
    public void onSubmit() {
        if (onTimePickListener != null) {
            onTimePickListener.onTimePicked(selectedInteger, selectedDecimals);
        }
    }

    /**
     * Gets selected hour.
     *
     * @return the selected hour
     */
    public String getSelectedHour() {
        return selectedInteger;
    }

    /**
     * Gets selected minute.
     *
     * @return the selected minute
     */
    public String getSelectedMinute() {
        return selectedDecimals;
    }


    /**
     * The interface On time pick listener.
     */
    public interface OnTimePickListener {

        /**
         * On time picked.
         *
         * @param integer   the Integer
         * @param decimals the decimals
         */
        void onTimePicked(String integer, String decimals);

    }
}
