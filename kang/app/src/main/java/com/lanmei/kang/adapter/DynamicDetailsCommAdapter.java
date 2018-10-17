package com.lanmei.kang.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.DynamicBean;
import com.lanmei.kang.bean.DynamicCommBean;
import com.lanmei.kang.event.AttentionFriendEvent;
import com.lanmei.kang.event.DynamicLikedEvent;
import com.lanmei.kang.helper.ShareHelper;
import com.lanmei.kang.ui.dynamic.activity.DynamicFriendsActivity;
import com.lanmei.kang.util.AKDialog;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.FormatTime;
import com.lanmei.kang.widget.SudokuView;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 动态详情评论
 */
public class DynamicDetailsCommAdapter extends SwipeRefreshAdapter<DynamicCommBean> {

    public int TYPE_BANNER = 100;
    private FormatTime time;
    private DynamicBean mBean;
    private boolean isSelf;//是否是自己的动态
    ShareHelper shareHelper;
    int who;

    public DynamicDetailsCommAdapter(Context context, DynamicBean bean, boolean isSelf) {
        super(context);
        time = new FormatTime(context);
        mBean = bean;
        this.isSelf = isSelf;
    }

    public void setShare(ShareHelper shareHelper){
        this.shareHelper = shareHelper;
    }

    public void setType(int who){
        this.who = who;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) { // banner
            return new BannerViewHolder(LayoutInflater.from(context).inflate(R.layout.head_dynamic_details, parent, false));
        }
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comm, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_BANNER) {
            onBindBannerViewHolder(holder, position); // banner
            return;
        }

        DynamicCommBean bean = getItem(position - 1);
        if (StringUtils.isEmpty(bean)) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }

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

        public void setParameter(final DynamicCommBean bean) {
            ImageHelper.load(context, bean.getPic(), userHeadIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
            nameTv.setText(bean.getNickname());
            contentTv.setText(bean.getContent());
            time.setTime(bean.getAddtime());
            timeTv.setText(time.getFormatTime());
        }
    }

    @Override
    public int getItemViewType2(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        }
        return super.getItemViewType2(position);
    }


    //头部
    public class BannerViewHolder extends RecyclerView.ViewHolder {


        @InjectView(R.id.user_head_iv)
        CircleImageView userHeadIv;
        @InjectView(R.id.user_name_tv)
        TextView userNameTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.attention_tv)
        TextView attentionTv;
        @InjectView(R.id.content_tv)
        TextView contentTv;
        @InjectView(R.id.zan_tv)
        TextView zanTv;
        @InjectView(R.id.share_tv)
        TextView shareTv;
        @InjectView(R.id.comment_tv)
        TextView commentTv;
        @InjectView(R.id.sudokuView)
        SudokuView sudokuView;
        @InjectView(R.id.ll_zan)
        LinearLayout llzan;
        @InjectView(R.id.ll_share)
        LinearLayout llshare;
        @InjectView(R.id.ll_comm)
        LinearLayout llcomm;
        @InjectView(R.id.zan_iv)
        ImageView zanIv;//点赞图片

        BannerViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    public void onBindBannerViewHolder(RecyclerView.ViewHolder holder, int position) {
        final BannerViewHolder viewHolder = (BannerViewHolder) holder;
        if (mBean == null) {
            return;
        }
        if (!isSelf) {
            CommonUtils.setTextViewType(context,mBean.getFollowed(),viewHolder.attentionTv,R.string.attention,R.string.attentioned);
            viewHolder.attentionTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommonUtils.isLogin(context)){
                        return;
                    }
                    String str = "";
                    if (CommonUtils.isZero.equals(mBean.getFollowed())){//0|1  未关注|已关注
                        str = context.getString(R.string.is_follow);
                    }else {
                        str = context.getString(R.string.is_no_follow);
                    }
                    AKDialog.getAlertDialog(context, str , new AKDialog.AlertDialogListener() {
                        @Override
                        public void yes() {
                            isFollow();
                        }
                    });
                }
            });
        } else {
            viewHolder.attentionTv.setVisibility(View.GONE);
        }
        viewHolder.userNameTv.setText(mBean.getNickname());
        time.setTime(mBean.getAddtime());
        viewHolder.timeTv.setText(time.getFormatTime());
        viewHolder.commentTv.setText(mBean.getReviews());
        viewHolder.contentTv.setText(mBean.getTitle());
        viewHolder.sudokuView.setListData(mBean.getFile());
        viewHolder.sudokuView.setOnSingleClickListener(new SudokuView.SudokuViewClickListener() {
            @Override
            public void onClick(int positionSub) {
                if (!CommonUtils.isLogin(context)){
                    return;
                }
                CommonUtils.startPhotoBrowserActivity(context, CommonUtils.toArray(mBean.getFile()), positionSub);
            }

            @Override
            public void onDoubleTap(int position) {

            }
        });

        viewHolder.llshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareHelper.share();
            }
        });

        viewHolder.zanTv.setText(mBean.getLike());
        viewHolder.commentTv.setText(String.format(context.getString(R.string.comm_num), mBean.getReviews()));//评论数
//        viewHolder.commentTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        viewHolder.userHeadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (who == 3){
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", mBean);
                IntentUtil.startActivity(context, DynamicFriendsActivity.class, bundle);
            }
        });
        ImageHelper.load(context, mBean.getPic(), viewHolder.userHeadIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
        final String liked = mBean.getLiked();
        if (StringUtils.isSame(liked, CommonUtils.isZero)) {
            viewHolder.zanIv.setImageResource(R.mipmap.d_zan_off);
        } else {
            viewHolder.zanIv.setImageResource(R.mipmap.d_zan_on);
        }
        viewHolder.llzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtils.isLogin(context)) {
                    return;
                }
                KangQiMeiApi api = new KangQiMeiApi("posts/like");
                api.addParams("uid",api.getUserId(context));
                api.addParams("id",mBean.getId());
                HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
                    @Override
                    public void onResponse(BaseBean response) {
                        int like = Integer.valueOf(mBean.getLike());
                        if (StringUtils.isSame(context.getString(R.string.zan_succeed), response.getInfo())) {
                            mBean.setLiked(CommonUtils.isOne);
                            like++;
                            viewHolder.zanIv.setImageResource(R.mipmap.d_zan_on);
                        } else {
                            mBean.setLiked(CommonUtils.isZero);
                            like--;
                            viewHolder.zanIv.setImageResource(R.mipmap.d_zan_off);
                        }
                        mBean.setLike("" + like);
                        EventBus.getDefault().post(new DynamicLikedEvent(mBean.getId(), mBean.getLike(), mBean.getLiked(), mBean.getReviews()));
                        UIHelper.ToastMessage(context, response.getInfo());
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void isFollow() {
        KangQiMeiApi api = new KangQiMeiApi("member_follow/follow");
        api.addParams("uid",api.getUserId(context));
        api.addParams("token",api.getToken(context));
        api.addParams("mid",mBean.getUid());
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (CommonUtils.isZero.equals(mBean.getFollowed())){//0|1  未关注|已关注
                    mBean.setFollowed(CommonUtils.isOne);
                }else {
                    mBean.setFollowed(CommonUtils.isZero);
                }
                UIHelper.ToastMessage(context,response.getInfo());
                EventBus.getDefault().post(new AttentionFriendEvent(mBean.getUid(),mBean.getFollowed()));//该uid都设置为关注或不关注
//                CommonUtils.loadUserInfo(context,null);
                notifyDataSetChanged();
            }
        });
    }

}
