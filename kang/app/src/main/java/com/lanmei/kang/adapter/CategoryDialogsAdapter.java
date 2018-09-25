package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.CategoryBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 分类（底部弹框）
 */
public class CategoryDialogsAdapter extends SwipeRefreshAdapter<CategoryBean> {


    public CategoryDialogsAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category_dialogs, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final CategoryBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CategoryBean> list = getData();
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    CategoryBean categoryBean = getItem(i);
                    categoryBean.setPitch(false);
                }
                bean.setPitch(true);
                notifyDataSetChanged();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.choose_img)
        ImageView chooseImg;
        @InjectView(R.id.num_tv)
        TextView numTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(CategoryBean bean) {
            if (bean.isPitch()){
                chooseImg.setImageResource(R.mipmap.pay_on);
            }else {
                chooseImg.setImageResource(R.mipmap.pay_off);
            }
            nameTv.setText(bean.getName());
            numTv.setText(String.format(context.getString(R.string.goods_num), bean.getCount()));
        }
    }
}
