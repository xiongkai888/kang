package com.lanmei.kang.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.event.RegisterEvent;
import com.lanmei.kang.util.CodeCountDownTimer;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.RandomUtil;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.FormatTextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 注册、忘记密码、重设密码
 */
public class RegisterActivity extends BaseActivity implements CodeCountDownTimer.CodeCountDownTimerListener, Toolbar.OnMenuItemClickListener {

    public static int REGISTER_STYLE = 1;
    public static int FORGOT_PWD_STYLE = 2;
    public static int RESET_PWD_STYLE = 3;

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.bt_register)
    Button mButton;
    @InjectView(R.id.et_mobile)
    EditText mEdMolile;
    @InjectView(R.id.nick_name_et)
    EditText nickNameEt;//姓名
    @InjectView(R.id.et_pwd)
    EditText mEdPwd;
    @InjectView(R.id.pwd_again_et)
    EditText pwdAgainEt;
    @InjectView(R.id.iv_showPwd)
    ImageView mIvshowPwd;
    @InjectView(R.id.et_code)
    EditText mEdCode;
    @InjectView(R.id.tgid_et)
    EditText tgidEt;
    @InjectView(R.id.ll_tgid)
    LinearLayout llTgid;
    @InjectView(R.id.ll_name)
    LinearLayout mllName;

    @InjectView(R.id.obtainCode_bt)
    Button mObtainCodeBt;
    @InjectView(R.id.agree_protocol_tv)
    FormatTextView agreeProtocolTv;
    @InjectView(R.id.ll_toolbar)
    LinearLayout llToolbar;

    private CodeCountDownTimer mCountDownTimer;//获取验证码倒计时

    int type;//1：注册 2：忘记密码 3：设置密码

    @Override
    public int getContentViewId() {
        return R.layout.activity_register;
    }

    public static void startActivity(Activity context, int type) {//
        Intent intent = new Intent();
        intent.setClass(context, RegisterActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        type = getIntent().getIntExtra("type", -1);
        if (type == REGISTER_STYLE) {
            mToolbar.setTitle(R.string.register);
            agreeProtocolTv.setVisibility(View.VISIBLE);
            setMenu(R.string.register);
        } else if (type == FORGOT_PWD_STYLE) {
            setMenu(R.string.submit);
            mToolbar.setTitle("找回密码");
            llTgid.setVisibility(View.GONE);
            mllName.setVisibility(View.INVISIBLE);
            agreeProtocolTv.setVisibility(View.GONE);
        } else if (RESET_PWD_STYLE == type) {//设置密码
            mToolbar.setTitle("修改密码");
            mButton.setText(R.string.submit);
            llTgid.setVisibility(View.GONE);
            mllName.setVisibility(View.INVISIBLE);
            agreeProtocolTv.setVisibility(View.GONE);
        }
        mToolbar.setNavigationIcon(R.mipmap.back_g);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //初始化倒计时
        initCountDownTimer();


    }

    private void setMenu(int titleId) {
        mButton.setText(titleId);
        mToolbar.inflateMenu(R.menu.menu_register);
        mToolbar.setOnMenuItemClickListener(this);
    }

    private void initCountDownTimer() {
        mCountDownTimer = new CodeCountDownTimer(60 * 1000, 1000);
        mCountDownTimer.setCodeCountDownTimerListener(this);
    }

    private String mMobile;



    //注册或找回密码、修改密码
    private void registerOrRetrievePwd(String nickName, String mobile, String pwd) {
        HttpClient httpClient = HttpClient.newInstance(this);
        if (type == REGISTER_STYLE) {//注册
            register(nickName, mobile, pwd, httpClient);
        } else if (type == FORGOT_PWD_STYLE || type == RESET_PWD_STYLE) {
            retrieveOrResetPwd(mobile, pwd, httpClient);//找回密码或修改密码
        }

    }


    //找回密码或修改密码
    private void retrieveOrResetPwd(final String mobile, String pwd, HttpClient httpClient) {
        KangQiMeiApi api = new KangQiMeiApi("public/resetPwd");
        api.add("phone", mobile);
        api.add("password", pwd);
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(RegisterActivity.this, response.getInfo());
                finish();
            }
        });
    }

    //注册
    private void register(String nickName, final String mobile, String pwd, HttpClient httpClient) {
        KangQiMeiApi api = new KangQiMeiApi("public/regist");
        api.add("phone", mobile);
        api.add("password", pwd);
        api.add("repassword", pwd);
        api.add("nickname", nickName);
        api.add("tgid", CommonUtils.getStringByEditText(tgidEt));
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                EventBus.getDefault().post(new RegisterEvent(mobile));
                UIHelper.ToastMessage(RegisterActivity.this, response.getInfo());
                finish();
            }
        });
    }

    String codeStr = "-000k";

    private void ajaxObtainCode(String mobile) {
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
                UIHelper.ToastMessage(RegisterActivity.this, response.getInfo());
            }
        });
    }

    private boolean isShowPwd = false;//是否显示密码

    @OnClick(R.id.iv_showPwd)
    public void showPassWord() {//是否显示密码
        if (!isShowPwd) {//显示密码
            mEdPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mIvshowPwd.setImageResource(R.mipmap.pwd_on);
        } else {//隐藏密码
            mEdPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mIvshowPwd.setImageResource(R.mipmap.pwd_off);
        }
        isShowPwd = !isShowPwd;
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
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        onBackPressed();
        return true;
    }


    @OnClick({R.id.obtainCode_bt, R.id.bt_register, R.id.agree_protocol_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.obtainCode_bt:
                //        UIHelper.ToastMessage(this,R.string.developing);
                mMobile = mEdMolile.getText().toString();//电话号码
                if (StringUtils.isEmpty(mMobile)) {
                    UIHelper.ToastMessage(this, R.string.input_phone_number);
                    return;
                }
                if (!StringUtils.isMobile(mMobile)) {
                    Toast.makeText(this, R.string.not_mobile_format, Toast.LENGTH_SHORT).show();
                    return;
                }
                ajaxObtainCode(mMobile);
                break;
            case R.id.bt_register:
                //        UIHelper.ToastMessage(this,R.string.developing);
                String nickName = "";
                if (type == REGISTER_STYLE) {//注册
                    nickName = CommonUtils.getStringByEditText(nickNameEt);
                    if (StringUtils.isEmpty(nickName)) {
                        UIHelper.ToastMessage(this, R.string.input_name);
                        return;
                    }
                }
                mMobile = CommonUtils.getStringByEditText(mEdMolile);//电话号码
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
                String pwd = CommonUtils.getStringByEditText(mEdPwd);
                if (StringUtils.isEmpty(pwd) || pwd.length() < 6) {
                    UIHelper.ToastMessage(this, R.string.input_password_count);
                    return;
                }
                if (StringUtils.isEmpty(pwd) || pwd.length() < 6) {
                    UIHelper.ToastMessage(this, R.string.input_password_count);
                    return;
                }
                String pwdAgain = CommonUtils.getStringByEditText(pwdAgainEt);
                if (!StringUtils.isSame(pwd, pwdAgain)) {
                    UIHelper.ToastMessage(this, R.string.password_inconformity);
                    return;
                }
                if (!code.equals(codeStr)) {
                    UIHelper.ToastMessage(this, R.string.code_error);
                    return;
                }
                registerOrRetrievePwd(nickName, mMobile, pwd);
                break;
            case R.id.agree_protocol_tv:
                IntentUtil.startActivity(this,ProtocolActivity.class);
                break;
        }
    }
}
