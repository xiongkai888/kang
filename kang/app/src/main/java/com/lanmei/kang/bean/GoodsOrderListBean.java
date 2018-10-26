package com.lanmei.kang.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xkai on 2018/10/24.
 * 商品订单列表
 */

public class GoodsOrderListBean implements Serializable{

    /**
     * id : 774
     * addtime : 1540368965
     * uptime : 1540368965
     * pay_no : 20181024B0100T8224
     * state : 0
     * order_no : 20181024B0100T7721
     * gname : 
     * num : 1
     * total_price : 888.00
     * addressid : 0
     * address : 广东省云浮市云城区美图秀哈哈
     * uid : 202
     * phone : 15914369252
     * username : 大熊
     * pay_type : 7
     * is_del : 0
     * endtime : 1540368965
     * pay_status : 0
     * courier : 
     * dis_type : 2
     * sellerid : 0
     * attribute : null
     * pay_time : null
     * goods : [{"goodsid":"25","price":"888.00","goodsname":"玩一玩","num":"1","danwei":"","cover":"http://qkmimages.img-cn-shenzhen.aliyuncs.com/Uploads/imgs/20180926/15379401148507.jpg","gid":"0","specifications":"","specificationsname":""}]
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

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean implements Serializable{
        /**
         * goodsid : 25
         * price : 888.00
         * goodsname : 玩一玩
         * num : 1
         * danwei : 
         * cover : http://qkmimages.img-cn-shenzhen.aliyuncs.com/Uploads/imgs/20180926/15379401148507.jpg
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
