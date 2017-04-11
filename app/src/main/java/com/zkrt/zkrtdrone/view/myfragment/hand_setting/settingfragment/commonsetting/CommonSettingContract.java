package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.commonsetting;


import com.zkrt.zkrtdrone.bean.LogRz;

import java.util.List;

import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIIMUState;
import dji.common.remotecontroller.DJIRCGimbalControlDirection;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseModel;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BasePresenter;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseView;

/**
 * Created by jack_xie on 17-2-21.
 */

public interface CommonSettingContract {
    interface Model extends BaseModel {
    }

    interface View extends BaseView {
        void showDJiOnIMUStateChangedCallback(DJIIMUState djiimuState);
        void showDjiError(DJIError djiError);

        void showRccontrolGimnalDirection(DJIRCGimbalControlDirection djircGimbalControlDirection);
    }

    abstract class Presenter extends BasePresenter<CommonSettingContract.Model, CommonSettingContract.View> {
        public abstract void DJiOnIMUStateChangedCallback();
    }
}
