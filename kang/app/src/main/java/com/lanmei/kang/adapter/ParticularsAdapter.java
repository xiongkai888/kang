package com.lanmei.kang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.ParticularsBean;

import java.util.List;

/**
 * Created by xkai on 2017/7/5.
 * 明细列表
 */

public class ParticularsAdapter extends BaseAdapter {

    private Context mContext;
    private List<ParticularsBean> mList;
    private LayoutInflater inflater;

    public ParticularsAdapter(Context context, List<ParticularsBean> list) {
        mList = list;
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ParticularsBean getItem(int position) {
        if (mList == null) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ParticularsBean bean = getItem(position);
        convertView = inflater.inflate(R.layout.item_particulars, parent, false);
        TextView item = (TextView)convertView.findViewById(R.id.item_tv);
        TextView price = (TextView)convertView.findViewById(R.id.price_tv);
        TextView people = (TextView)convertView.findViewById(R.id.people_num_tv);
        TextView hourNum = (TextView)convertView.findViewById(R.id.hourNum_tv);
        TextView total = (TextView)convertView.findViewById(R.id.total_tv);
        item.setText(bean.getItem());
        price.setText(bean.getPrice());
        people.setText(bean.getNum());
        hourNum.setText(bean.getTime());
        total.setText(bean.getTotal());
        return convertView;
    }
}
