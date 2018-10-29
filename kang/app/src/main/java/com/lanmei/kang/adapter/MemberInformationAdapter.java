package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.TeamBean;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 会员信息
 */
public class MemberInformationAdapter extends SwipeRefreshAdapter<String> {

    private TeamBean bean;

    public MemberInformationAdapter(Context context, TeamBean bean) {
        super(context);
        this.bean = bean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_member_information, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        String s = getItem(position);
        switch (position){
            case 0:
                s = s+bean.getReg_time();//加入 时间
                break;
            case 1:
                s = s+0;//推广人数
                break;
            case 2:
                s = s+bean.getPhone();//联系电话
                break;
            case 3:
                s = s+ (StringUtils.isEmpty(bean.getAddress())?"":bean.getAddress());//地址
                break;
        }
        viewHolder.informationTv.setText(s);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.information_tv)
        TextView informationTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
