package com.lanmei.kang.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xkai on 2018/1/29.
 * 商家详情
 */

public class MerchantDetailsBean implements Serializable{

    /**
     * id : 15
     * uid : 206
     * name : 静雅轩调理养生馆
     * pics : ["http://qkmimages.img-cn-shenzhen.aliyuncs.com/180201/5a727920a820b.png","http://qkmimages.img-cn-shenzhen.aliyuncs.com/180201/5a727923752ab.png"]
     * stime : 10:00:00
     * etime : 22:00:00
     * address : 天河区广利路75号东洲大厦A座11楼1102房
     * album : ["http://qkmimages.img-cn-shenzhen.aliyuncs.com/180201/5a727934ebd21.png"]
     * lat : 23.1364380
     * lon : 113.3267190
     * place_introduction : 静雅轩调理养生馆专业养生馆
     * tel : 020-38625171
     * favoured : 0
     * distance : 12,395.85
     * goods : [{"id":"286","name":"金木浴疗墨玉泡澡","sell_price":"188.00","img":"http://stdrimages.oss-cn-shenzhen.aliyuncs.com/206/151745230178.png","content":"金木浴疗墨玉泡澡不错","is_del":"0","status":"1","pic":["","http://stdrimages.oss-cn-shenzhen.aliyuncs.com/206/15174523016.png","http://stdrimages.oss-cn-shenzhen.aliyuncs.com/206/151745230145.png"],"category":["7"],"good_favoured":"1"},{"id":"285","name":"单人脸部深层护理 眼部调理","sell_price":"39.00","img":"http://stdrimages.oss-cn-shenzhen.aliyuncs.com/206/151745210327.png","content":"单人脸部深层护理 眼部调理，不错手法","is_del":"0","status":"1","pic":["","http://stdrimages.oss-cn-shenzhen.aliyuncs.com/206/151745210323.png","http://stdrimages.oss-cn-shenzhen.aliyuncs.com/206/15174521032.png"],"category":["7"],"good_favoured":"0"}]
     */

    private String id;
    private String uid;
    private String name;
    private String stime;
    private String etime;
    private String address;
    private String lat;
    private String lon;
    private String place_introduction;
    private String tel;
    private String favoured;
    private String distance;
    private List<String> pics;
    private List<String> album;
    private List<GoodsBean> goods;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPlace_introduction() {
        return place_introduction;
    }

    public void setPlace_introduction(String place_introduction) {
        this.place_introduction = place_introduction;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFavoured() {
        return favoured;
    }

    public void setFavoured(String favoured) {
        this.favoured = favoured;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public List<String> getAlbum() {
        return album;
    }

    public void setAlbum(List<String> album) {
        this.album = album;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean implements Serializable{
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
         * good_favoured : 1
         * file: ["http://stdrimages.oss-cn-shenzhen.aliyuncs.com/lanmei/kang/img/2018-02-24%2016%3A04%3A03-0.jpg"]
         */

        private String id;
        private String name;
        private String sell_price;
        private String img;
        private String content;
        private String is_del;
        private String status;
        private String mid;//自己加的
        private String place_name;//自己加的
        private String good_favoured;
        private List<String> pic;
        private List<String> category;
        private List<String> file;

        public void setFile(List<String> file) {
            this.file = file;
        }

        public List<String> getFile() {
            return file;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public void setPlace_name(String place_name) {
            this.place_name = place_name;
        }

        public String getMid() {
            return mid;
        }

        public String getPlace_name() {
            return place_name;
        }


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
}
