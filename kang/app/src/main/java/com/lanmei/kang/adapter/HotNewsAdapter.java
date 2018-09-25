package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.NewsCategoryListBean;
import com.lanmei.kang.util.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;


/**
 * 热点新闻
 */
public class HotNewsAdapter extends SwipeRefreshAdapter<NewsCategoryListBean> {

    private final Context context;
    FormatTime mTime = new FormatTime();

    public HotNewsAdapter(Context context) {
        super(context);
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
//        final NewsCategoryListBean bean = getItem(position);
//        if (bean == null){
//            return;
//        }
//        //这里写列表内容
//        viewHolder.mTitleTv.setText(bean.getTitle());
//        viewHolder.mLabelTv.setText(bean.getLabel());
//        mTime.setTime(bean.getAddtime());
//        viewHolder.mTimeTv.setText(mTime.getFormatTime());
//        List<String> file = bean.getFile();
//        if (file != null && file.size()>0){
//            ImageHelper.load(context, file.get(0), viewHolder.mPicIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
//        }
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String cName = bean.getCname();
//                if (Constant.TUAN_TI.equals(cName) || Constant.SI_REN.equals(cName)|| Constant.ZAI_XIAN.equals(cName)|| Constant.GAO_DUAN.equals(cName)|| Constant.SHANG_WU.equals(cName)|| Constant.JU_HUI.equals(cName)){
//                    CourseDetailsActivity.startActivityCourseDetails(context,bean);
//                }else {
//                    NewsDetailsActivity.startActivityNewsDetails(context, bean);
//                }
//            }
//        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

//        @InjectView(R.id.pic_iv)
//        ImageView mPicIv;
//        @InjectView(R.id.title_tv)
//        TextView mTitleTv;//标题
//        @InjectView(R.id.time_tv)
//        TextView mTimeTv;//时间
//        @InjectView(R.id.label_tv)
//        TextView mLabelTv;//标签

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
