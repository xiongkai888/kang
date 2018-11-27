package com.lanmei.kang.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.lanmei.kang.R;
import com.lanmei.kang.bean.AdBean;
import com.xson.common.helper.ImageHelper;
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
                if (StringUtils.isEmpty(data.getUrl()) || !data.getUrl().startsWith("http")) {
                    return;
                }
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.getUrl())));
            }
        });
    }
}
