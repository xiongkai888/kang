package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.GoodFriendsBean;
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

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 好友的好友列表
 */
public class FriendsFriendsAdapter extends SwipeRefreshAdapter<GoodFriendsBean> {


    public FriendsFriendsAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_friends_friends, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {

        final GoodFriendsBean bean = getItem(position);
        if (StringUtils.isEmpty(bean)){
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        if (StringUtils.isSame(bean.getId(), CommonUtils.getUserId(context))){//是自己的就隐藏
            viewHolder.mAttentionTv.setVisibility(View.GONE);
        }else {
//            viewHolder.mAttentionTv.setVisibility(View.VISIBLE);
            final String followed = bean.getFollowed();
            if ("0".equals(followed)){
                viewHolder.mAttentionTv.setText(R.string.attention);
            }else {
                viewHolder.mAttentionTv.setText(R.string.attentioned);
            }
            viewHolder.mAttentionTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = "";
                    if ("0".equals(followed)){//0|1  未关注|已关注
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
        }
        ImageHelper.load(context,bean.getPic(),viewHolder.headIv,null,true,R.mipmap.default_pic,R.mipmap.default_pic);
        viewHolder.nameTv.setText(bean.getNickname());
    }

    private void isFollow(final GoodFriendsBean bean) {
        KangQiMeiApi api = new KangQiMeiApi("member_follow/follow");
        api.addParams("uid",api.getUserId(context));
        api.addParams("mid",bean.getId());
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                final String followed = bean.getFollowed();
                if ("0".equals(followed)){//0|1  未关注|已关注
                    bean.setFollowed(1+"");
                }else {
                    bean.setFollowed(0+"");
                }
                UIHelper.ToastMessage(context,response.getInfo());
//                CommonUtils.loadUserInfo(context,null);
                notifyDataSetChanged();
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.head_iv)
        CircleImageView headIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.attention_tv)
        TextView mAttentionTv;//关注

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

}
