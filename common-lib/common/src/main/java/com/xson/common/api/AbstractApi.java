package com.xson.common.api;

import android.content.Context;

import com.xson.common.bean.UserBean;
import com.xson.common.helper.UserHelper;
import com.xson.common.utils.L;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Milk <249828165@qq.com>
 */
public abstract class AbstractApi {

    private int p;
    public static String API_URL = "";
    private HashMap<String, Object> paramsHashMap = new HashMap<>();
    private Method method = Method.POST;

    public void setMethod(Method method) {
        this.method = method;
    }

    public static enum Method {
        GET,
        POST,
    }

    public static enum Enctype {
        TEXT_PLAIN,
        MULTIPART,
    }


    public String getUserId(Context context) {
        UserBean userBean = UserHelper.getInstance(context).getUserBean();
        if (userBean != null) {
            return userBean.getId();
        }
        return "";
    }

    public String getToken(Context context) {
        UserBean userBean = UserHelper.getInstance(context).getUserBean();
        if (userBean != null) {
            return userBean.getToken();
        }
        return "";
    }

    protected abstract String getPath();

    public Method requestMethod() {
        return method;
    }

    public Enctype requestEnctype() {
        return Enctype.TEXT_PLAIN;
    }

    public String getUrl() {
        return API_URL + getPath();
    }

    public void setPage(int page) {
        this.p = page;
    }

    public AbstractApi add(String key, Object value) {
        paramsHashMap.put(key, value);
        return this;
    }

    public Map<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        for (Map.Entry<String, Object> item : paramsHashMap.entrySet()) {
            params.put(item.getKey(), item.getValue());
//            if (item.getValue() instanceof com.alibaba.fastjson.JSONArray) {
//                params.put(item.getKey(), (com.alibaba.fastjson.JSONArray) item.getValue());
//                L.d(L.TAG, item.getKey() + "," + item.getValue());
//            } else {
//                params.put(item.getKey(), item.getValue());
//            }
        }
        if (p > 0) {
            params.put(L.p, p);
        }
        return params;
    }
}
