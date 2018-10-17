package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.NewsCommentBean;
import com.lanmei.kang.bean.NewsDetailsBean;
import com.lanmei.kang.event.CollectNewsEvent;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.FormatTime;
import com.lanmei.kang.util.WebViewPhotoBrowserUtil;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 资讯详情评论
 */
public class NewsDetailsCommentAdapter extends SwipeRefreshAdapter<NewsCommentBean> {

    final public static int TYPE_BANNER = 100;
    BannerViewHolder bannerHolder;
    NewsDetailsBean detailsBean;
    FormatTime time;

    public NewsDetailsCommentAdapter(Context context) {
        super(context);
        time = new FormatTime(context);
    }


    public void setNewsDetailsBean(NewsDetailsBean detailsBean) {
        this.detailsBean = detailsBean;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        if (viewType == TYPE_BANNER) { // banner
            bannerHolder = new BannerViewHolder(LayoutInflater.from(context).inflate(R.layout.head_news_details, parent, false));
            return bannerHolder;
        }
        // 列表  item_home_list
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comm, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_BANNER) {
            onBindBannerViewHolder(holder, position); // banner
            return;
        }
        NewsCommentBean bean = getItem(position - 1);
        if (StringUtils.isEmpty(bean)) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    @Override
    public int getItemViewType2(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        }
        return super.getItemViewType2(position);
    }


    // 列表item
    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.user_head_iv)
        CircleImageView userHeadIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.content_tv)
        TextView contentTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(NewsCommentBean bean) {
            ImageHelper.load(context,bean.getPic(),userHeadIv,null,true,R.mipmap.default_pic,R.mipmap.default_pic);
            nameTv.setText(bean.getNickname());
            time.setTime(bean.getAddtime());
            timeTv.setText(time.getFormatTime());
            contentTv.setText(bean.getContent());
        }

    }

    public void onBindBannerViewHolder(RecyclerView.ViewHolder holder, int position) {
        final BannerViewHolder viewHolder = (BannerViewHolder) holder;
        if (detailsBean == null) {
            return;
        }
        viewHolder.mTitleTv.setText(detailsBean.getTitle());
        time.setTime(detailsBean.getAddtime());
        viewHolder.mCommentTv.setText(String.format(context.getString(R.string.reviews_num), detailsBean.getReviews()));
        viewHolder.mTimeTv.setText(time.getFormatTime());
        viewHolder.mTime1Tv.setText(time.getFormatTime());
        viewHolder.mCnameTv.setText(detailsBean.getCname());
        CommonUtils.setTextViewType(context,detailsBean.getFavoured(),viewHolder.mCollectTv,R.string.collect,R.string.collected);
        viewHolder.mCollectTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtils.isLogin(context)){
                    return;
                }
                KangQiMeiApi api = new KangQiMeiApi("post/do_favour");
                api.addParams("uid",api.getUserId(context));
                api.addParams("token",api.getToken(context));
                api.addParams("id",detailsBean.getId());
                if (StringUtils.isSame(detailsBean.getFavoured(), CommonUtils.isOne)) {
                    api.addParams("del",detailsBean.getFavoured());
                }
                HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
                    @Override
                    public void onResponse(BaseBean response) {
                        if (StringUtils.isSame(detailsBean.getFavoured(), CommonUtils.isZero)) {//0为未收藏，1为收藏
                            detailsBean.setFavoured(CommonUtils.isOne);
                        } else {
                            detailsBean.setFavoured(CommonUtils.isZero);
                        }
                        CommonUtils.setTextViewType(context,detailsBean.getFavoured(),viewHolder.mCollectTv,R.string.collect,R.string.collected);
                        UIHelper.ToastMessage(context, response.getInfo());
                        EventBus.getDefault().post(new CollectNewsEvent());
                    }
                });
            }
        });
        WebViewPhotoBrowserUtil.photoBrowser(context, viewHolder.mWebView, detailsBean.getContent());
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.web_view)
        WebView mWebView;
        @InjectView(R.id.title_tv)
        TextView mTitleTv;//标题
        @InjectView(R.id.time_tv)
        TextView mTimeTv;//时间
        @InjectView(R.id.collect_tv)
        TextView mCollectTv;//收藏
        @InjectView(R.id.cname_tv)
        TextView mCnameTv;//咨询类型
        @InjectView(R.id.comment_tv)
        TextView mCommentTv;//评论
        @InjectView(R.id.time1_tv)
        TextView mTime1Tv;//时间1

        BannerViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }


}
