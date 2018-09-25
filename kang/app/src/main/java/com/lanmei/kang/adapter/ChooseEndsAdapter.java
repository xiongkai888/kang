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
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 选择场地
 */
public class ChooseEndsAdapter extends SwipeRefreshAdapter<MerchantListBean> {


    public ChooseEndsAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_choose_ends, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final MerchantListBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        ImageHelper.load(context, bean.getPics(), viewHolder.mUserHeadIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
        viewHolder.mNameTv.setText(bean.getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChoosePlaceListener != null){
                    mChoosePlaceListener.onClickChoosePlace(bean.getName());
                }
//                IntentUtil.startActivity(context, MerchantIntroduceActivity.class);
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.user_head_iv)
        ImageView mUserHeadIv;
        @InjectView(R.id.name_tv)
        TextView mNameTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    public interface ChoosePlaceListener {//选择场地监听
        void onClickChoosePlace(String place);
    }

    ChoosePlaceListener mChoosePlaceListener;

    public void setChoosePlaceListener(ChoosePlaceListener chooseplacelistener){
        mChoosePlaceListener = chooseplacelistener;
    }

}
