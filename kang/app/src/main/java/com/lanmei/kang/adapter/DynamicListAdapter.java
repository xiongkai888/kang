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
import com.lanmei.kang.event.DynamicLikedEvent;
import com.lanmei.kang.ui.dynamic.activity.DynamicDetailsActivity;
import com.lanmei.kang.ui.dynamic.activity.DynamicFriendsActivity;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.FormatTime;
import com.lanmei.kang.widget.SudokuView;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 动态  (最新、精选列表)（我的、朋友圈）
 */
public class DynamicListAdapter extends SwipeRefreshAdapter<DynamicBean> {

    private FormatTime time;

    int who;//3:好友的动态

    public DynamicListAdapter(Context context) {
        super(context);
        time = new FormatTime(context);
    }

    public void setType(int who){
        this.who = who;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dynamic_list, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final DynamicBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean, position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(bean);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.user_head_iv)
        CircleImageView userHeadIv;
        @InjectView(R.id.user_name_tv)
        TextView nameTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.title_tv)
        TextView titleTv;//
        @InjectView(R.id.sudokuView)
        SudokuView sudokuView;
        @InjectView(R.id.zan_iv)
        ImageView zanIv;//点赞图片
        @InjectView(R.id.zan_tv)
        TextView zanTv;
        @InjectView(R.id.share_tv)
        TextView shareTv;
        @InjectView(R.id.comment_tv)
        TextView commentTv;
        @InjectView(R.id.position_tv)
        TextView positionTv;
        @InjectView(R.id.ll_zan)
        LinearLayout llzan;
        @InjectView(R.id.ll_share)
        LinearLayout llshare;
        @InjectView(R.id.ll_comm)
        LinearLayout llcomm;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(final DynamicBean bean, final int position) {
            ImageHelper.load(context, bean.getPic(), userHeadIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
            userHeadIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!StringUtils.isSame(bean.getUid(), CommonUtils.getUserId(context))) {
//                        UIHelper.ToastMessage(context, R.string.developing);
                        if (who == 3){//动态好友
                            return;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("bean",bean);
                        bundle.putInt("who",who);
                        IntentUtil.startActivity(context, DynamicFriendsActivity.class, bundle);
                    }
                }
            });
            if (StringUtils.isEmpty(bean.getCity())){
                positionTv.setVisibility(View.GONE);
            }else {
                positionTv.setVisibility(View.VISIBLE);
                positionTv.setText(bean.getCity());
            }
            L.d("attention",position+","+bean.getFollowed());
            nameTv.setText(bean.getNickname());
            time.setTime(bean.getAddtime());
            timeTv.setText(time.getFormatTime());
            titleTv.setText(bean.getTitle());
            zanTv.setText(bean.getLike());//点赞数
            commentTv.setText(String.format(context.getString(R.string.comm_num), bean.getReviews()));//评论数
            sudokuView.setListData(bean.getFile());
            sudokuView.setOnSingleClickListener(new SudokuView.SudokuViewClickListener() {
                @Override
                public void onClick(int positionSub) {
                    startActivity(bean);
//                    UIHelper.ToastMessage(context,R.string.developing);
                }

                @Override
                public void onDoubleTap(int position) {
                    CommonUtils.startPhotoBrowserActivity(context, CommonUtils.toArray(bean.getFile()), position);
                }
            });
            final String liked = bean.getLiked();
            if (StringUtils.isSame(liked, CommonUtils.isZero)) {
                zanIv.setImageResource(R.mipmap.d_zan_off);
            } else {
                zanIv.setImageResource(R.mipmap.d_zan_on);
            }
            llzan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommonUtils.isLogin(context)) {
                        return;
                    }
                    KangQiMeiApi api = new KangQiMeiApi("posts/like");
                    api.addParams("uid",api.getUserId(context));
                    api.addParams("id",bean.getId());
                    HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
                        @Override
                        public void onResponse(BaseBean response) {
                            String str;
                            int like = Integer.valueOf(bean.getLike());
                            if (StringUtils.isSame(context.getString(R.string.zan_succeed), response.getInfo())) {
                                str = CommonUtils.isOne;
                                like++;
                                zanIv.setImageResource(R.mipmap.d_zan_on);
                            } else {
                                str = CommonUtils.isZero;
                                like--;
                                zanIv.setImageResource(R.mipmap.d_zan_off);
                            }
                            bean.setLiked(str);
                            EventBus.getDefault().post(new DynamicLikedEvent(bean.getId(), "" + like, str, bean.getReviews()));//动态
                            bean.setLike("" + like);
                            UIHelper.ToastMessage(context, response.getInfo());
//                            notifyDataSetChanged();
                        }
                    });
                }
            });
            llshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommonUtils.isLogin(context)) {
                        return;
                    }
                    CommonUtils.developing(context);
                }
            });
        }

    }


    public void startActivity(DynamicBean bean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        bundle.putInt("who",who);
        IntentUtil.startActivity(context, DynamicDetailsActivity.class, bundle);
    }

}
