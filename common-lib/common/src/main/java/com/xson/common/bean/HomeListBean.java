package com.xson.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class HomeListBean<T> extends AbsListBean {
    public Data<T> data;

    public class Data<T> {
        public List<BannerBean> banner;
        public List<CategoryBean> category;
        public List<T> place;

    }

    @Override
    public List<T> getDataList() {
        return data != null ? data.place : null;
    }

    public List<BannerBean> getBannerList() {
        return data != null ? data.banner : null;
    }

    public List<CategoryBean> getCategoryList() {
        return data != null ? data.category : null;
    }


    /**
     * Created by xkai on 2018/1/6.
     * 首页轮播图
     */

    public static class BannerBean {

        /**
         * pic : http://qkmimages.img-cn-shenzhen.aliyuncs.com/180201/5a7275ea7ffa2.png
         * link : p_178
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


    /**
     * Created by xkai on 2018/1/6.
     * 首页分类
     */

    public static class CategoryBean implements Serializable {


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

}
