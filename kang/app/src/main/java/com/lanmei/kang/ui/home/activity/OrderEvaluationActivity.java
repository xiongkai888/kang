package com.lanmei.kang.ui.home.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.event.OrderOperationEvent;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.api.AbstractApi;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;

/**
 * 订单晒单评价
 */
public class OrderEvaluationActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.content_et)
    EditText mContentEt;
    String order_id;

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_evaluation;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("评价");
        actionbar.setHomeAsUpIndicator(R.mipmap.back);
        order_id = getIntent().getStringExtra("value");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                String content = CommonUtils.getStringByEditText(mContentEt);
                if (StringUtils.isEmpty(content)) {
                    UIHelper.ToastMessage(this, R.string.qin);
                    return super.onOptionsItemSelected(item);
                }
                ajaxEvaluation(content);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ajaxEvaluation(String content) {
        HttpClient httpClient = HttpClient.newInstance(this);
        KangQiMeiApi api = new KangQiMeiApi("PlaceReviews/add");
        api.addParams("content",content);
        api.addParams("token",api.getToken(this));
        api.addParams("uid",api.getUserId(this));
        api.addParams("order_id",order_id);
        api.setMethod(AbstractApi.Method.GET);
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()){
                    return;
                }
                UIHelper.ToastMessage(OrderEvaluationActivity.this,response.getInfo());
                EventBus.getDefault().post(new OrderOperationEvent());//
                finish();
            }
        });
    }

}
