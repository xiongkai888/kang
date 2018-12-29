package com.lanmei.kang.helper.coupon;

import android.content.Context;

import com.lanmei.kang.KangApp;
import com.lanmei.kang.ui.merchant_tab.goods.shop.ShopCarBean;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.utils.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

/**
 *
 * 优惠券工具类
 */
public class GoodsCoupons {
    String TAG = "GoodsCoupons";
    private int errRequestCount = 0;
    private boolean isDoneData = false;
    private OnGouponsListener onGouponsListener;
    //优惠券数据源
    private LinkedHashMap<String, ArrayList<BeanCoupon>> mapCouponsSource = new LinkedHashMap<>();
    //商品数据源
    private List<ShopCarBean> cartGoodsList;

    //商品优惠券分类并集
    LinkedHashSet<String> goodsUnionSet = new LinkedHashSet<>();


    private boolean isRefresh;
    DBCouponsManager dbCouponsManager;

    public GoodsCoupons(Context context, OnGouponsListener onGouponsListener) {
        this.onGouponsListener = onGouponsListener;
        dbCouponsManager = DBCouponsManager.newInstance(context);
    }

    public void setCouponsList(List<BeanCoupon> list) {
        isDoneData = true;
        mapCouponsSource.clear();
        errRequestCount = 0;
        if (StringUtils.isEmpty(list)) {
            return;
        }
        dbCouponsManager.addAllCoupons(list);
        for (BeanCoupon item : list) {
            if (mapCouponsSource.containsKey(item.getType())) {
                mapCouponsSource.get(item.getType()).add(item);
            } else {
                ArrayList<BeanCoupon> arrayList = new ArrayList<>();
                arrayList.add(item);
                mapCouponsSource.put(item.getType() + "", arrayList);
            }

        }
    }


    public void setGoodsTypes(List<ShopCarBean> cartGoodsList) {
        this.cartGoodsList = cartGoodsList;
        initUnionSet();
        isRefresh = false;
        if (isDoneData) {
            queryCoupons();
        } else {
            isRefresh = true;
        }

    }

    private boolean isContains(String params, String[] souArr) {
        for (String item : souArr) {
            if (item.equals(params)) {
                return true;
            }
        }
        return false;
    }

    private double getGoodsTotalMoney(String typeItem) {
        double money = 0;
        for (ShopCarBean item : cartGoodsList) {
            if (isContains(typeItem, item.getTypeArr())) {
                money += Double.valueOf(CommonUtils.getRatioPrice(KangApp.applicationContext, String.valueOf(item.getSell_price()), new DecimalFormat(CommonUtils.ratioStr))) * item.getGoodsCount();//折扣后
            }
        }
        return money;
    }


    private void queryCoupons() {
        dbCouponsManager.delAllTemp();
        for (String typeItem : goodsUnionSet) {
            double totalMoney = getGoodsTotalMoney(typeItem);
            dbCouponsManager.queryCouponAdd(typeItem, totalMoney);
        }
        if (onGouponsListener != null) {
            onGouponsListener.onChangeCoupon(dbCouponsManager.queryCoupon());
        }
    }

    /**
     * 求并集
     *
     * @param
     * @return 默认添加"0"
     */
    private void initUnionSet() {
        goodsUnionSet.clear();
        goodsUnionSet.add("0");
        for (ShopCarBean item : cartGoodsList) {
            goodsUnionSet.addAll(Arrays.asList(item.getTypeArr()));
        }
    }


    public interface OnGouponsListener {
        void onChangeCoupon(List<BeanCoupon> result);
    }
}
