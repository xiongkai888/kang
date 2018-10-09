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
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.FormatTime;
import com.lanmei.kang.widget.SudokuView;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 资讯列表
 */
public class NewsItemAdapter extends SwipeRefreshAdapter<NewsCategoryListBean> {


    FormatTime time;

    public NewsItemAdapter(Context context) {
        super(context);
        time = new FormatTime();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news_tab, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final NewsCategoryListBean bean = getItem(position);
        if (StringUtils.isEmpty(bean)) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context, NewsDetailsActivity.class, bean.getId());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.title_tv)
        TextView titleTv;
        @InjectView(R.id.sudokuView)
        SudokuView sudokuView;
        @InjectView(R.id.cname_tv)
        TextView cnameTv;
        @InjectView(R.id.comment_tv)
        TextView commentTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(final NewsCategoryListBean bean) {
            titleTv.setText(bean.getTitle());
            cnameTv.setText(bean.getName());
            commentTv.setText(String.format(context.getString(R.string.comment_num), bean.getReviews()));
            time.setTime(bean.getAddtime());
            timeTv.setText(time.getFormatTime());
            sudokuView.setListData(bean.getFile());
            sudokuView.setOnSingleClickListener(new SudokuView.SudokuViewClickListener() {
                @Override
                public void onClick(int positionSub) {
                    IntentUtil.startActivity(context, NewsDetailsActivity.class, bean.getId());
                }

                @Override
                public void onDoubleTap(int position) {
                    CommonUtils.startPhotoBrowserActivity(context, CommonUtils.getStringArr(bean.getFile()), position);
                }
            });
        }
    }
}
