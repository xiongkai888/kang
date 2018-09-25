package com.lanmei.kang.bean;

/**
 * Created by xkai on 2017/8/15.
 * 任务信息（签到）
 */

public class TaskBean {


    /**
     * id : 20
     * name : 登录签到
     * awards : 2
     * exp : 0
     * single : 0
     * limit : 1
     * time : 0
     * info : 登录签到
     */

    private String id;
    private String name;
    private String awards;
    private String exp;
    private String single;
    private String limit;
    private int time;
    private String info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
