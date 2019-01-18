package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.CouponSubBean;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.FormatTime;
import com.lanmei.kang.view.DashedLineView;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 优惠券
 */
public class CouponTabAdapter extends SwipeRefreshAdapter<CouponSubBean> {

    private FormatTime time;

    public CouponTabAdapter(Context context) {
        super(context);
        time = new FormatTime(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_coupon, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        CouponSubBean bean = getItem(position);
        if (StringUtils.isEmpty(bean)) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.value_tv)
        TextView valueTv;
        @InjectView(R.id.use_price_tv)
        TextView usePriceTv;
        @InjectView(R.id.dashed_line)
        DashedLineView dashedLine;
        @InjectView(R.id.ll_money_bg)
        LinearLayout llMoneyBg;
        @InjectView(R.id.ll_bg)
        LinearLayout llBg;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);

        }

        public void setParameter(CouponSubBean bean) {
            if (StringUtils.isSame(bean.getState(), CommonUtils.isOne)) {//未使用
                dashedLine.setColor(R.color.colorFF7676);
                llBg.setBackground(context.getResources().getDrawable(R.drawable.border_coupon));
                llMoneyBg.setBackground(context.getResources().getDrawable(R.drawable.button_corners_4_radius_ff7676));
            } else {//已使用或者已过期
                dashedLine.setColor(R.color.color666);
                llBg.setBackground(context.getResources().getDrawable(R.drawable.border_coupon_sub));
                llMoneyBg.setBackground(context.getResources().getDrawable(R.drawable.button_corners_4_radius_666));
            }
            nameTv.setText(bean.getLname());
            valueTv.setText(String.format(context.getString(R.string.price), String.valueOf(bean.getMoney())));
            usePriceTv.setText(String.format(context.getString(R.string.full_available), String.valueOf(bean.getConsume())));
            timeTv.setText(String.format(context.getString(R.string.period_validity), time.formatterTime(bean.getStarttime() + ""), time.formatterTime(bean.getEndtime() + "")));
        }
    }
}
