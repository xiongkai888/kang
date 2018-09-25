package com.lanmei.kang.api;

import android.content.Context;

import com.lanmei.kang.bean.UserBean;
import com.lanmei.kang.helper.UserHelper;
import com.xson.common.api.ApiV2;

/**
 * Created by xkai on 2018/1/4.
 * 需要获取id或者获取token时继承这个抽象类
 */

public abstract class KangApi extends ApiV2{

    @Override
    public  String getUserId(Context context) {
        UserBean userBean = UserHelper.getInstance(context).getUserBean();
        if(userBean != null) {
            return userBean.getId();
        }
        return "";
    }

    @Override
    public String getToken(Context context) {
        UserBean userBean = UserHelper.getInstance(context).getUserBean();
        if(userBean != null) {
            return userBean.getToken();
        }
        return "";
    }
}
