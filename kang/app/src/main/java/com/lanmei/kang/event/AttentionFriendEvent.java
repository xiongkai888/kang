package com.lanmei.kang.event;

/**
 * Created by xkai on 2018/1/10.
 * 动态点赞事件或评论
 */

public class AttentionFriendEvent {

    private String uid;//动态id
    private String followed;//是否关注


    public AttentionFriendEvent(String uid, String followed) {
        this.uid = uid;
        this.followed = followed;

    }

    public String getUid() {
        return uid;
    }

    public String getFollowed() {
        return followed;
    }

}
