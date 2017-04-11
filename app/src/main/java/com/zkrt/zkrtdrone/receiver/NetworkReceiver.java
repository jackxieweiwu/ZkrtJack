package com.zkrt.zkrtdrone.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import zkrtdrone.zkrt.com.maplib.App;


/**
 * Created by jack_xie on 17-2-9.
 */

public class NetworkReceiver extends BroadcastReceiver {
    private volatile static boolean isConnected = true;
    private volatile static NetType curNetType = NetType.unknown;

    @Override
    public void onReceive(Context context, Intent intent) {

        checkConnect();
    }

    public void regist(Context context){
        IntentFilter f = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(this, f);

        checkConnect();
    }

    public void unRegist(Context context){
        context.unregisterReceiver(this);
    }

    /**
     * 网络是否连接
     */
    public static boolean isConnected() {
        return isConnected;
    }

    /**
     * 获得当前网络类型
     * @return
     */
    public static NetType getCurNetType() {
        return curNetType;
    }



    private void checkConnect(){
        boolean curConnected = isNetConnected();
        NetType thisNetType = getNetType();

        NetworkReceiver.isConnected = curConnected;
        NetworkReceiver.curNetType = thisNetType;
    }

    /**
     * 检测网络是否连接
     */
    private static boolean isNetConnected() {
        ConnectivityManager conManager = (ConnectivityManager) App.app
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if (networkInfo != null)
            return networkInfo.isAvailable();
        return false;
    }

    /**
     * 获取网络类型
     * @return unknown 0, gprs 1, 3g 2, wifi 3
     */
    private static NetType getNetType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.app
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null) {
            int type = activeNetInfo.getType();
            if (type == ConnectivityManager.TYPE_WIFI)
                return NetType.wifi;
            else if (type == ConnectivityManager.TYPE_MOBILE) {
                type = activeNetInfo.getSubtype();
                if (type >= TelephonyManager.NETWORK_TYPE_EVDO_0)
                    //>=5(TelephonyManager.NETWORK_TYPE_EVDO_0)表示3G网络
                    return NetType.threeG;
                else
                    return NetType.gprs;
            }
        }
        return NetType.unknown;
    }

    public static enum NetType {
        unknown,
        gprs,
        threeG,
        wifi
    }
}
