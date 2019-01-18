package com.lanmei.kang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.util.CodeCountDownTimer;
import com.lanmei.kang.util.RandomUtil;
import com.xson.common.api.AbstractApi;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

/**
 * Created by xkai on 2017/8/8.
 * 更换手机号码(自定义view)
 */

public class ChangePhoneView extends LinearLayout implements CodeCountDownTimer.CodeCountDownTimerListener {

    private Context context;
    private Button mNextStepBt;//下一步
    private EditText mPhoneEt;//手机号码
    private TextView mObtainCodeTv;//获取验证码
    private EditText mCodeEt;//输入验证码
    private TextView mTitleTv;//弹框内容
    private String mPhone;
    private CodeCountDownTimer mCountDownTimer;//获取验证码倒计时
    private boolean isChangePhone;
    public static String changePhone = "changePhone";


    public ChangePhoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mNextStepBt = (Button) findViewById(R.id.bt_next_step);
        mPhoneEt = (EditText) findViewById(R.id.phone_et);
        mPhoneEt.setEnabled(false);
        mObtainCodeTv = (TextView) findViewById(R.id.obtain_code_tv);
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mCodeEt = (EditText) findViewById(R.id.code_et);
        mNextStepBt.setOnClickListener(new OnClickListener() {//下一步
            @Override
            public void onClick(View v) {
                mPhone = mPhoneEt.getText().toString().trim();
                if (StringUtils.isEmpty(mPhone)) {
                    UIHelper.ToastMessage(context, context.getString(R.string.input_phone_number));
                    return;
                }
                if (!StringUtils.isMobile(mPhone)) {
                    UIHelper.ToastMessage(context, context.getString(R.string.not_mobile_format));
                    return;
                }
                String code = mCodeEt.getText().toString().trim();
                if (StringUtils.isEmpty(code)) {
                    UIHelper.ToastMessage(context, context.getString(R.string.input_code));
                    return;
                }
                if (!code.equals(codeStr)) {
                    UIHelper.ToastMessage(context, context.getString(R.string.code_error));
                    return;
                }
                if (StringUtils.isSame(changePhone, type)) {//更换手机
                    mNextStepBt.setText("确认并更换");
                    mPhoneEt.setText("");
                    mCodeEt.setText("");
                    mPhoneEt.setHint("请输入需要更新的手机号");
                    mPhoneEt.setEnabled(true);
                    mCountDownTimer.cancel();
                    mCountDownTimer.onFinish();
                    if (isChangePhone) {
                        ajaxChangePhone();
                    }
                    isChangePhone = true;
                } else {//解绑银行卡
                    loadUnBoundCar();
                }

            }
        });
        mObtainCodeTv.setOnClickListener(new OnClickListener() {//获取验证码
            @Override
            public void onClick(View v) {
                if (isChangePhone && StringUtils.isSame(changePhone, type)) {//更换手机(确认并更换)
                    loadCheckPhone();//先检查手机号是否被占用
                } else {//解除绑定银行卡
                    ajaxObtainCode();
                }

            }
        });
        //初始化倒计时
        initCountDownTimer();
    }


    private void loadCheckPhone() {
        KangQiMeiApi api = new KangQiMeiApi("Public/user_search");
        api.add("phone", mPhone);
        HttpClient.newInstance(getContext()).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<UserBean>>() {
            @Override
            public void onResponse(NoPageListBean<UserBean> response) {
                if (mNextStepBt == null) {
                    return;
                }
                if (!StringUtils.isEmpty(response.data)) {
                    UIHelper.ToastMessage(getContext(), "该手机号已经被其他用户绑定");
                    return;
                }
                ajaxObtainCode();
            }
        });
    }

    private void loadUnBoundCar() {
        HttpClient httpClient = HttpClient.newInstance(context);
        KangQiMeiApi api = new KangQiMeiApi("member/bank_card");
        api.add("uid", api.getUserId(getContext()));
        api.add("del", 1);//表示删除
        api.add("id", type);
        api.setMethod(AbstractApi.Method.GET);
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (mChangePhoneListener != null) {
                    mChangePhoneListener.unBound();
                }
            }
        });
    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    private void ajaxChangePhone() {
        HttpClient httpClient = HttpClient.newInstance(context);
        KangQiMeiApi api = new KangQiMeiApi("member/update");
        api.add("token", api.getToken(context));
        api.add("phone", mPhone);
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                UIHelper.ToastMessage(context, "更新号码成功");
                if (mChangePhoneListener != null) {
                    mChangePhoneListener.succeed(mPhone);
                }
            }
        });
    }

    String codeStr = "好神奇的验证码";

    //获取验证码
    private void ajaxObtainCode() {
        codeStr = RandomUtil.generateNumberString(6);//随机生成的六位验证码
        HttpClient httpClient = HttpClient.newInstance(context);
        KangQiMeiApi api = new KangQiMeiApi("public/send_sms");
        api.add("phone", mPhone);
        api.add("code", codeStr);
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (mCountDownTimer == null) {
                    return;
                }
                mCountDownTimer.start();
                UIHelper.ToastMessage(context, response.getInfo());
            }
        });
    }

    private void initCountDownTimer() {
        mCountDownTimer = new CodeCountDownTimer(60 * 1000, 1000);
        mCountDownTimer.setCodeCountDownTimerListener(this);
    }

    public void setPhone(String phone) {
        mPhone = phone;
        mPhoneEt.setText(phone);
    }

    String type;//changePhone为更换手机号，否则为解绑银行卡

    public void setType(String type) {
        this.type = type;
        if (!StringUtils.isSame(changePhone, type)) {
            mNextStepBt.setText("确定");
        }
    }

//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        if (mCountDownTimer != null) {
//            mCountDownTimer.cancel();
//        }
//    }

    @Override
    public void onTick(long l) {
        if (mObtainCodeTv != null) {
            mObtainCodeTv.setText(l / 1000 + context.getString(R.string.s_regain));
            mObtainCodeTv.setClickable(false);
            mObtainCodeTv.setTextSize(12);
        }
    }

    @Override
    public void onFinish() {
        if (mObtainCodeTv != null) {
            mObtainCodeTv.setText(context.getString(R.string.obtain_code));
            mObtainCodeTv.setClickable(true);
            mObtainCodeTv.setTextSize(16);
        }
    }

    ChangePhoneListener mChangePhoneListener;

    public void setChangePhoneListener(ChangePhoneListener l) {
        mChangePhoneListener = l;
    }

    public interface ChangePhoneListener {
        void succeed(String newPhone);//更新号码成功监听

        void unBound();//解绑银行卡
    }

}
