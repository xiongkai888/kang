package com.lanmei.kang.helper;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.CouponListDialogAdapter;
import com.lanmei.kang.helper.coupon.BeanCoupon;
import com.lanmei.kang.ui.merchant_tab.activity.ConfirmOrderActivity;
import com.xson.common.utils.L;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by xson on 2017/12/25.
 * 确认订单界面-选择优惠券底部弹框
 */

public class CouponListDialogFragment extends DialogFragment {

    private View mRootView;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<BeanCoupon> couponBeanList;
    private BeanCoupon couponBean;//用户选择的优惠券

    public void setCouponBeanList(List<BeanCoupon> couponBeanList) {
        this.couponBeanList = couponBeanList;
        couponBean = couponBeanList.get(0);
    }

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
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        display.getMetrics(dm);

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = (int)(display.getHeight()*0.6);
        L.d(L.TAG,"WRAP_CONTENT = "+params.height);
        win.setAttributes(params);
    }

    public void setParameter() {
        CouponListDialogAdapter adapter = new CouponListDialogAdapter(getContext());
        adapter.setData(couponBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setChooseCouponListener(new CouponListDialogAdapter.ChooseCouponListener() {
            @Override
            public void chooseCoupon(BeanCoupon coupon) {
                couponBean = coupon;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.dialog_choose_coupon_list, null);
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


    @OnClick(R.id.sure_tv)
    public void onViewClicked() {
        ((ConfirmOrderActivity)getActivity()).setCoupon(couponBean);
        dismiss();
    }
}