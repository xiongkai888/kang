package com.lanmei.kang.helper.coupon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lanmei.kang.ui.merchant_tab.goods.shop.DBhelper;
import com.xson.common.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */

public class DBCouponsManager {

    private Context mContext;
    private DBhelper dBhelper;
    private SQLiteDatabase db;


    public static final String TABLE_NAME = "coupons";
    public static final String KEY_id = "id";
    public static final String KEY_lname = "lname";
    public static final String KEY_title = "title";
    public static final String KEY_type = "type";
    public static final String KEY_uid = "uid";
    public static final String KEY_addtime = "addtime";
    public static final String KEY_uptime = "uptime";
    public static final String KEY_starttime = "starttime";
    public static final String KEY_endtime = "endtime";
    public static final String KEY_state = "state";
    public static final String KEY_consume = "consume";
    public static final String KEY_money = "money";
    public static final String KEY_employ = "employ";
    public static final String KEY_is_del = "is_del";
    public static final String KEY_logid = "logid";
    public static final String KEY_name = "name";

    public static final String KEY_temp = "temp";


    public static final String createTable = "CREATE TABLE " + TABLE_NAME +
            "(" + KEY_id + " TEXT , " +
            KEY_lname + " TEXT, " +
            KEY_title + " TEXT, " +
            KEY_type + " TEXT, " +
            KEY_uid + " TEXT, " +
            KEY_addtime + " Long, " +
            KEY_uptime + " Long, " +
            KEY_starttime + " Long, " +
            KEY_endtime + " Long, " +
            KEY_state + " Integer, " +
            KEY_consume + " Double, " +
            KEY_money + " Double, " +
            KEY_employ + " Integer, " +
            KEY_is_del + " Integer, " +
            KEY_logid + " Integer, " +
            KEY_temp + " Integer, " +
            KEY_name + " TEXT)";

    public static DBCouponsManager instance;

    public static DBCouponsManager newInstance(Context mContext) {
        if (instance == null)
            instance = new DBCouponsManager(mContext);
        return instance;
    }

    public DBCouponsManager(Context mContext) {
        this.mContext = mContext;

        dBhelper = DBhelper.newInstance(mContext);
        db = dBhelper.getWritableDatabase();
    }

    public void addAllCoupons(List<BeanCoupon> beanCoupons) {
        delAll();
        if (beanCoupons == null)
            return;
        for (BeanCoupon item : beanCoupons) {
            addCoupon(false, item);
        }
    }

    /**
     * @param temp ture 临时数据
     */
    private void addCoupon(boolean temp, BeanCoupon beanCoupon) {
        ContentValues values = new ContentValues();
        values.put(KEY_id, beanCoupon.getId());
        values.put(KEY_lname, beanCoupon.getLname());
        values.put(KEY_title, beanCoupon.getTitle());
        values.put(KEY_type, beanCoupon.getType());
        values.put(KEY_uid, beanCoupon.getUid());

        values.put(KEY_addtime, beanCoupon.getAddtime());
        values.put(KEY_uptime, beanCoupon.getUptime());
        values.put(KEY_starttime, beanCoupon.getStarttime());
        values.put(KEY_endtime, beanCoupon.getEndtime());
        values.put(KEY_state, beanCoupon.getState());

        values.put(KEY_consume, beanCoupon.getConsume());
        values.put(KEY_money, beanCoupon.getMoney());
        values.put(KEY_employ, beanCoupon.getEmploy());
        values.put(KEY_is_del, beanCoupon.getIs_del());
        values.put(KEY_logid, beanCoupon.getLogid());
        values.put(KEY_name, beanCoupon.getName());

        values.put(KEY_temp, temp ? 1 : 0);

        long insN = db.insert(TABLE_NAME, KEY_id, values);
        L.d(TABLE_NAME, "add:insert:" + insN);

    }


    /**
     * 查询可用优惠券,并添加为临时数据
     */
    public void queryCouponAdd(String typeItem, double totalMoney) {
        long currentTime = System.currentTimeMillis() / 1000;
        String selection =
                KEY_temp + " = 0 "
                        + " and " + KEY_type + " = ?"
                        + " and " + KEY_consume + " <= ?"
                        + " and " + KEY_starttime + " <= ?"
                        + " and " + KEY_endtime + " > ?";
        Cursor c = db.query(TABLE_NAME, null, selection, new String[]{typeItem, totalMoney + "", currentTime + "", currentTime + ""}, null, null, KEY_endtime + " asc");
        BeanCoupon item;
        L.d(TABLE_NAME, "typeItem:" + typeItem + ":totalMoney:" + totalMoney + ":count:" + c.getCount());
        while (c.moveToNext()) {
            item = getBeanCoupon(c);
            addCoupon(true, item);
        }
        c.close();
    }


    public List<BeanCoupon> queryCoupon() {
        String selection =
                KEY_temp + " = 1 ";
        String orderBy = KEY_money + " desc , " + KEY_endtime + " asc";

        Cursor c = db.query(TABLE_NAME, null, selection, null, null, null, orderBy);
        L.d(TABLE_NAME,"可用优惠券:count:"+c.getCount());
        List<BeanCoupon> result = new ArrayList<>();
        while (c.moveToNext()) {
            result.add(getBeanCoupon(c));
        }
        c.close();
        return result;
    }

    public BeanCoupon getBeanCoupon(Cursor c) {
        BeanCoupon beanCoupon = new BeanCoupon();

        beanCoupon.setId(c.getString(c.getColumnIndex(KEY_id)));
        beanCoupon.setLname(c.getString(c.getColumnIndex(KEY_lname)));
        beanCoupon.setTitle(c.getString(c.getColumnIndex(KEY_title)));
        beanCoupon.setType(c.getString(c.getColumnIndex(KEY_type)));
        beanCoupon.setUid(c.getString(c.getColumnIndex(KEY_uid)));

        beanCoupon.setAddtime(c.getLong(c.getColumnIndex(KEY_addtime)));
        beanCoupon.setUptime(c.getLong(c.getColumnIndex(KEY_uptime)));
        beanCoupon.setStarttime(c.getLong(c.getColumnIndex(KEY_starttime)));
        beanCoupon.setEndtime(c.getLong(c.getColumnIndex(KEY_endtime)));
        beanCoupon.setState(c.getInt(c.getColumnIndex(KEY_state)));

        beanCoupon.setConsume(c.getDouble(c.getColumnIndex(KEY_consume)));
        beanCoupon.setMoney(c.getDouble(c.getColumnIndex(KEY_money)));
        beanCoupon.setEmploy(c.getInt(c.getColumnIndex(KEY_employ)));
        beanCoupon.setIs_del(c.getInt(c.getColumnIndex(KEY_is_del)));
        beanCoupon.setLogid(c.getInt(c.getColumnIndex(KEY_logid)));

        beanCoupon.setName(c.getString(c.getColumnIndex(KEY_name)));
        return beanCoupon;
    }

    public int delAll() {
        return db.delete(TABLE_NAME, null, null);
    }

    public int delAllTemp() {
        return db.delete(TABLE_NAME, KEY_temp + " = 1", null);
    }

}
