package com.lanmei.kang.bean;

import java.util.List;

/**
 * Created by xkai on 2018/2/2.
 * 我的收藏 服务（）bean
 */

public class CollectItemsBean {

    /**
     * id : 286
     * name : 金木浴疗墨玉泡澡
     * sell_price : 188.00
     * img : http://stdrimages.oss-cn-shenzhen.aliyuncs.com/206/151745230178.png
     * content : 金木浴疗墨玉泡澡不错
     * is_del : 0
     * status : 1
     * pic : ["","http://stdrimages.oss-cn-shenzhen.aliyuncs.com/206/15174523016.png","http://stdrimages.oss-cn-shenzhen.aliyuncs.com/206/151745230145.png"]
     * category : ["7"]
     * mid : 15
     * place_name : 静雅轩调理养生馆
     * good_favoured : 1
     */

    private String id;
    private String name;
    private String sell_price;
    private String img;
    private String content;
    private String is_del;
    private String status;
    private String mid;
    private String place_name;
    private String good_favoured;
    private List<String> pic;
    private List<String> category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSell_price() {
        return sell_price;
    }

    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIs_del() {
        return is_del;
    }

    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getGood_favoured() {
        return good_favoured;
    }

    public void setGood_favoured(String good_favoured) {
        this.good_favoured = good_favoured;
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
}
