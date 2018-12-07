package com.lanmei.kang.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/16.
 * 库存列表
 */

public class InventoryListBean implements Serializable{


    /**
     * id : 8
     * uid : 202
     * gid : 48
     * kucun : 50
     * addtime : 1544162200
     * uptime : null
     * goods_sale : null
     * goodsname : 天美纪蓝甘菊舒润水
     * barcode : 6971666130070
     */

    private String id;
    private String uid;
    private String gid;
    private String kucun;
    private String addtime;
    private String uptime;
    private String goods_sale;
    private String goodsname;
    private String barcode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getKucun() {
        return kucun;
    }

    public void setKucun(String kucun) {
        this.kucun = kucun;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getGoods_sale() {
        return goods_sale;
    }

    public void setGoods_sale(String goods_sale) {
        this.goods_sale = goods_sale;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
