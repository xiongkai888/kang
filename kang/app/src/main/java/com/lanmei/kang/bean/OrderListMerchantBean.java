package com.lanmei.kang.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/20.
 * 商家订单列表Bean
 */

public class OrderListMerchantBean implements Serializable{


    /**
     * id : 416
     *  uid : 202
     * pay_no : 1802050020411030
     * addtime : 1517811030
     * stime : 1517811024
     * etime : 1517811024
     * guest : 1
     * status : 2
     * amount : 2002.00
     * pic : http://stdrimages.oss-cn-shenzhen.aliyuncs.com/lanmei/kang/img/head849892561.jpg.tmp
     * nickname : 大冒险
     * phone : 15914369666
     * name : 商品分类商品
     */

    private String id;
    private String uid;
    private String pay_no;
    private String addtime;
    private String stime;
    private String etime;
    private String guest;
    private String status;
    private String amount;
    private String pic;
    private String nickname;
    private String phone;
    private String name;

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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
