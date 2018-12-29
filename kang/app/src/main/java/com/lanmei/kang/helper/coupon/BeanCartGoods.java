package com.lanmei.kang.helper.coupon;

/**
 * Created by Administrator on 2016/8/31.
 */

public class BeanCartGoods extends BeanGoods {
    private static final long serialVersionUID = 3479351215101942907L;

    public BeanCartGoods() {
    }

    private int goodsCount;//购物车数量
    private boolean isSelect = true;
    private int is_comment;

    public void setBeanGoods(BeanGoods beanGoods) {

        setId(beanGoods.getId());
        setGid(beanGoods.getGid());
        setArea(beanGoods.getArea());
        setInventory(beanGoods.getInventory());
        setGoodsname(beanGoods.getGoodsname());
        setCover(beanGoods.getCover());
        setPrice(beanGoods.getPrice());
        setSale_price(beanGoods.getSale_price());
        setVipprice(beanGoods.getVipprice());
        setSpecificationsname(beanGoods.getSpecificationsname());
        setSpecifications(beanGoods.getSpecifications());
        setSales(beanGoods.getSales());
        setUserid(beanGoods.getUserid());
        setType(beanGoods.getType());
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    private int goodsid;

    public int getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(int goodsid) {
        this.goodsid = goodsid;
        setId(goodsid);
    }

    public int getIs_comment() {
        return is_comment;
    }

    public void setIs_comment(int is_comment) {
        this.is_comment = is_comment;
    }

    private int num;//订单中每个商品数量

    public void setNum(int num) {
        this.num = num;
        setGoodsCount(num);
        setSale_price(getPrice());
    }
}
