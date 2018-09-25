package com.lanmei.kang.bean;

/**
 * Created by xkai on 2017/9/3.
 * 明细
 */

public class ParticularsBean {
    private String item;//项目
    private String price;//价格
    private String num;//人数
    private String time;//时间
    private String total;//合计

    public void setItem(String item) {
        this.item = item;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {

        return total;
    }

    public String getTime() {
        return time;
    }

    public String getPrice() {
        return price;
    }

    public String getItem() {
        return item;
    }

}
