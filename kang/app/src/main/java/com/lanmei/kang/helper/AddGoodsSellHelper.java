package com.lanmei.kang.helper;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.GoodsSellBean;
import com.lanmei.kang.bean.MerchantTabGoodsBean;
import com.lanmei.kang.qrcode.ScanActivity;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SimpleTextWatcher;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.FormatTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by xkai on 2018/9/28.
 * 添加商品销售
 */

public class AddGoodsSellHelper {

    private Context context;
    private LinearLayout root;
    private FormatTextView totalPriceTv;
    private List<GoodsSellBean> list;
    private int scan_goods_position;//
    private Map<Integer, ViewHolder> map;

    public List<GoodsSellBean> getList() {
        return list;
    }

    public AddGoodsSellHelper(Context context, LinearLayout root, FormatTextView totalPriceTv) {
        map = new HashMap<>();
        this.context = context;
        this.root = root;
        this.totalPriceTv = totalPriceTv;
        list = new ArrayList<>();
        GoodsSellBean bean = new GoodsSellBean();
        list.add(bean);
        addView(0);
    }

    private boolean isEmpty() {
        return StringUtils.isEmpty(list);
    }


    public void addItem() {
        GoodsSellBean bean = new GoodsSellBean();
        list.add(bean);
        addView(list.size() - 1);
    }

    private void addView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goods_sell, null);
        root.addView(view, position);
        map.put(position, new ViewHolder(view, position));
    }

    //二维码获取商品编号更新
    public void updateData(String result) {
        searchGoods(result);
    }

    public class ViewHolder implements TextView.OnEditorActionListener {

        @InjectView(R.id.subtract_iv)
        ImageView subtractIv;//删除
        @InjectView(R.id.number_et)
        EditText numberEt;//编号
        @InjectView(R.id.qr_code_iv)
        ImageView qrCodeIv;
        @InjectView(R.id.num_et)
        EditText numEt;//数量
        @InjectView(R.id.price_et)
        EditText priceEt;//价格
        @InjectView(R.id.unit_et)
        EditText unitEt;//单位

        public int position;
        public GoodsSellBean bean;

        /**
         * @param position 采购明细
         */
        public ViewHolder(View view, int position) {
            ButterKnife.inject(this, view);
            this.position = position;
            bean = list.get(position);
            setParameter();
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String key = CommonUtils.getStringByTextView(v);
                if (StringUtils.isEmpty(key)) {
                    UIHelper.ToastMessage(context, R.string.input_goods_number_or_scan);
                    return false;
                }
                searchGoods(key);
                scan_goods_position = position;
                return true;
            }
            return false;
        }

        public void setParameter() {
            subtractIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isEmpty() && list.size() == 1) {
                        UIHelper.ToastMessage(context, "至少添加一项");
                    } else {
                        list.remove(position);
                        refresh();
                        totalPriceTv.setTextValue(String.format("%.2f", getTotalPrice()));
                    }

                }
            });
            qrCodeIv.setOnClickListener(new View.OnClickListener() {//条形码
                @Override
                public void onClick(View v) {
                    scan_goods_position = position;
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", ScanActivity.SELL_GOODS_POSITION_SCAN);//(添加销售商品)扫描商品获取编号
                    bundle.putBoolean("isQR", true);//条形码
                    IntentUtil.startActivity(context, ScanActivity.class, bundle);
                }
            });
            numberEt.setText(bean.getNumber());
            if (bean.getNum() != 0) {
                numEt.setText(String.valueOf(bean.getNum()));
            }
            if (bean.getPrice() != 0) {
                priceEt.setText(String.valueOf(bean.getPrice()));
            }
            unitEt.setText(bean.getUnit());
            numberEt.setOnEditorActionListener(this);
            numberEt.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setNumber(String.valueOf(s));
                }
            });
            numberEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) { // 此处为得到焦点时的处理内容
                    } else {// 此处为失去焦点时的处理内容
                        String number = CommonUtils.getStringByEditText(numberEt);
                        if (!StringUtils.isEmpty(number)) {
                            scan_goods_position = position;
                            MerchantTabGoodsBean tabGoodsBean = bean.getBean();
                            if (StringUtils.isEmpty(tabGoodsBean)) {
                                searchGoods(number);
                            } else {
                                if (!StringUtils.isSame(tabGoodsBean.getBarcode(), number)) {
                                    searchGoods(number);
                                }
                            }
                        }
                    }
                }
            });
            numEt.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setNum(SimpleTextWatcher.StringToInt(s));
                    if (bean.getPrice() != 0) {
                        totalPriceTv.setTextValue(String.format("%.2f", getTotalPrice()));
                    }
                }
            });
            priceEt.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setPrice(SimpleTextWatcher.StringToDouble(s));
                    if (bean.getNum() != 0) {
                        totalPriceTv.setTextValue(String.format("%.2f", getTotalPrice()));
                    }

                }
            });
            unitEt.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setUnit(String.valueOf(s));
                }
            });
        }

        public void setData(MerchantTabGoodsBean merchantTabGoodsBean) {
            boolean isNull = StringUtils.isEmpty(merchantTabGoodsBean);
            numberEt.setText(isNull ? "" : merchantTabGoodsBean.getBarcode());
            numEt.setText(isNull ? "" : CommonUtils.isOne);
            priceEt.setText(isNull ? "" : merchantTabGoodsBean.getPrice());
            unitEt.setText(isNull ? "" : context.getString(R.string.jian));
            list.get(scan_goods_position).setGid(isNull ? "" : merchantTabGoodsBean.getId());
            list.get(scan_goods_position).setBean(merchantTabGoodsBean);
        }

    }

    private void searchGoods(final String barcode) {
        KangQiMeiApi api = new KangQiMeiApi("app/good_list");
        api.add("barcode", barcode);
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<MerchantTabGoodsBean>>() {
            @Override
            public void onResponse(NoPageListBean<MerchantTabGoodsBean> response) {
                if (root == null) {
                    return;
                }
                List<MerchantTabGoodsBean> beanList = response.data;
                ViewHolder viewHolder = map.get(scan_goods_position);
                if (StringUtils.isEmpty(beanList)) {
                    UIHelper.ToastMessage(context, "不存在该商品");
                    viewHolder.setData(null);
                    return;
                }
                MerchantTabGoodsBean merchantTabGoodsBean = beanList.get(0);
                viewHolder.setData(merchantTabGoodsBean);
            }
        });
    }


    private double getTotalPrice() {
        double totalPrice = 0;
        if (isEmpty()) {
            return totalPrice;
        }
        for (GoodsSellBean bean : list) {
            if (bean.getPrice() != 0 && bean.getPrice() != 0) {
                totalPrice += bean.getPrice() * bean.getNum();
            }
        }
        return totalPrice;
    }


    public void refresh() {
        map.clear();
        root.removeAllViews();
        if (isEmpty()) {
            GoodsSellBean bean = new GoodsSellBean();
            list.add(bean);
            addView(0);
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            addView(i);
        }
    }

}
