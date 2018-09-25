package com.lanmei.kang.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.PublishHonorApi;
import com.lanmei.kang.bean.HonorBean;
import com.lanmei.kang.helper.UserHelper;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * 发布荣誉
 */
public class PublishHonorActivity extends BaseActivity {

    public static String PUBLISH_SUCCEED = "PUBLISH_SUCCEED";//发布成功广播

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    //    @InjectView(R.id.title_et)
//    EditText mTitleEt;//标题
    @InjectView(R.id.item_tv)
    TextView mItemTv;//比赛项目
//    @InjectView(R.id.time_tv)
//    TextView mTimeTv;//比赛时间
    @InjectView(R.id.ranking_tv)
    TextView mRankingTv;//比赛名次

    HonorBean bean;

    @Override
    public int getContentViewId() {
        return R.layout.activity_publish_honor;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("发布荣誉");
        actionbar.setHomeAsUpIndicator(R.mipmap.back);

        bean = (HonorBean)getIntent().getSerializableExtra("bean");
        mItemTv.setText(bean.getTitle());
    }

    public static void startPublishHonorActivity(Context context, HonorBean bean){
        Intent intent = new Intent(context,PublishHonorActivity.class);
        intent.putExtra("bean",bean);
        context.startActivity(intent);
    }

    @OnClick(R.id.ll_competition_ranking)
    public void showRanking() {//比赛名次
        OptionPicker picker = new OptionPicker(this, new String[]{
                "金牌", "银牌", "铜牌"
        });
        picker.setOffset(1);
        picker.setSelectedIndex(1);
        picker.setTextSize(14);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                mRankingTv.setText(option);
            }
        });
        picker.show();
    }

    String mRanking;

    @OnClick(R.id.bt_submit)
    public void showSubmit() {//提交
        mRanking = mRankingTv.getText().toString().trim();
        if (StringUtils.isEmpty(mRanking)) {
            UIHelper.ToastMessage(this, getString(R.string.match_ranking));
            return;
        }
        ajaxHonor(mRanking);//发布荣誉
    }

    private void ajaxHonor(final String ranking) {
        HttpClient httpClient = HttpClient.newInstance(this);
        final PublishHonorApi api = new PublishHonorApi();
        api.token = UserHelper.getInstance(this).getToken();
        if (bean != null){
            api.id = bean.getId();
        }
        final String uid = CommonUtils.getUid(this);
        if (ranking.equals("金牌")) {
            api.first = uid;
            bean.setFirst(bean.getFirst()+","+uid);
        } else if (ranking.equals("银牌")) {
            api.second = uid;
            bean.setSecond(bean.getSecond()+","+uid);
        } else if (ranking.equals("铜牌")) {
            api.third = uid;
            bean.setThird(bean.getThird()+","+uid);
        }
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (PublishHonorActivity.this.isFinishing()){
                    return;
                }
                Intent intent = new Intent(PUBLISH_SUCCEED);
                intent.putExtra("bean",bean);
                sendBroadcast(intent);
                UIHelper.ToastMessage(PublishHonorActivity.this,"发布成功");
                finish();
            }
        });
    }

}
