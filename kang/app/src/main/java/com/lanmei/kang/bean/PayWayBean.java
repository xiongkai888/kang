package com.lanmei.kang.bean;

/**
 * Created by xkai on 2018/10/11.
 * 支付方式信息
 */

public class PayWayBean {


    private boolean isChoose;


    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public boolean isChoose() {
        return isChoose;
    }
    /**
     * id : 7
     * seller_email : abc@bcd.com
     * partner_id : 223232
     * partner_key : 2322323
     * pay_name : wxpay
     * c_name : 微信支付
     * descript : null
     * status : 1
     */

    private String id;
    private String seller_email;
    private String partner_id;
    private String partner_key;
    private String pay_name;
    private String c_name;
    private String descript;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeller_email() {
        return seller_email;
    }

    public void setSeller_email(String seller_email) {
        this.seller_email = seller_email;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public String getPartner_key() {
        return partner_key;
    }

    public void setPartner_key(String partner_key) {
        this.partner_key = partner_key;
    }

    public String getPay_name() {
        return pay_name;
    }

    public void setPay_name(String pay_name) {
        this.pay_name = pay_name;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
