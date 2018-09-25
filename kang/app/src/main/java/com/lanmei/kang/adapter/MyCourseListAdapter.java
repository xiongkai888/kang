package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.MyCourseBean;
import com.lanmei.kang.bean.NewsCategoryListBean;
import com.lanmei.kang.ui.details.CourseDetailsActivity;
import com.lanmei.kang.util.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 我的课程、我的活动公用
 */
public class MyCourseListAdapter extends SwipeRefreshAdapter<MyCourseBean> {

    FormatTime time = new FormatTime();

    public MyCourseListAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_course, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;
        final MyCourseBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        time.setTime(bean.getAddtime());
        viewHolder.mTimeTv.setText("报名时间：" + time.formatterTime());
        viewHolder.mItemTv.setText(bean.getTitle());
        viewHolder.mLabelTv.setText(bean.getLabel());
        String url = getUrl(bean.getFile());
        ImageHelper.load(context, url, viewHolder.mImageView, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DataLoader.getInstance().getNewsCategoryName(bean.ge)
                startCourseDetailsActivity(bean);
            }
        });
    }

    private void startCourseDetailsActivity(MyCourseBean bean) {
        NewsCategoryListBean newsBean = new NewsCategoryListBean();
//        newsBean.setContent(bean.getContent());
//        newsBean.setFavoured(bean.getFavoured());
//        newsBean.setAddtime(bean.getAddtime());
//        newsBean.setTitle(bean.getTitle());
//        newsBean.setId(bean.getPost_id());
//        newsBean.setShowApply(true);//不显示底部报名
        CourseDetailsActivity.startActivityCourseDetails(context, newsBean);
    }


    private String getUrl(List<String> list) {
        String url = "";
        if (list == null || list.size() == 0) {
            return url;
        }
        return list.get(0);//取第一张图片
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.item_name_tv)
        TextView mItemTv;
        @InjectView(R.id.time_tv)
        TextView mTimeTv;
        @InjectView(R.id.label_tv)
        TextView mLabelTv;
        @InjectView(R.id.pic_iv)
        ImageView mImageView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
