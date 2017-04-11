package com.zkrt.zkrtdrone.view.layout.main;

//import com.app.annotation.apt.Instance;
import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by jack_xie on 16/4/22.
 */
//@Instance
public class MainPresenter extends MainContract.Presenter {
    @Override
    public void onAttached() {
        getFlightControllerState();
        //initEvent();
        getFragment();
        getMapFragment();
        getMountFragment();
        getCameraFileFragment();
    }

    @Override
    public void getFragment() {
        mView.showHandFragment(mModel.getHandFragment());
    }

    @Override
    public void getMapFragment() {
        mView.showMyMapFragment(mModel.getMyMapFragment());
    }

    @Override
    public void getMountFragment() {
        mView.showMountFragment(mModel.getMountFragment());
    }

    @Override
    public void getCameraFileFragment() {
        mView.showCameraFileFragment(mModel.getCameraFileFragment(),mModel.getModelCameraAndOne());
        mView.showSensorManager(mModel.getSensorManager());
    }

    @Override
    public void getFlightControllerState() {
        mView.showCameraFPV(mModel.getCameraFPV());
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(DJIModuleVerificationUtil.isFlightControllerAvailable()) {
                    mView.showFlightControllerState(DJISampleApplication.getAircraftInstance().getFlightController().getCurrentState());
                    if(DJIModuleVerificationUtil.isRemoteControllerAvailable()){
                        mView.showRemoteBool(DJISampleApplication.getAircraftInstance().getRemoteController().isConnected());
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}
