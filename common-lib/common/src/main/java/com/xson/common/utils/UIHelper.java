package com.xson.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDRootLayout;
import com.fangdr.widget.wheel.WheelDateView;
import com.xson.common.R;

import org.joor.Reflect;

import java.util.Date;


/**
 * Created by tianyi on 2014/12/25.
 */
public class UIHelper {
    /**
     * 弹出Toast消息
     *
     * @param msg
     */
    public static void ToastMessage(Context cont, String msg) {
        Toast.makeText(cont.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void ToastMessage(Context cont, int msg) {
        Toast.makeText(cont.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void ToastMessage(Context cont, String msg, int time) {
        Toast.makeText(cont.getApplicationContext(), msg, time).show();
    }



    /**
     * 跳转登录
     * @param activity
     */
//    public static void showLogin(Context activity) {
//        Intent intent = new Intent(activity, LoginActivity.class);
////        if (context instanceof MainActivity)
////            intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_MAIN);
////        else if (context instanceof Setting)
////            intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_SETTING);
////        else
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        activity.startActivity(intent);
//        if(activity instanceof Activity)
//            ((Activity)activity).overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
//    }

    /**
     * 跳转个人中心
     * @param activity
     */
//    public static void showMyInfo(Context activity) {
//        Intent intent = new Intent();
//        intent.setClass(activity, MyInfoActivity.class);
//        activity.startActivity(intent);
//    }

    /**
     * 跳转客户报备
     * @param activity
     * @param houseBean
     */
//    public static void showReport(Context activity,HouseBean houseBean){
//        UnReportListActivity.startActivity(activity, houseBean);
//    }

    /**
     * 拼接房子信息 单元号/面积/总价
     * @param context
     * @param buildFloorRoom
     * @param houseArea
     * @param totalPrice
     * @return
     */
//    public static String joinHouseInfo(Context context, String buildFloorRoom, String houseArea, String totalPrice) {
//        return String.format("%s# / %s㎡ / %s"+context.getString(R.string.ten_thousand), buildFloorRoom, houseArea, totalPrice);
//    }


//    public static String convertTimeLeft(Context context, int hour) {
//        if(hour < 24) {
//            return "<font color=red>" + String.valueOf(hour) + "</font>" + context.getString(R.string.hour);
//        }
//        return "<font color=red>" + String.valueOf(hour/24) + "</font>" + context.getString(R.string.day)
//                + "<font color=red>" + String.valueOf(hour%24) + "</font>" + context.getString(R.string.hour);
//    }
//
//    /**
//     * 用户注销
//     */
//    public static void Logout(Context context) {
//        AppConfig.getAppConfig(context.getApplicationContext()).cleanLoginInfo();
//    }

    /**
     * 用intent启动拨打电话
     * @param context
     * @param phone
     */
    public static void callPhone(Context context,String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    /**
     * 发送短信
     * @param smsBody
     */
    public static void sendSMS(Context context, String phone, String smsBody) {
        Uri smsToUri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", smsBody);
        context.startActivity(intent);

    }

    /**
     * 获取App安装包信息
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if(info == null) info = new PackageInfo();
        return info;
    }

    /**
     * 底部列表窗口，单选
     * @param context
     * @param items
     * @param callback
     */
    public static void showBottomListDialog(Context context, CharSequence[] items, MaterialDialog.ListCallback callback) {
        MaterialDialog dialog = new MaterialDialog.Builder(context).items(items)
                .itemsColor(Color.parseColor("#FF7F28"))
                .itemsCallback(callback).itemsGravity(GravityEnum.CENTER)
                .build();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();
    }

    public interface OnDateSetListener {

        /**
         * @param year The year that was set.
         * @param monthOfYear The month that was set (0-11) for compatibility
         *  with {@link java.util.Calendar}.
         * @param dayOfMonth The day of the month that was set.
         */
        void onDateSet(int year, int monthOfYear, int dayOfMonth);
    }

    /**
     * 日期选择Dialog
     * @param context
     * @param listener
     */
    public static void showDatePicker(Context context, final OnDateSetListener listener, Date date) {
        WheelDateView dateView = (WheelDateView) View.inflate(context, R.layout.date_picker, null);
        dateView.setDate(date);
        MaterialDialog dlg = new MaterialDialog.Builder(context)
                .title(R.string.select_date)
                .titleGravity(GravityEnum.CENTER)
                .customView(dateView, false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        WheelDateView wheelDateView = (WheelDateView) dialog.findViewById(R.id.wheelDateView);
                        listener.onDateSet(wheelDateView.getYear(), wheelDateView.getMonth(), wheelDateView.getDay());
                    }
                })
                .build();
        Window window = dlg.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        MDRootLayout layout = (MDRootLayout)dlg.getView();
        Reflect reflect = Reflect.on(layout);
        reflect.set("mDrawTopDivider", true);
        reflect.set("mDrawBottomDivider", true);

        dlg.show();
    }
}
