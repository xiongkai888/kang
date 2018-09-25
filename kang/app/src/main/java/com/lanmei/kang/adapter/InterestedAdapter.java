package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.AddFriendsApi;
import com.lanmei.kang.api.FollowApi;
import com.lanmei.kang.bean.InterestedBean;
import com.lanmei.kang.event.AddFriendsEvent;
import com.lanmei.kang.event.FollowInNewFriendEvent;
import com.lanmei.kang.util.AKDialog;
import com.lanmei.kang.util.CommonUtils;
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
 * 可能感兴趣的人
 */
public class InterestedAdapter extends SwipeRefreshAdapter<InterestedBean> {


    String uid;//自己的uid

    public InterestedAdapter(Context context) {
        super(context);
        uid = CommonUtils.getUserId(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_two_botton, parent, false));
    }

    @Override
    public void onBindViewHolder2(final RecyclerView.ViewHolder holder, int position) {
        final InterestedBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        final ViewHolder viewHolder = (ViewHolder) holder;
        if (StringUtils.isSame(uid, bean.getId())) {
            viewHolder.addFriendsTv.setVisibility(View.GONE);
            viewHolder.attentionTv.setVisibility(View.GONE);
        } else {
            viewHolder.addFriendsTv.setVisibility(View.VISIBLE);
            viewHolder.attentionTv.setVisibility(View.VISIBLE);
            final String join_stauts = bean.getJoin_status();
            if (StringUtils.isSame(join_stauts,CommonUtils.isZero)) {
                viewHolder.addFriendsTv.setVisibility(View.VISIBLE);
                viewHolder.addFriendsTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AKDialog.getAlertDialog(context, "确认添加为好友？" , new AKDialog.AlertDialogListener() {
                            @Override
                            public void yes() {
                                isAddFriends(bean);
                            }
                        });
                    }
                });
            } else {
                viewHolder.addFriendsTv.setVisibility(View.GONE);
            }
            CommonUtils.setTextViewType(context,bean.getFollowed(),viewHolder.attentionTv,R.string.attention,R.string.attentioned);
            viewHolder.attentionTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = "";
                    if (StringUtils.isSame(bean.getFollowed(),CommonUtils.isZero)) {//0|1  未关注|已关注
                        str = context.getString(R.string.is_follow);
                    } else {
                        str = context.getString(R.string.is_no_follow);
                    }
                    AKDialog.getAlertDialog(context, str, new AKDialog.AlertDialogListener() {
                        @Override
                        public void yes() {
                            isFollow(bean);
                        }
                    });
                }
            });
        }
        viewHolder.nameTv.setText(bean.getNickname());
        ImageHelper.load(context, bean.getPic(), viewHolder.headIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
    }

    private void isAddFriends(final InterestedBean bean) {
        HttpClient httpClient = HttpClient.newInstance(context);
        AddFriendsApi api = new AddFriendsApi();
        api.mid = bean.getId();
        api.uid = api.getUserId(context);
        api.token = api.getToken(context);
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                bean.setJoin_status(CommonUtils.isOne);
//                            CommonUtils.loadUserInfo(context,null);
                UIHelper.ToastMessage(context, response.getInfo());
                EventBus.getDefault().post(new AddFriendsEvent());//刷新好友列表
                notifyDataSetChanged();
            }
        });
    }

    private void isFollow(final InterestedBean bean) {
        FollowApi api = new FollowApi();
        api.uid = api.getUserId(context);
        api.mid = bean.getId();
        api.token = api.getToken(context);
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                final String followed = bean.getFollowed();
                if (StringUtils.isSame(followed,CommonUtils.isZero)) {//0|1  未关注|已关注
                    bean.setFollowed(CommonUtils.isOne);
                } else {
                    bean.setFollowed(CommonUtils.isZero);
                }
                UIHelper.ToastMessage(context, response.getInfo());
                EventBus.getDefault().post(new FollowInNewFriendEvent(bean.getId(),bean.getFollowed()));
//                CommonUtils.loadUserInfo(context, null);
                notifyDataSetChanged();
            }
        });
    }

    RefreshListListener mRefreshListListener;

    public interface RefreshListListener {
        void refresh();
    }

    public void setRefreshListListener(RefreshListListener refreshListListener) {
        mRefreshListListener = refreshListListener;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.head_iv)
        CircleImageView headIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.add_friends_tv)
        TextView addFriendsTv;
        @InjectView(R.id.attention_tv)
        TextView attentionTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

}
