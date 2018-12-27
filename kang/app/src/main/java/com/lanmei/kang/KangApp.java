package com.lanmei.kang;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.common.OSSLog;
import com.baidu.mapapi.SDKInitializer;
import com.easemob.redpacketsdk.RPInitRedPacketCallback;
import com.easemob.redpacketsdk.RPValueCallback;
import com.easemob.redpacketsdk.RedPacket;
import com.easemob.redpacketsdk.bean.RedPacketInfo;
import com.easemob.redpacketsdk.bean.TokenData;
import com.easemob.redpacketsdk.constant.RPConstant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.lanmei.kang.update.UpdateAppConfig;
import com.lanmei.kang.util.Constant;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.xson.common.app.BaseApp;
import com.xson.common.utils.L;

/**
 * Created by xkai on 2017/5/5.
 */

public class KangApp extends BaseApp {
    public static Context applicationContext;
    private static KangApp instance;

    public static final String HX_USER_Head = "u_";

    @Override
    public void onCreate() {
        SDKInitializer.initialize(this);//百度地图
        super.onCreate();
    }

    @Override
    protected void installMonitor() {
        applicationContext = this;
        instance = this;
        L.debug = OSSLog.enableLog = true;
        if (L.debug) {
//            LeakCanary.install(this);//LeakCanary内存泄漏监控
        }
        UpdateAppConfig.initUpdateApp(this);//app版本更新
        initHx();
        //友盟初始化设置
        initUM();

    }

    public void initUM() {

        PlatformConfig.setWeixin(Constant.WEIXIN_APP_ID, Constant.WEIXIN_APP_SECRET);

        UMConfigure.setLogEnabled(L.debug);//如果查看初始化过程中的LOG，一定要在调用初始化方法前将LOG开关打开。

        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
    }

    //环信初始化设置
    private void initHx() {
        //init demo helper
        DemoHelper.getInstance().init(applicationContext);
        //red packet code : 初始化红包SDK，开启日志输出开关
        RedPacket.getInstance().initRedPacket(applicationContext, RPConstant.AUTH_METHOD_EASEMOB, new RPInitRedPacketCallback() {

            @Override
            public void initTokenData(RPValueCallback<TokenData> callback) {
                TokenData tokenData = new TokenData();
                tokenData.imUserId = EMClient.getInstance().getCurrentUser();
                //此处使用环信id代替了appUserId 开发者可传入App的appUserId
                tokenData.appUserId = EMClient.getInstance().getCurrentUser();
                tokenData.imToken = EMClient.getInstance().getAccessToken();
                //同步或异步获取TokenData 获取成功后回调onSuccess()方法
                callback.onSuccess(tokenData);
            }

            @Override
            public RedPacketInfo initCurrentUserSync() {
                //这里需要同步设置当前用户id、昵称和头像url
                String fromAvatarUrl = "";
                String fromNickname = EMClient.getInstance().getCurrentUser();
                EaseUser easeUser = EaseUserUtils.getUserInfo(fromNickname);
                if (easeUser != null) {
                    fromAvatarUrl = TextUtils.isEmpty(easeUser.getAvatar()) ? "none" : easeUser.getAvatar();
                    fromNickname = TextUtils.isEmpty(easeUser.getNick()) ? easeUser.getUsername() : easeUser.getNick();
                }
                RedPacketInfo redPacketInfo = new RedPacketInfo();
                redPacketInfo.fromUserId = EMClient.getInstance().getCurrentUser();
                redPacketInfo.fromAvatarUrl = fromAvatarUrl;
                redPacketInfo.fromNickName = fromNickname;
                return redPacketInfo;
            }
        });
        RedPacket.getInstance().setDebugMode(true);
        //end of red packet code
    }

    @Override
    public void watch(Object object) {

    }

    public static KangApp getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
