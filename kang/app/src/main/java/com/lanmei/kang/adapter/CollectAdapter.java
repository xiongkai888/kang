package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.NewsCategoryListBean;
import com.lanmei.kang.ui.news.activity.NewsDetailsActivity;
import com.lanmei.kang.util.FormatTime;
import com.lanmei.kang.widget.MyGridView;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.IntentUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 我的收藏
 */
public class CollectAdapter extends SwipeRefreshAdapter<NewsCategoryListBean> {

    private FormatTime time;

    public CollectAdapter(Context context) {
        super(context);
        time = new FormatTime(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news_tab, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;
        final NewsCategoryListBean bean = getItem(position);
        if (bean == null) {
            return;
        }

        DetailsItemImageAdapter adapter = new DetailsItemImageAdapter(context, bean.getFile());
        viewHolder.mGridView.setAdapter(adapter);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context,NewsDetailsActivity.class);
            }
        });
        time.setTime(bean.getAddtime());
        viewHolder.mTimeTv.setText(time.getFormatTime());
        viewHolder.mTitleTv.setText(bean.getTitle());
//        viewHolder.mCnameTv.setText(bean.getCname());
        viewHolder.mCommentTv.setText(bean.getReviews() + " 条评论");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.title_tv)
        TextView mTitleTv;
        @InjectView(R.id.cname_tv)
        TextView mCnameTv;//
        @InjectView(R.id.time_tv)
        TextView mTimeTv;//
        @InjectView(R.id.comment_tv)
        TextView mCommentTv;//评论数
        @InjectView(R.id.gridView)
        MyGridView mGridView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
