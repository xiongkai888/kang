package com.lanmei.kang.helper.coupon;

import java.io.Serializable;

public class BeanGoods implements Serializable {


    private static final long serialVersionUID = 7143045943868799339L;
    /**
     * id : 4
     * addtime : 1524208190
     * uptime : 1524208190
     * goodsname : test4
     * type : 90
     * price : 111.00
     * sale_price: "13.00"
     * up_time : 1524208190
     * down_time : null
     * inventory : 111
     * is_del : null
     * order_by : 0
     * cover : http://image-znsc.img-cn-shenzhen.aliyuncs.com/Uploads/imgs/20180420/15242081751013.png
     * imgs : null
     * state : 1
     * userid : 5
     * content :
     * hot : 1
     * vipprice : 11.00
     * sales : 11
     * promotion : 1
     * specificationsname : 尺寸,类型
     * specifications : 1,类型a,红
     */

    private int id;
    private int gid;
    private String area;
    private long addtime;
    private long uptime;
    private String goodsname;
    private String type;
    public Double price;
    public Double sale_price;
    private long up_time;
    private Object down_time;
    private int inventory;
    private Object is_del;
    private int order_by;
    private String cover;
    private Object imgs;
    private int state;
    private int userid;
    private String content;
    private int hot;
    private Double vipprice;
    private int sales;
    private int promotion;
    private String specificationsname;
    private String specifications;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public long getAddtime() {
        return addtime;
    }

    public void setAddtime(long addtime) {
        this.addtime = addtime;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getType() {
        return type;
    }

    String[] typeArr;
    public String[] getTypeArr() {
        return typeArr;
    }

    public void setType(String type) {
        this.type = type;
        typeArr=type.split(",");
    }

    public Double getPrice() {
        if (price==null)
            return 0.00d;
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSale_price() {
        if (sale_price==null)
            return 0.00d;
        return sale_price;
    }

    public void setSale_price(Double sale_price) {
        this.sale_price = sale_price;
    }

    public long getUp_time() {
        return up_time;
    }

    public void setUp_time(long up_time) {
        this.up_time = up_time;
    }

    public Object getDown_time() {
        return down_time;
    }

    public void setDown_time(Object down_time) {
        this.down_time = down_time;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public Object getIs_del() {
        return is_del;
    }

    public void setIs_del(Object is_del) {
        this.is_del = is_del;
    }

    public int getOrder_by() {
        return order_by;
    }

    public void setOrder_by(int order_by) {
        this.order_by = order_by;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Object getImgs() {
        return imgs;
    }

    public void setImgs(Object imgs) {
        this.imgs = imgs;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public Double getVipprice() {
        if (price==null)
            return 0.00d;
        return vipprice;
    }

    public void setVipprice(Double vipprice) {
        this.vipprice = vipprice;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public int getPromotion() {
        return promotion;
    }

    public void setPromotion(int promotion) {
        this.promotion = promotion;
    }

    public String getSpecificationsname() {
        return specificationsname;
    }



    public void setSpecificationsname(String specificationsname) {
        this.specificationsname = specificationsname;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }
}
