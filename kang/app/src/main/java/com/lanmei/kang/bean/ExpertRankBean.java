package com.lanmei.kang.bean;

/**
 * Created by xkai on 2017/7/4.
 * 达人排名
 */

public class ExpertRankBean {

    /**
     * id : 6
     * pic : http://stdrimages.oss-cn-shenzhen.aliyuncs.com/lanmei/daren/img/head920865880.jpg.tmp
     * nickname : 我不在家了
     * distance_total : 192.58
     */

    private String id;
    private String pic;
    private String nickname;
    private String distance_total;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDistance_total() {
        return distance_total;
    }

    public void setDistance_total(String distance_total) {
        this.distance_total = distance_total;
    }
}
