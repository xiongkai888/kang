package com.lanmei.kang.bean;

/**
 * Created by xkai on 2018/4/3.
 */

public class RechargeSubBean {

    public String getMoney() {
        return money;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    private String money;
    private int moneyInt;

    public void setMoneyInt(int moneyInt) {
        this.moneyInt = moneyInt;
    }

    public int getMoneyInt() {
        return moneyInt;
    }

    private boolean selected;
}
