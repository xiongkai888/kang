package com.lanmei.kang.helper;

import android.content.Context;

import com.lanmei.kang.bean.LabelBean;
import com.xson.common.utils.StringUtils;

import java.util.List;

/**
 * Created by xkai on 2017/5/26.
 */

public class LabelDataHelper {

    List<LabelBean> mList;
    Context mContext;

    public LabelDataHelper(Context context) {
        mContext = context;
    }

    public void setLabelList(List<LabelBean> list,String label) {
        mList = list;
        String[] arr = getLabelArr(label);
        if (!isEmpty()) {
            int size = mList.size();
            for (int i = 0; i < size; i++) {
                LabelBean bean = getLabelBean(i);
                if (bean != null && arr != null){
                    int sizeArr = arr.length;
                    for (int j = 0;j<sizeArr;j++){
                        String labelStr = arr[j];
                        if (!StringUtils.isEmpty(labelStr) && labelStr.equals(bean.getName())){
                            bean.setChoose(true);
                        }
                    }
                }
            }
        }
    }

    public LabelBean getLabelBean(int position) {
        LabelBean bean = null;
        if (!isEmpty()) {
            bean = mList.get(position);
        }
        return bean;
    }

    //是否为空或者没有数据
    public boolean isEmpty() {
        if (mList == null || mList.size() == 0) {
            return true;
        }
        return false;
    }

    public int getCount() {
        if (isEmpty()) {
            return 0;
        }
        return mList.size();
    }

    public List<LabelBean> getList() {
        return mList;
    }

    private String[] getLabelArr(String label) {
        if (StringUtils.isEmpty(label)){
            return null;
        }
        return label.split(",");
    }
}
