package com.lanmei.kang.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.lanmei.kang.KangApp;
import com.lanmei.kang.R;
import com.lanmei.kang.adapter.BannerHolderView;
import com.lanmei.kang.bean.AlbumBean;
import com.lanmei.kang.bean.ItemsBean;
import com.lanmei.kang.bean.ItemsCompileBean;
import com.lanmei.kang.bean.SystemSettingInfoBean;
import com.lanmei.kang.bean.UserBean;
import com.lanmei.kang.helper.UserHelper;
import com.lanmei.kang.loader.DataLoader;
import com.lanmei.kang.ui.login.LoginActivity;
import com.lanmei.kang.webviewpage.PhotoBrowserActivity;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CommonUtils {

    public final static String isOne = "1";
    public final static String isZero = "0";

    /**
     * List<String> 传化未String[]
     *
     * @param list
     * @return
     */
    public static String[] getStringArr(List<String> list) {
        String[] arr = null;
        if (list != null && list.size() > 0) {
            int size = list.size();
            arr = new String[size];
            for (int i = 0; i < size; i++) {
                arr[i] = list.get(i);
            }
        }
        return arr;
    }

    /**
     * 判断是不是自己
     * @param context
     * @param uid
     * @return
     */
    public static boolean isSelf(Context context,String uid){
        return StringUtils.isSame(uid,getUserId(context));
    }

    public static String getUserId(Context context){
        UserBean bean = UserHelper.getInstance(context).getUserBean();
        if (StringUtils.isEmpty(bean)){
            return "";
        }
        return bean.getId();
    }

    /**
     * List<AlbumBean> 传化未String[]
     *
     * @param list
     * @return
     */
    public static String[] getStringArry(List<AlbumBean> list) {
        String[] arr = null;
        if (list != null && list.size() > 0) {
            int size = list.size();
            arr = new String[size];
            for (int i = 0; i < size; i++) {
                AlbumBean bean = list.get(i);
                if (bean != null) {
                    arr[i] = bean.getPic();
                }
            }
        }
        return arr;
    }
    /**
     * List<ItemsCompileBean> 传化未String[]
     *
     * @param list
     * @return
     */
    public static String[] getItemsCompileStringArry(List<ItemsCompileBean> list) {
        String[] arr = null;
        if (!StringUtils.isEmpty(list)) {
            int size = list.size();
            arr = new String[size];
            for (int i = 0; i < size; i++) {
                ItemsCompileBean bean = list.get(i);
                if (bean != null) {
                    arr[i] = bean.getPic();
                }
            }
        }
        return arr;
    }


    //获取项目id
    public static String getItemId(List<ItemsBean> itemList, String itemStr) {
        if (itemList != null && itemList.size() > 0 && !StringUtils.isEmpty(itemStr)) {
            int size = itemList.size();
            for (int i = 0; i < size; i++) {
                ItemsBean bean = itemList.get(i);
                if (bean != null && bean.getName().equals(itemStr)) {
                    return bean.getId();
                }
            }
        }
        return "";
    }



    /**
     * 字符串转换成 List<String>
     *
     * @param pic "dfasf,fasdfa,fasdfa"
     * @return
     */
    public static List<String> getListString(String pic) {
        if (StringUtils.isEmpty(pic)) {
            return null;
        }
        String[] arr = pic.split(",");
        List<String> list = new ArrayList<>();
        if (arr != null && arr.length > 0) {
            int size = arr.length;
            for (int i = 0; i < size; i++) {
                list.add(arr[i]);
            }
        }
        return list;
    }

    public static boolean isLogin(Context context) {
        if (!UserHelper.getInstance(context).hasLogin()) {
            IntentUtil.startActivity(context, LoginActivity.class);
            return false;
        }
        return true;
    }

    public static String getUid(Context context) {
        String uid = "";
        UserBean userBean = UserHelper.getInstance(context).getUserBean();
        if (userBean != null) {
            uid = userBean.getId();
        }
        return uid;
    }


    public static UserBean getUserBean(Context context) {
        return UserHelper.getInstance(context).getUserBean();
    }

    /**
     * 浏览图片
     *
     * @param context
     * @param arry     图片地址数组
     * @param imageUrl 点击的图片地址
     */
    public static void showPhotoBrowserActivity(Context context, String[] arry, String imageUrl) {
        Intent intent = new Intent();
        intent.putExtra("imageUrls", arry);
        intent.putExtra("curImageUrl", imageUrl);
        intent.setClass(context, PhotoBrowserActivity.class);
        context.startActivity(intent);
    }

    public static void notifyDoSomething(Context context, String action) {
        Intent intent = new Intent(action);
        context.sendBroadcast(intent);
    }


    /**
     * 获取上传相册的图片本地地址
     *
     * @param list
     * @return
     */
    public static List<String> getUploadingList(List<AlbumBean> list) {
        List<String> upList = null;
        if (list != null && list.size() > 0) {
            upList = new ArrayList<>();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                AlbumBean bean = list.get(i);
                if (bean != null && bean.isPicker()) {
                    upList.add(bean.getPic());
                }
            }
        }
        return upList;
    }

    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 商家相册
     *
     * @param stringList
     * @return
     */
    public static List<AlbumBean> getAlbumList(List<String> stringList) {
        if (!StringUtils.isEmpty(stringList)) {
            int size = stringList.size();
            List<AlbumBean> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                AlbumBean bean = new AlbumBean();
                bean.setPic(stringList.get(i));
                bean.setPicker(false);
                list.add(bean);
            }
            return list;
        }
        return null;
    }

    /**
     * 获取要上传相册的数量
     *
     * @param list
     * @return
     */
    public static int getNativeAlbumsNum(List<AlbumBean> list) {
        int num = 0;
        if (list == null || list.size() == 0) {
            return num;
        }
        for (AlbumBean bean : list) {
            if (bean != null && bean.isPicker()) {
                num++;
            }
        }
        return num;
    }

    /**
     * 获取要上传相册的地址(原本剩下的)，用于更新
     *
     * @param list
     * @return
     */
    public static String getAlbumsPics(List<AlbumBean> list) {
        String pics = "";
        if (list == null || list.size() == 0) {
            return pics;
        }
        for (AlbumBean bean : list) {
            if (bean != null && !bean.isPicker()) {
                pics += bean.getPic() + ",";
            }
        }
        return pics;
    }

    /**
     * 去掉后面最后一个字符
     *
     * @param decs
     * @return
     */
    public static String getSubString(String decs) {
        if (StringUtils.isEmpty(decs)) {
            return "";
        }
        return decs.substring(0, decs.length() - 1);
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static String getVersionCode() {
        SystemSettingInfoBean settingInfoBean = DataLoader.getInstance().getSystemInfoBean();
        if (settingInfoBean != null) {
            SystemSettingInfoBean.AndroidUpdateBean updateBean = settingInfoBean.getAndroid_update();
            if (updateBean != null) {
                return updateBean.getVersion();
            }
        }
        return "";
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static String getUpdateAppUrl() {
        SystemSettingInfoBean settingInfoBean = DataLoader.getInstance().getSystemInfoBean();
        if (settingInfoBean != null) {
            SystemSettingInfoBean.AndroidUpdateBean updateBean = settingInfoBean.getAndroid_update();
            if (updateBean != null) {
                return updateBean.getUrl();
            }
        }
        return "";
    }

    /**
     * 获取第一张图片地址
     *
     * @param pics
     * @return
     */
    public static String getPic(String pics) {
        List<String> list = CommonUtils.getListString(pics);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return "";
    }


    /**
     * 获取TextView 字符串
     *
     * @param textView
     * @return
     */
    public static String getStringByTextView(TextView textView) {
        return textView.getText().toString().trim();
    }

    /**
     * 获取EditText 字符串
     *
     * @param editText
     * @return
     */
    public static String getStringByEditText(EditText editText) {
        return editText.getText().toString().trim();
    }


    /**
     * @param context 关闭输入法，需要一个activity
     */
    public static void closeInputMethod(Activity context) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // TODO: handle exception
            Log.d("beanre", "关闭输入法异常");
        }
    }


    /**
     * 图片预览
     *
     * @param context
     * @param arry        图片地址数组
     * @param position 点击当前图片的地址位置
     */
    public static void startPhotoBrowserActivity(Context context, String[] arry, int position) {
        Intent intent = new Intent();
        intent.putExtra("imageUrls", arry);
        intent.putExtra("curImageUrl", arry[position]);
        intent.setClass(context, PhotoBrowserActivity.class);
        context.startActivity(intent);
    }


    /**
     * 获取年月日
     */
    public static String getData() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
    }


    public static void setBanner(ConvenientBanner banner,List<String> list,boolean isTurning) {
        //初始化商品图片轮播
        banner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new BannerHolderView();
            }
        }, list);
        banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        banner.setPageIndicator(new int[]{R.drawable.shape_item_index_white, R.drawable.shape_item_index_red});
        banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        if (list.size() == 1) {
            return;
        }
        if (!isTurning){
            return;
        }
        banner.startTurning(3000);
    }

    LoadUserInfoListener l;

    public interface LoadUserInfoListener{
        void succeed(UserBean bean);
    }

    /**
     * 启动聊天界面
     * @param context
     * @param userId  聊天对方的id
     * @param isGroup 是不是群聊
     */
    public static void startChatActivity(Context context,String userId,boolean isGroup){
        Intent intent = new Intent(context, ChatActivity.class);
        if (isGroup){
            intent.putExtra(com.hyphenate.chatuidemo.Constant.EXTRA_CHAT_TYPE, com.hyphenate.chatuidemo.Constant.CHATTYPE_GROUP);
        }else {
            userId = KangApp.HX_USER_Head + userId;
        }
        intent.putExtra(com.hyphenate.chatuidemo.Constant.EXTRA_USER_ID, userId);
        context.startActivity(intent);
    }

    /**
     * 设置字体的背景和颜色，文字。
     * @param context
     * @param type  0或1
     * @param textView
     * @param strId
     * @param strIdEd
     */
    public static void setTextViewType(Context context,String type,TextView textView,int strId,int strIdEd){
        if (CommonUtils.isZero.equals(type)){
            textView.setText(strId);
            textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            textView.setBackground(context.getResources().getDrawable(R.drawable.send));
        }else {
            textView.setText(strIdEd);
            textView.setTextColor(context.getResources().getColor(R.color.color999));
            textView.setBackground(context.getResources().getDrawable(R.drawable.send_on));
        }
    }

}
