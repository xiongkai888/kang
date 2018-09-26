package com.lanmei.kang.helper;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.xson.common.utils.StringUtils;

import java.util.List;

/**
 * Created by xkai on 2018/1/2.
 * tab帮助类
 */

public class TabHelper {

    private Context context;
    private List<String> titleList;
    private int[] imageArray;
    private TabLayout tabLayout;
    private int colorId;

    /**
     *
     * @param context
     * @param tabLayout
     * @param colorId  选中的颜色ID
     */
    public TabHelper(Context context,TabLayout tabLayout,int colorId) {
        this.context = context;
        this.tabLayout = tabLayout;
        this.colorId = colorId;
//        setupTabIcons();
    }

    /**
     * @param titleList  Tab标题列表
     * @param imageArray 图片资源
     */
    public void setParameter(List<String> titleList, int[] imageArray){
        this.titleList = titleList;
        this.imageArray = imageArray;
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_tab, null);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setText(titleList.get(position));
        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        if (position == 0) {
//            txt_title.setTextColor(Color.parseColor("#0cc215"));
            txt_title.setTextColor(context.getResources().getColor(colorId));
            img_title.setImageResource(imageArray[position]);
        } else {
            txt_title.setTextColor(Color.parseColor("#000000"));
            img_title.setImageResource(imageArray[position * 2 + 1]);
        }
        return view;
    }

    public void setupTabIcons() {
        if (StringUtils.isEmpty(titleList)) {
            return;
        }
        int size = titleList.size();
        for (int i = 0; i < size; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tabLayout.addTab(tab);
            tab.setCustomView(getTabView(i));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabSelect(tab);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabNormal(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //选中时tab字体颜色和icon
    private void changeTabSelect(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        int position = tab.getPosition();
        img_title.setImageResource(imageArray[position*2]);
        txt_title.setTextColor(context.getResources().getColor(colorId));
    }

    //默认tab字体颜色和icon
    private void changeTabNormal(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        int position = tab.getPosition();
        img_title.setImageResource(imageArray[position*2+1]);
        txt_title.setTextColor(Color.parseColor("#000000"));
    }

}
