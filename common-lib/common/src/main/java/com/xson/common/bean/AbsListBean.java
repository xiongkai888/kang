package com.xson.common.bean;

import java.util.List;

/**
 * Created by xson on 2016/12/15.
 */

public abstract class AbsListBean<T> extends  BaseBean{
    public abstract int getTotalPage();
    public abstract int getCurrPage();
    public abstract List<T> getDataList();
}
