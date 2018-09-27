package com.lanmei.kang.ui.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.ParticularsAdapter;
import com.lanmei.kang.bean.ParticularsBean;
import com.lanmei.kang.widget.WithScrollListView;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * 用户或者商家订单明细
 */
public class OrderParticularsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.listView)
    WithScrollListView mListViewTV;//明细列表

    private String money;
    private String[] dateCountArry;
    private String workMoneyStr;
    private String weekMoneyStr;
    private String holidaysStr;
    private String[] workMoneyArry;
    private String[] weekMoneyArry;
    private String[] holidaysArry;
    private int numPeople;
    private int numCoach;
    private int numHelmsman;

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_particulars;
    }

    public static void startActivityOrderParticulars(Context context,String money,String dateCount,String workMoneyStr,String weekMoneyStr,String holidaysStr,int numPeople,int numCoach,int numHelmsman){
        Intent intent = new Intent(context,OrderParticularsActivity.class);
        intent.putExtra("money",money);//这个参数暂时还用不上
        intent.putExtra("dateCount",dateCount);
        intent.putExtra("workMoneyStr",workMoneyStr);
        intent.putExtra("weekMoneyStr",weekMoneyStr);
        intent.putExtra("holidaysStr",holidaysStr);
        intent.putExtra("numPeople",numPeople);
        intent.putExtra("numCoach",numCoach);
        intent.putExtra("numHelmsman",numHelmsman);
        context.startActivity(intent);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("订单明细");
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        Intent intent = getIntent();
        if (intent == null){
            return;
        }
        money = intent.getStringExtra("money");
        String dateCount = intent.getStringExtra("dateCount");
        if (!StringUtils.isEmpty(dateCount)){
            dateCountArry = dateCount.split(",");
        }
        workMoneyStr = intent.getStringExtra("workMoneyStr");
        weekMoneyStr = intent.getStringExtra("weekMoneyStr");
        holidaysStr = intent.getStringExtra("holidaysStr");
        numPeople = intent.getIntExtra("numPeople",0);
        numCoach = intent.getIntExtra("numCoach",0);
        numHelmsman = intent.getIntExtra("numHelmsman",0);
        if (!StringUtils.isEmpty(workMoneyStr)){
            workMoneyArry = workMoneyStr.split(",");
        }
        if (!StringUtils.isEmpty(weekMoneyStr)){
            weekMoneyArry = weekMoneyStr.split(",");
        }
        if (!StringUtils.isEmpty(holidaysStr)){
            holidaysArry = holidaysStr.split(",");
        }
        List<ParticularsBean> list = new ArrayList<>();
        if (dateCountArry !=  null){
            String holidayHalfHours = dateCountArry[0];
            if (!"0".equals(holidayHalfHours)){
                if (numPeople != 0){
                    list.add(setParticularsBean("人数\n(节假日)",numPeople,holidaysArry[0],holidayHalfHours));
                }
                if (numCoach != 0){
                    list.add(setParticularsBean("教练\n(节假日)",numCoach,holidaysArry[1],holidayHalfHours));
                }
                if (numHelmsman != 0){
                    list.add(setParticularsBean("舵手\n(节假日)",numHelmsman,holidaysArry[2],holidayHalfHours));
                }
            }
            String weekendHalfHours = dateCountArry[1];
            if (!"0".equals(weekendHalfHours)){
                if (numPeople != 0){
                    list.add(setParticularsBean("人数\n(周末)",numPeople,weekMoneyArry[0],weekendHalfHours));
                }
                if (numCoach != 0){
                    list.add(setParticularsBean("教练\n(周末)",numCoach,weekMoneyArry[1],weekendHalfHours));
                }
                if (numHelmsman != 0){
                    list.add(setParticularsBean("舵手\n(周末)",numHelmsman,weekMoneyArry[2],weekendHalfHours));
                }
            }
            String workHalfHours = dateCountArry[2];
            if (!"0".equals(workHalfHours)){
                if (numPeople != 0){
                    list.add(setParticularsBean("人数\n(工作日)",numPeople,workMoneyArry[0],workHalfHours));
                }
                if (numCoach != 0){
                    list.add(setParticularsBean("教练\n(工作日)",numCoach,workMoneyArry[1],workHalfHours));
                }
                if (numHelmsman != 0){
                    list.add(setParticularsBean("舵手\n(工作日)",numHelmsman,workMoneyArry[2],workHalfHours));
                }
            }
        }
        ParticularsAdapter adapter = new ParticularsAdapter(this,list);
        mListViewTV.setAdapter(adapter);
    }

    /**
     *
     * @param peopleNum 人数
     * @param price 单价
     * @param halfHourNum 半个小时数
     * @return
     */
    private ParticularsBean setParticularsBean(String item,int peopleNum,String price,String halfHourNum){
        ParticularsBean  bean = new ParticularsBean();
        bean.setNum(""+peopleNum);
        bean.setItem(item);
        bean.setPrice(price);
        float priceF = Float.parseFloat(price);//单价
        float time = (float)(Float.parseFloat(halfHourNum)/2);//小时数
        bean.setTime(time+"");//时间
        bean.setTotal(time*priceF*peopleNum+"");//合计
        return bean;
    }

}
