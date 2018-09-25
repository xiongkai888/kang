package com.lanmei.kang.view;

import android.app.Activity;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import cn.qqtheme.framework.picker.WheelPicker;
import cn.qqtheme.framework.util.DateUtils;
import cn.qqtheme.framework.widget.WheelView;

/**
 * Created by xkai on 2017/6/5.
 */

public class TimePickerView extends WheelPicker {
    /**
     * 24小时
     */
    public static final int HOUR_OF_DAY = 0;
    /**
     * 12小时
     */
    public static final int HOUR = 1;
    private OnTimePickListener onTimePickListener;
    private int mode;
    private String hourLabel = "时", minuteLabel = "分", secondLabel = "秒";
    private String selectedHour = "00", selectedMinute = "00", selectedSecond = "00";

    /**
     * 安卓开发应避免使用枚举类（enum），因为相比于静态常量enum会花费两倍以上的内存。
     *
     * @link http ://developer.android.com/training/articles/memory.html#Overhead
     */
    @IntDef(flag = false, value = {HOUR_OF_DAY, HOUR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    /**
     * Instantiates a new Time picker.
     *
     * @param activity the activity
     */
    public TimePickerView(Activity activity) {
        this(activity, HOUR_OF_DAY);
    }

    /**
     * Instantiates a new Time picker.
     *
     * @param activity the activity
     * @param mode     the mode
     * @see #HOUR_OF_DAY #HOUR_OF_DAY#HOUR_OF_DAY
     * @see #HOUR #HOUR#HOUR
     */
    public TimePickerView(Activity activity, @Mode int mode) {
        super(activity);
        this.mode = mode;
//        selectedHour = DateUtils.fillZero(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
//        selectedMinute = DateUtils.fillZero(Calendar.getInstance().get(Calendar.MINUTE));
//        selectedSecond = DateUtils.fillZero(Calendar.getInstance().get(Calendar.SECOND));
    }

    /**
     * Sets label.
     *
     * @param hourLabel   the hour label
     * @param minuteLabel the minute label
     */
    public void setLabel(String hourLabel, String minuteLabel, String secondLabel) {
        this.hourLabel = hourLabel;
        this.minuteLabel = minuteLabel;
        this.secondLabel = secondLabel;
    }

    /**
     * Sets selected item.
     *
     * @param hour   the hour
     * @param minute the minute
     */
    public void setSelectedItem(int hour, int minute, int second) {
        selectedHour = String.valueOf(hour);
        selectedMinute = String.valueOf(minute);
        selectedSecond = String.valueOf(second);
    }

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
        //时
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        WheelView hourView = new WheelView(activity);
        hourView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        hourView.setTextSize(textSize);
        hourView.setTextColor(textColorNormal, textColorFocus);
        hourView.setLineVisible(lineVisible);
        hourView.setLineColor(lineColor);
        layout.addView(hourView);
        TextView hourTextView = new TextView(activity);
        hourTextView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        hourTextView.setTextSize(textSize);
        hourTextView.setTextColor(textColorFocus);
        if (!TextUtils.isEmpty(hourLabel)) {
            hourTextView.setText(hourLabel);
        }
        layout.addView(hourTextView);
//分
        WheelView minuteView = new WheelView(activity);
        minuteView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        minuteView.setTextSize(textSize);
        minuteView.setTextColor(textColorNormal, textColorFocus);
        minuteView.setLineVisible(lineVisible);
        minuteView.setLineColor(lineColor);
        minuteView.setOffset(offset);
        layout.addView(minuteView);
        TextView minuteTextView = new TextView(activity);
        minuteTextView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        minuteTextView.setTextSize(textSize);
        minuteTextView.setTextColor(textColorFocus);
        if (!TextUtils.isEmpty(minuteLabel)) {
            minuteTextView.setText(minuteLabel);
        }
        layout.addView(minuteTextView);
//秒
        WheelView secondView = new WheelView(activity);
        secondView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        secondView.setTextSize(textSize);
        secondView.setTextColor(textColorNormal, textColorFocus);
        secondView.setLineVisible(lineVisible);
        secondView.setLineColor(lineColor);
        secondView.setOffset(offset);
        layout.addView(secondView);
        TextView secondTextView = new TextView(activity);
        secondTextView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        secondTextView.setTextSize(textSize);
        secondTextView.setTextColor(textColorFocus);
        if (!TextUtils.isEmpty(secondLabel)) {
            secondTextView.setText(secondLabel);
        }
        layout.addView(secondTextView);


        ArrayList<String> hours = new ArrayList<String>();
        if (mode == HOUR) {
            for (int i = 1; i <= 12; i++) {
                hours.add(DateUtils.fillZero(i));
            }
        } else {
            for (int i = 0; i < 24; i++) {
                hours.add(DateUtils.fillZero(i));
            }
        }
        hourView.setItems(hours, selectedHour);

        ArrayList<String> minutes = new ArrayList<String>();
        for (int i = 0; i < 60; i++) {
            minutes.add(DateUtils.fillZero(i));
        }
        minuteView.setItems(minutes, selectedMinute);

        ArrayList<String> second = new ArrayList<String>();
        for (int i = 0; i < 60; i++) {
            second.add(DateUtils.fillZero(i));
        }
        secondView.setItems(minutes, selectedSecond);

        hourView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedHour = item;
            }
        });
        minuteView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedMinute = item;
            }
        });

        secondView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedSecond = item;
            }
        });
        return layout;
    }

    @Override
    public void onSubmit() {
        if (onTimePickListener != null) {
            onTimePickListener.onTimePicked(selectedHour, selectedMinute, selectedSecond);
        }
    }

    /**
     * Gets selected hour.
     *
     * @return the selected hour
     */
    public String getSelectedHour() {
        return selectedHour;
    }

    /**
     * Gets selected minute.
     *
     * @return the selected minute
     */
    public String getSelectedMinute() {
        return selectedMinute;
    }

    /**
     * Gets selected Second.
     *
     * @return the selected Second
     */
    public String getSelectedSecond() {
        return selectedSecond;
    }

    /**
     * The interface On time pick listener.
     */
    public interface OnTimePickListener {

        /**
         * On time picked.
         *
         * @param hour   the hour
         * @param minute the minute
         */
        void onTimePicked(String hour, String minute, String second);

    }
}
