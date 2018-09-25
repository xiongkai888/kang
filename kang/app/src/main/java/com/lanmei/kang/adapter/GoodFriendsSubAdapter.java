package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.FollowApi;
import com.lanmei.kang.bean.GoodFriendsBean;
import com.lanmei.kang.ui.mine.activity.AddressBookFriendsActivity;
import com.lanmei.kang.ui.mine.activity.InterestedFriendActivity;
import com.lanmei.kang.ui.mine.activity.NewFriendsActivity;
import com.lanmei.kang.ui.mine.activity.SearchUserActivity;
import com.lanmei.kang.util.AKDialog;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CircleImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 *  好友(私信)
 */
public class GoodFriendsSubAdapter extends SwipeRefreshAdapter<GoodFriendsBean> implements View.OnClickListener{

    public int TYPE_BANNER = 100;

    public GoodFriendsSubAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) { // banner
            BannerViewHolder bannerHolder = new BannerViewHolder(LayoutInflater.from(context).inflate(R.layout.head_friends, parent, false));
            return bannerHolder;
        }
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_two_botton, parent, false));
    }

    @Override
    public int getItemViewType2(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        }
        return super.getItemViewType2(position);
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_BANNER) {
            onBindBannerViewHolder(holder, position); // banner
            return;
        }
        final GoodFriendsBean bean = getItem(position-1);
        if (StringUtils.isEmpty(bean)){
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.mAttentionTv.setText(R.string.attention);
        viewHolder.mSendInfoTv.setText(R.string.send_message);
        CommonUtils.setTextViewType(context,bean.getFollowed(),viewHolder.mAttentionTv,R.string.attention,R.string.attentioned);
        viewHolder.mAttentionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "";
                if (CommonUtils.isZero.equals(bean.getFollowed())){//0|1  未关注|已关注
                    str = context.getString(R.string.is_follow);
                }else {
                    str = context.getString(R.string.is_no_follow);
                }
                AKDialog.getAlertDialog(context, str , new AKDialog.AlertDialogListener() {
                    @Override
                    public void yes() {
                        isFollow(bean);
                    }
                });
            }
        });
        viewHolder.mSendInfoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.startChatActivity(context,bean.getId(),false);
            }
        });
        ImageHelper.load(context,bean.getPic(),viewHolder.headIv,null,true,R.mipmap.default_pic,R.mipmap.default_pic);
        viewHolder.nameTv.setText(bean.getNickname());
    }

    private void isFollow(final GoodFriendsBean bean) {
        FollowApi api = new FollowApi();
        api.uid = api.getUserId(context);
        api.token = api.getToken(context);
        api.mid = bean.getId();
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (CommonUtils.isZero.equals(bean.getFollowed())){//0|1  未关注|已关注
                    bean.setFollowed(CommonUtils.isOne);
                }else {
                    bean.setFollowed(CommonUtils.isZero);
                }
                UIHelper.ToastMessage(context,response.getInfo());
//                CommonUtils.loadUserInfo(context,null);
                notifyDataSetChanged();
            }
        });
    }


    public void onBindBannerViewHolder(RecyclerView.ViewHolder holder, int position) {
        BannerViewHolder viewHolder = (BannerViewHolder) holder;
        viewHolder.mllnewFriends.setOnClickListener(this);
        viewHolder.mllinterestedFriend.setOnClickListener(this);
        viewHolder.mlladdressBookFriends.setOnClickListener(this);
        viewHolder.mllsearchFriends.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_new_friends://新的朋友
                IntentUtil.startActivity(context, NewFriendsActivity.class);
                break;
            case R.id.ll_interested_friend://可能感兴趣的人
                IntentUtil.startActivity(context, InterestedFriendActivity.class);
                break;
            case R.id.ll_address_book_friends://通讯录
                IntentUtil.startActivity(context, AddressBookFriendsActivity.class);
                break;
            case R.id.ll_search://搜索
                IntentUtil.startActivity(context, SearchUserActivity.class,"user");
                break;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.head_iv)
        CircleImageView headIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.add_friends_tv)
        TextView mAttentionTv;//关注
        @InjectView(R.id.attention_tv)
        TextView mSendInfoTv;//发消息

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.ll_new_friends)
        LinearLayout mllnewFriends;//
        @InjectView(R.id.ll_interested_friend)
        LinearLayout mllinterestedFriend;//
        @InjectView(R.id.ll_address_book_friends)
        LinearLayout mlladdressBookFriends;//通讯录
        @InjectView(R.id.ll_search)
        LinearLayout mllsearchFriends;//搜索


        BannerViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

}
