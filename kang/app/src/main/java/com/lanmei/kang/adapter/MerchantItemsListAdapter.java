package com.lanmei.kang.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.MerchantItemsListBean;
import com.lanmei.kang.ui.merchant.activity.ItemCompileActivity;
import com.lanmei.kang.util.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 商家产品列表
 */
public class MerchantItemsListAdapter extends SwipeRefreshAdapter<MerchantItemsListBean> {


    private FormatTime time;
    private String pid;

    public MerchantItemsListAdapter(Context context) {
        super(context);
        time = new FormatTime(context);
    }

    public void setPid(String pid){
        this.pid = pid;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_merchant_item, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final MerchantItemsListBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean",bean);
                bundle.putString("pid",pid);
                IntentUtil.startActivity(context,ItemCompileActivity.class,bundle);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.pic_iv)
        ImageView picIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.price_tv)
        TextView priceTv;
        @InjectView(R.id.status_tv)
        TextView statusTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(MerchantItemsListBean bean) {
            ImageHelper.load(context,bean.getImg(),picIv,null,true,R.mipmap.default_pic,R.mipmap.default_pic);
            nameTv.setText(bean.getName());
            priceTv.setText(String.format(context.getString(R.string.price),bean.getSell_price()));
            switch (bean.getStatus()){
                case "0"://审核中
                    statusTv.setText(R.string.auditing);
                    break;
                case "1"://已审核
                    statusTv.setText(R.string.audited);
                    break;
                case "2"://拒绝
                    statusTv.setText(R.string.reject);
                    break;
            }
        }

    }
}
