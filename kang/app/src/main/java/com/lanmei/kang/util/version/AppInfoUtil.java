package com.lanmei.kang.util.version;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.xson.common.utils.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class AppInfoUtil {

	/**
	 * 返回当前程序版本名
	 * 
	 * @throws NameNotFoundException
	 */
	public static String getAppVersionName(Context context) throws NameNotFoundException {
		String versionName = "";
		PackageManager pm = context.getPackageManager();
		PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
		versionName = pi.versionName;
		if (StringUtils.isEmpty(versionName)) {
			return "";
		}
		return versionName;
	}
	/**
	 * version number of this package
	 * @throws NameNotFoundException
	 */
	public static int getAppVersionCode(Context context) throws NameNotFoundException {
		int versionCode;
		PackageManager pm = context.getPackageManager();
		PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
		versionCode = pi.versionCode;
		return versionCode;
	}

	/**
	 * 获取ip地址
	 * 
	 * @return
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en
					.hasMoreElements();) {
				NetworkInterface intf = (NetworkInterface) en.nextElement();
				for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr
						.hasMoreElements();) {
					InetAddress inetAddress = (InetAddress) enumIpAddr
							.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @Description: 检测当前的版本信息
	 * @param
	 * @return int
	 * @throws
	 */
	public static int getSystemVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

}
