package com.zkrt.zkrtdrone.view.myfragment.mapmvp;

import android.graphics.Bitmap;

import dji.common.camera.DJICameraExposureParameters;
import dji.common.camera.DJICameraSettingsDef;
import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.remotecontroller.DJIRCGPSData;
import dji.sdk.remotecontroller.DJIRemoteController;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseModel;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BasePresenter;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseView;

/**
 * Created by jack_xie on 16-12-27.
 */

public interface MapContract {
    interface Model extends BaseModel {
        android.view.View getDroneMap();
    }


    interface View extends BaseView {

        void showDjiBaseProdouct(DJIFlightControllerCurrentState djiFlightControllerCurrentState);
        void showDroneBitMap(Bitmap bitmap);
        void showRCGps(DJIRemoteController djiRemoteController, DJIRCGPSData djircgpsData);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getBaseProdouct();
        public abstract void getDroneView();
    }
}
