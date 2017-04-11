package com.zkrt.zkrtdrone.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;

import dji.sdk.base.DJIBaseProduct;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseApplication;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BasePresenter;

/**
 * Created by jack on 17-1-10.
 */

public abstract class BaseJackPresenter<M,V>extends BasePresenter<M,V> {

    @Override
    public void onAttached() {
    }

    //DJI IntentFilter
    public void setIntentFilter(){
        refreshSDKRelativeUI();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BaseApplication.FLAG_CONNECTION_CHANGE);
        DJISampleApplication.mContext.registerReceiver(mReceiver, filter);
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshSDKRelativeUI();
        }
    };

    protected void refreshSDKRelativeUI(){
        if(DJIModuleVerificationUtil.isProductModuleAvailable()){
            getDjiBaseProduct(DJISampleApplication.getProductInstance());
        }
    }

    protected abstract void getDjiBaseProduct(DJIBaseProduct productInstance);
}
