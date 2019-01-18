package com.lanmei.kang.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chatuidemo.DemoHelper;
import com.lanmei.kang.KangApp;
import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.event.LoginQuitEvent;
import com.lanmei.kang.ui.MainActivity;
import com.lanmei.kang.util.CodeCountDownTimer;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.RandomUtil;
import com.lanmei.kang.util.SharedAccount;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.UserHelper;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 绑定手机
 */
public class BindingPhoneActivity extends BaseActivity implements CodeCountDownTimer.CodeCountDownTimerListener {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    private UserBean bean;
    @InjectView(R.id.obtainCode_bt)
    Button mObtainCodeBt;
    @InjectView(R.id.et_mobile)
    EditText mEdMolile;
    @InjectView(R.id.et_code)
    EditText mEdCode;//验证码
    @InjectView(R.id.tgid_et)
    EditText tgidEt;//验证码

    @Override
    public int getContentViewId() {
        return R.layout.activity_binding_phone;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            bean = (UserBean) bundle.getSerializable("bean");
        }
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("绑定手机");
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);
        initCountDownTimer();
        UIHelper.ToastMessage(this,"先绑定手机号");
    }


    private CodeCountDownTimer mCountDownTimer;//获取验证码倒计时


    private void initCountDownTimer() {
        mCountDownTimer = new CodeCountDownTimer(60 * 1000, 1000);
        mCountDownTimer.setCodeCountDownTimerListener(this);
    }

    private String mMobile;

    String codeStr = "-000k";

    private void ajaxObtainCode(String mobile) {
        this.mMobile = mobile;//获取验证码的手机号
        codeStr = RandomUtil.generateNumberString(6);//随机生成的六位验证码
        L.d("codeStr", codeStr);
        HttpClient httpClient = HttpClient.newInstance(this);

        KangQiMeiApi api = new KangQiMeiApi("public/send_sms");
        api.add("phone", mobile);
        api.add("code", codeStr);

        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                mCountDownTimer.start();
                UIHelper.ToastMessage(getContext(), response.getInfo());
            }
        });
    }


    @Override
    public void onTick(long l) {
        if (mObtainCodeBt != null) {
            mObtainCodeBt.setText(l / 1000 + "s后重新获取");
            mObtainCodeBt.setClickable(false);
            mObtainCodeBt.setTextSize(11);
        }
    }

    @Override
    public void onFinish() {
        if (mObtainCodeBt != null) {
            mObtainCodeBt.setText(getString(R.string.obtain_code));
            mObtainCodeBt.setClickable(true);
            mObtainCodeBt.setTextSize(14);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        if (!UserHelper.getInstance(this).hasLogin()){//没有绑定手机号,退出环信，
            DemoHelper.getInstance().logout(false, null);  //退出环信
        }
    }


    @OnClick({R.id.obtainCode_bt, R.id.sure_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.obtainCode_bt:
                mMobile = CommonUtils.getStringByEditText(mEdMolile);//电话号码
                if (StringUtils.isEmpty(mMobile)) {
                    UIHelper.ToastMessage(this, R.string.input_phone_number);
                    return;
                }
                if (!StringUtils.isMobile(mMobile)) {
                    Toast.makeText(this, R.string.not_mobile_format, Toast.LENGTH_SHORT).show();
                    return;
                }
                KangQiMeiApi api = new KangQiMeiApi("Public/user_search");
                api.add("phone",mMobile);
                HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<UserBean>>() {
                    @Override
                    public void onResponse(NoPageListBean<UserBean> response) {
                        if (isFinishing()){
                            return;
                        }
                        if (!StringUtils.isEmpty(response.data)){
                            UIHelper.ToastMessage(getContext(),"该手机号已经被其他用户绑定");
                            return;
                        }
                        ajaxObtainCode(mMobile);
                    }
                });

                break;
            case R.id.sure_bt:
                if (StringUtils.isEmpty(mMobile)) {
                    UIHelper.ToastMessage(this, R.string.input_phone_number);
                    return;
                }
                if (!StringUtils.isMobile(mMobile)) {
                    Toast.makeText(this, R.string.not_mobile_format, Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = CommonUtils.getStringByEditText(mEdCode);//
                if (StringUtils.isEmpty(code)) {
                    UIHelper.ToastMessage(this, R.string.input_code);
                    return;
                }
                if (!code.equals(codeStr)) {
                    UIHelper.ToastMessage(this, R.string.code_error);
                    return;
                }
                loadBindingPhone();
                break;
        }
    }

    private void loadBindingPhone() {
        KangQiMeiApi api = new KangQiMeiApi("member/update");
        api.add("uid",bean.getId());
        api.add("phone",mMobile);
        api.add("tgid",CommonUtils.getStringByEditText(tgidEt));
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()){
                    return;
                }
                EventBus.getDefault().post(new LoginQuitEvent());//
                UIHelper.ToastMessage(getContext(),response.getInfo());
                UserHelper.getInstance(getContext()).saveBean(bean);
                CommonUtils.loadUserInfo(KangApp.applicationContext,null);
                SharedAccount.getInstance(getContext()).saveMobile(mMobile);
                MainActivity.showHome(getContext());
                finish();
            }
        });
    }



}
