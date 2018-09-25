package com.lanmei.kang.bean;

/**
 * Created by xkai on 2018/2/3.
 * 订单详情bean
 */

public class OrderDetailsBean {

    /**
     * id : 377
     * pay_no : 1802020020459258
     * addtime : 1517559258
     * stime : 2147483647
     * etime : 4294967295
     * guest : 1
     * status : 1
     * amount : 188.00
     * pay_type : 6
     * fee_introduction : http://qkmimages.img-cn-shenzhen.aliyuncs.com/180201/5a72791430783.jpg
     * uid : 206
     * name : 静雅轩调理养生馆
     * tel : 020-38625171
     * goodsName : 金木浴疗墨玉泡澡
     */

    private String id;
    private String pay_no;
    private String addtime;
    private String stime;
    private String etime;
    private String guest;
    private String status;
    private String amount;
    private String pay_type;
    private String is_reviews;//是否已经评论
    private String fee_introduction;
    private String uid;
    private String name;
    private String tel;
    private String goodsName;

    public String getIs_reviews() {
        return is_reviews;
    }

    public void setIs_reviews(String is_reviews) {
        this.is_reviews = is_reviews;
    }

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

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
