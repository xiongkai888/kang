package com.lanmei.kang.util;

import com.xson.common.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/8.
 */

public class FormatTime {

    private long time;
    private boolean is12Hour;
    private Calendar calendar = Calendar.getInstance();

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Date date = new Date();

    public FormatTime() {
        this.time = System.currentTimeMillis();
    }

    /**
     * @param time 毫秒
     */
    public FormatTime(long time) {
        this.time = time * 1000;
        calendar.setTimeInMillis(this.time);
    }

    /**
     * @param timeStr 毫秒 String类型
     */
    public FormatTime(String timeStr) {
        if (StringUtils.isEmpty(timeStr)) {
            timeStr = "0";
        }
        this.time = Long.parseLong(timeStr) * 1000;
        calendar.setTimeInMillis(this.time);
    }

    /**
     * @param time 毫秒
     */
    public void setTime(long time) {
        this.time = time * 1000;
        calendar.setTimeInMillis(this.time);

    }

    /**
     * @param timeStr 毫秒  String 类型
     */
    public void setTime(String timeStr) {
        if (StringUtils.isEmpty(timeStr)) {
            timeStr = "0";
        }
        this.time = Long.parseLong(timeStr) * 1000;
        calendar.setTimeInMillis(this.time);

    }

    /**
     * 时间戳格式为“yyyy-MM-dd  HH:mm:ss     ”
     */
    public String formatterTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return format.format(date);
    }

    /**
     * 时间戳格式为“yyyy-MM-dd”
     */
    public String formatterTimeToDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        return format.format(date);
    }

    /**
     * 时间戳格式为“yyyy-MM-dd  HH:mm     ”
     */
    public String formatterTimeNoSeconds() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(time);
        return format.format(date);
    }

    /**
     * @param is12Hour 是否12小时
     */
    public void setIs12Hour(boolean is12Hour) {
        this.is12Hour = is12Hour;
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public int getDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getHour() {
        return calendar.get(is12Hour ? Calendar.HOUR : Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        return calendar.get(Calendar.MINUTE);
    }

    public int getWeek() {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public String getWeekStr() {
        String week = "";
        switch (getWeek()) {
            case 1:
                week = "星期日";

                break;
            case 2:
                week = "星期一";

                break;
            case 3:
                week = "星期二";

                break;
            case 4:
                week = "星期三";

                break;
            case 5:
                week = "星期四";

                break;
            case 6:
                week = "星期五";

                break;
            case 7:
                week = "星期六";
                break;

        }
        return week;
    }

    public boolean isAM() {
        //0-上午；1-下午
        int am = calendar.get(Calendar.AM_PM);
        return am == 0;
    }

    public String getAgoDateFomat() {

        long curr = System.currentTimeMillis() / 1000;
        long item = curr - (this.time / 1000);

        if (item < 60) {
            return "刚刚";
        } else if (item < 60 * 60) {
            return item / 60 + "分钟前";
        } else if (item < (60 * 60 * 24)) {
            return item / 60 / 60 + "小时前";
        } else if (item < (60 * 60 * 24 * 30)) {
            return item / 60 / 60 / 24 + "天前";
        } else if (item < (60 * 60 * 24 * 30 * 12)) {
            return item / 60 / 60 / 24 / 30 + "个月前";
        } else
            return item / 60 / 60 / 24 / 30 / 12 + "年前";
    }


    public String getFormatTime() {

        long curr = System.currentTimeMillis() / 1000;
        long item = curr - (this.time / 1000);

        if (item < 60) {
            return "刚刚";
        } else if (item < 60 * 60) {
            return item / 60 + "分钟前";
        } else if (item < (60 * 60 * 24)) {
            return item / 60 / 60 + "小时前";
        } else if (item < (60 * 60 * 24 * 30)) {
            long day = item / 60 / 60 / 24;
            if (day == 1) {
                return "昨天";
            } else if (day == 2) {
                return "前天";
            } else if (day > 2 && day < 11) {
                return day+"天前";
            } else {
                return formatterTime();
            }
        }else {
            return formatterTime();
        }
    }

}
