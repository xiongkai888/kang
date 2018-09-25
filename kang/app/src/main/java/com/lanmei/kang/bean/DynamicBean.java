package com.lanmei.kang.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/24.
 */

public class DynamicBean implements Serializable{


    /**
     * id : 308
     * uid : 86
     * title : 今天下雨了
     * file : ["http://stdrimages.oss-cn-shenzhen.aliyuncs.com/86/150096918839.png","http://stdrimages.oss-cn-shenzhen.aliyuncs.com/86/150096918858.png","http://stdrimages.oss-cn-shenzhen.aliyuncs.com/86/150096918864.png"]
     * like : 1
     * reviews : 0
     * addtime : 1500969190
     * lr : 1
     * city : 广州市天河区五山路
     * pic : http://stdrimages.oss-cn-shenzhen.aliyuncs.com/86/1500429169.png
     * nickname : add
     * username : u_000086
     * liked : 0
     * followed : 0
     */

    private String id;
    private String uid;
    private String title;
    private String like;
    private String reviews;
    private String addtime;
    private String lr;
    private String city;
    private String pic;
    private String nickname;
    private String username;
    private String liked;
    private String followed;
    private List<String> file;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getLr() {
        return lr;
    }

    public void setLr(String lr) {
        this.lr = lr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getFollowed() {
        return followed;
    }

    public void setFollowed(String followed) {
        this.followed = followed;
    }

    public List<String> getFile() {
        return file;
    }

    public void setFile(List<String> file) {
        this.file = file;
    }
}
