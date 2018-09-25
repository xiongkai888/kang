package com.xson.common.bean;

import java.util.List;

/**
 * @author Milk
 * 不翻页
 */
public class NoPageListBean<T> extends AbsListBean {
    public List<T> data;

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
        return data;
    }
}