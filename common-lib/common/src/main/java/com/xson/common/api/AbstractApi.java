package com.xson.common.api;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Milk <249828165@qq.com>
 */
public abstract class AbstractApi {

    private int p;
    public static String API_URL = "";
    private HashMap<String, Object> paramsHashMap = new HashMap<String, Object>();
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

    public AbstractApi addParams(String key, Object value) {
        paramsHashMap.put(key, value);
        return this;
    }

    public Map<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        for (Map.Entry<String, Object> item : paramsHashMap.entrySet()) {
            if (item.getValue() instanceof com.alibaba.fastjson.JSONArray) {
                params.put(item.getKey(), (com.alibaba.fastjson.JSONArray) item.getValue());
//                L.d(L.TAG, item.getKey() + "," + item.getValue());
            } else {
                params.put(item.getKey(), item.getValue());
            }
        }
        return params;
    }
}