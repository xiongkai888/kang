package com.lanmei.kang.ui.merchant_tab.goods.shop;

/**
 * Created by xkai on 2018/1/20.
 */

public class ShopCarBean {

    private static final long serialVersionUID = 3479351215101942907L;

    private int recordId;
    private String goodsStoreName;
    private int goodsStoreId;
    private String goodsBrand;
    private String goodsName;
    private Double goodsPrice;
    private String goodsImg;
    private String goodsParams;
    private String uid;
    private int goodsCount;
    private int goodsComments;//评论数
    private int goodsSale;//出售数
    private int store_nums;//库存


    private double cost_price;
    private String goods_id;
    private double market_price;
    private String products_img;
    private String products_no;
    private double sell_price;
    private double weight;
    private String spec;
    private double express_fee;

    private boolean isSelect = false;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsBrand() {
        return goodsBrand;
    }

    public void setGoodsBrand(String goodsBrand) {
        this.goodsBrand = goodsBrand;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getGoodsStoreName() {
        return goodsStoreName;
    }

    public void setGoodsStoreName(String goodsStoreName) {
        this.goodsStoreName = goodsStoreName;
    }

    public int getGoodsStoreId() {
        return goodsStoreId;
    }

    public void setGoodsStoreId(int goodsStoreId) {
        this.goodsStoreId = goodsStoreId;
    }

    public String getGoodsParams() {
        return goodsParams;
    }

    public void setGoodsParams(String goodsParams) {
        this.goodsParams = goodsParams;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public double getCost_price() {
        return cost_price;
    }

    public void setCost_price(double cost_price) {
        this.cost_price = cost_price;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public double getMarket_price() {
        return market_price;
    }

    public void setMarket_price(double market_price) {
        this.market_price = market_price;
    }

    public String getProducts_img() {
        return products_img;
    }

    public void setProducts_img(String products_img) {
        this.products_img = products_img;
    }

    public String getProducts_no() {
        return products_no;
    }

    public void setProducts_no(String products_no) {
        this.products_no = products_no;
    }

    public double getSell_price() {
        return sell_price;
    }

    public void setSell_price(double sell_price) {
        this.sell_price = sell_price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public double getExpress_fee() {
        return express_fee;
    }

    public void setExpress_fee(double express_fee) {
        this.express_fee = express_fee;
    }

    public int getGoodsComments() {
        return goodsComments;
    }

    public void setGoodsComments(int goodsComments) {
        this.goodsComments = goodsComments;
    }

    public int getGoodsSale() {
        return goodsSale;
    }

    public void setGoodsSale(int goodsSale) {
        this.goodsSale = goodsSale;
    }

    public int getStore_nums() {
        return store_nums;
    }

    public void setStore_nums(int store_nums) {
        this.store_nums = store_nums;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
