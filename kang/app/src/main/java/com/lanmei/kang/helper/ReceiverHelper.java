package com.lanmei.kang.helper;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by xkai on 2017/8/17.
 * 广播接收帮助类
 */

public class ReceiverHelper {

    Context context;
    BroadcastReceiverHelper broadcastReceiverHelper;
    IntentFilter filter;

    public ReceiverHelper(Context context) {
        this.context = context;
        filter = new IntentFilter();
        broadcastReceiverHelper = new BroadcastReceiverHelper();
        broadcastReceiverHelper.setOnReceiveListener(new BroadcastReceiverHelper.OnReceiveListener() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mReceiveHelperListener != null) {
                    mReceiveHelperListener.onReceive(context, intent);
                }
            }
        });
    }

    public void addAction(String action) {
        filter.addAction(action);
    }

    public void registerReceiver() {
        context.registerReceiver(broadcastReceiverHelper, filter);
    }

    ReceiveHelperListener mReceiveHelperListener;

    public void setReceiveHelperListener(ReceiveHelperListener l) {
        mReceiveHelperListener = l;
    }

    public interface ReceiveHelperListener {
        void onReceive(Context context, Intent intent);
    }

    public void onDestroy() {
        context.unregisterReceiver(broadcastReceiverHelper);
    }
}
