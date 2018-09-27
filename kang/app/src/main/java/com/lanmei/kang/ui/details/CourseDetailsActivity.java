package com.lanmei.kang.ui.details;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.NewsCategoryListBean;
import com.lanmei.kang.helper.ShareHelper;
import com.lanmei.kang.loader.DataLoader;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 课程详情或活动详情
 */
public class CourseDetailsActivity extends BaseActivity {
    public static String COLLECT_COURSE_DETAILS = "COLLECT_COURSE_DETAILS";//课程详情或活动详情收藏广播
    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    //推荐使用StandardGSYVideoPlayer，功能一致
    //CustomGSYVideoPlayer部分功能处于试验阶段
    @InjectView(R.id.web_view)
    WebView mWebView;
    @InjectView(R.id.title_tv)
    TextView mTitleTv;//标题
    @InjectView(R.id.time_tv)
    TextView mTimeTv;//时间
    @InjectView(R.id.limit_tv)
    TextView mLimitTv;//活动的期限
    @InjectView(R.id.collect_tv)
    TextView mCollectTv;//收藏
    @InjectView(R.id.apply_now_tv)
    TextView mApplyTv;//立即报名
    @InjectView(R.id.bottom_layout)
    LinearLayout mllBottom;//底部报名
    @InjectView(R.id.phone_iv)
    ImageView mPhoneIv;//电话
    @InjectView(R.id.ll_toolbar)
    LinearLayout mlltoolbar;//
    @InjectView(R.id.ll_apply)
    LinearLayout mllApply;//立即报名

    String id;//资讯id
    String cName;//课程还是活动
    String mTEL;//联系电话
    String videoUrl;//视频地址

    NewsCategoryListBean bean;
    private ShareHelper mShareHelper;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_course_details;
    }


    public static void startActivityCourseDetails(Context context, NewsCategoryListBean bean) {
        Intent intent = new Intent(context, CourseDetailsActivity.class);
        intent.putExtra("bean", bean);
        context.startActivity(intent);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);
        //分享初始化
        mShareHelper = new ShareHelper(this);
        Intent intent = getIntent();
        if (intent != null) {
            bean = (NewsCategoryListBean) intent.getSerializableExtra("bean");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_more:
                if (!CommonUtils.isLogin(this)) {
                    break;
                }
                mShareHelper.share();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 结果返回
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareHelper.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mShareHelper.onDestroy();
    }


    @OnClick(R.id.ll_apply)
    public void showApply() {//立即报名
        if (!CommonUtils.isLogin(this)) {
            return;
        }
       UIHelper.ToastMessage(this,R.string.developing);
    }

    @OnClick(R.id.collect_tv)
    public void showCollect() {//收藏
        if (!CommonUtils.isLogin(this)) {
            return;
        }
    }

    @OnClick(R.id.ll_service)
    public void showService() {//客服咨询
        if (!CommonUtils.isLogin(this)) {
            return;
        }
        DataLoader.getInstance().onlineService(this);//在线客服
    }

    @OnClick(R.id.phone_iv)
    public void showTEL() {//课程电话
        if (!StringUtils.isEmpty(mTEL)) {
            UIHelper.callPhone(this, mTEL);
        } else {
            UIHelper.ToastMessage(this, getString(R.string.cannot_contact));
        }
    }


}
