package com.zkrt.zkrtdrone.view.myfragment.mapmvp;

import android.graphics.Bitmap;
import android.view.View;

//import com.app.annotation.apt.Instance;
import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;
import com.zkrt.zkrtdrone.view.widget.RotateImageView;

import dji.common.camera.DJICameraExposureParameters;
import dji.common.camera.DJICameraSettingsDef;
import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.remotecontroller.DJIRCGPSData;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.camera.DJICamera;
import dji.sdk.flightcontroller.DJIFlightControllerDelegate;
import dji.sdk.remotecontroller.DJIRemoteController;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.DensityUtil;

/**
 * Created by jack_xie on 16/4/22.
 */
//@Instance
public class MapPresenter extends MapContract.Presenter {
    //private AttitudeIndicator dron_attitu;
    private RotateImageView dron_attitu;
    private View viewDrone;
    @Override
    public void onAttached() {
        getBaseProdouct();
        //initEvent();
        getDroneView();
    }

    @Override
    public void getBaseProdouct() {
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()) {
            DJISampleApplication.getAircraftInstance().getFlightController().
                    setUpdateSystemStateCallback(new DJIFlightControllerDelegate.
                            FlightControllerUpdateSystemStateCallback() {
                        @Override
                        public void onResult(DJIFlightControllerCurrentState djiFlightControllerCurrentState) {
                            if(DJIModuleVerificationUtil.isRemoteControllerAvailable()){
                                DJISampleApplication.getAircraftInstance().getRemoteController().setGpsDataUpdateCallback(new DJIRemoteController.RCGpsDataUpdateCallback() {
                                    @Override
                                    public void onGpsDataUpdate(DJIRemoteController djiRemoteController, DJIRCGPSData djircgpsData) {
                                        mView.showRCGps(djiRemoteController,djircgpsData);
                                    }
                                });
                            }

                            mView.showDroneBitMap(droneDirection(djiFlightControllerCurrentState));
                            mView.showDjiBaseProdouct(djiFlightControllerCurrentState);
                        }
                    });
        }
    }

    @Override
    public void getDroneView() {
        viewDrone = mModel.getDroneMap();
        if(viewDrone !=null) {
            dron_attitu = (RotateImageView) viewDrone.findViewById(R.id.img_drone);
        }
    }

    //自定义布局根据无人机的机头转变方向
    public Bitmap droneDirection(DJIFlightControllerCurrentState djiFlightControllerCurrentState){
        getDroneView();
        if(viewDrone != null){
            /*dron_attitu.setAttitude(-djiFlightControllerCurrentState.getAttitude().roll,djiFlightControllerCurrentState.getAttitude().pitch,
                    djiFlightControllerCurrentState.getAttitude().yaw);*/
            dron_attitu.setAttitude(djiFlightControllerCurrentState.getAttitude().yaw);
            return loadBitmapFromView(viewDrone);
        }
        return null;
    }

    public Bitmap loadBitmapFromView(View view) {
        if (view == null) {
            return null;
        }
        view.measure(View.MeasureSpec.makeMeasureSpec(DensityUtil.dip2px(DJISampleApplication.mContext, 40f),
                View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                DensityUtil.dip2px(DJISampleApplication.mContext, 45f), View.MeasureSpec.EXACTLY));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }
}