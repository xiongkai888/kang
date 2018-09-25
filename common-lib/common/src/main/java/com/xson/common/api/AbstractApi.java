package com.xson.common.api;

import android.content.Context;

import com.xson.common.utils.L;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Milk <249828165@qq.com>
 */
public abstract class AbstractApi {

    private int p;
    private int limit = L.limit;
    public static String API_URL = "";

    public static enum Method {
        GET,
        POST,
    }

    public static enum Enctype {
        TEXT_PLAIN,
        MULTIPART,
    }

    protected abstract String getPath();

    public abstract Method requestMethod();

    public Enctype requestEnctype() {
        return Enctype.TEXT_PLAIN;
    }

    public String getUrl() {
        return API_URL + getPath();
    }

    public void setPage(int page) {
        this.p = page;
    }

    public Map<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<String, Object>();

        Field[] field;
        Class clazz = getClass();
        try {
            for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
                field = c.getDeclaredFields();
                for (Field f : field) {
                    f.setAccessible(true);
                    Object value = f.get(this);
                    if (value != null && !f.getName().contains("$") && !"API_URL".equals(f.getName()) && !"serialVersionUID".equals(f.getName())) {
                        params.put(f.getName(), value);
                    }
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            L.e(e);
        }
        if (p > 0) {
            params.put("p", p);
            params.put("limit", limit);
        } else {
            params.remove("p");
            params.remove("limit");
        }

        return params;
    }




    public void handleParams(Context context, Map<String, Object> params) {

//        JSONObject jsonObject = new JSONObject();
//        Class clazz = head.getClass();
//        Field[] field = clazz.getDeclaredFields();
//        try {
//            for (Field f : field) {
//                f.setAccessible(true);
//                if (f.get(head) != null && !f.getName().contains("$")) { //exclude "shadow$_monitor_":0,"shadow$_klass_":"com.fangdr.linker.api.ShopNumberApi"
//                    jsonObject.put(f.getName(), f.get(head));
//                }
//            }
//        } catch (IllegalArgumentException | IllegalAccessException e) {
//            e.printStackTrace();
//        }

        //move file field
        HashMap<String, Object> fileMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof File)
                fileMap.put(entry.getKey(), entry.getValue());
            else if (value instanceof Iterable) { // List<File>, Collection<File>, etc...
                Iterator iter = ((Iterable) value).iterator();
                if (iter.hasNext() && iter.next() instanceof File) {
                    fileMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        if (!fileMap.isEmpty())
            for (String key : fileMap.keySet()) {
                params.remove(key);
            }
        //end

//        params.put("head", jsonObject);
//        String paramJson = JSON.toJSONString(params);
//        final String key = "fangdr";
//        String sign = CyptoUtils.md5(paramJson + key);
//        params.clear();
//        params.put("params", paramJson);
//        params.put("sign", sign);
        params.putAll(fileMap);
    }

}
