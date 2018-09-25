package com.lanmei.kang.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by xkai on 2017/8/17.
 */

public class BroadcastReceiverHelper extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mOnReceiveListener != null){
            mOnReceiveListener.onReceive(context,intent);
        }
    }

    OnReceiveListener mOnReceiveListener;

    public void setOnReceiveListener(OnReceiveListener l){
        mOnReceiveListener = l;
    }

    public interface OnReceiveListener{
        void onReceive(Context context, Intent intent);
    }

}
