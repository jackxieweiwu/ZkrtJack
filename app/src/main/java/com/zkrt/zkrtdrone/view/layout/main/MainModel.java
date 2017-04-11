package com.zkrt.zkrtdrone.view.layout.main;

import android.content.Context;
import android.hardware.SensorManager;

//import com.app.annotation.apt.Instance;
import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.view.myfragment.camera.CameraFPV;
import com.zkrt.zkrtdrone.view.myfragment.camera.ModelCameraAndOne;
import com.zkrt.zkrtdrone.view.myfragment.camera.cameraFileSetting.CameraFileFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.hand.HandFragment;
import com.zkrt.zkrtdrone.view.myfragment.mount.MountFragment;
import com.zkrt.zkrtdrone.view.myfragment.mapmvp.MyMapFragment;

/**
 * Created by jack_xie on 16/5/2.
 */
//@Instance
public class MainModel implements MainContract.Model {

    @Override
    public HandFragment getHandFragment() {
        return new HandFragment();
    }

    @Override
    public MyMapFragment getMyMapFragment() {
        return new MyMapFragment();
    }

    @Override
    public MountFragment getMountFragment() {
        return new MountFragment();
    }

    @Override
    public CameraFileFragment getCameraFileFragment() {
        return new CameraFileFragment();
    }

    @Override
    public ModelCameraAndOne getModelCameraAndOne() {
        return new ModelCameraAndOne();
    }

    @Override
    public CameraFPV getCameraFPV() {
        return new CameraFPV();
    }

    @Override
    public SensorManager getSensorManager() {
        return (SensorManager) DJISampleApplication.activity.getSystemService(Context.SENSOR_SERVICE);
    }
}
