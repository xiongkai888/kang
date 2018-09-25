package com.lanmei.kang.ui.merchant;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.CategoryDialogsAdapter;
import com.lanmei.kang.bean.CategoryBean;
import com.lanmei.kang.event.ChooseCategoryEvent;
import com.lanmei.kang.ui.merchant.activity.CategoryActivity;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by xson on 2017/12/25.
 * 分类至选择对话框
 */

public class ClassifyToDialogFragment extends DialogFragment {


    @InjectView(R.id.sold_out_tv)
    TextView cancelTv;//取消
    @InjectView(R.id.delete_tv)
    TextView sureTv;//确定
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;//列表
    private View mRootView;
    List<CategoryBean> list;
    String pid;
    CategoryDialogsAdapter dialogsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, R.style.UI_Dialog);
        super.onCreate(savedInstanceState);
    }

    public void setList(List<CategoryBean> list) {
        this.list = list;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.dialog_classfy_to, null);
            ButterKnife.inject(this, mRootView); // Dialog即View
            cancelTv.setText(R.string.cancel);
            sureTv.setText(R.string.sure);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            dialogsAdapter = new CategoryDialogsAdapter(getContext());
            dialogsAdapter.setData(list);
            recyclerView.setAdapter(dialogsAdapter);

        }

        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        ButterKnife.inject(this, mRootView);
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.ll_add_classify, R.id.sold_out_tv, R.id.delete_tv, R.id.compile_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_add_classify://新建分类
                UIHelper.ToastMessage(getContext(), R.string.developing);
                break;
            case R.id.sold_out_tv://取消
                dismiss();
                break;
            case R.id.delete_tv://确定
                CategoryBean bean = getCategoryBean();
                if (getCategoryBean() == null){
                    UIHelper.ToastMessage(getContext(),"请先选择分类");
                    return;
                }
                EventBus.getDefault().post(new ChooseCategoryEvent(bean));//
                dismiss();
                break;
            case R.id.compile_tv://编辑
                dismiss();
                IntentUtil.startActivity(getContext(), CategoryActivity.class, pid);
                break;
        }
    }

    private CategoryBean getCategoryBean(){
        List<CategoryBean> beanList = dialogsAdapter.getData();
        if (StringUtils.isEmpty(beanList)) {
            return null;
        }
        int size = beanList.size();
        for (int i = 0; i < size; i++){
            CategoryBean bean = beanList.get(i);
            if (bean.isPitch()){
                return bean;
            }
        }
        return null;
    }

}