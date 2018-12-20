package com.lanmei.kang.bean;

/**
 * Created by xkai on 2018/9/28.
 */

public class GoodsSellBean {

    private String number;//商品编号
    private int num;//数量
    private double price;//单价
    private String unit;//单位
    private String gid;//商品id
    private MerchantTabGoodsBean bean;

    public void setBean(MerchantTabGoodsBean bean) {
        this.bean = bean;
    }

    public MerchantTabGoodsBean getBean() {
        return bean;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getGid() {
        return gid;
    }

    public String getNumber() {
        return number;
    }

    public int getNum() {
        return num;
    }

    public double getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
