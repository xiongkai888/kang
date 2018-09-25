package com.lanmei.kang.event;

import com.lanmei.kang.bean.CategoryBean;

/**
 * Created by xkai on 2018/2/7.
 * 选择分类事件
 */

public class ChooseCategoryEvent {

    CategoryBean bean;

    public CategoryBean getBean() {
        return bean;
    }

    public ChooseCategoryEvent(CategoryBean bean){
        this.bean = bean;
    }

}
