package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.HomeBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;



/**
 * 我的积分
 */
public class MyIntegralAdapter extends SwipeRefreshAdapter<HomeBean> {
    private final Context context;

    public MyIntegralAdapter(Context context) {
        super(context);
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_integral, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (MyIntegralAdapter.ViewHolder)holder;
    }

    @Override
    public int getCount() {
        return 5;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{


        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
