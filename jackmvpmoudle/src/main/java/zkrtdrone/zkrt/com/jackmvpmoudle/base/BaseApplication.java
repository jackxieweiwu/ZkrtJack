package zkrtdrone.zkrt.com.jackmvpmoudle.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import org.litepal.LitePalApplication;

import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.camera.DJICamera;
import dji.sdk.products.DJIAircraft;
import dji.sdk.products.DJIHandHeld;
import dji.sdk.sdkmanager.DJISDKManager;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.helper.CrashHandler;

/**
 * Created by jack_xie on 16-12-22.
 */

public abstract class BaseApplication extends LitePalApplication {
    private static final String TAG = BaseApplication.class.getName();
    private Handler mHandler;
    public static final String FLAG_CONNECTION_CHANGE = "com_dji_jack_connection_change";
    private static DJIBaseProduct mProduct;
    public static FragmentManager fragmentManager;
    private Context context;
    public static  boolean settingBool = false;
    public static Activity activity;
    public static double peploLat=0;
    public static double peploLng=0;
    public static  boolean rightMenu = true;

    public static synchronized DJIBaseProduct getProductInstance() {
        if (null == mProduct) {
            mProduct = DJISDKManager.getInstance().getDJIProduct();
        }
        return mProduct;
    }
    public static synchronized DJICamera getCameraInstance() {
        if (getProductInstance() == null) return null;
        DJICamera camera = null;
        if (getProductInstance() instanceof DJIAircraft){
            camera = ((DJIAircraft) getProductInstance()).getCamera();
        } else if (getProductInstance() instanceof DJIHandHeld) {
            camera = ((DJIHandHeld) getProductInstance()).getCamera();
        }
        return camera;
    }

    public static boolean isAircraftConnected() {
        return getProductInstance() != null && getProductInstance() instanceof DJIAircraft;
    }

    public static boolean isHandHeldConnected() {
        return getProductInstance() != null && getProductInstance() instanceof DJIHandHeld;
    }

    public static synchronized DJIAircraft getAircraftInstance() {
        if (!isAircraftConnected()) return null;
        return (DJIAircraft) getProductInstance();
    }

    public static synchronized DJIHandHeld getHandHeldInstance() {
        if (!isHandHeldConnected()) return null;
        return (DJIHandHeld) getProductInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        CrashHandler.getInstance().initialize(this);
        mHandler = new Handler(Looper.getMainLooper());
        DJISDKManager.getInstance().initSDKManager(getApplicationContext(), mDJISDKManagerCallback);
        //mHandler.postDelayed(djiSDKManagerRunnable, 1000);
        initView();
    }

    protected abstract void initView();

   /* private Runnable djiSDKManagerRunnable = new Runnable() {
        @Override
        public void run() {
            *//*if(!DJISDKManager.getInstance().hasSDKRegistered()){
                DJISDKManager.getInstance().registerApp();
            }*//*

        }
    };*/

    private DJISDKManager.DJISDKManagerCallback mDJISDKManagerCallback = new DJISDKManager.DJISDKManagerCallback() {
        @Override
        public void onGetRegisteredResult(DJIError error) {
            Handler handler = new Handler(Looper.getMainLooper());
            if(error == DJISDKError.REGISTRATION_SUCCESS) {
                DJISDKManager.getInstance().startConnectionToProduct();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"验证成功", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "SDK 注册失败。请检查绑定 ID 和您的网络连接",
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        }

        @Override
        public void onProductChanged(DJIBaseProduct oldProduct, DJIBaseProduct newProduct) {

            Log.v(TAG, String.format("onProductChanged oldProduct:%s, newProduct:%s", oldProduct, newProduct));
            mProduct = newProduct;
            if(mProduct != null) {
                mProduct.setDJIBaseProductListener(mDJIBaseProductListener);
            }
            notifyStatusChange();
        }

        private DJIBaseProduct.DJIBaseProductListener mDJIBaseProductListener = new DJIBaseProduct.DJIBaseProductListener() {

            @Override
            public void onComponentChange(DJIBaseProduct.DJIComponentKey key, DJIBaseComponent oldComponent, DJIBaseComponent newComponent) {

                if(newComponent != null) {
                    newComponent.setDJIComponentListener(mDJIComponentListener);
                }
                Log.v(TAG, String.format("onComponentChange key:%s, oldComponent:%s, newComponent:%s", key, oldComponent, newComponent));
                notifyStatusChange();
            }

            @Override
            public void onProductConnectivityChanged(boolean isConnected) {

                Log.v(TAG, "onProductConnectivityChanged: " + isConnected);

                notifyStatusChange();
            }

        };

        private DJIBaseComponent.DJIComponentListener mDJIComponentListener = new DJIBaseComponent.DJIComponentListener() {

            @Override
            public void onComponentConnectivityChanged(boolean isConnected) {
                notifyStatusChange();
            }

        };

        private void notifyStatusChange() {
            mHandler.removeCallbacks(updateRunnable);
            mHandler.postDelayed(updateRunnable, 500);
        }

        private Runnable updateRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FLAG_CONNECTION_CHANGE);
                sendBroadcast(intent);
            }
        };
    };

    @Override
    public void onTerminate() {
        super.onTerminate();
        DJISDKManager.getInstance().stopConnectionToProduct();
        DJISDKManager.getInstance().destroy();
        terMinate();
    }
    protected abstract void terMinate();
}
