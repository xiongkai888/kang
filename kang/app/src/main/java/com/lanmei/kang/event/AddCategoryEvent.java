package com.lanmei.kang.event;

import com.lanmei.kang.bean.CategoryBean;

import java.util.List;

/**
 * Created by xkai on 2018/2/8.
 * 添加分类或编辑分类
 */

public class AddCategoryEvent {

    public List<CategoryBean> list;

    public List<CategoryBean> getList() {
        return list;
    }

    public AddCategoryEvent(List<CategoryBean> list){
        this.list = list;
    }

}
