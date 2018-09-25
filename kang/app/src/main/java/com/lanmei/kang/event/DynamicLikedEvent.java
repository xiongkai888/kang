package com.lanmei.kang.event;

/**
 * Created by xkai on 2018/1/10.
 * 动态点赞事件或评论
 */

public class DynamicLikedEvent {

    private String id;//动态id
    private String like;//点赞数量
    private String liked;//是否点赞过
    private String commNum;//评论数量


    public DynamicLikedEvent(String id, String like, String liked, String commNum) {
        this.id = id;
        this.like = like;
        this.liked = liked;
        this.commNum = commNum;

    }

    public String getId() {
        return id;
    }

    public String getLike() {
        return like;
    }

    public String getLiked() {
        return liked;
    }


    public String getCommNum() {
        return commNum;
    }

}
