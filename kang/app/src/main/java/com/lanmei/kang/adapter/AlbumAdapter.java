package com.lanmei.kang.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 * 动态九宫格Adapter
 */
public class AlbumAdapter extends SwipeRefreshAdapter<AlbumBean> {

    String[] arry;
    String idStr = "";//要删除的相片id(多张相片id用逗号隔开)

    public AlbumAdapter(Context context) {
        super(context);
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_album, parent, false));
    }

    public void setStringArry(String[] arry) {
        this.arry = arry;
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, final int position) {
        final AlbumBean bean = getItem(position);
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
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arry != null) {
                    CommonUtils.showPhotoBrowserActivity(context, arry, arry[position]);
                }
            }
        });
        viewHolder.mCrossIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bean.isPicker()) {//服务器上的相片
                    idStr += bean.getId() + ",";
                }
                //                UIHelper.ToastMessage(context, "前：" + getData().size());
                getData().remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
                //                UIHelper.ToastMessage(context, "后：" + getData().size());
                arry = CommonUtils.getStringArry(getData());
                if (mAlbumListener != null) {
                    mAlbumListener.deleteAlbums(idStr);
                }
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

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

}
