package com.lanmei.kang.bean;

/**
 * Created by xkai on 2017/9/2.
 * 节日bean
 */

public class HolidaysBean {

    private String holidays;
    private boolean isChoosed;

    public void setHolidays(String holidays) {
        this.holidays = holidays;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public String getHolidays() {

        return holidays;
    }

    public boolean isChoosed() {
        return isChoosed;
    }
}
