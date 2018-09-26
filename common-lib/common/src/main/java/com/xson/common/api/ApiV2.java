package com.xson.common.api;

import android.content.Context;

import com.xson.common.bean.UserBean;
import com.xson.common.helper.UserHelper;

/**
 * @author Milk <249828165@qq.com>
 */
public abstract class ApiV2 extends AbstractApi {

    public  String getUserId(Context context) {
        UserBean userBean = UserHelper.getInstance(context).getUserBean();
        if(userBean != null) {
            return userBean.getId();
        }
        return "";
    }

    public String getToken(Context context) {
        UserBean userBean = UserHelper.getInstance(context).getUserBean();
        if(userBean != null) {
            return userBean.getToken();
        }
        return "";
    }
}
