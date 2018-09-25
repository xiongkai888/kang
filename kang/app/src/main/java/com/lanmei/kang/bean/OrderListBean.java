package com.lanmei.kang.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/20.
 * 我的订单列表Bean
 */

public class OrderListBean implements Serializable{


    /**
     * id : 359
     * pay_no : 1801290000017727
     * addtime : 1517217727
     * stime : 1517217723
     * etime : 1517217723
     * guest : 7
     * status : 2
     * amount : 0.00
     * is_reviews : 0
     * fee_introduction : http://qkmimages.img-cn-shenzhen.aliyuncs.com/180104/5a4d9cc620025.JPG
     * uid : 199
     * name : 旅游团费商家
     * goodsName : 商品分类商品
     */

    private String id;
    private String pay_no;
    private String addtime;
    private String stime;
    private String etime;
    private String guest;
    private String status;
    private String amount;
    private String is_reviews;
    private String fee_introduction;
    private String uid;
    private String name;
    private String goodsName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPay_no() {
        return pay_no;
    }

    public void setPay_no(String pay_no) {
        this.pay_no = pay_no;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIs_reviews() {
        return is_reviews;
    }

    public void setIs_reviews(String is_reviews) {
        this.is_reviews = is_reviews;
    }

    public String getFee_introduction() {
        return fee_introduction;
    }

    public void setFee_introduction(String fee_introduction) {
        this.fee_introduction = fee_introduction;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
