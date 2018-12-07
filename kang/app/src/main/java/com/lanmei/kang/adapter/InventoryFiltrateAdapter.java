package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.MerchantTabClassifyBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Inventory(下拉筛选)
 */
public class InventoryFiltrateAdapter extends SwipeRefreshAdapter<MerchantTabClassifyBean> {


    public InventoryFiltrateAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_inventory_filtrate, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final MerchantTabClassifyBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bean.isSelect()) {
                    List<MerchantTabClassifyBean> list = getData();
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        MerchantTabClassifyBean examinationTopicBean = list.get(i);
                        if (examinationTopicBean.isSelect()) {
                            examinationTopicBean.setSelect(false);
                        }
                    }
                    bean.setSelect(!bean.isSelect());
                    notifyDataSetChanged();
                }
                if (inventoryFiltrateListener != null) {
                    inventoryFiltrateListener.onFiltrate(bean);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.name_tv)
        TextView nameTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(MerchantTabClassifyBean bean) {
            nameTv.setTextColor(bean.isSelect() ? context.getResources().getColor(R.color.color0066B3) : context.getResources().getColor(R.color.color666));
            nameTv.setText(bean.getClassname());
        }
    }

    private InventoryFiltrateListener inventoryFiltrateListener;

    public void setInventoryFiltrateListener(InventoryFiltrateListener inventoryFiltrateListener) {
        this.inventoryFiltrateListener = inventoryFiltrateListener;
    }

    public interface InventoryFiltrateListener {
        void onFiltrate(MerchantTabClassifyBean bean);
    }

}
