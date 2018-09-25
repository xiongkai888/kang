package com.lanmei.kang.event;

/**
 * Created by xkai on 2018/1/10.
 * 服务收藏事件
 */

public class CollectItemsEvent {

    private String id;
    private String favoured;

    public String getFavoured() {
        return favoured;
    }

    public String getId() {
        return id;
    }

    public CollectItemsEvent(String id,String favoured) {
        this.id = id;
        this.favoured = favoured;
    }
}
