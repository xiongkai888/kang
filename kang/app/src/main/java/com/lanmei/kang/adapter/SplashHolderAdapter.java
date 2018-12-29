package com.lanmei.kang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.lanmei.kang.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 引导页
 */
public class SplashHolderAdapter implements Holder<Integer> {

    @InjectView(R.id.banner_img)
    ImageView bannerImg;

    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner_img, null);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, Integer data) {
        bannerImg.setImageResource(data);
    }
}
