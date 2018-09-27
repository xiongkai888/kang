package com.lanmei.kang.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.UserHelper;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;

/**
 * 发布留言
 */
public class LeavePublishActivity extends BaseActivity {

    public static String LEAVE_LIST_REFRESH = "LEAVE_LIST_REFRESH";//发布留言成功广播

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.leave_content_et)
    EditText mLeaveContentEt;//留言内容
    String uid;//达人的uid

    @Override
    public int getContentViewId() {
        return R.layout.activity_leave_publish;
    }

    public static void startActivityLeavePublish(Context context, String uid) {
        Intent intent = new Intent(context, LeavePublishActivity.class);
        intent.putExtra("uid", uid);
        context.startActivity(intent);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.leave);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);
        uid = getIntent().getStringExtra("uid");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish_dynamic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_publish:
                if (!CommonUtils.isLogin(this)) {
                    break;
                }
                String content = mLeaveContentEt.getText().toString().trim();
                if (StringUtils.isEmpty(content)) {
                    UIHelper.ToastMessage(this, R.string.leave_content);
                    break;
                }
                ajaxLeave(content);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //发起留言
    private void ajaxLeave(String content) {
        KangQiMeiApi api = new KangQiMeiApi("TalentReviews/add");
        api.addParams("mid",uid);
        api.addParams("token",UserHelper.getInstance(this).getToken());
        api.addParams("content",content);
        HttpClient httpClient = HttpClient.newInstance(this);
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (LeavePublishActivity.this.isFinishing()) {
                    return;
                }
                CommonUtils.notifyDoSomething(LeavePublishActivity.this,LEAVE_LIST_REFRESH);
                UIHelper.ToastMessage(LeavePublishActivity.this, response.getInfo());
                finish();
            }
        });
    }
}
