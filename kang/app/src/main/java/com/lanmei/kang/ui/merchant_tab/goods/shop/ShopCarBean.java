package com.lanmei.kang.ui.merchant_tab.goods.shop;

import com.xson.common.utils.StringUtils;

import java.io.Serializable;

/**
 * Created by xkai on 2018/1/20.
 */

public class ShopCarBean implements Serializable {

    private static final long serialVersionUID = 3479351215101942907L;

    private String goodsName;
    private String goodsImg;
    private String specifications;
    private String uid;
    private int goodsCount;
    private String goods_id;
    private String gid;//规格id
    private String type;
    private String[] typeArr;
    private boolean isSelect = false;
    private double sell_price;//价格

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        typeArr = StringUtils.isEmpty(type) ? new String[]{} : type.split(",");
    }

    public void setTypeArr(String[] typeArr) {
        this.typeArr = typeArr;
    }

    public String[] getTypeArr() {
        return typeArr;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getGid() {
        return gid;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }


    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }


    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }


    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }


    public double getSell_price() {
        return sell_price;
    }

    public void setSell_price(double sell_price) {
        this.sell_price = sell_price;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
