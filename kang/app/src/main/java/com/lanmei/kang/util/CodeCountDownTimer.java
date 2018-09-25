package com.lanmei.kang.util;

import android.os.CountDownTimer;

/**
 * 倒计时
 * Created by xkai on 2017/5/4.
 */

public class CodeCountDownTimer extends CountDownTimer {

    public CodeCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long l) {
        if (codeCountDownTimerListener != null){
            codeCountDownTimerListener.onTick(l);
        }
    }

    @Override
    public void onFinish() {
        if (codeCountDownTimerListener != null){
            codeCountDownTimerListener.onFinish();
        }
    }
    public interface CodeCountDownTimerListener {
        void onTick(long l);
        void onFinish();
    }
    public CodeCountDownTimerListener codeCountDownTimerListener;

    public void setCodeCountDownTimerListener(CodeCountDownTimerListener countDownTimerListener){
        codeCountDownTimerListener = countDownTimerListener;
    }
}
