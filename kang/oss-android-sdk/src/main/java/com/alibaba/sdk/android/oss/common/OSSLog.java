package com.alibaba.sdk.android.oss.common;

import android.util.Log;

/**
 * Created by zhouzhuo on 11/22/15.
 */
public class OSSLog {

    private static final String TAG = "OSS-Android-SDK";
    public static boolean enableLog = true;


    /**
     * info级别log
     *
     * @param msg
     */
    public static void logI(String msg) {
        if (enableLog) {
            Log.i(TAG, msg);
        }
    }

    /**
     * verbose级别log
     *
     * @param msg
     */
    public static void logV(String msg) {
        if (enableLog) {
            Log.v(TAG, msg);
        }
    }

    /**
     * warning级别log
     *
     * @param msg
     */
    public static void logW(String msg) {
        if (enableLog) {
            Log.w(TAG, msg);
        }
    }

    /**
     * debug级别log
     *
     * @param msg
     */
    public static void logD(String msg) {
        if (enableLog) {
            Log.d(TAG, msg);
        }
    }

    /**
     * error级别log
     *
     * @param msg
     */
    public static void logE(String msg) {
        if (enableLog) {
            Log.e(TAG, msg);
        }
    }
}
