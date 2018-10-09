package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.FriendsRankBean;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.widget.CircleImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 排名(别人的)
 */
public class FriendsRankAdapter extends SwipeRefreshAdapter<FriendsRankBean> {


    public FriendsRankAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_friends_rank, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        FriendsRankBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        int rank = position + 1;
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.userNameTv.setText(bean.getNickname());
        if (rank > 3) {
            viewHolder.rankingTv.setBackgroundResource(R.drawable.circle_colorfe6);
            viewHolder.rankingTv.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            viewHolder.rankingTv.setBackgroundResource(R.drawable.circle_colorfe3);
            viewHolder.rankingTv.setTextColor(context.getResources().getColor(R.color.white));
        }
        viewHolder.rankingTv.setText(rank + "");
        viewHolder.distanceTotalTv.setText(bean.getDistance_total() + "km");
        ImageHelper.load(context, bean.getPic(), viewHolder.headIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        @InjectView(R.id.ranking_tv)
        TextView rankingTv;
        @InjectView(R.id.head_iv)
        CircleImageView headIv;
        @InjectView(R.id.user_name_tv)
        TextView userNameTv;
        @InjectView(R.id.distance_total_tv)
        TextView distanceTotalTv;


        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

}
