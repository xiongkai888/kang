package com.lanmei.kang.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.lanmei.kang.KangApp;
import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.InterestedBean;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.UIHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;



/**
 * 达人  好友
 */
public class ExpertFriendAdapter extends SwipeRefreshAdapter<InterestedBean> {


    public ExpertFriendAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final InterestedBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        final ViewHolder viewHolder = (ViewHolder) holder;
        String uid = CommonUtils.getUid(context);
        if (bean.getId().equals(uid)) {
            viewHolder.mAttentionTv.setVisibility(View.GONE);
        } else {
            viewHolder.mAttentionTv.setVisibility(View.VISIBLE);
            String followed = bean.getFollowed();
            if ("0".equals(followed)) {
                viewHolder.mAttentionTv.setText("关注");
            } else {
                viewHolder.mAttentionTv.setText("已关注");
            }
            viewHolder.mAttentionTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpClient httpClient = HttpClient.newInstance(context);
                    KangQiMeiApi api = new KangQiMeiApi("member_follow/follow");
                    api.addParams("mid",bean.getId());
                    httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
                        @Override
                        public void onResponse(BaseBean response) {
                            if (bean.getFollowed().equals("0")) {
                                viewHolder.mAttentionTv.setText("已关注");
                                bean.setFollowed("1");//设置为已关注
                            } else {
                                viewHolder.mAttentionTv.setText("关注");
                                bean.setFollowed("0");//设置为已取消关注
                            }
                            notifyDataSetChanged();
                            DemoHelper.getInstance().UpdateUserInfo();
                            UIHelper.ToastMessage(context, response.getInfo());
                        }
                    });
                }
            });
        }

        viewHolder.mNameTv.setText(bean.getNickname());
        ImageHelper.load(context, bean.getPic(), viewHolder.mUserHeadIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(Constant.EXTRA_USER_ID, KangApp.HX_USER_Head + bean.getId());
                context.startActivity(intent);
            }
        });


    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.user_head_iv)
        ImageView mUserHeadIv;
        @InjectView(R.id.name_tv)
        TextView mNameTv;
        @InjectView(R.id.attention_tv)
        TextView mAttentionTv;//关注

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

}
