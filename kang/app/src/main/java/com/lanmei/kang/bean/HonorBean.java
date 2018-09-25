package com.lanmei.kang.bean;

import java.io.Serializable;

/**
 * Created by xkai on 2017/6/14.
 * 荣誉
 */

public class HonorBean implements Serializable{


    /**
     * id : 1
     * title : 肥仔艇友仔杯
     * items_id : 9
     * first : 0,5
     * second : 0
     * third : 0
     * join : null
     * addtime : 1497491017
     */

    private String id;
    private String title;
    private String items_id;
    private String first;
    private String second;
    private String third;
    private String join;
    private String addtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItems_id() {
        return items_id;
    }

    public void setItems_id(String items_id) {
        this.items_id = items_id;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getThird() {
        return third;
    }

    public void setThird(String third) {
        this.third = third;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
