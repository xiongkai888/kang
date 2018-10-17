package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.ChuKuListBean;
import com.lanmei.kang.util.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 出库（入库）列表
 */
public class ChuKuListAdapter extends SwipeRefreshAdapter<ChuKuListBean> {

    private FormatTime formatTime;

    public ChuKuListAdapter(Context context) {
        super(context);
        formatTime = new FormatTime(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chu_ku_list, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        ChuKuListBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IntentUtil.startActivity(context, MerchantIntroduceActivity.class, bean.getUid());
//            }
//        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.barcode_tv)
        TextView barcodeTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.goodsname_tv)
        TextView goodsnameTv;
        @InjectView(R.id.number_tv)
        TextView numberTv;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(ChuKuListBean bean) {
            barcodeTv.setText(String.format(context.getString(R.string.barcode),bean.getBarcode()));
            goodsnameTv.setText(String.format(context.getString(R.string.goodsname),bean.getGoodsname()));
            numberTv.setText(String.format(context.getString(R.string.number),bean.getNumber()));
            if (!StringUtils.isEmpty(bean.getAddtime())){
                formatTime.setTime(bean.getAddtime());
                timeTv.setText(formatTime.formatterTime());
            }
        }
    }
}
