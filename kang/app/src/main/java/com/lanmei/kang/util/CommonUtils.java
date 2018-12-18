package com.lanmei.kang.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.lanmei.kang.KangApp;
import com.lanmei.kang.R;
import com.lanmei.kang.adapter.BannerHolderView;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.AlbumBean;
import com.lanmei.kang.bean.ItemsBean;
import com.lanmei.kang.bean.ItemsCompileBean;
import com.lanmei.kang.bean.UserInfoBean;
import com.lanmei.kang.event.SetUserInfoEvent;
import com.lanmei.kang.ui.login.LoginActivity;
import com.lanmei.kang.webviewpage.PhotoBrowserActivity;
import com.oss.ManageOssUpload;
import com.oss.OssUserInfo;
import com.xson.common.api.AbstractApi;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.UserHelper;
import com.xson.common.utils.DoubleUtil;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class CommonUtils {

    public final static String isOne = "1";
    public final static String isZero = "0";
    public final static String ratioStr = "#.0";//保留小数位数


    /**
     * @param list
     * @return
     */
    public static String[] toArray(List<String> list) {
        return list.toArray(new String[list.size()]);
    }

    /**
     * 判断是不是自己
     *
     * @param context
     * @param uid
     * @return
     */
    public static boolean isSelf(Context context, String uid) {
        return StringUtils.isSame(uid, getUserId(context));
    }

    //获取用户ID
    public static String getUserId(Context context) {
        UserBean bean = getUserBean(context);
        if (StringUtils.isEmpty(bean)) {
            return "";
        }
        return bean.getId();
    }

    //获取用户类型
    public static String getUserType(Context context) {
        UserBean bean = getUserBean(context);
        if (StringUtils.isEmpty(bean)) {
            return "";
        }
        return bean.getUser_type();
    }

    //获取用户类型
    public static boolean isUser(Context context) {
        return !StringUtils.isSame(getUserType(context),CommonUtils.isOne);
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
     * 计算指定月份的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
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

    public static void developing(Context context) {
        UIHelper.ToastMessage(context, R.string.developing);
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



    //获取用户信息
    public static void loadUserInfo(final Context context, final UserInfoListener l) {
        KangQiMeiApi api = new KangQiMeiApi("member/member");
        api.add("uid", api.getUserId(context));
        api.setMethod(AbstractApi.Method.GET);
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<DataBean<UserInfoBean>>() {
            @Override
            public void onResponse(DataBean<UserInfoBean> response) {
                if (context == null) {
                    return;
                }
                UserInfoBean userInfoBean = response.data;
                if (userInfoBean != null) {
                    UserBean bean = CommonUtils.getUserBean(context);
                    if (bean == null){
                        return;
                    }
                    bean.setNickname(userInfoBean.getNickname());
                    bean.setPic(userInfoBean.getPic());
                    bean.setRealname(userInfoBean.getRealname());
                    bean.setEmail(userInfoBean.getEmail());
                    bean.setPhone(userInfoBean.getPhone());
                    bean.setCustom(userInfoBean.getCustom());
                    bean.setSignature(userInfoBean.getSignature());
                    bean.setMoney(userInfoBean.getMoney());
                    bean.setFiles_img(userInfoBean.getFiles_img());
                    bean.setRidname(userInfoBean.getRidname());
                    bean.setRatio(userInfoBean.getRatio());
                    if (l != null) {
                        l.userInfo(bean);
                    }
                    UserHelper.getInstance(context).saveBean(bean);
                    EventBus.getDefault().post(new SetUserInfoEvent(bean));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (l != null) {
                    l.error(error.getMessage());
                }
            }
        });
    }

    public interface UserInfoListener {
        void userInfo(UserBean bean);
        void error(String error);
    }


    /**
     * 字符串转换成 List<String>
     *
     * @param pic "dfasf,fasdfa,fasdfa"
     * @return
     */
    public static List<String> asList(String pic) {
        if (StringUtils.isEmpty(pic)) {
            return null;
        }
        return Arrays.asList(pic.split(L.cornet));
    }

    public static boolean isLogin(Context context) {
        if (!UserHelper.getInstance(context).hasLogin()) {
            IntentUtil.startActivity(context, LoginActivity.class);
            return false;
        }
        return true;
    }

    public static double getRatio(Context context) {
        double ratio = 100.0;
        UserBean userBean = getUserBean(context);
        if (userBean != null) {
            ratio = userBean.getRatio();
            if (ratio == 0){
                ratio = 100.0;
            }
        }
        return ratio;
    }

    public static String getRatioPrice(Context context,String price,java.text.DecimalFormat df){
//        double ratio = 100;
        double ratio = getRatio(context);
        L.d(L.TAG,"ratio:"+ratio);
        if (StringUtils.isEmpty(price)){
            price = isZero;
        }
        ratio = Double.valueOf(price)*(ratio/100);
        return DoubleUtil.formatFloatNumber(ratio,df);
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
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
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

        StringBuffer buffer = new StringBuffer();
        if (list == null || list.size() == 0) {
            return buffer.toString();
        }
        for (AlbumBean bean : list) {
            if (bean != null && !bean.isPicker()) {
                buffer.append(bean.getPic()).append(L.cornet);
            }
        }
        return buffer.toString();
    }

    /**
     * 去掉后面最后一个字符
     *
     * @param decs
     * @return
     */
    public static String getSubString(String decs) {
        if (StringUtils.isEmpty(decs)) {
            return decs;
        }
        return decs.substring(0, decs.length() - 1);
    }


    /**
     * 获取第一张图片地址
     *
     * @param pics
     * @return
     */
    public static String getPic(String pics) {
        List<String> list = asList(pics);
        if (!StringUtils.isEmpty(list)) {
            return list.get(0);
        }
        return "";
    }


    public static String getStringByList(List<String> list) {
        if (StringUtils.isEmpty(list)) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            buffer.append(((size - 1) != i) ? list.get(i) + L.cornet : list.get(i));
        }
        return buffer.toString();
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
     * @param arry     图片地址数组
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


    public static void setBanner(ConvenientBanner banner, List<String> list, boolean isTurning) {
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
        if (!isTurning) {
            return;
        }
        banner.startTurning(3000);
    }




    /**
     * 启动聊天界面
     *
     * @param context
     * @param userId  聊天对方的id
     * @param isGroup 是不是群聊
     */
    public static void startChatActivity(Context context, String userId, boolean isGroup) {
        Intent intent = new Intent(context, ChatActivity.class);
        if (isGroup) {
            intent.putExtra(com.hyphenate.chatuidemo.Constant.EXTRA_CHAT_TYPE, com.hyphenate.chatuidemo.Constant.CHATTYPE_GROUP);
        } else {
            userId = KangApp.HX_USER_Head + userId;
        }
        intent.putExtra(com.hyphenate.chatuidemo.Constant.EXTRA_USER_ID, userId);
        context.startActivity(intent);
    }

    /**
     * 设置字体的背景和颜色，文字。
     *
     * @param context
     * @param type     0或1
     * @param textView
     * @param strId
     * @param strIdEd
     */
    public static void setTextViewType(Context context, String type, TextView textView, int strId, int strIdEd) {
        if (StringUtils.isSame(CommonUtils.isZero,type)) {
            textView.setText(strId);
            textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            textView.setBackgroundResource(R.drawable.send);
        } else {
            textView.setText(strIdEd);
            textView.setTextColor(context.getResources().getColor(R.color.color999));
            textView.setBackgroundResource(R.drawable.send_on);
        }
    }

    /**
     * @param context
     * @param textView
     * @param drawableId 本地图片资源
     * @param colorId    textView的颜色id
     * @param position   图片在文字的位置  0左、1上、2右、3下
     */
    public static void setCompoundDrawables(Context context, TextView textView, int drawableId, int colorId, int position) {
        if (colorId > 0) {
            textView.setTextColor(context.getResources().getColor(colorId));
        }
        if (drawableId > 0) {
            Drawable img = context.getResources().getDrawable(drawableId);
            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            textView.setCompoundDrawables(position == 0 ? img : null, position == 1 ? img : null, position == 2 ? img : null, position == 3 ? img : null); //设置右图标
        } else {//清除文字周围的图片
            textView.setCompoundDrawables(null, null, null, null);
        }
    }

    public static String getObjectKey(String url) {
        if (StringUtils.isEmpty(url) || !url.contains(OssUserInfo.endpoint)) {
            return "";
        }
        return url.substring(url.indexOf("/", 35) + 1, url.length());
    }


    /**
     * 删除OSS文件
     *
     * @param url
     */
    public static void deleteOssObject(String url) {
        String objectKey = getObjectKey(url);
        if (StringUtils.isEmpty(objectKey)) {
            return;
        }
        ManageOssUpload manageOssUpload = new ManageOssUpload(KangApp.applicationContext);
        manageOssUpload.deleteObject(new DeleteObjectRequest(OssUserInfo.testBucket, objectKey));
        manageOssUpload.logAyncListObjects();
    }

    /**
     * 删除OSS文件(批量)
     *
     * @param paths
     */
    public static void deleteOssObjectList(final List<String> paths) {
        if (StringUtils.isEmpty(paths)) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ManageOssUpload manageOssUpload = new ManageOssUpload(KangApp.applicationContext);
                manageOssUpload.logAyncListObjects();
                int size = paths.size();
                for (int i = 0; i < size; i++) {
                    String objectKey = getObjectKey(paths.get(i));
                    if (!StringUtils.isEmpty(objectKey)) {
                        manageOssUpload.deleteObject(new DeleteObjectRequest(OssUserInfo.testBucket, objectKey));
                    }
                }
                manageOssUpload.logAyncListObjects();
            }
        }).start();
    }

}
