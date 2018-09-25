package com.lanmei.kang.bean;

/**
 * Created by xkai on 2017/6/16.
 * 达人 关注
 */

public class ExpertAttentionBean {


    /**
     * id : 142
     * uid : 42
     * mid : 48
     * addtime : 1500826079
     * nickname : 浪迹天涯
     * pic : http://stdrimages.oss-cn-shenzhen.aliyuncs.com/48/1499649121.png
     * user_type : 0
     * followed : 1
     * is_friend : 1
     */

    private String id;
    private String uid;
    private String mid;
    private String addtime;
    private String nickname;
    private String pic;
    private String user_type;
    private String followed;
    private int is_friend;

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

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getFollowed() {
        return followed;
    }

    public void setFollowed(String followed) {
        this.followed = followed;
    }

    public int getIs_friend() {
        return is_friend;
    }

    public void setIs_friend(int is_friend) {
        this.is_friend = is_friend;
    }
}
