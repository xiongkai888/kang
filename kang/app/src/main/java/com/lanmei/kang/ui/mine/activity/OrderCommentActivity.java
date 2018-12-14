package com.lanmei.kang.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.EditText;
import android.widget.RatingBar;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.GoodsOrderListBean;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

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
    GoodsOrderListBean bean;//我的订单item信息
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
            bean = (GoodsOrderListBean) bundle.getSerializable("bean");
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
        CommonUtils.developing(this);
//        KangQiMeiApi api = new KangQiMeiApi("");
//        api.content = content;
//        api.orderid = bean.getId();
//        api.uid = api.getUserId(this);
//        api.proid = getProid();
//        api.point = point;
//        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
//            @Override
//            public void onResponse(BaseBean response) {
//                if (isFinishing()) {
//                    return;
//                }
//
//            }
//        });
    }

    private String getProid() {
        String id = "";
        if (bean == null) {
            return id;
        }
//        List<MineOrderBean.ProductBean> list = bean.getProduct();
//        if (StringUtils.isEmpty(list)) {
//            return id;
//        }
//        int size = list.size();
//        for (int i = 0; i < size; i++) {
//            MineOrderBean.ProductBean bean = list.get(i);
//            if (!StringUtils.isEmpty(bean) && !StringUtils.isEmpty(bean.getId())){
//                id += bean.getId()+",";
//            }
//        }
        if (!StringUtils.isEmpty(id)){
            id = id.substring(0,id.length()-1);
        }
        return id;
    }
}
