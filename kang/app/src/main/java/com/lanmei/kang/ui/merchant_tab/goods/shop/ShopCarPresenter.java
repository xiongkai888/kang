package com.lanmei.kang.ui.merchant_tab.goods.shop;


import android.content.Context;

import com.xson.common.utils.StringUtils;

import java.util.List;

/**
 * Created by xkai on 2018/1/20.
 */

class ShopCarPresenter implements ShopCartContract.Presenter {

    private ShopCartContract.View view;
    private Context content;
    private ShopCartDataHelper mDataHelper;

    @Override
    public void setGoodsNum(String goodsId,int position, int num) {
        mDataHelper.setGoodsNum(position, num);
        update(goodsId,num);
        updateList();
    }

    public ShopCarPresenter(Context content, ShopCartContract.View view) {
        this.content = content;
        this.view = view;
        mDataHelper = new ShopCartDataHelper(content);
    }

    @Override
    public void start() {
        List<ShopCarBean> list = getShopCartList();
        mDataHelper.setShopCarList(list);
        view.initShopCart(list);
        updateList();
        if (StringUtils.isEmpty(list)){
            view.showEmpty();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void selectAll(boolean seleted) {
        mDataHelper.selectAll(seleted);
        updateList();
    }

    public void updateList() {
        view.summation(mDataHelper.getSelectedTotalPrice(), mDataHelper.isAllSelected());
    }

    @Override
    public int getShopCarCount() {
        return 0;
    }

    @Override
    public void deleteSelectShopCar(List<ShopCarBean> list) {
        mDataHelper.deleteSelectShopCar(list);
        start();
//        updateList();
        
    }

    @Override
    public void clearShopCart() {

    }

    @Override
    public List<ShopCarBean> getSeletctedCarList() {
        return mDataHelper.getSeletctedCarList();
    }

    @Override
    public void setSelect(int position, boolean select) {
        mDataHelper.setSelect(position, select);
        updateList();
    }

    @Override
    public List<ShopCarBean> getShopCartList() {
        return mDataHelper.getShopCartList();
    }

    @Override
    public void update(String goodsId,int count){
        mDataHelper.update(goodsId,count);
    }

}
