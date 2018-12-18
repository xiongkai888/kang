package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.GoodsCommentBean;
import com.lanmei.kang.util.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CircleImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 商品详情评论
 */
public class GoodsCommentAdapter extends SwipeRefreshAdapter<GoodsCommentBean> {

    FormatTime time;
    boolean isOnly;

    public GoodsCommentAdapter(Context context) {
        super(context);
        time = new FormatTime(context);
        time.setApplyToTimeYearMonthDay();
    }

    //设置只有一个item
    public void setOnlyItem(boolean isOnly) {
        this.isOnly = isOnly;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment_goods, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, final int position) {
        GoodsCommentBean bean = getItem(position);
        if (StringUtils.isEmpty(bean)) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.head_iv)
        CircleImageView headIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.ratingbar)
        RatingBar ratingBar;
        @InjectView(R.id.content_tv)
        TextView contentTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(GoodsCommentBean bean) {
            ImageHelper.load(context, bean.getUser_pic(), headIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
            nameTv.setText(bean.getUsername());
            time.setTime(bean.getComment_time());
            timeTv.setText(time.formatterTime());
            contentTv.setText(bean.getContents());
//            float point = Float.valueOf(StringUtils.isEmpty(bean.getPoint())?0:Float.valueOf(bean.getPoint()));
            float point = 3;
            ratingBar.setRating(point);
        }

    }

    @Override
    public int getCount() {
        if (isOnly && super.getCount() > 1) {
            return 1;
        }
        return super.getCount();
    }
}
