package com.lanmei.kang.bean;

/**
 * Created by xkai on 2018/2/7.
 * 至分类
 */

public class CategoryBean {

    /**
     * id : 9
     * name : 象州胡安泉
     * count : 0
     */

    private String id;
    private String name;
    private String count;
    private boolean isPitch;//选中

    public boolean isPitch() {
        return isPitch;
    }

    public void setPitch(boolean pitch) {
        isPitch = pitch;
    }

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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
