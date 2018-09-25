package com.hyphenate.chatuidemo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.runtimepermissions.PermissionsManager;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.util.EasyUtils;
import com.lanmei.kang.R;
import com.lanmei.kang.bean.UserInfoBean;

import static com.hyphenate.chatuidemo.ui.ConversationListFragment.REFRESH_CONVERSATIONLIST;

/**
 * chat activity，EaseChatFragment was used {@link #EaseChatFragment}
 *
 */
public class ChatActivity extends BaseActivity{
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    public String toChatUsername;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);

        IntentFilter filter = new IntentFilter();
        filter.addAction(REFRESH_CONVERSATIONLIST); //刷新环信会话列表广播
        registerReceiver(mReceiver, filter);

        activityInstance = this;
        //get user id or group id
        toChatUsername = getIntent().getExtras().getString("userId");
        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
        unregisterReceiver(mReceiver);
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	// make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
    
    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    
    public String getToChatUsername(){
        return toChatUsername;
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String userName = intent.getStringExtra("username");
            if (REFRESH_CONVERSATIONLIST.equals(action)) {
                if (DemoHelper.getInstance().userBeanMap.containsKey(userName)){
                    UserInfoBean bean = DemoHelper.getInstance().userBeanMap.get(userName);
                    if (bean != null){
                        chatFragment.setTitlebar(bean.getNickname());
                    }
                }
            }
        }
    };

}
