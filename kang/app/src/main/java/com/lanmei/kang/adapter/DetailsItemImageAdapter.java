package com.lanmei.kang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lanmei.kang.R;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.helper.ImageHelper;

import java.util.List;


/**
 *详情中的gridView 的Adapter
 */
public class DetailsItemImageAdapter extends BaseAdapter {

    Context mContext;
    List<String> mList;
    String[] arry;

    public DetailsItemImageAdapter(Context context, List<String> list){
        mContext = context;
        mList = list;
        arry = CommonUtils.toArray(mList);
    }

    @Override
    public int getCount() {
        return mList == null? 0: mList.size();
    }

    @Override
    public String getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_details_imageview, parent, false);
        ImageView imageView =  (ImageView) convertView.findViewById(R.id.iv_item_image);
        ImageHelper.load(mContext, getItem(position), imageView, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.showPhotoBrowserActivity(mContext,arry,arry[position]);
            }
        });
        return convertView;

    }
}
