package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.lanmei.kang.R;
import com.lanmei.kang.bean.AdBean;
import com.lanmei.kang.bean.MerchantTabClassifyBean;
import com.lanmei.kang.bean.MerchantTabGoodsBean;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 *
 */
public class MerchantTabAdapter extends SwipeRefreshAdapter<MerchantTabGoodsBean> {


    public int TYPE_BANNER = 100;
    private BannerViewHolder bannerViewHolder;

    public MerchantTabAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) { // banner
            bannerViewHolder = new BannerViewHolder(LayoutInflater.from(context).inflate(R.layout.head_merchant_tab, parent, false));
            return bannerViewHolder;
        }
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_merchant_tab, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_BANNER) {

            return;
        }
        MerchantTabGoodsBean bean = getItem(position - 1);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.image)
        ImageView image;
        @InjectView(R.id.content_tv)
        TextView contentTv;
        @InjectView(R.id.money_tv)
        TextView moneyTv;
        @InjectView(R.id.sell_num_tv)
        TextView sellNumTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(MerchantTabGoodsBean bean) {
            ImageHelper.load(context, bean.getImgs(), image, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
            contentTv.setText(bean.getGoodsname());
            moneyTv.setText(String.format(context.getString(R.string.price), bean.getPrice()));
            sellNumTv.setText(String.format(context.getString(R.string.have_sales), bean.getSales()));
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
        @InjectView(R.id.recyclerView_hot_img)
        RecyclerView recyclerViewHotImg;


        BannerViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setBannerParameter(List<AdBean> adBeanList) {
            banner.setPages(new CBViewHolderCreator() {
                @Override
                public Object createHolder() {
                    return new MerchantTabAdAdapter();
                }
            }, adBeanList);
            banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
            banner.setPageIndicator(new int[]{R.drawable.shape_item_index_white, R.drawable.shape_item_index_red});
            banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

        }
        public void setClassifyParameter(List<MerchantTabClassifyBean> adBeanList) {
            MerchantTabClassifyAdapter adapter = new MerchantTabClassifyAdapter(context);
            adapter.setData(adBeanList);
            recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(adapter);
        }
        public void setHotImgParameter(List<AdBean> adBeanList) {
            MerchantTabHotImgAdapter adapter = new MerchantTabHotImgAdapter(context);
            adapter.setData(adBeanList);
            recyclerViewHotImg.setLayoutManager(new LinearLayoutManager(context));
            recyclerViewHotImg.setNestedScrollingEnabled(false);
            recyclerViewHotImg.setAdapter(adapter);
        }
    }

    public void setBannerParameter(List<AdBean> adBeanList) {
        bannerViewHolder.setBannerParameter(adBeanList);
    }

    public void setClassifyParameter(List<MerchantTabClassifyBean> list) {
        bannerViewHolder.setClassifyParameter(list);
    }

    public void setHotImgParameter(List<AdBean> list) {
        bannerViewHolder.setHotImgParameter(list);
    }

}
