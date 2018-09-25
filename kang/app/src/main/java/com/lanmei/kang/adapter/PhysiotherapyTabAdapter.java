package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.MerchantListBean;
import com.lanmei.kang.ui.merchant.activity.MerchantIntroduceActivity;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.DoubleUtil;
import com.xson.common.utils.IntentUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 *理疗界面（中医、西医、中西、高端理疗）或推荐商家的列表
 */
public class PhysiotherapyTabAdapter extends SwipeRefreshAdapter<MerchantListBean> {


    public PhysiotherapyTabAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final MerchantListBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context, MerchantIntroduceActivity.class, bean.getUid());
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

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

        public void setParameter(MerchantListBean bean){
            nameTv.setText(bean.getName());//商家名称
            priceTv.setText(String.format(context.getString(R.string.price),bean.getMoney()));//价格
            distanceTv.setText(DoubleUtil.formatDistance(bean.getDistance()) + "  " + bean.getAddress());//商家地址
            labelTv.setText(bean.getArea());//商家地区
            ImageHelper.load(context, bean.getFee_introduction(), picIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
        }
    }
}
