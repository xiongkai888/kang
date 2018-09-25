package com.xson.common.bean;

import java.util.List;

/**
 * @author Milk <249828165@qq.com>
 *带翻页列表结果格式的bean
{
"status":0,
"msg":"成功"，
"data":{
"pageNumber":1
"pageSize":10
"totalPage":20
"totalRow":1000,
"list":{  // 这里是放置泛型T的地方
}
}}
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
    public int getTotalPage() {
        return 0;
    }

    @Override
    public int getCurrPage() {
        return 0;
    }

    @Override
    public List<T> getDataList() {
        return data != null ? data.place: null;
    }
    public List<BannerBean> getBannerList() {
        return data != null ? data.banner: null;
    }
    public List<CategoryBean> getCategoryList() {
        return data != null ? data.category: null;
    }
    
    
}
