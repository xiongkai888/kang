package com.lanmei.kang.bean;

import java.util.List;

/**
 * Created by xkai on 2018/10/18.
 * 销售列表
 */

public class GoodsSellListBean {

    /**
     * id : 698
     * addtime : 1539853670
     * uptime : 1539853670
     * pay_no : 20181018B0100T1624
     * state : 1
     * order_no : 20181018B0100T1423
     * gname :
     * num : 20
     * total_price : 12000.00
     * addressid : 0
     * address : 554
     * uid : 201
     * phone : 18814379628
     * username : 小明
     * pay_type : 6
     * is_del : 0
     * endtime : 1539853670
     * pay_status : 0
     * courier :
     * dis_type : 1
     * sellerid : 500
     * attribute : null
     * pay_time : null
     * nickname : A好
     * pic : http://qkmimages.img-cn-shenzhen.aliyuncs.com/180119/5a61c001001ce.jpg
     * goods : [{"goodsid":"26","price":"200.00","goodsname":"玩一玩","num":"10","danwei":"","cover":"http://qkmimages.img-cn-shenzhen.aliyuncs.com/Uploads/imgs/20180926/15379532752527.jpg","gid":"96","specifications":"红色,M","specificationsname":"颜色,尺寸"},{"goodsid":"7","price":"1000.00","goodsname":"苹果手机","num":"10","danwei":null,"cover":"/Public/images/banner1.jpg","gid":"81","specifications":"紫色,XXL","specificationsname":"颜色,尺寸"}]
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
    private Object attribute;
    private Object pay_time;
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

    public Object getAttribute() {
        return attribute;
    }

    public void setAttribute(Object attribute) {
        this.attribute = attribute;
    }

    public Object getPay_time() {
        return pay_time;
    }

    public void setPay_time(Object pay_time) {
        this.pay_time = pay_time;
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
         * goodsid : 26
         * price : 200.00
         * goodsname : 玩一玩
         * num : 10
         * danwei :
         * cover : http://qkmimages.img-cn-shenzhen.aliyuncs.com/Uploads/imgs/20180926/15379532752527.jpg
         * gid : 96
         * specifications : 红色,M
         * specificationsname : 颜色,尺寸
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
