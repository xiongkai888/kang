package com.lanmei.kang.ui.merchant;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.MerchantInfoBean;
import com.lanmei.kang.event.MerchantUpdataAdEvent;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.view.MerchantTimePicker;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.TimePicker;


/**
 * 商家资料
 */
public class MerchantDataActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    @InjectView(R.id.banner)
    ConvenientBanner banner;

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.start_time_tv)
    TextView mStartTime;//营业开始时间
    @InjectView(R.id.end_time_tv)
    TextView mEndTime;//营业结束时间
    @InjectView(R.id.name_et)
    EditText mNameEt;//场地名称
    @InjectView(R.id.address_et)
    EditText mAddressEt;//场地地址
    @InjectView(R.id.phone_et)
    EditText mPhoneEt;//电话
    @InjectView(R.id.place_introduction_et)
    EditText mPlaceIntroductionEt;//商家介绍
    EditText[] etID;
    boolean isTurning = true;

    @Override
    public int getContentViewId() {
        return R.layout.activity_merchant_data;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.merchant_data);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);
        //toolbar的menu点击事件的监听
        mToolbar.setOnMenuItemClickListener(this);
        etID = new EditText[]{mNameEt, mAddressEt, mPhoneEt, mPlaceIntroductionEt};
        for (EditText et : etID) {
            et.setFocusableInTouchMode(false);//设置不可编辑
        }
        //设置不可点击
        mStartTime.setClickable(false);
        mEndTime.setClickable(false);
        initTimePicker();//初始化时间
        loadData();
        EventBus.getDefault().register(this);
    }
    MerchantInfoBean bean;
    private void loadData(){
        KangQiMeiApi api = new KangQiMeiApi("place/index");
        api.add("uid",api.getUserId(this));
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<MerchantInfoBean>>() {
            @Override
            public void onResponse(DataBean<MerchantInfoBean> response) {
                if (isFinishing()){
                    return;
                }
                bean = response.data;
                setMerchant(bean,isTurning);
            }
        });
    }

    MerchantTimePicker timePicker;
    int timeType ;

    private void initTimePicker() {
        timePicker = new MerchantTimePicker(this);
        timePicker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                String time = hour + ":" + minute;
                if (timeType == 1){
                    mStartTime.setText(time);
                }else {
                    mEndTime.setText(time);
                }

            }
        });
    }


    private void setMerchant(MerchantInfoBean bean,boolean isTurning) {
        if (bean == null){
            return;
        }
        //广告轮播
        CommonUtils.setBanner(banner,bean.getPics(),isTurning);
        String startTime = bean.getStime();
        String endTime = bean.getEtime();
        if (!StringUtils.isEmpty(startTime) && startTime.length() > 5) {
            mStartTime.setText(startTime.substring(0, 5));
        } else {
            mStartTime.setText(startTime);
        }
        if (!StringUtils.isEmpty(endTime) && endTime.length() > 5) {
            mEndTime.setText(endTime.substring(0, 5));
        } else {
            mEndTime.setText(endTime);
        }
        mNameEt.setText(bean.getName());
        mAddressEt.setText(bean.getAddress());
        mPhoneEt.setText(bean.getTel());
        mPlaceIntroductionEt.setText(bean.getPlace_introduction());

    }

    @Subscribe
    public void merchantUpdataAdEvent(MerchantUpdataAdEvent event){
        isTurning = false;
        loadData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @OnClick({R.id.start_time_tv, R.id.end_time_tv})
    public void setBusinessTime(View view) {
        switch (view.getId()) {
            case R.id.start_time_tv://开始时间
                timeType = 1;
                timePicker.show();
                break;
            case R.id.end_time_tv://结束时间
                timeType = 2;
                timePicker.show();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if (item.getItemId() == R.id.action_edit) {
            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.menu_save);
            for (EditText et : etID) {
                et.setFocusableInTouchMode(true);//设置可编辑
            }
            mStartTime.setClickable(true);
            mEndTime.setClickable(true);
        } else if (item.getItemId() == R.id.action_save) {//先退出输入法
            String name = mNameEt.getText().toString().trim();
            if (StringUtils.isEmpty(name)){
                UIHelper.ToastMessage(this,"请输入场地名称");
                return false;
            }
            String stime = mStartTime.getText().toString().trim();
            if (StringUtils.isEmpty(stime)){
                UIHelper.ToastMessage(this,"请选择营业开始时间");
                return false;
            }
            String etime = mEndTime.getText().toString().trim();
            if (StringUtils.isEmpty(etime)){
                UIHelper.ToastMessage(this,"请选择营业结束时间");
                return false;
            }
            String address = mAddressEt.getText().toString().trim();
            if (StringUtils.isEmpty(address)){
                UIHelper.ToastMessage(this,"请输入场地地址");
                return false;
            }
            String phone = mPhoneEt.getText().toString().trim();
            if (StringUtils.isEmpty(phone)){
                UIHelper.ToastMessage(this,"请输入联系电话");
                return false;
            }
            String placeIntroduction = mPlaceIntroductionEt.getText().toString().trim();
            if (StringUtils.isEmpty(placeIntroduction)){
                UIHelper.ToastMessage(this,"请输入场地介绍");
                return false;
            }
            updateMerchantData(name,stime,etime,address,phone,placeIntroduction);
            mStartTime.setClickable(false);
            mEndTime.setClickable(false);
            View view = this.getCurrentFocus();
            if (view != null) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.menu_edit);
            for (EditText et : etID) {
                et.setFocusable(false);
                et.setFocusableInTouchMode(false);//设置不可编辑
            }
        }
        return true;
    }

    //更新商家资料
    private void updateMerchantData(String name,String stime,String etime,String address,String phone,String placeIntroduction) {
        HttpClient httpClient = HttpClient.newInstance(this);
        KangQiMeiApi api = new KangQiMeiApi("place/update");
        api.add("token",api.getToken(this));
        api.add("uid",api.getUserId(this));
        api.add("name",name);
        api.add("stime",stime);
        api.add("etime",etime);
        api.add("place_address",address);
        api.add("tel",phone);
        api.add("place_introduction",placeIntroduction);
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(MerchantDataActivity.this, response.getInfo());
                isTurning = false;
//                DemoHelper.getInstance().UpdateUserInfo();
            }
        });
    }

    @OnClick(R.id.uploading_tv)
    public void showUploadingIv() {//点击上传图片
        if (bean == null){
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean",bean);
        IntentUtil.startActivity(this, MerchantAlbumActivity.class,bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
