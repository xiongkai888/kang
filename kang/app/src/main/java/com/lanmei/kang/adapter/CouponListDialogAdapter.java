package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.helper.coupon.BeanCoupon;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 确认订单 选择优惠券
 */
public class CouponListDialogAdapter extends SwipeRefreshAdapter<BeanCoupon> {


    public CouponListDialogAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_choose_coupon_list, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final BeanCoupon bean = getItem(position);
        if (StringUtils.isEmpty(bean)) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.isChoose()) {
                    return;
                }
                for (int i = 0; i < getItemCount(); i++) {
                    getData().get(i).setChoose(false);
                }
                bean.setChoose(true);
                notifyDataSetChanged();
                if (listener != null){
                    listener.chooseCoupon(bean);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.coupon_tv)
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);

        }

        public void setParameter(BeanCoupon bean) {
            textView.setText(bean.getLname());
            CommonUtils.setCompoundDrawables(context, textView, bean.isChoose() ? R.mipmap.pay_on : R.mipmap.pay_off, 0, 2);
        }
    }

    private ChooseCouponListener listener;

    public void setChooseCouponListener(ChooseCouponListener listener) {
        this.listener = listener;
    }

    public interface ChooseCouponListener{
        void chooseCoupon(BeanCoupon coupon);
    }

}
