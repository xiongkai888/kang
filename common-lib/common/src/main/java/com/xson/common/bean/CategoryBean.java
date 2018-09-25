package com.xson.common.bean;

import java.io.Serializable;

/**
 * Created by xkai on 2018/1/6.
 * 首页分类
 */

public class CategoryBean implements Serializable{

    /**
     * id : 126
     * name : 西药理疗
     * pic : http://qkmimages.img-cn-shenzhen.aliyuncs.com/180102/5a4b2405e4587.jpg
     */

    private String id;
    private String name;
    private String pic;

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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}

