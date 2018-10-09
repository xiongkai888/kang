package com.lanmei.kang.ui.merchant_tab.goods.shop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lanmei.kang.util.CommonUtils;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */

public class DBShopCartHelper {

    private Context context;
    private DBhelper dBhelper;
    private SQLiteDatabase db;

    public static final String Cart = "goodscart";//

    public static final String Cart_id = "id";
    public static final String Cart_user_id = "uid";
    public static final String Cart_goods_record_id = "goods_record_id";
    public static final String Cart_goodsid = "goodsid";
    public static final String Cart_goodsStore = "goodsStore";
    public static final String Cart_goodsName = "goodsName";
    public static final String Cart_goodsImg = "goodsImg";
    public static final String Cart_goodsPrice = "goodsPrice";
    public static final String Cart_goodsParams = "goodsParams";
    public static final String Cart_goodsCount = "goodsCount";



    public static final String createTable = "CREATE TABLE " + Cart +
            "(" + Cart_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Cart_user_id + " TEXT, " +
            Cart_goodsid + " TEXT, " +
            Cart_goodsStore + " TEXT, " +
            Cart_goodsName + " TEXT, " +
            Cart_goodsImg + " TEXT, " +
            Cart_goodsPrice + " REAL, " +
            Cart_goodsParams + " TEXT, " +
            Cart_goodsCount + " INTEGER)";

    public static String uid;
    public static double momey = 0;
    public static int goodsCount = 0;
    public static DBShopCartHelper dbGoodsCartManager;

    public DBShopCartHelper(Context context) {
        this.context = context;
        uid = CommonUtils.getUserId(context);
        dBhelper = DBhelper.newInstance(context);
        db = dBhelper.getWritableDatabase();
    }

    public static DBShopCartHelper getInstance(Context context){
        if (dbGoodsCartManager == null){
            dbGoodsCartManager = new DBShopCartHelper(context);
        }
        return dbGoodsCartManager;
    }

    /**
     * 获取当前账户的购物车
     */
    public List<ShopCarBean> getShopCarList() {
        List<ShopCarBean> shopCarBeanList = new ArrayList<>();
        String selection = Cart_user_id + " = " + uid;
        Cursor c = db.query(Cart, null, selection, null, null, null, null);
        momey = 0;
        goodsCount = 0;
        if (c.getCount() > 0) {
            ShopCarBean cartGoods;
            while (c.moveToNext()) {
                cartGoods = new ShopCarBean();
                cartGoods.setGoodsCount(c.getInt(c.getColumnIndex(Cart_goodsCount)));
                cartGoods.setGoodsName(c.getString(c.getColumnIndex(Cart_goodsName)));
                cartGoods.setGoodsImg(c.getString(c.getColumnIndex(Cart_goodsImg)));
                cartGoods.setGoods_id(c.getString(c.getColumnIndex(Cart_goodsid)));
                cartGoods.setSell_price(c.getDouble(c.getColumnIndex(Cart_goodsPrice)));
                shopCarBeanList.add(cartGoods);
            }
        }
        L.d(Cart, "c.getCount():" + c.getCount());
        c.close();
        return shopCarBeanList;
    }

    //获取购物车个数
    public int getShopCarListCount(){
        String selection = Cart_user_id + " = " + uid;
        Cursor c = db.query(Cart, null, selection, null, null, null, null);
        return c.getCount();
    }


//加入购物车（将数据插入数据库）
    public long insertGoods(ShopCarBean bean) {
        String selection = Cart_user_id + " = " + uid;
        Cursor c = db.query(Cart, null, selection, null, null, null, null);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                if (StringUtils.isSame(c.getString(c.getColumnIndex(Cart_goodsid)),bean.getGoods_id())){//已经插入的商品（根据id判断）
                    updateGoods(bean.getGoods_id(),c.getInt(c.getColumnIndex(Cart_goodsCount))+bean.getGoodsCount(),bean.getGoodsImg(),bean.getGoodsName(),bean.getSell_price());
                    UIHelper.ToastMessage(context, "加入购物车成功!");
                    return 0;
                }
            }
        }


        ContentValues values = new ContentValues();
        values.put(Cart_user_id, uid);
        values.put(Cart_goodsid, bean.getGoods_id());
        values.put(Cart_goodsName, bean.getGoodsName());
        values.put(Cart_goodsImg, bean.getGoodsImg());
        values.put(Cart_goodsPrice, bean.getSell_price());
//        values.put(Cart_goodsParams, bean.getSpec());
        values.put(Cart_goodsCount, bean.getGoodsCount());
        long insC = db.insert(Cart, Cart_goodsid, values);
        L.d(DBhelper.TAG, ":加入购物车:id" + bean.getGoods_id() + "---insC:" + insC);
        if (insC > 0) {
            UIHelper.ToastMessage(context, "加入购物车成功!!");
            EventBus.getDefault().post(new ShowShopCountEvent());
//            queryUserCartGoods();
        }
        return insC;
    }

    public void deleteDatabase(){
        dBhelper.deleteDatabase(context);
    }


    public void delete(List<ShopCarBean> list) {
        String where = Cart_goodsid+"=?";
        int size  = list.size();
        for (int i = 0;i<size;i++){
            db.delete(Cart, where, new String[]{list.get(i).getGoods_id()});
        }
    }

    /**
     * 根据goodsId 更新商品个数
     * @param goodsId
     * @param count
     */
    public void update(String goodsId,int count){
        ContentValues cv = new ContentValues();
        cv.put(Cart_goodsCount, count);
        db.update(Cart ,cv, Cart_goodsid + "=" + goodsId, null);
    }

    /**
     * 根据goodsId 更新商品个数、图片、商品名称，价格
     * @param goodsId
     * @param count
     * @param imge
     * @param goodsName
     * @param sellPrice
     */
    public void updateGoods(String goodsId,int count,String imge,String goodsName,double sellPrice){
        ContentValues cv = new ContentValues();
        cv.put(Cart_goodsCount, count);

        cv.put(Cart_goodsName, goodsName);
        cv.put(Cart_goodsImg, imge);
        cv.put(Cart_goodsPrice, sellPrice);
//        values.put(Cart_goodsParams, bean.getSpec());
        db.update(Cart ,cv, Cart_goodsid + "=" + goodsId, null);
    }

}
