package com.lanmei.kang.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.AdBean;
import com.lanmei.kang.bean.DJCouponBean;
import com.lanmei.kang.bean.MerchantTabClassifyBean;
import com.lanmei.kang.ui.merchant_tab.activity.GoodsListActivity;
import com.lanmei.kang.ui.merchant_tab.goods.activity.GoodsDetailsActivity;
import com.lanmei.kang.ui.news.activity.NewsDetailsActivity;
import com.lanmei.kang.util.AKDialog;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 首页图片轮播适配器(有地址就跳转)
 */
public class MerchantTabAdAdapter implements Holder<AdBean> {

    @InjectView(R.id.banner_img)
    ImageView bannerImg;

    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner_img, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void UpdateUI(final Context context, final int position, final AdBean data) {
        if (data == null) {
            return;
        }
        ImageHelper.load(context, data.getPic(), bannerImg, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
        bannerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = data.getLink();
                if (StringUtils.isEmpty(link)) {
                    return;
                }
                if (link.startsWith("http")) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                } else if (link.startsWith("c")) {
                    String[] strings = link.split("_");
                    if (!StringUtils.isEmpty(strings) && strings.length == 2) {
                        Bundle bundle = new Bundle();
                        MerchantTabClassifyBean bean = new MerchantTabClassifyBean();
                        bean.setId(strings[1]);
                        bean.setClassname("商品列表");
                        bundle.putSerializable("bean", bean);
                        IntentUtil.startActivity(context, GoodsListActivity.class, bundle);
                    }
                } else if (link.startsWith("g")) {
                    String[] strings = link.split("_");
                    if (!StringUtils.isEmpty(strings) && strings.length == 2) {
                        IntentUtil.startActivity(context, GoodsDetailsActivity.class, strings[1]);
                    }
                } else if (link.startsWith("p")) {
                    String[] strings = link.split("_");
                    if (!StringUtils.isEmpty(strings) && strings.length == 2) {
                        IntentUtil.startActivity(context, NewsDetailsActivity.class, strings[1]);
                    }
                } else if (link.startsWith("l")) {
                    if (!CommonUtils.isLogin(context)) {
                        return;
                    }
                    String[] strings = link.split("_");
                    if (!StringUtils.isEmpty(strings) && strings.length == 2) {
                        loadDjCoupon(context, strings[1]);
                    }
                }
            }
        });
    }

    private void loadDjCoupon(final Context context, final String couponId) {
        KangQiMeiApi api = new KangQiMeiApi("app/dj_coupon");//点击领取
        api.add("uid", api.getUserId(context));
        api.add("coupon_id", couponId);
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<DJCouponBean>>() {
            @Override
            public void onResponse(DataBean<DJCouponBean> response) {
                if (((BaseActivity) context).isFinishing()) {
                    return;
                }
                DJCouponBean bean = response.data;
                if (StringUtils.isEmpty(bean)) {
                    return;
                }
                AKDialog.showCouponDialog(context, bean);
            }
        });
    }

}
