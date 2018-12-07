package com.lanmei.kang.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.AdBean;
import com.lanmei.kang.bean.MerchantTabClassifyBean;
import com.lanmei.kang.ui.merchant_tab.activity.GoodsListActivity;
import com.lanmei.kang.ui.merchant_tab.goods.activity.GoodsDetailsActivity;
import com.lanmei.kang.ui.news.activity.NewsDetailsActivity;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 用户端-商家tab-热门活动
 */
public class MerchantTabHotImgAdapter extends SwipeRefreshAdapter<AdBean> {

    public MerchantTabHotImgAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_merchant_tab_hot_img, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final AdBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        ImageHelper.load(context,bean.getPic(),viewHolder.picIv,null,true,R.mipmap.default_pic,R.mipmap.default_pic);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = bean.getLink();
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
                        IntentUtil.startActivity(context, GoodsDetailsActivity.class,strings[1]);
                    }
                } else if (link.startsWith("p")) {
                    String[] strings = link.split("_");
                    if (!StringUtils.isEmpty(strings) && strings.length == 2) {
                        IntentUtil.startActivity(context, NewsDetailsActivity.class,strings[1]);
                    }
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.pic_iv)
        ImageView picIv;
        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
