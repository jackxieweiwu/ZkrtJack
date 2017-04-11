package com.zkrt.zkrtdrone.view.myfragment.hand_setting.hand;

import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.RemoteSettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.batterysetting.BatterySettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.commonsetting.CommonSettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.dronesetting.DroneSettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.hdsetting.HDSettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.holdersetting.HolderSettingFragment;

import java.util.ArrayList;

import dji.common.airlink.DJISignalInformation;
import dji.common.battery.DJIBatteryState;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseModel;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BasePresenter;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseView;

/**
 * Created by jack_xie on 17-2-15.
 */

public interface HandContract {

    interface Model extends BaseModel {
        BatterySettingFragment getBatterySetting();
        CommonSettingFragment getCommonSetting();
        DroneSettingFragment getDroneSetting();
        HolderSettingFragment getHolderSetting();
        HDSettingFragment getHDsetting();
        RemoteSettingFragment getRemoteSetting();
    }

    interface View extends BaseView {
        //void showDjiBaseProdouct(DJIFlightControllerCurrentState djiFlightControllerCurrentState);
        void showDJIBatteryState(DJIBatteryState djiBatteryState);
        void showDJISignalInformation(ArrayList<DJISignalInformation> arrayList);

       /* void showDjiBaseProdouct(DJIFlightControllerCurrentState currentState);*/

        void showSettingFragment(BatterySettingFragment batterySettingFragment,
                                 CommonSettingFragment commonSettingFragment,
                                 DroneSettingFragment droneSettingFragment,
                                 HolderSettingFragment holderSettingFragment,
                                 HDSettingFragment hdSettingFragment,
                                 RemoteSettingFragment remoteSettingFragment);

    }

    abstract class Presenter extends BasePresenter<HandContract.Model, HandContract.View> {
        public abstract void getBaseProdouct();
        public abstract void getSettingFrgment();
    }
}
