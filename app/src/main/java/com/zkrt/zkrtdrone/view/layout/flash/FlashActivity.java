package com.zkrt.zkrtdrone.view.layout.flash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;
import com.zkrt.zkrtdrone.view.dialog.TimeDialog;
import com.zkrt.zkrtdrone.view.layout.main.MainActivity;

import butterknife.BindView;
import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.flightcontroller.DJIFlightLimitation;
import dji.sdk.products.DJIAircraft;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseApplication;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseMvpActivity;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.ToastUtil;

public class FlashActivity extends BaseMvpActivity implements DJIBaseProduct.DJIVersionCallback{
    @BindView(R.id.ftb_start) FloatingActionButton ftb_start;
    @BindView(R.id.ftb_start2) FloatingActionButton ftb_start2;
    private DJIBaseProduct djiBaseProduct;


    @Override
    public int getLayoutId() {
        return R.layout.activity_flash;
    }

    @Override
    public void onProductVersionChange(String s, String s1) {
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        getBaseProdouct();
        refreshSDKRelativeUI();

        TimeDialog timeDialog = new TimeDialog(this,R.style.set_dialogs);
        timeDialog.setCanceledOnTouchOutside(false);
        ftb_start.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        ftb_start2.setOnClickListener(v -> setTImeDialog(timeDialog));
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshSDKRelativeUI();
            //context.unregisterReceiver(this);
        }
    };

    protected void refreshSDKRelativeUI(){
        if(DJIModuleVerificationUtil.isProductModuleAvailable()){
            DJIBaseProduct djiBaseProduct = DJISampleApplication.getProductInstance();
            if (djiBaseProduct != null && djiBaseProduct.isConnected()) {
                if (djiBaseProduct instanceof DJIAircraft) {
                    showDjiBaseProdouct(djiBaseProduct);
                    zkrtSusse(true);
                }
            }else{
                zkrtSusse(false);
                if(DJIModuleVerificationUtil.isRemoteControllerAvailable()){
                    showRc(DJISampleApplication.getAircraftInstance().getRemoteController().isConnected());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        java.lang.System.gc();
    }

    private void setTImeDialog(TimeDialog timeDialog){
        if(DJIModuleVerificationUtil.isRemoteControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getRemoteController().enterRCPairingMode(new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if(djiError == null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timeDialog.showDialog();
                            }
                        });
                    }
                }
            });
        }
    }

    public void getBaseProdouct() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BaseApplication.FLAG_CONNECTION_CHANGE);
        registerReceiver(mReceiver, filter);
    }

    public void showDjiBaseProdouct(DJIBaseProduct djiBaseProducta) {
        djiBaseProduct = djiBaseProducta;
        if(djiBaseProducta !=null) djiBaseProduct.setDJIVersionCallback(FlashActivity.this);
    }

    public void zkrtSusse(boolean bool) {
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()) {
            //IMU正在预热。
            /*if (DJISampleApplication.getAircraftInstance().getFlightController().getCurrentState().isIMUPreheating())
                Utils.setResultToToast(this, "IMU正在预热");*/
        }
        if(djiBaseProduct !=null && null != djiBaseProduct.getModel() && bool){
            ToastUtil.show(this,"连接成功...");
            DJISampleApplication.aBoolean = true;
            if(ftb_start !=null) ftb_start.setVisibility(View.VISIBLE);
            if(ftb_start2 !=null) ftb_start2.setVisibility(View.GONE);
            //if(ftb_start2 !=null) ftb_start2.setVisibility(View.GONE);
        }else{
            ToastUtil.show(this,"未与设备连接...");
            DJISampleApplication.aBoolean = false;
            if(ftb_start !=null) ftb_start.setVisibility(View.GONE);
        }
    }

    public void showRc(boolean bool) {
        if(ftb_start2 == null) return;
        if(bool){
            ftb_start2.setVisibility(View.VISIBLE);
        }else ftb_start2.setVisibility(View.GONE);
    }
}