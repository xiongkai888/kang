package com.lanmei.kang.event;

/**
 * Created by xkai on 2018/10/18.
 *
 */

public class CardEvent {

    private String name;
    private int type;//1卡名

    public CardEvent(int type){
        this.type = type;
    }

    public void setName(String cardName) {
        this.name = cardName;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
