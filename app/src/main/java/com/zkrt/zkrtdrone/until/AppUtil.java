package com.zkrt.zkrtdrone.until;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.zkrt.zkrtdrone.R;

import java.text.DecimalFormat;

import zkrtdrone.zkrt.com.maplib.App;

/**
 * Created by jack_xie on 17-2-9.
 */

public class AppUtil {
    public static int getVerCode() {
        int verCode = 0;
        try {
            verCode = App.app.getPackageManager().getPackageInfo(
                    App.app.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    public static String getVerName() {
        String verName = "";
        try {
            verName = App.app.getPackageManager().getPackageInfo(
                    App.app.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    public static String getAppName() {
        String verName = App.getResources().getText(R.string.app_name).toString();
        return verName;
    }

    public static String doubleToString(double d1){
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(d1);
    }

    public static String getAppPackageName() {
        String pName = "";
        try {
            pName = App.app.getPackageManager().getPackageInfo(
                    App.app.getPackageName(), 0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pName;
    }

    public static String getImei() {
        TelephonyManager tm = (TelephonyManager) App.app
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        if (imei == null) {
            imei = "";
        }
        return imei;
    }

    public static String getImsi() {
        TelephonyManager mTelephonyMgr = (TelephonyManager) App.app
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();
        if (imsi == null) {
            imsi = "";
        }
        return imsi;
    }

    /**
     * 获得AndroidMainfest里的meta-data信息 例如：<meta-data android:value="2bulu"
     * android:name="UMENG_CHANNEL"/>里的UMENG_CHANNEL对应的值
     *
     * @param name
     * @return
     */
    public static String getMainfestMetaData(String name) {
        ApplicationInfo info;
        try {
            info = App.app.getPackageManager().getApplicationInfo(
                    App.app.getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
        return "";
    }


    private static String channel = null;
    /**
     * 获得渠道号
     * @return
     */
    public static String getChannel(){
        if(channel == null){
            channel = getMainfestMetaData("UMENG_CHANNEL");
        }
        return channel;
    }
}
