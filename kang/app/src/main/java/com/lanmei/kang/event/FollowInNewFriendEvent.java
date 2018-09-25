package com.lanmei.kang.event;

/**
 * Created by xkai on 2018/1/12.
 * 在新的朋友里点击默认关注事件
 */

public class FollowInNewFriendEvent {

    private String uid;
    private String follow;

    public String getFollow() {
        return follow;
    }

    public FollowInNewFriendEvent(String uid, String follow){
        this.uid = uid;
        this.follow = follow;
    }

    public String getUid() {
        return uid;
    }
}
