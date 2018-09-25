package com.lanmei.kang.loader;

import android.content.Context;
import android.content.Intent;

import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.lanmei.kang.KangApp;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.api.SystemSettingInfoApi;
import com.lanmei.kang.api.UserInfoApi;
import com.lanmei.kang.bean.NewsCategoryTabBean;
import com.lanmei.kang.bean.SystemSettingInfoBean;
import com.lanmei.kang.bean.UserInfoBean;
import com.lanmei.kang.event.SetUserInfoEvent;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.UserHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * Created by xkai on 2017/7/7.
 */

public class DataLoader {

    private static DataLoader sInstance;
    private UserBean mUserBean;

    public static DataLoader getInstance() {
        if (sInstance == null) {
            sInstance = new DataLoader();
        }
        return sInstance;
    }

    public void clear() {
        sInstance = null;
    }

    //加载用户数据
    public void loadUserInfo(final Context context, final LoadUserInfoListener l) {
        HttpClient httpClient = HttpClient.newInstance(context);
        UserInfoApi api = new UserInfoApi();
        api.token = api.getToken(context);
        api.uid = api.getUserId(context);
        httpClient.request(api, new BeanRequest.SuccessListener<DataBean<UserBean>>() {
            @Override
            public void onResponse(DataBean<UserBean> response) {
                mUserBean = response.data;
                if (l != null) {
                    l.succeed(mUserBean);
                }
                if (mUserBean != null) {
                    UserHelper.getInstance(context).saveBean(mUserBean);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorStr = error.getMessage().toString();//请先登录
                if ("请先登录".equals(errorStr)) {
                    UserHelper.getInstance(context).cleanLogin();
                    EventBus.getDefault().post(new SetUserInfoEvent());
                }
                if (l != null) {
                    l.failure(error.getMessage().toString());
                }
            }
        });
    }


    public UserBean getUserBean() {
        return mUserBean;
    }

    public interface LoadUserInfoListener {
        void succeed(UserBean bean);

        void failure(String error);//获取用户信息失败
    }

    public void loadLogin(final Context context, final String phone, final String pwd, final LoginListener listener) {
        HttpClient httpClient = HttpClient.newInstance(context);
        KangQiMeiApi api = new KangQiMeiApi("public/login");
        api.addParams("phone",phone);
        api.addParams("password",pwd);

        httpClient.request(api, new BeanRequest.SuccessListener<DataBean<UserBean>>() {
            @Override
            public void onResponse(DataBean<UserBean> response) {
                UserBean bean = response.data;
                if (listener != null) {
                    listener.succeed(bean);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UserHelper.getInstance(context).cleanLogin();
                if (listener != null) {
                    listener.failure(error.getMessage().toString());
                }
            }
        });

    }

    //登录监听
    public interface LoginListener {
        void succeed(UserBean bean);//登录成功

        void failure(String error);//登录失败
    }

    SystemSettingInfoBean systemInfoBean;//系统基本设置信息
    UserInfoBean systemInfoUserBean;//系统基本设置信息

    /**
     * 加载系统基本设置信息
     *
     * @param context
     */
    public void loadSystemSettingInfo(final Context context, final SystemInfoListener l) {
        if (systemInfoUserBean != null) {
            if (l != null) {
                l.succeed(systemInfoUserBean);
            }
            return;
        }
        HttpClient httpClient = HttpClient.newInstance(context);
        SystemSettingInfoApi api = new SystemSettingInfoApi();
        httpClient.request(api, new BeanRequest.SuccessListener<DataBean<SystemSettingInfoBean>>() {
            @Override
            public void onResponse(DataBean<SystemSettingInfoBean> response) {
                systemInfoBean = response.data;
                if (systemInfoBean != null) {
                    final SystemSettingInfoBean.BaseBean baseBean = systemInfoBean.getBase();
                    if (baseBean != null) {
                        DemoHelper.getInstance().getUserBean(KangApp.HX_USER_Head + baseBean.getPlatform_service(), false, new DemoHelper.UserInfoListener() {
                            @Override
                            public void succeed(UserInfoBean bean) {
                                bean.setNickname("在线客服");
                                systemInfoUserBean = bean;
                                if (l != null) {
                                    l.succeed(systemInfoUserBean);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public SystemSettingInfoBean getSystemInfoBean() {

        return systemInfoBean;
    }

    public UserInfoBean getSystemInfoUserBean() {

        return systemInfoUserBean;
    }

    public interface SystemInfoListener {
        void succeed(UserInfoBean bean);
    }


    List<NewsCategoryTabBean> tabListBean;//资讯Tab数据

    public String getCid(String cname) {

        if (tabListBean != null && tabListBean.size() > 0) {
            int size = tabListBean.size();
            for (int i = 0; i < size; i++) {
                NewsCategoryTabBean bean = tabListBean.get(i);
                if (bean != null) {
                    if (cname.equals(bean.getName())) {
                        return bean.getId();
                    }
                }
            }
        }

        return "";
    }

    /**
     * 联系在线客服
     *
     * @param context
     */
    public void onlineService(final Context context) {
        UserInfoBean bean = DataLoader.getInstance().getSystemInfoUserBean();
        if (bean != null) {
            intoChat(context, bean);
        } else {
            DataLoader.getInstance().loadSystemSettingInfo(context, new DataLoader.SystemInfoListener() {
                @Override
                public void succeed(UserInfoBean bean) {
                    if (bean != null) {
                        intoChat(context, bean);
                    }
                }
            });
        }
    }

    private void intoChat(Context context, UserInfoBean bean) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constant.EXTRA_USER_ID, KangApp.HX_USER_Head + bean.getId());
        context.startActivity(intent);
    }


}
