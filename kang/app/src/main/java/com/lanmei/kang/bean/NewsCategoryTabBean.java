package com.lanmei.kang.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/18.
 * 资讯TabLayout 信息
 */

public class NewsCategoryTabBean implements Serializable{

    /**
     * id : 1
     * name : 课程
     * recommend : 1
     * icon : http://dfglimages.img-cn-shanghai.aliyuncs.com/161222/585b6bbfc31b5.jpg
     * pic : http://dfglimages.img-cn-shanghai.aliyuncs.com/161222/585b6be2f10d1.jpg
     * post_count : 25
     * reviews_count : 2
     */

    private String id;
    private String name;
    private String recommend;
    private String icon;
    private String pic;
    private String post_count;
    private String reviews_count;

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

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPost_count() {
        return post_count;
    }

    public void setPost_count(String post_count) {
        this.post_count = post_count;
    }

    public String getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(String reviews_count) {
        this.reviews_count = reviews_count;
    }
}
