package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.batterysetting;


import dji.common.battery.DJIBatteryAggregationState;
import dji.common.battery.DJIBatteryLowCellVoltageOperation;
import dji.common.battery.DJIBatteryState;
import dji.common.battery.DJIBatteryWarningInformation;
import dji.common.error.DJIError;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseModel;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BasePresenter;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseView;

/**
 * Created by jack_xie on 17-2-21.
 */

public interface BatterySettingContract {
    interface Model extends BaseModel {

    }

    interface View extends BaseView {
        void showDJIBatteryState(DJIBatteryState djiBatteryState);
        void showDjiError(DJIError djiError);
        void showDJIBatteryAggregationState(DJIBatteryAggregationState djiBatteryAggregationState);
        void showLevel1CellVoltageThreshold(Integer integer);
        void showLevel2CellVoltageThreshold(Integer integer);
        void showLevel1CellVoltageperation(DJIBatteryLowCellVoltageOperation djiBatteryLowCellVoltageOperation);

        void showLevel2CellVoltageperation(DJIBatteryLowCellVoltageOperation djiBatteryLowCellVoltageOperation);
    }

    abstract class Presenter extends BasePresenter<BatterySettingContract.Model, BatterySettingContract.View> {
        public abstract void getDJIBatteryState();
        public abstract void getDJIBatteryLowCellVoltageOperation();
    }
}
