package com.lanmei.kang.util;

import android.content.Context;

public class SharedAccount {

	private static SharedPreferencesTool share;
	private SharedAccount(){}
	private static SharedAccount instance = null;
	public static SharedAccount getInstance(Context context){
		if (instance == null) {
			instance = new SharedAccount();
		}
		share = SharedPreferencesTool.getInstance(context, "account");
		return instance;
	}
	

	public void saveMobile(String mobile){
		share.edit().putString("mobile", mobile).commit();
	}
	public void saveLat(String lat){
		share.edit().putString("lat", lat).commit();
	}
	public void saveCity(String city){
		share.edit().putString("city", city).commit();
	}
	public void saveLon(String lon){
		share.edit().putString("lon", lon).commit();
	}

	public String getLat(){
		return share.getString("lat", "");
	}
	public String getCity(){
		return share.getString("city", "");
	}

	public String getLon(){
		return share.getString("lon", "");
	}

	public void saveLoginCount(int count){
		share.edit().putInt("count", count).commit();
	}
	public void saveAd(String ad){//轮播图
		share.edit().putString("ad", ad).commit();
	}

	public String getAd(){
		return share.getString("ad", "");
	}

	public String getMobile(){
		return share.getString("mobile", "");
	}

	public int getLoginCount(){
		return share.getInt("count", 0);
	}


	public void setNoFirstLogin(boolean isFirstLogin){
		share.edit().putBoolean("isFirstLogin", isFirstLogin).commit();
	}

	public boolean isFirstLogin(){
		return share.getBoolean("isFirstLogin", false);
	}


	public void clear(){
		share.clear();
	}
}
