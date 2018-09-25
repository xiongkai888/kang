package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.lanmei.kang.R;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 搜索位置信息列表
 */
public class SearchPositionAdapter extends SwipeRefreshAdapter<PoiInfo> {


    public SearchPositionAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search_position, parent, false));
    }

    @Override
    public void onBindViewHolder2(final RecyclerView.ViewHolder holder, int position) {
        final PoiInfo info = getItem(position);
        if (info == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.mAddressNameTv.setText(info.name);
        viewHolder.mAddressDetailsTv.setText(info.address);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.getAddress(info.name);
                }
            }
        });
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.address_name_tv)
        TextView mAddressNameTv;//地址名字
        @InjectView(R.id.address_details_tv)
        TextView mAddressDetailsTv;//地址详情

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    SearchPositionListener mListener;

    public void setSearchPositionListener(SearchPositionListener l){
        mListener = l;
    }

    public interface SearchPositionListener{
        void getAddress(String address);
    }

}
