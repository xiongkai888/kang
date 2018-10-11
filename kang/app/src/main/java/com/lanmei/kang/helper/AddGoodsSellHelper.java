package com.lanmei.kang.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.GoodsSellBean;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.helper.SimpleTextWatcher;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.FormatTextView;

import java.util.ArrayList;
import java.util.List;

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

    public List<GoodsSellBean> getList() {
        return list;
    }

    public AddGoodsSellHelper(Context context, LinearLayout root, FormatTextView totalPriceTv) {
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
        new ViewHolder(view, position);
    }


    public class ViewHolder {

        @InjectView(R.id.subtract_iv)
        ImageView subtractIv;//删除
        @InjectView(R.id.number_et)
        EditText numberEt;//编号
        @InjectView(R.id.qr_code_iv)
        ImageView qrCodeIv;
        @InjectView(R.id.num_tv)
        EditText numTv;//数量
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

        public void setParameter() {
            subtractIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isEmpty() && list.size() == 1) {
                        UIHelper.ToastMessage(context, "至少添加一项");
                    } else {
                        list.remove(position);
                        refresh();
                    }

                }
            });
            qrCodeIv.setOnClickListener(new View.OnClickListener() {//条形码
                @Override
                public void onClick(View v) {
                    CommonUtils.developing(context);
                }
            });
            numberEt.setText(bean.getNumber());
            numTv.setText(bean.getNum() + "");
            priceEt.setText(bean.getPrice() + "");
            unitEt.setText(bean.getUnit());

            numberEt.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setNumber(String.valueOf(s));
                }
            });
            numTv.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setNum(SimpleTextWatcher.StringToDouble(s));
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

    }


    private double getTotalPrice() {
        double totalPrice = 0;
        if (isEmpty()) {
            return 0;
        }
        for (GoodsSellBean bean : list) {
            if (bean.getPrice() != 0 && bean.getPrice() != 0) {
                totalPrice += bean.getPrice() * bean.getNum();
            }
        }
        return totalPrice;
    }


    public void refresh() {
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
