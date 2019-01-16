package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.helper.coupon.BeanCoupon;
import com.lanmei.kang.util.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 优惠券
 */
public class CouponTabAdapter extends SwipeRefreshAdapter<BeanCoupon> {

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
        BeanCoupon bean = getItem(position);
        if (StringUtils.isEmpty(bean)){
            return;
        }
        ViewHolder viewHolder = (ViewHolder)holder;
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

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);

        }
        public void setParameter(BeanCoupon bean) {
            nameTv.setText(bean.getName());
            valueTv.setText(String.format(context.getString(R.string.price),String.valueOf(bean.getMoney())));
            usePriceTv.setText(String.format(context.getString(R.string.full_available),String.valueOf(bean.getConsume())));
            timeTv.setText(String.format(context.getString(R.string.period_validity),time.formatterTime(bean.getStarttime()+""),time.formatterTime(bean.getEndtime()+"")));
        }
    }
}
