package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.lanmei.kang.R;
import com.lanmei.kang.bean.HomeBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 *
 */
public class MerchantTabAdapter extends SwipeRefreshAdapter<HomeBean> {


    public int TYPE_BANNER = 100;

    public MerchantTabAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) { // banner
            return new BannerViewHolder(LayoutInflater.from(context).inflate(R.layout.head_merchant_tab, parent, false));
        }
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_merchant_tab, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_BANNER) {
            onBindBannerViewHolder(holder, position); // banner
            return;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);

        }
    }

    @Override
    public int getItemViewType2(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        }
        return super.getItemViewType2(position);
    }



    //头部
    public class BannerViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.banner)
        ConvenientBanner banner;
        @InjectView(R.id.recyclerView)
        RecyclerView recyclerView;



        BannerViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(int position) {
        }
    }


    @Override
    public int getCount() {
        return 5;
    }

    public void onBindBannerViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

}
