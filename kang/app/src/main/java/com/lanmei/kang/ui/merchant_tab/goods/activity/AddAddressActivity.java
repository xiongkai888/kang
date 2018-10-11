package com.lanmei.kang.ui.merchant_tab.goods.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.AddressListBean;
import com.lanmei.kang.event.AddAddressEvent;
import com.lanmei.kang.event.AlterAddressEvent;
import com.lanmei.kang.helper.AddressAsyncTask;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;

/**
 * 添加收货地址
 */
public class AddAddressActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.name_et)
    EditText nameEt;
    @InjectView(R.id.phone_et)
    EditText phoneEt;
    @InjectView(R.id.area_tv)
    TextView areaTv;
    @InjectView(R.id.detail_address_et)
    EditText detailAddressEt;
    @InjectView(R.id.is_default_checkbox)
    CheckBox isDefaultCheckbox;
    String isDefault = "0";
    AddressListBean bean;//地址信息
    boolean isAdd;
    private AddressAsyncTask addressAsyncTask;
    private AddressPicker addressPicker;

    @Override
    public int getContentViewId() {
        return R.layout.activity_add_address;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (!StringUtils.isEmpty(bundle)){
            bean = (AddressListBean)bundle.getSerializable("bean");
        }
        isAdd = (bean == null);
    }

    private void initAddressPicker(ArrayList<Province> data) {
        addressPicker = new AddressPicker(this, data);
        addressPicker.setSelectedItem("广东", "广州", "天河");
        addressPicker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
            @Override
            public void onAddressPicked(Province province, City city, County county) {
                areaTv.setText(province.getAreaName()+"  "+city.getAreaName()+"  "+county.getAreaName());
                provinceId = province.getAreaId();
                cityId = city.getAreaId();
                areaId = county.getAreaId();
                L.d("AddressPicker",province.getAreaId()+","+city.getAreaId()+","+county.getAreaId());
            }
        });
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        addressAsyncTask = new AddressAsyncTask();//异步获取省市区列表
        addressAsyncTask.setAddressAsyncTaskListener(new AddressAsyncTask.AddressAsyncTaskListener() {
            @Override
            public void setAddressList(ArrayList<Province> result) {
                initAddressPicker(result);
            }
        });
        addressAsyncTask.execute();


        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        if (isAdd){
            actionbar.setTitle("添加收货地址");
        }else {
            actionbar.setTitle("编辑地址");
            String address = bean.getAddress();
            detailAddressEt.setText(bean.getAddr());
            if (StringUtils.isSame("1",bean.getDefaultX())){
                isDefaultCheckbox.setChecked(true);
                isDefault = "1";
            }else {
                isDefaultCheckbox.setChecked(false);
            }
            provinceId = bean.getProvince();
            cityId = bean.getCity();
            areaId = bean.getArea();
            if (!StringUtils.isEmpty(address)){
                address = address.replace(bean.getAddr(),"");
                areaTv.setText(address);
            }

        }
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        isDefaultCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isDefault = "1";
                } else {
                    isDefault = "0";
                }
            }
        });
        UserBean bean = CommonUtils.getUserBean(this);
        if (StringUtils.isEmpty(bean)) {
            return;
        }
        nameEt.setText(bean.getNickname());
        phoneEt.setText(bean.getPhone());
    }
    String provinceId;//省ID
    String cityId;//市ID
    String areaId;//


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            loadAddAddress();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadAddAddress() {
        String name = CommonUtils.getStringByEditText(nameEt);
        if (StringUtils.isEmpty(name)){
            UIHelper.ToastMessage(this,R.string.input_reception_name);
            return;
        }
        String phone = CommonUtils.getStringByEditText(phoneEt);//areaTv
        if (StringUtils.isEmpty(phone)) {
            UIHelper.ToastMessage(this, R.string.input_phone_number);
            return;
        }
        if (!StringUtils.isMobile(phone)) {
            UIHelper.ToastMessage(this, R.string.not_mobile_format);
            return;
        }
        String area = CommonUtils.getStringByTextView(areaTv);//
        if (StringUtils.isEmpty(area)) {
            UIHelper.ToastMessage(this, "请选择项所在区域");
            return;
        }
        String detailAddress = CommonUtils.getStringByEditText(detailAddressEt);//areaTv
        if (StringUtils.isEmpty(detailAddress)) {
            UIHelper.ToastMessage(this, R.string.input_details_address);
            return;
        }
        httpAddress(name,phone,area,detailAddress);
    }

    private void httpAddress(String name, String phone, String area, String detailAddress) {
        KangQiMeiApi api = new KangQiMeiApi("");
        api.addParams("userid",api.getUserId(this));
        if (isAdd) {
            api.addParams("operation",1);//1|2|3|4=>添加|修改|删除|列表
        }else {
            api.addParams("operation",2);//1|2|3|4=>添加|修改|删除|列表
            api.addParams("id",bean.getId());
        }
        api.addParams("uname",name);
        api.addParams("uphone",phone);
        api.addParams("uarea",detailAddress);
        api.addParams("province",provinceId);
        api.addParams("city",cityId);
        api.addParams("area",areaId);
        api.addParams("mDefault",isDefault);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()){
                    return;
                }
                EventBus.getDefault().post(new AddAddressEvent());
                if (!isAdd){
                    EventBus.getDefault().post(new AlterAddressEvent());//通知确认订单的地址，地址有变化
                }
                finish();
            }
        });
    }


    @OnClick(R.id.ll_area)
    public void onViewClicked() {//所在区域
      if (addressPicker != null){
          addressPicker.show();
      }
    }
}
