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
public class ListBean<T> extends AbsListBean {
    public Data<T> data;
    public class Data<T1> {
        public int pageNumber; // 页码
        public int pageSize; // 页大小
        public int totalPage; // 总页数
        public int totalRow; // 总条数
        public List<T1> list;

    }

    @Override
    public int getTotalPage() {
        return data != null ? data.totalPage : 0;
    }

    @Override
    public int getCurrPage() {
        return data != null ? data.pageNumber : 0;
    }

    @Override
    public List<T> getDataList() {
        return data != null ? data.list: null;
    }
}
