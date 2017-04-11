package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.commonsetting;


import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;

import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIIMUState;
import dji.common.remotecontroller.DJIRCGimbalControlDirection;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.flightcontroller.DJIFlightControllerDelegate;

/**
 * Created by jack_xie on 17-2-21.
 */

public class CommonSettingPresenter extends CommonSettingContract.Presenter{
    @Override
    public void onAttached() {
        DJiOnIMUStateChangedCallback();
    }

    @Override
    public void DJiOnIMUStateChangedCallback() {
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getFlightController().setOnIMUStateChangedCallback(new DJIFlightControllerDelegate.FlightControllerIMUStateChangedCallback() {
                @Override
                public void onStateChanged(DJIIMUState djiimuState) {
                    mView.showDJiOnIMUStateChangedCallback(djiimuState);
                }
            });
        }

        if(DJIModuleVerificationUtil.isRemoteControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getRemoteController().getRCControlGimbalDirection(new DJICommonCallbacks.DJICompletionCallbackWith<DJIRCGimbalControlDirection>() {
                @Override
                public void onSuccess(DJIRCGimbalControlDirection djircGimbalControlDirection) {
                    mView.showRccontrolGimnalDirection(djircGimbalControlDirection);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });
        }
    }
}
