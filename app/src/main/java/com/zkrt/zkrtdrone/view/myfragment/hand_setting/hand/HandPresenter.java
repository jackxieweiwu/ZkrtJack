package com.zkrt.zkrtdrone.view.myfragment.hand_setting.hand;

//import com.app.annotation.apt.Instance;
import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;

import java.util.ArrayList;

import dji.common.airlink.DJISignalInformation;
import dji.common.airlink.SignalQualityUpdatedCallback;
import dji.common.battery.DJIBatteryState;
import dji.sdk.airlink.DJILBAirLink;
import dji.sdk.battery.DJIBattery;

/**
 * Created by jack_xie on 16/4/22.
 */
//@Instance
public class HandPresenter extends HandContract.Presenter {
    @Override
    public void onAttached() {
        getSettingFrgment();
        getBaseProdouct();
        //initEvent();
    }

    /*@BusRegister
    protected void initEvent() {
        RxBus.getDefault().register(this);
    }*/

    @Override
    public void getBaseProdouct() {
        /*if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
            ((DJIAircraft) DJISampleApplication.getProductInstance()).getFlightController().setUpdateSystemStateCallback(
                    new DJIFlightControllerDelegate.FlightControllerUpdateSystemStateCallback() {
                        @Override
                        public void onResult(DJIFlightControllerCurrentState
                                                     djiFlightControllerCurrentState) {
                            mView.showDjiBaseProdouct(djiFlightControllerCurrentState);
                        }
                    });
        }*/

        if(DJIModuleVerificationUtil.isBattery()){
            DJISampleApplication.getProductInstance().getBattery().setBatteryStateUpdateCallback(
                    new DJIBattery.DJIBatteryStateUpdateCallback() {
                        @Override
                        public void onResult(DJIBatteryState djiBatteryState) {
                            mView.showDJIBatteryState(djiBatteryState);


                        }
                    });
        }

        if(DJIModuleVerificationUtil.isLBAirlinkAvailable()) {
            DJISampleApplication.getProductInstance().getAirLink().getLBAirLink().
                    setLBAirLinkUpdatedRemoteControllerSignalInformationCallback(new DJILBAirLink.
                            DJILBAirLinkUpdatedRemoteControllerSignalInformationCallback() {
                        @Override
                        public void onResult(ArrayList<DJISignalInformation> arrayList) {
                            mView.showDJISignalInformation(arrayList);
                        }
                    });

        }
        /*if(DJIModuleVerificationUtil.isLBAirlinkAvailable()) {
            DJISampleApplication.getProductInstance().getAirLink().getLBAirLink().
                    setLBAirLinkUpdatedRemoteControllerSignalInformationCallback(new DJILBAirLink.
                            DJILBAirLinkUpdatedRemoteControllerSignalInformationCallback() {
                        @Override
                        public void onResult(ArrayList<DJISignalInformation> arrayList) {
                            mView.showDJISignalInformation(arrayList);
                        }
                    });
        }*/
     /*   Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(DJIModuleVerificationUtil.isFlightControllerAvailable()) {
                    mView.showDjiBaseProdouct(DJISampleApplication.getAircraftInstance().getFlightController().getCurrentState());
                }
            }
        }, 0, 1, TimeUnit.SECONDS);*/
    }

    @Override
    public void getSettingFrgment() {
        mView.showSettingFragment(mModel.getBatterySetting(),mModel.getCommonSetting(),mModel.getDroneSetting(),
                mModel.getHolderSetting(),mModel.getHDsetting(),mModel.getRemoteSetting());
    }

    /*@Override
    @BusUnRegister
    public void onDetached() {
        super.onDetached();
        RxBus.getDefault().unRegister(this);
    }*/
}