package com.lanmei.kang.ui.merchant_tab.goods.shop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lanmei.kang.helper.coupon.DBCouponsManager;
import com.xson.common.utils.L;


/**
 * Created by xkai
 */

public class DBhelper extends SQLiteOpenHelper {

    public static String TAG = "DBhelper";

    private static String dbName = "kang.db";
    private static int dbVersion = 5;
    public static DBhelper dBhelper;

    public static DBhelper newInstance(Context context) {
        if (dBhelper == null)
            dBhelper = new DBhelper(context);
        return dBhelper;
    }

    public DBhelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        L.d(TAG, "创建数据库成功:" + dbVersion);
//        update(0,db);
        db.execSQL(DBCouponsManager.createTable);//优惠券表
        db.execSQL(DBShopCartHelper.createTable);//购物车表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        L.d(TAG, "更新数据库成功:" + oldVersion + "  newVersion:" + newVersion);
        update(oldVersion,db);

    }

    private void update(int oldVersion, SQLiteDatabase db) {
        switch (oldVersion) {
            case 4:
                db.execSQL(DBCouponsManager.createTable);
                db.execSQL(DBShopCartHelper.createTable);
                break;
        }
    }

    public void deleteDatabase(Context context) {
        context.deleteDatabase(dbName);
    }

}
