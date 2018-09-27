package com.lanmei.kang;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

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
import com.lanmei.kang.util.Constant;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.xson.common.app.BaseApp;

/**
 * Created by Administrator on 2017/5/5.
 */

public class KangApp extends BaseApp {
    public static Context applicationContext;
    private static KangApp instance;
    // login user name
    public final String PREF_USERNAME = "username";

    public static final String HX_USER_Head="u_";

    /**
     * nickname for current user, the nickname instead of ID be shown when user receive notification from APNs
     */
    public static String currentUserNick = "";

    @Override
    public void onCreate() {
//        MultiDex.install(this);
        SDKInitializer.initialize(this);//百度地图
        super.onCreate();
    }

    @Override
    protected void installMonitor() {
        initHx();
        //友盟初始化设置
        initUM();

    }

    public void initUM() {

        PlatformConfig.setWeixin(Constant.WEIXIN_APP_ID,
                Constant.WEIXIN_APP_SECRET);
//
//        PlatformConfig.setSinaWeibo(Constant.SINA_APP_ID,
//                Constant.SINA_APP_SECRET,Constant.SINA_NOTIFY_URL);
//
//        PlatformConfig.setQQZone(Constant.QQ_APP_ID, Constant.QQ_APP_SECRET);

        Config.DEBUG = true;//筛选内容umeng_tool
    }

    //环信初始化设置
    private void initHx(){
        // ============== fabric start
        //		Fabric.with(this, new Crashlytics());
        // ============== fabric end
        applicationContext = this;
        instance = this;

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