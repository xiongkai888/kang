package com.lanmei.kang.bean;

import java.util.List;

/**
 * Created by xkai on 2018/10/18.
 * 销售列表
 */

public class GoodsSellListBean {

    /**
     * id : 969
     * addtime : 1545207352
     * uptime : 1545207352
     * pay_no : 20181219B0100T1895
     * state : 5
     * order_no : 20181219B0100T1425
     * gname : 
     * num : 3
     * total_price : 1197.00
     * addressid : 0
     * address : 
     * uid : 204
     * phone : 
     * username : 
     * pay_type : 4
     * is_del : 0
     * endtime : 1545207352
     * pay_status : 1
     * courier : 
     * dis_type : null
     * sellerid : 202
     * attribute : null
     * pay_time : null
     * yj_status : 0
     * c_type : 0
     * nickname : 大冒险
     * pic : http://stdrimages.oss-cn-shenzhen.aliyuncs.com/lanmei/kang/img1/head-1532470495.jpg
     * goods : [{"goodsid":"46","price":"399.0","goodsname":"天美纪雪肌赋活水","num":"1.0","danwei":"元","cover":"http://qkmimages.img-cn-shenzhen.aliyuncs.com/Uploads/imgs/20181116/1542348908176.jpg","gid":"0","specifications":"","specificationsname":""},{"goodsid":"46","price":"399.0","goodsname":"天美纪雪肌赋活水","num":"2.0","danwei":"元","cover":null,"gid":null,"specifications":null,"specificationsname":null}]
     */

    private String id;
    private String addtime;
    private String uptime;
    private String pay_no;
    private String state;
    private String order_no;
    private String gname;
    private int num;
    private String total_price;
    private String addressid;
    private String address;
    private String uid;
    private String phone;
    private String username;
    private String pay_type;
    private String is_del;
    private String endtime;
    private String pay_status;
    private String courier;
    private String dis_type;
    private String sellerid;
    private String attribute;
    private String pay_time;
    private String yj_status;
    private String c_type;
    private String nickname;
    private String pic;
    private List<GoodsBean> goods;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPay_no() {
        return pay_no;
    }

    public void setPay_no(String pay_no) {
        this.pay_no = pay_no;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getAddressid() {
        return addressid;
    }

    public void setAddressid(String addressid) {
        this.addressid = addressid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getIs_del() {
        return is_del;
    }

    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getDis_type() {
        return dis_type;
    }

    public void setDis_type(String dis_type) {
        this.dis_type = dis_type;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getYj_status() {
        return yj_status;
    }

    public void setYj_status(String yj_status) {
        this.yj_status = yj_status;
    }

    public String getC_type() {
        return c_type;
    }

    public void setC_type(String c_type) {
        this.c_type = c_type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * goodsid : 46
         * price : 399.0
         * goodsname : 天美纪雪肌赋活水
         * num : 1.0
         * danwei : 元
         * cover : http://qkmimages.img-cn-shenzhen.aliyuncs.com/Uploads/imgs/20181116/1542348908176.jpg
         * gid : 0
         * specifications : 
         * specificationsname : 
         */

        private String goodsid;
        private String price;
        private String goodsname;
        private String num;
        private String danwei;
        private String cover;
        private String gid;
        private String specifications;
        private String specificationsname;

        public String getGoodsid() {
            return goodsid;
        }

        public void setGoodsid(String goodsid) {
            this.goodsid = goodsid;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getGoodsname() {
            return goodsname;
        }

        public void setGoodsname(String goodsname) {
            this.goodsname = goodsname;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getDanwei() {
            return danwei;
        }

        public void setDanwei(String danwei) {
            this.danwei = danwei;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getSpecifications() {
            return specifications;
        }

        public void setSpecifications(String specifications) {
            this.specifications = specifications;
        }

        public String getSpecificationsname() {
            return specificationsname;
        }

        public void setSpecificationsname(String specificationsname) {
            this.specificationsname = specificationsname;
        }
    }
}
