package com.lanmei.kang.event;

/**
 * Created by xkai on 2018/1/10.
 * 资讯评论事件
 */

public class NewsCommEvent {

    private String id;//资讯id
    private String commNum;//评论数量


    public NewsCommEvent(String id,String commNum) {
        this.id = id;
        this.commNum = commNum;

    }

    public String getId() {
        return id;
    }


    public String getCommNum() {
        return commNum;
    }

}
