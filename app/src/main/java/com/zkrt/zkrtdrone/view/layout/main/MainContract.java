package com.zkrt.zkrtdrone.view.layout.main;

import android.hardware.SensorManager;

import com.zkrt.zkrtdrone.view.myfragment.camera.CameraFPV;
import com.zkrt.zkrtdrone.view.myfragment.camera.ModelCameraAndOne;
import com.zkrt.zkrtdrone.view.myfragment.camera.cameraFileSetting.CameraFileFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.hand.HandFragment;
import com.zkrt.zkrtdrone.view.myfragment.mount.MountFragment;
import com.zkrt.zkrtdrone.view.myfragment.mapmvp.MyMapFragment;

import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseModel;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BasePresenter;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseView;

/**
 * Created by jack_xie on 16/4/22.
 */
public interface MainContract {
    interface Model extends BaseModel {
        HandFragment getHandFragment();
        MyMapFragment getMyMapFragment();
        MountFragment getMountFragment();
        CameraFileFragment getCameraFileFragment();
        ModelCameraAndOne getModelCameraAndOne();
        CameraFPV getCameraFPV();
        SensorManager getSensorManager();
    }

    interface View extends BaseView {
        void showHandFragment(HandFragment fragment);
        void showMyMapFragment(MyMapFragment fragment);
        void showMountFragment(MountFragment fragment);
        void showCameraFPV(CameraFPV cameraFPV);
        void showCameraFileFragment(CameraFileFragment fragment,ModelCameraAndOne modelCameraAndOne);
        void showFlightControllerState(DJIFlightControllerCurrentState djiFlightControllerCurrentState);
        void showSensorManager(SensorManager sensorManager);
        void showRemoteBool(boolean bool);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getFragment();
        public abstract void getMapFragment();
        public abstract void getMountFragment();
        public abstract void getCameraFileFragment();
        public abstract void getFlightControllerState();
    }
}
