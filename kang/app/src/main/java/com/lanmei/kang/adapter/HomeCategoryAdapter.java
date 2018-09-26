package com.lanmei.kang.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.ui.home.activity.PhysiotherapyActivity;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.bean.HomeListBean;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 首页分类
 */
public class HomeCategoryAdapter extends SwipeRefreshAdapter<HomeListBean.CategoryBean> {

    public HomeCategoryAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home_category, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final HomeListBean.CategoryBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.nameTv.setText(bean.getName());
        ImageHelper.load(context,bean.getPic(),viewHolder.picIv,null,true,R.mipmap.default_pic,R.mipmap.default_pic);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean",bean);
                IntentUtil.startActivity(context, PhysiotherapyActivity.class,bundle);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.pic_iv)
        ImageView picIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;
        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
