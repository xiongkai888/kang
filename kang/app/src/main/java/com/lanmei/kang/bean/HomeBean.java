package com.lanmei.kang.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 * 首页商家
 *
 */

public class HomeBean implements Serializable{


    private List<BannerBean> banner;
    private List<CategoryBean> category;
    private List<PlaceBean> place;

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public List<CategoryBean> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryBean> category) {
        this.category = category;
    }

    public List<PlaceBean> getPlace() {
        return place;
    }

    public void setPlace(List<PlaceBean> place) {
        this.place = place;
    }

    public static class BannerBean {
        /**
         * pic : http://qkmimages.img-cn-shenzhen.aliyuncs.com/180201/5a7275e33d003.png
         * link : p_102
         */

        private String pic;
        private String link;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }

    public static class CategoryBean implements Serializable{
        /**
         * id : 125
         * name : 中药理疗
         * pic : http://qkmimages.img-cn-shenzhen.aliyuncs.com/180201/5a727575bb0a0.png
         */

        private String id;
        private String name;
        private String pic;

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

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }

    public static class PlaceBean {
        /**
         * id : 16
         * uid : 203
         * fee_introduction : http://qkmimages.img-cn-shenzhen.aliyuncs.com/180204/5a76791a1d8da.png
         * name : 嘉豪国际养身会所
         * address : 广州市白云区白云大道北-地铁站
         * money : 200.00
         * area : 广州
         * lat : 23.1827180
         * lon : 113.2564340
         * distance : 10391
         */

        private String id;
        private String uid;
        private String fee_introduction;
        private String name;
        private String address;
        private String money;
        private String area;
        private String lat;
        private String lon;
        private int distance;

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

        public String getFee_introduction() {
            return fee_introduction;
        }

        public void setFee_introduction(String fee_introduction) {
            this.fee_introduction = fee_introduction;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }
    }
}
