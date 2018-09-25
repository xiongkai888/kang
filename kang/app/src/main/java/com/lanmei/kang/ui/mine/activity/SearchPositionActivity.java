package com.lanmei.kang.ui.mine.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.lanmei.kang.R;
import com.lanmei.kang.adapter.SearchPositionAdapter;
import com.lanmei.kang.event.LocationChooseEvent;
import com.lanmei.kang.util.loc.LocationService;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.DrawClickableEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 检索周边的地址列表
 */
public class SearchPositionActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @InjectView(R.id.keywordEditText)
    DrawClickableEditText mKeywordEditText;
    @InjectView(R.id.ll_no_location)
    LinearLayout mNoLocation;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    SearchPositionAdapter adapter;

    private static final int PERMISSION_LOCATION = 100;

    @Override
    public int getContentViewId() {
        return R.layout.activity_position;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
//        initLocation();//百度定位
        initPermission();
        initPoiSearch();
        mNoLocation.setVisibility(View.VISIBLE);
        mKeywordEditText.setOnEditorActionListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchPositionAdapter(this);
        adapter.setSearchPositionListener(new SearchPositionAdapter.SearchPositionListener() {
            @Override
            public void getAddress(String address) {
                location(address);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }


    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION);
            } else {
                initBaiDu();
            }
        } else {
            initBaiDu();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_LOCATION){
            initBaiDu();
        }
    }

    // 地理编码
    GeoCoder mGeoCoder = null;


    private void initPoiSearch() {
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(GeoListener);

        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (!StringUtils.isEmpty(poiResult) &&  !StringUtils.isEmpty(poiResult.getAllPoi())) {
                    mInfoList.clear();
                    mInfoList = poiResult.getAllPoi();
                    adapter.setData(mInfoList);
                    adapter.notifyDataSetChanged();
                    L.d("resultListener", "setOnGetPoiSearchResultListener");
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
    }

    List<PoiInfo> mInfoList = new ArrayList<>();

    // 地理编码监听器
    OnGetGeoCoderResultListener GeoListener = new OnGetGeoCoderResultListener() {

        public void onGetGeoCodeResult(GeoCodeResult result) {

            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有检索到结果
            }
            // 获取地理编码结果
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有找到检索结果
            } else {  // 获取反向地理编码结果
                mInfoList.clear();
                // 将周边信息加入表
                if (result.getPoiList() == null) {
                    return;
                }
                mInfoList.addAll(result.getPoiList());
                adapter.setData(mInfoList);
                adapter.notifyDataSetChanged();
                L.d("resultListener", "OnGetGeoCoderResultListener");
                if (mInfoList != null && mInfoList.size() > 0) {
                    int size = mInfoList.size();
                    for (int i = 0; i < size; i++) {
                        PoiInfo info = mInfoList.get(i);
                        if (info != null) {
                            L.d("resultListener", "lon = "+info.location.longitude+",lat = "+info.location.latitude);
                        }
                    }
                }
            }
        }
    };

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String key = v.getText().toString().trim();
            if (StringUtils.isEmpty(key)) {
                return true;
            }
            searchNeayBy(key);
            return false;
        }
        return false;
    }

    private LocationService locationService;

    private void initBaiDu() {
        // -----------location config ------------
        locationService = new LocationService(getApplicationContext());//放在SattingApp里面有问题
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
    }

    BDLocation lastLocation;
    double mCurrentLantitude;
    double mCurrentLongitude;


    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            lastLocation = location;
            mCurrentLantitude = lastLocation.getLatitude();
            mCurrentLongitude = lastLocation.getLongitude();
            // 发起反地理编码检索
            mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())
                    .location(new LatLng(mCurrentLantitude, mCurrentLongitude)));
            L.d("resultListener", "onReceiveLocation");
        }

        public void onConnectHotSpotMessage(String s, int i) {
        }
    };

    PoiSearch poiSearch;

    /**
     * 搜索周边地理位置
     * by hankkin at:2015-11-01 22:54:49
     */
    private void searchNeayBy(String address) {
        UIHelper.ToastMessage(this,address);
        PoiNearbySearchOption option = new PoiNearbySearchOption();
        option.keyword(address);
        option.sortType(PoiSortType.distance_from_near_to_far);
        option.location(new LatLng(mCurrentLantitude, mCurrentLongitude));
//        if (radius != 0) {
//            option.radius(radius);
//        } else {
        option.radius(10000);
//        }

        option.pageCapacity(20);
        poiSearch.searchNearby(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationService != null){
            locationService.unregisterListener(mListener);
            locationService.stop();
        }
        if (poiSearch != null){
            poiSearch.destroy();
        }
        if (mGeoCoder != null){
            mGeoCoder.destroy();
        }

    }

    @OnClick(R.id.back_iv)
    public void showBack() {//返回
        finish();
    }
    @OnClick(R.id.ll_no_location)
    public void showNoLocation() {//不显示位置
        location("");
    }
    private void location(String location){
        EventBus.getDefault().post(new LocationChooseEvent(location));
        finish();
    }
}
