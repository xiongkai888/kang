package com.lanmei.kang.ui.merchant_tab.goods.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.GoodsDetailsBean;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.helper.ImageHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by xson on 2017/12/25.
 * 加入购物车底部弹框
 */

public class AddShopCarDialogFragment extends DialogFragment {

    @InjectView(R.id.items_icon_iv)
    ImageView itemsIconIv;
    @InjectView(R.id.name_tv)
    TextView nameTv;
    @InjectView(R.id.price_tv)
    TextView priceTvTv;
    @InjectView(R.id.num_tv)
    TextView numTv;
    @InjectView(R.id.pay_num_et)
    EditText payNumEt;
    private View mRootView;
    GoodsDetailsBean bean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, R.style.UI_Dialog);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparency)));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }

    public void setParameter() {
        nameTv.setText(bean.getGoodsname());
        ImageHelper.load(getContext(), bean.getCover(), itemsIconIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
        priceTvTv.setText(String.format(getString(R.string.price), bean.getPrice()));
        numTv.setText(String.format(getString(R.string.inventory), bean.getInventory()));
        payNumEt.setFocusable(false);
    }

    public void setData(GoodsDetailsBean bean) {
        this.bean = bean;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.dialog_add_shop_car, null);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        ButterKnife.inject(this, mRootView);
        setParameter();
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    int orderNum = 1;//订购数量,默认1

    @OnClick({R.id.crossIv, R.id.num_subtract_iv, R.id.num_add_iv, R.id.add_shop_car_tv, R.id.pay_now_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.crossIv:
                dismiss();
                break;
            case R.id.num_subtract_iv:
                if (orderNum <= 1) {
                    return;
                }
                orderNum--;
                payNumEt.setText(orderNum + "");
                break;
            case R.id.num_add_iv:
                if (orderNum == 9999) {
                    return;
                }
                orderNum++;
                payNumEt.setText(orderNum + "");
                break;
            case R.id.add_shop_car_tv:
                CommonUtils.developing(getContext());
//                ShopCarBean shopCarBean = new ShopCarBean();
//                shopCarBean.setGoodsName(bean.getGoodsname());
////                L.d(DBhelper.TAG, DoubleUtil.formatFloatNumber(bean.getSell_price())+"  =   "+bean.getSell_price());
//                shopCarBean.setSell_price(DoubleUtil.formatFloatNumber(bean.getPrice()));
//                shopCarBean.setGoods_id(bean.getId());
//                shopCarBean.setGoodsImg(bean.getCover());
//                shopCarBean.setGoodsCount(orderNum);
//                DBShopCartHelper.getInstance(getContext().getApplicationContext()).insertGoods(shopCarBean);
//                dismiss();
                break;
            case R.id.pay_now_tv:
                CommonUtils.developing(getContext());
//                List<GoodsDetailsBean.ProductsBean> list = bean.getProducts();
//                if (!StringUtils.isEmpty(list)){
//                    for(GoodsDetailsBean.ProductsBean productsBean:list){//只有一件商品
////                        productsBean.setId(bean.getId());
//                        productsBean.setCount(orderNum);
//                        productsBean.setName(bean.getName());//要自己加，坑
//                        productsBean.setSell_price(bean.getSell_price());
//                        productsBean.setProducts_img(bean.getImg());
//                    }
//                }
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("bean", bean);
//                bundle.putInt("num", orderNum);
//                IntentUtil.startActivity(getContext(), ConfirmOrderActivity.class, bundle);
//                dismiss();
                break;
        }
    }
}