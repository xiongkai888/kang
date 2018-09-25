package com.lanmei.kang.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xkai on 2018/2/6.
 * 商家服务项目
 */

public class MerchantItemsListBean implements Serializable{

    /**
     * id : 277
     * name : 商品分类商品
     * sell_price : 2002.00
     * img : http://qkmimages.img-cn-shenzhen.aliyuncs.com/180105/5a4f37c986483.jpg
     * content : 商品分类商品分类商品分类商品分类商品分类商品分类商品分类商品分类商品分类商品分类商品分类商品分类商品分类商品分类商品分类商品分类商品分类商品分类商品分类商品分类商品分类商品分类商品分类商品分类
     * is_del : 0
     * status : 1
     * pic : ["http://qkmimages.img-cn-shenzhen.aliyuncs.com/180105/5a4f37ced2873.JPG","http://qkmimages.img-cn-shenzhen.aliyuncs.com/180105/5a4f37d1a75ad.jpg"]
     * category : [{"id":"3","name":"中西理疗(商品)"},{"id":"4","name":"高端理疗(商品)"}]
     * file : []
     */

    private String id;
    private String name;
    private String sell_price;
    private String img;
    private String content;
    private String is_del;
    private String status;
    private List<String> pic;
    private List<CategoryBean> category;
    private List<String> file;

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

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }

    public List<CategoryBean> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryBean> category) {
        this.category = category;
    }

    public List<String> getFile() {
        return file;
    }

    public void setFile(List<String> file) {
        this.file = file;
    }

    public static class CategoryBean implements Serializable{
        /**
         * id : 3
         * name : 中西理疗(商品)
         */

        private String id;
        private String name;

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
    }
}
