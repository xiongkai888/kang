package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.AlbumBean;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 *
 */
public class AlbumOtherAdapter extends SwipeRefreshAdapter<AlbumBean>{

    String[] arry;

    public AlbumOtherAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_imageview2, parent, false));
    }

    public void setStringArry(String[] arry){
        this.arry = arry;
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder,final int position) {
        AlbumBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        ImageHelper.load(context,bean.getPic(),viewHolder.imageView,null,true,R.mipmap.default_pic,R.mipmap.default_pic);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arry != null){
                    CommonUtils.showPhotoBrowserActivity(context,arry,arry[position]);
                }
            }
        });
    }
    static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.item_image)
        ImageView imageView;
        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

}
