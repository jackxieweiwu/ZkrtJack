package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.dronesetting;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;

import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIFlightFailsafeOperation;
import dji.common.util.DJICommonCallbacks;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.rxbean.CommonRxTask;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.rxbean.IOTask;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.rxutil.RxjavaUtil;

/**
 * Created by jack_xie on 17-2-21.
 */

public class DroneSettingPresenter extends DroneSettingContract.Presenter{
    @Override
    public void onAttached() {
        getDJIFlightFailsafeOperation();
        getGoHomeHight();
        getMaxFlightHeight();
        getMaxFlightRadius();
        getMaxFlightRadiusLimitationEnable();
    }

    @Override
    public void getDJIFlightFailsafeOperation() {
        mView.showAdapter(mModel.getAdapter());
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getFlightController().
                    getFlightFailsafeOperation(new DJICommonCallbacks.DJICompletionCallbackWith<DJIFlightFailsafeOperation>() {
                @Override
                public void onSuccess(DJIFlightFailsafeOperation djiFlightFailsafeOperation) {
                    mView.showDJIFlightFailsafeOperation(djiFlightFailsafeOperation);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showError(djiError);
                }
            });
        }
    }

    @Override
    public void getGoHomeHight() {
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getFlightController().getGoHomeAltitude(new DJICommonCallbacks.DJICompletionCallbackWith<Float>() {
                @Override
                public void onSuccess(Float aFloat) {
                    mView.showGoHomeHight(aFloat);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showError(djiError);
                }
            });
        }
    }

    @Override
    public void getMaxFlightHeight() {
        if (DJIModuleVerificationUtil.isFlightControllerAvailable()) {
            DJISampleApplication.getAircraftInstance().getFlightController().getFlightLimitation().getMaxFlightHeight(
                    new DJICommonCallbacks.DJICompletionCallbackWith<Float>() {
                        @Override
                        public void onSuccess(Float aFloat) {
                            mView.showMaxFlightHeight(aFloat);
                        }

                        @Override
                        public void onFailure(DJIError djiError) {
                            mView.showError(djiError);
                        }
                    });
        }
    }

    @Override
    public void getMaxFlightRadius() {
        if (DJIModuleVerificationUtil.isFlightControllerAvailable()) {
            DJISampleApplication.getAircraftInstance().getFlightController().getFlightLimitation().getMaxFlightRadius(
                    new DJICommonCallbacks.DJICompletionCallbackWith<Float>() {
                        @Override
                        public void onSuccess(Float aFloat) {
                            mView.showMaxFlightRadius(aFloat);
                        }

                        @Override
                        public void onFailure(DJIError djiError) {
                            mView.showError(djiError);
                        }
                    });
        }
    }

    @Override
    public void getMaxFlightRadiusLimitationEnable() {
    //获取最大飞行半径限制是否已启用。
        if (DJIModuleVerificationUtil.isFlightControllerAvailable()) {
            DJISampleApplication.getAircraftInstance().getFlightController().getFlightLimitation()
                    .getMaxFlightRadiusLimitationEnable(new DJICommonCallbacks.DJICompletionCallbackWith<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            mView.showMaxFlightRadiusLimitationEnable(aBoolean);
                        }

                        @Override
                        public void onFailure(DJIError djiError) {
                            mView.showError(djiError);
                        }
                    });
        }
    }
}
