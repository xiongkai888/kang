package com.xson.common.adapter;

import android.content.Context;

import com.xson.common.utils.StringUtils;

import java.util.List;

/**
 * @author Milk <249828165@qq.com>
 */
public abstract class SwipeRefreshAdapter<T> extends LoadMoreAdapter<T> {
    protected List<T> data;
    protected final Context context;

    public SwipeRefreshAdapter(Context context) {
        this.context = context;
    }

    public void clear() {
        if(data != null)
            data.clear();
    }

    @Override
    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public void addData(List<T> data) {
        if(!StringUtils.isEmpty(this.data) && !StringUtils.isEmpty(data)) {
            this.data.addAll(data);
        }
    }

    @Override
    public int getCount() {
        if(data == null)
            return 0;
        return data.size();
    }

    public List<T> getData() {
        return data;
    }

    public T getItem(int position) {
        return data.get(position);
    }

}
