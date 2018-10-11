package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.MerchantListBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 销售列表
 */
public class GoodsSellListAdapter extends SwipeRefreshAdapter<MerchantListBean> {


    public GoodsSellListAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_goods_sell_list, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
//        final MerchantListBean bean = getItem(position);
//        if (bean == null) {
//            return;
//        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(null);
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IntentUtil.startActivity(context, MerchantIntroduceActivity.class, bean.getUid());
//            }
//        });
    }


    @Override
    public int getCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.ll_items)
        LinearLayout layout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(MerchantListBean bean) {
            layout.removeAllViews();
            int size = 2;
            for (int i = 0; i < size; i++) {
                addView(i, size);
            }

        }

        private void addView(int position, int size) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_goods_sell_list_sub, null);
            if (position == size - 1) {
                view.findViewById(R.id.line_tv).setVisibility(View.GONE);
            }
            layout.addView(view);
        }

    }
}
