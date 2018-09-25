package com.lanmei.kang.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xkai on 2018/2/5.
 * 商家信息
 */

public class MerchantInfoBean implements Serializable{

    /**
     * id : 13
     * uid : 199
     * pics : ["http://qkmimages.img-cn-shenzhen.aliyuncs.com/180116/5a5dcd0c4079b.jpg","http://qkmimages.img-cn-shenzhen.aliyuncs.com/180116/5a5dcd0f669b0.jpg"]
     * name : 旅游团费商家
     * stime : 08:00:00
     * etime : 22:00:00
     * address : 天河城阿萨德发是阿斯蒂芬阿斯蒂芬
     * tel : 13502647328
     * place_introduction : 123酒吧当年的确是以一种很“文化”、很反叛的姿态出现的，是我们这个城市对深夜不归的一种默许，它悄悄地却是越来越多地出现在中国大都市的一个个角落，成为青年人的天下，亚文化的发生地。随着都市文化的迅猛发展，曾经占尽风光的电影院在酒吧、迪厅、电子游戏室的崛起中显得有些被冷落的感觉。以新新人类自居的酷男辣妹，对于“泡吧”更是情有独钟，因为酒吧里欣赏歌舞、听音乐、扎堆聊天、喝酒品茶甚至蹦迪，无所不包，随你玩到尽兴，又显出时尚派头，自然成了流行的消闲娱乐方式。酒吧文化在中国不过十几年的历史，但是它发展迅速，可以称得上是适时而生。多年前在茶馆和酒楼听传统戏曲是当时大众最为重要的文化生活，随着时代的变迁，大众对音乐取向的变换和选择也是必然。由于八十年代外资与合资的酒店在大陆大规模的发展，相当一部分富有开拓精神的人们对酒店内的酒吧发生了兴趣；追求发展和变化的心态促使一部分原来开餐厅和酒馆的人们做起了酒吧生意，将酒吧这一形式从酒店复制到城市的繁华街区和外国人聚集的使馆、文化商业区。
     */

    private String id;
    private String uid;
    private String name;
    private String stime;
    private String etime;
    private String address;
    private String tel;
    private String place_introduction;
    private List<String> pics;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPlace_introduction() {
        return place_introduction;
    }

    public void setPlace_introduction(String place_introduction) {
        this.place_introduction = place_introduction;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }
}
