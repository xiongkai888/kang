package com.lanmei.kang.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lanmei.kang.R;
import com.xson.common.adapter.ViewPagerAdapter;
import com.xson.common.bean.HomeListBean;
import com.xson.common.helper.ImageHelper;

import java.util.List;


/**
 * 首页  广告轮播图
 */
public class homeAdAdapter extends ViewPagerAdapter {


    private Context context;

    List<HomeListBean.BannerBean> mList;

    public homeAdAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<HomeListBean.BannerBean> list) {
        mList = list;
    }

    @Override
    public View getView(int position, ViewGroup pager) {
        ImageView view = (ImageView) View.inflate(context, R.layout.item_ad, null);
//        view.setImageResource(R.mipmap.tem_news);
        ImageHelper.load(context,mList.get(position).getPic(),view,null,true,R.mipmap.default_pic,R.mipmap.default_pic);
        return view;
    }


    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }
//    @Override
//    public int getCount() {
//        return 3;
//    }
}
