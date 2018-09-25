package com.lanmei.kang.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.ItemsCompileBean;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 商家产品添加或编辑 的图片（商家图片，商家详情图片）
 */
public class ItemsCompileAdapter extends SwipeRefreshAdapter<ItemsCompileBean> {

    String[] arry;
    int type;


    public ItemsCompileAdapter(Context context, int type) {
        super(context);
        this.type = type;
        arry = CommonUtils.getItemsCompileStringArry(getData());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_items_compile_img, parent, false));
    }

    public void setStringArry(List<ItemsCompileBean> list) {
        this.arry = CommonUtils.getItemsCompileStringArry(list);
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, final int position) {
        final ItemsCompileBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        boolean isPicker = bean.isPicker();
        if (isPicker) {
            Bitmap bmp = BitmapFactory.decodeFile(bean.getPic());
            viewHolder.mPicIv.setImageBitmap(bmp);
        } else {
            ImageHelper.load(context, bean.getPic(), viewHolder.mPicIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
        }
        if (type == 1) {
            if (bean.isCover()) {//
                viewHolder.coverTv.setVisibility(View.VISIBLE);
                viewHolder.mCrossIv.setVisibility(View.GONE);
            } else {
                viewHolder.coverTv.setVisibility(View.GONE);
                viewHolder.mCrossIv.setVisibility(View.VISIBLE);
            }
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 1) {
                    if (bean.isCover()) {//
                        return;
                    } else {
                        List<ItemsCompileBean> list = getData();
                        int size = list.size();
                        for (int i = 0; i < size; i++) {
                            ItemsCompileBean bean1 = list.get(i);
                            bean1.setCover(false);
                        }
                        bean.setCover(true);
                        notifyDataSetChanged();
                    }
                    return;
                }
                if (arry != null) {
                    CommonUtils.showPhotoBrowserActivity(context, arry, arry[position]);
                }
            }
        });
        viewHolder.mCrossIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                UIHelper.ToastMessage(context, "前：" + getData().size());
                getData().remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
                //                UIHelper.ToastMessage(context, "后：" + getData().size());
                arry = CommonUtils.getItemsCompileStringArry(getData());
            }
        });
    }

    public interface AlbumListener {
        void deleteAlbums(String ids);//
    }

    AlbumListener mAlbumListener;

    public void setAlbumListener(AlbumListener l) {
        mAlbumListener = l;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.pic_iv)
        ImageView mPicIv;
        @InjectView(R.id.cross_iv)
        ImageView mCrossIv;//删除图标
        @InjectView(R.id.cover_tv)
        TextView coverTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

}
