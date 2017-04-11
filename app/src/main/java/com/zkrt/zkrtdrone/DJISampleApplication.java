package com.zkrt.zkrtdrone;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;


import com.zkrt.zkrtdrone.bean.DeviceCallBackTwo;
import com.zkrt.zkrtdrone.bean.exelBean;
import com.zkrt.zkrtdrone.receiver.NetworkReceiver;
import com.zkrt.zkrtdrone.view.layout.main.MainActivity;

import java.io.File;

import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseApplication;
import zkrtdrone.zkrt.com.maplib.App;
import zkrtdrone.zkrt.com.maplib.until.location.GpsUtil;

/**
 * Created by jack_xie on 16/12/15.
 */
public class DJISampleApplication extends BaseApplication {
    private static final String TAG = DJISampleApplication.class.getName();
    public static int DEVICE_TYPE_CAMERA = 0;
    public static float HH = 0;
    public static double Lat = 0.0d;
    public static double log = 0.0d;
    public static SharedPreferences mySharedPreferences,jettisoninSharedPreferences;
    public static File file;
    public static Context mContext;
    private NetworkReceiver networkReceiver;
    public static  boolean aBoolean = false;
    public static  double lowone = 0d;
    public static  double lowtwo = 0d;
    public static DeviceCallBackTwo deviceCallBackTwo;
    public static MainActivity mainActivity;
    public static boolean bool = false;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this) ;
    }

    @Override
    protected void initView() {
        App.app = this;
        mContext = this;
        mySharedPreferences = getSharedPreferences("gases", Activity.MODE_PRIVATE);
        jettisoninSharedPreferences = getSharedPreferences("jettisonin", Activity.MODE_PRIVATE);

        networkReceiver = new NetworkReceiver();
        networkReceiver.regist(this);
        GpsUtil.checkGPSDevice();
    }

    public void terMinate() {
        super.onTerminate();
        App.app = null;
        networkReceiver.unRegist(this);
    }
}
