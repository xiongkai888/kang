package com.lanmei.kang.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.db.DemoDBManager;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.lanmei.kang.KangApp;
import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.event.LoginQuitEvent;
import com.lanmei.kang.event.RegisterEvent;
import com.lanmei.kang.event.SetUserInfoEvent;
import com.lanmei.kang.ui.MainActivity;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.SharedAccount;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.UserHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.DrawClickableEditText;
import com.xson.common.widget.ProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.mobile_et)
    DrawClickableEditText mMobileET;
    @InjectView(R.id.pwd_et)
    EditText mPwdET;
    @InjectView(R.id.iv_showPwd)
    ImageView mIvshowPwd;

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    /**
     * 登录等待
     */
    ProgressHUD mProgressHUD;

    private void initProgressDialog() {
        mProgressHUD = ProgressHUD.show(this, "正在登录...", true, false, null);
        mProgressHUD.cancel();
        mProgressHUD.setCancelable(true);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initProgressDialog();
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.login);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);
        mShareAPI = UMShareAPI.get(this);
        String mobile = SharedAccount.getInstance(this).getMobile();
        mMobileET.setText(mobile);
//        mPwdET.setText("123456");
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    public static void startActivityForResult(Activity context, int requstCode) {
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivityForResult(i, requstCode);
    }

    public static void startActivityForResult(Fragment fragment, int requstCode) {
        Intent i = new Intent(fragment.getContext(), LoginActivity.class);
        fragment.startActivityForResult(i, requstCode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_register:
                RegisterActivity.startActivity(this, RegisterActivity.REGISTER_STYLE);//注册
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.forgotPwd_tv)
    public void showForgotPwd() {//忘记密码
        RegisterActivity.startActivity(this, RegisterActivity.FORGOT_PWD_STYLE);
    }

    private boolean isShowPwd = false;//是否显示密码

    @OnClick(R.id.iv_showPwd)
    public void showPassWord() {
        if (!isShowPwd) {//显示密码
            mPwdET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mIvshowPwd.setImageResource(R.mipmap.pwd_on);
        } else {//隐藏密码
            mPwdET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mIvshowPwd.setImageResource(R.mipmap.pwd_off);
        }
        isShowPwd = !isShowPwd;
    }

    String phone;
    UserBean mBean;

    String userType;
    String currentPassword = "123456";
    String currentUsername;

    public void loginHx() {
        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
        if (mBean != null) {
            userType = mBean.getUser_type();
            currentUsername = KangApp.HX_USER_Head + mBean.getId();
        }
        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }


        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(currentUsername);

        EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                if (!isFinishing() && mProgressHUD.isShowing()) {
                    mProgressHUD.dismiss();
                }
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                if (StringUtils.isEmpty(mBean.getPhone())){//要是手机号为空就绑定手机
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean",mBean);
                    IntentUtil.startActivity(getContext(),BindingPhoneActivity.class,bundle);
                }else {
                    EventBus.getDefault().post(new LoginQuitEvent());//
                    UserHelper.getInstance(LoginActivity.this).saveBean(mBean);
                    CommonUtils.loadUserInfo(KangApp.applicationContext,null);
                    if (StringUtils.isEmpty(loginType)) {//手机号登录时保存
                        SharedAccount.getInstance(LoginActivity.this).saveMobile(phone);
                    }
                    IntentUtil.startActivity(LoginActivity.this, MainActivity.class);
                }
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
//                Log.d(TAG, "login: onError: " + code);
                runOnUiThread(new Runnable() {
                    public void run() {
                        mProgressHUD.dismiss();
                        UserHelper.getInstance(LoginActivity.this).cleanLogin();
                        EventBus.getDefault().post(new SetUserInfoEvent(null));
                        Toast.makeText(getApplicationContext(), "登录超时，请重新登录！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Subscribe
    public void onEventMainThread(RegisterEvent event) {
        mMobileET.setText(event.getPhone());
    }

    //登录时候调用
    @Subscribe()
    public void loginQuitEvent(LoginQuitEvent event) {
        finish();
    }


    private String loginType;//登录的类型     1、微信  2、QQ  3、新浪微博
    private UMShareAPI mShareAPI;//友盟第三方登录api

    private void doOauthVerify(SHARE_MEDIA platform, String loginType) {
        mProgressHUD.show();
        this.loginType = loginType;
        mShareAPI.doOauthVerify(this, platform, umAuthListener);
    }

    private void getUserInfo(SHARE_MEDIA platform) {

        mShareAPI.getPlatformInfo(this, platform, new UMAuthListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {
//                L.d("impower", "onStart");
            }

            @Override
            public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {
                mProgressHUD.cancel();
            }

            @Override
            public void onComplete(SHARE_MEDIA arg0, int arg1, Map<String, String> data) {
                mProgressHUD.cancel();
                // 拿到具体数据
                switch (loginType) {
                    case "1"://微信
                        otherTypeLogin(loginType, data.get("openid"), data.get("name"),
                                data.get("iconurl"));
                        break;
                    case "2"://QQ
                        otherTypeLogin(loginType, data.get("openid"), data.get("screen_name"),
                                data.get("profile_image_url"));
                        break;
                    case "3"://新浪
                        otherTypeLogin(loginType, data.get("id"),
                                data.get("screen_name"),
                                data.get("iconurl"));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA arg0, int arg1) {
                mProgressHUD.cancel();
            }
        });
    }

    protected void otherTypeLogin(String loginType, String openid, String userName, final String userImgUrl) {
        HttpClient httpClient = HttpClient.newInstance(this);
        KangQiMeiApi api = new KangQiMeiApi("public/login");
        api.add("open_type",loginType);
        api.add("open_id",openid);
        api.add("nickname",userName);
        api.add("pic",userImgUrl);
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<DataBean<UserBean>>() {
            @Override
            public void onResponse(DataBean<UserBean> response) {
                if (isFinishing()){
                    return;
                }
                mBean = response.data;
                loginHx();
            }
        });
    }

    //第三方登录授权监听
    private UMAuthListener umAuthListener = new UMAuthListener() {

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            getUserInfo(platform);
            UIHelper.ToastMessage(getContext(), getString(R.string.impower_succeed));
            mProgressHUD.cancel();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            UIHelper.ToastMessage(getContext(), getString(R.string.impower_failed));
            mProgressHUD.cancel();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            UIHelper.ToastMessage(getContext(), getString(R.string.impower_cancel));
            mProgressHUD.cancel();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mShareAPI.get(this).release();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.get(this).onActivityResult(requestCode, resultCode, data);//完成回调
    }

    @OnClick({R.id.bt_login, R.id.iv_weixinlogin, R.id.iv_qqlogin, R.id.iv_weibologin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_login://登录
                login();
                break;
            case R.id.iv_weixinlogin://微信登录
                doOauthVerify(SHARE_MEDIA.WEIXIN, CommonUtils.isOne);
                break;
            case R.id.iv_qqlogin://qq登录
//                doOauthVerify(SHARE_MEDIA.QQ, "2");
                break;
            case R.id.iv_weibologin://微博登录
//                doOauthVerify(SHARE_MEDIA.SINA, "3");
                break;
        }
    }

    private void login() {
        loginType = "";
        phone = mMobileET.getText().toString();
        String pwd = mPwdET.getText().toString();
        if (StringUtils.isEmpty(phone)) {
            Toast.makeText(this, R.string.input_phone_number, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isMobile(phone)) {
            Toast.makeText(this, R.string.not_mobile_format, Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(pwd) || pwd.length() < 6) {
            Toast.makeText(this, R.string.input_password_count, Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressHUD.show();
        KangQiMeiApi api = new KangQiMeiApi("public/login");
        api.add("phone",phone);
        api.add("password",pwd);
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<DataBean<UserBean>>() {
            @Override
            public void onResponse(DataBean<UserBean> response) {
                if (isFinishing()) {
                    return;
                }
                mBean = response.data;
                loginHx();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isFinishing()) {
                    return;
                }
                mProgressHUD.cancel();
                UserHelper.getInstance(getContext()).cleanLogin();
                UIHelper.ToastMessage(LoginActivity.this, error.getMessage());
            }
        });
    }

}