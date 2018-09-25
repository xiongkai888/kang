package com.lanmei.kang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lanmei.kang.R;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.view.DoubleScaleImageView;
import com.xson.common.helper.ImageHelper;

import java.util.List;


/**
 *
 */
public class ItemImageAdapter extends BaseAdapter {

    Context context;
    List<String> mList;
    String[] arry;

    public ItemImageAdapter(Context context, List<String> list) {
        this.context = context;
        mList = list;
        arry = CommonUtils.getStringArr(mList);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
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
        convertView = LayoutInflater.from(context).inflate(R.layout.item_imageview, parent, false);
        DoubleScaleImageView imageView = (DoubleScaleImageView) convertView.findViewById(R.id.iv_item_image);
        ImageHelper.load(context, getItem(position), imageView, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
        imageView.setDoubleClickListener(new DoubleScaleImageView.DoubleClickListener() {
            @Override
            public void onSingleTapConfirmed() {
                if (l != null){
                    l.onClick(position);
                }
            }

            @Override
            public void onDoubleTap() {
                if (l != null){
                    l.onDoubleTap(position);
                }
            }
        });
        return convertView;

    }

    SingleTapConfirmedListener l;//单击点击

    public interface SingleTapConfirmedListener{
       void onClick(int position);
       void onDoubleTap(int position);
    }

    public void setOnClickListener(SingleTapConfirmedListener l){
        this.l = l;
    }

}
