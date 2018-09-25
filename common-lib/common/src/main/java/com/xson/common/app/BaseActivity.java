package com.xson.common.app;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.xson.common.R;
import com.xson.common.utils.L;
import com.xson.common.utils.SysUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * @author Milk <249828165@qq.com>
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final int RC_PERMISSION_STORAGE = 110;
    private boolean isAttached;
    private ArrayList<DispatchTouchEventListener> dispatchTouchEventListeners;

    /**
     *
     * @return contentViewId   布局Id
     */
    public abstract int getContentViewId();

    /**
     *
     * @param savedInstanceState  onCreate()中的参数,Bundle类型
     */
    protected abstract void initAllMembersView(Bundle savedInstanceState);
    public interface DispatchTouchEventListener {
        void onDispatchTouchEvent(MotionEvent event);
    }

    public void initIntent(Intent intent){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ((BaseApp)getApplication()).watch(this);
        ButterKnife.reset(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        initIntent(getIntent());
        ButterKnife.inject(this, this);
        initAllMembersView(savedInstanceState);
        if (SysUtils.isMonkeyTester(this)) {//Monkey Test
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED
                ) {
            requestPermission();
        }

        if (SysUtils.isAutoTester(this) || SysUtils.isMonkeyTester(this)) {
            hideStatusBar();
        }
    }

    private void requestPermission() {
        final String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.READ_PHONE_STATE
        };
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.need_to_enable_read_storage_permissions)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BaseActivity.this, permissions, RC_PERMISSION_STORAGE);
                            finish();
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, permissions, RC_PERMISSION_STORAGE);
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode){
//            case KeyEvent.KEYCODE_HOME:
//                return true;
//            case KeyEvent.KEYCODE_BACK:
//                return true;
//            case KeyEvent.KEYCODE_CALL:
//                return true;
//            case KeyEvent.KEYCODE_SYM:
//                return true;
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                return true;
//            case KeyEvent.KEYCODE_VOLUME_UP:
//                return true;
//            case KeyEvent.KEYCODE_STAR:
//                return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    public Context getContext() {
        return this;
    }

    private void hideStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getName());
        MobclickAgent.onPause(this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttached = true;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttached = false;
    }

    public boolean isAttached() {
        return isAttached;
    }

    public boolean isDetached() {
        return !isAttached;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (dispatchTouchEventListeners != null) {
            for (DispatchTouchEventListener l : dispatchTouchEventListeners) {
                l.onDispatchTouchEvent(ev);
            }
        }
        //fix PhotoViewAttacher bug
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            L.e(e);
            return true;
        }
    }

    public void addDispatchTouchEventListener(DispatchTouchEventListener l) {
        if (dispatchTouchEventListeners == null)
            dispatchTouchEventListeners = new ArrayList<>();
        dispatchTouchEventListeners.add(l);
    }

    public void removeDispatchTouchEventListener(DispatchTouchEventListener l) {
        if (dispatchTouchEventListeners == null)
            return;
        dispatchTouchEventListeners.remove(l);
    }

}
