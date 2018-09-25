package com.lanmei.kang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.RechargeSubBean;

import java.util.List;

/**
 * Created by xkai on 2017/7/5.
 * 充值金额
 */

public class RechargeAdapter extends BaseAdapter {

    private Context mContext;
    private List<RechargeSubBean> list;
    private LayoutInflater inflater;

    public RechargeAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<RechargeSubBean> list) {
        this.list = list;
    }

    public List<RechargeSubBean> getData() {
        return list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public RechargeSubBean getItem(int position) {
        if (list == null) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_recharge, parent, false);
            holder = new ItemHolder();
            holder.textView = (TextView) convertView;
            convertView.setTag(holder);
        } else {
            holder = (ItemHolder) convertView.getTag();
        }
        holder.textView.setText(getItem(position).getMoney());
        if (getItem(position).isSelected()){
            holder.textView.setBackground(mContext.getResources().getDrawable(R.drawable.item_recharge_checked));
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.white));
        }else {
            holder.textView.setBackground(mContext.getResources().getDrawable(R.drawable.item_recharge_unchecked));
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        }
//        holder.textView.setSelected(getItem(position).isSelected());
        return convertView;
    }

    public static class ItemHolder {
        TextView textView;
    }

}
