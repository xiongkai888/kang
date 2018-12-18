package com.lanmei.kang.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.EditText;
import android.widget.RatingBar;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.GoodsOrderListBean;
import com.lanmei.kang.event.OrderOperationEvent;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 商品评论
 */
public class OrderCommentActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.content_et)
    EditText contentEt;
    @InjectView(R.id.ratingbar)
    RatingBar ratingBar;
    GoodsOrderListBean.GoodsBean bean;//我的订单item信息
    int point = 1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_comment;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (!StringUtils.isEmpty(bundle)) {
            bean = (GoodsOrderListBean.GoodsBean) bundle.getSerializable("bean");
        }
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("商品评论");
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                point = (int) rating;
            }
        });
    }


    @OnClick(R.id.submit_bt)
    public void onViewClicked() {
        String content = CommonUtils.getStringByEditText(contentEt);
        if (StringUtils.isEmpty(content)) {
            UIHelper.ToastMessage(this, R.string.input_comment);
            return;
        }
        KangQiMeiApi api = new KangQiMeiApi("app/comment_save");
        api.add("userid", api.getUserId(this));
        api.add("order_no", bean.getOrder_no());
        api.add("goodsid", bean.getGid());
        api.add("content", content);
        api.add("point", point);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(),response.getInfo());
                EventBus.getDefault().post(new OrderOperationEvent());
                finish();
            }
        });
    }
}
