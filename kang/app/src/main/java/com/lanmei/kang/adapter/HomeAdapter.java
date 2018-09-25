package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.lanmei.kang.R;
import com.lanmei.kang.bean.HomeBean;
import com.lanmei.kang.ui.home.activity.RecomMerchantListActivity;
import com.lanmei.kang.ui.merchant.activity.MerchantIntroduceActivity;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.bean.BannerBean;
import com.xson.common.bean.HomeListBean;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.DoubleUtil;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 *
 */
public class HomeAdapter extends SwipeRefreshAdapter<HomeBean> {

    final public static int TYPE_BANNER = 100;
    HomeListBean<HomeBean> listBean;
    boolean isFirst = true;

    public HomeAdapter(Context context) {
        super(context);
    }


    public void setData(HomeListBean<HomeBean> listBean) {
        this.listBean = listBean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        if (viewType == TYPE_BANNER) { // banner
            BannerViewHolder bannerHolder = new BannerViewHolder(LayoutInflater.from(context).inflate(R.layout.home_head_ad, parent, false));
            return bannerHolder;
        }
        // 列表  item_home_list
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {


        if (getItemViewType(position) == TYPE_BANNER) {
            onBindBannerViewHolder(holder, position); // banner
            return;
        }
        if (StringUtils.isEmpty(listBean) || StringUtils.isEmpty(listBean.getDataList())) {
            return;
        }
        List<HomeBean> list = listBean.getDataList();
        final HomeBean bean = list.get(position - 1);

        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.nameTv.setText(bean.getName());//商家名称
        viewHolder.priceTv.setText(String.format(context.getString(R.string.price),bean.getMoney()));//价格
        viewHolder.distanceTv.setText(DoubleUtil.formatDistance(bean.getDistance())+"  "+bean.getAddress());//商家地址
        viewHolder.labelTv.setText(bean.getArea());//商家地区
        ImageHelper.load(context, bean.getFee_introduction(), viewHolder.picIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context, MerchantIntroduceActivity.class, bean.getUid());
            }
        });
    }

    @Override
    public int getCount() {
        if (StringUtils.isEmpty(listBean) || StringUtils.isEmpty(listBean.getDataList())) {
            return 1;
        }
        return listBean.getDataList().size() + 1;
    }

    @Override
    public int getItemViewType2(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        }
        return super.getItemViewType2(position);
    }


    // 推荐商品
    static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.pic_iv)
        ImageView picIv;
        @InjectView(R.id.label_tv)
        TextView labelTv;
        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.price_tv)
        TextView priceTv;
        @InjectView(R.id.distance_tv)
        TextView distanceTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    public void onBindBannerViewHolder(RecyclerView.ViewHolder holder, int position) {
        final BannerViewHolder viewHolder = (BannerViewHolder) holder;
        if (!StringUtils.isEmpty(listBean) && !StringUtils.isEmpty(listBean.getBannerList())) {
            List<BannerBean> bannerList = listBean.getBannerList();
            List<String> list = new ArrayList<>();
            for (BannerBean bean : bannerList) {
                list.add(bean.getPic());
            }
            CommonUtils.setBanner(viewHolder.banner, list, isFirst);//防止不断刷新，banner越滑越快
            if (isFirst) {
                isFirst = !isFirst;
            }
        }
        if (!StringUtils.isEmpty(listBean) && !StringUtils.isEmpty(listBean.getCategoryList())) {
            HomeCategoryAdapter adAdapter = new HomeCategoryAdapter(context);
            adAdapter.setData(listBean.getCategoryList());
            viewHolder.recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
            viewHolder.recyclerView.setAdapter(adAdapter);
        }

        viewHolder.mMore.setOnClickListener(new View.OnClickListener() {//更多  热点新闻
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context, RecomMerchantListActivity.class);
            }
        });
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.banner)
        ConvenientBanner banner;
        @InjectView(R.id.ll_more)
        LinearLayout mMore; // 更多
        @InjectView(R.id.recyclerView)
        RecyclerView recyclerView;//分类

        BannerViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);

        }
    }

}
