package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.ClientValuateBean;
import com.lanmei.kang.util.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 *客户评价
 */
public class ClientValuateAdapter extends SwipeRefreshAdapter<ClientValuateBean> {

    private final Context context;
    private FormatTime time;
    public ClientValuateAdapter(Context context) {
        super(context);
        this.context = context;
        time = new FormatTime();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comm, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        ClientValuateBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder)holder;
        String addTime = bean.getAddtime();
        time.setTime(addTime);
        viewHolder.mTimeTv.setText(time.getFormatTime());
        viewHolder.mNameTv.setText(bean.getNickname());
        viewHolder.mContentTv.setText(bean.getContent());
        ImageHelper.load(context,bean.getPic(),viewHolder.mHeadIv,null,true,R.mipmap.default_pic,R.mipmap.default_pic);
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        @InjectView(R.id.name_tv)
        TextView mNameTv;
        @InjectView(R.id.time_tv)
        TextView mTimeTv;
        @InjectView(R.id.content_tv)
        TextView mContentTv;
        @InjectView(R.id.user_head_iv)
        ImageView mHeadIv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
