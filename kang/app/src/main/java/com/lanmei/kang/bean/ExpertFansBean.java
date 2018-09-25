package com.lanmei.kang.bean;

/**
 * Created by xkai on 2017/6/16.
 * 达人 粉丝
 */

public class ExpertFansBean {


    /**
     * id : 147
     * uid : 58
     * mid : 42
     * addtime : 1500862914
     * nickname : 老臭虫
     * pic : http://stdrimages.oss-cn-shenzhen.aliyuncs.com/lanmei/daren/img/head1517837442.jpg.tmp
     * user_type : 0
     * followed : 0
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
