package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.batterysetting;


import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;

import java.math.BigDecimal;

import dji.common.battery.DJIBatteryAggregationState;
import dji.common.battery.DJIBatteryLowCellVoltageOperation;
import dji.common.battery.DJIBatteryState;
import dji.common.battery.DJIBatteryWarningInformation;
import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.battery.DJIBattery;

/**
 * Created by jack_xie on 17-2-21.
 */

public class BatterySettingPresenter extends BatterySettingContract.Presenter{
    @Override
    public void onAttached() {
        getDJIBatteryState();
        getDJIBatteryLowCellVoltageOperation();
    }

    @Override
    public void getDJIBatteryState() {
        if(DJIModuleVerificationUtil.isBattery()) {
            //获取电量
            DJISampleApplication.getProductInstance().getBattery().setBatteryStateUpdateCallback(new DJIBattery.DJIBatteryStateUpdateCallback() {
                @Override
                public void onResult(DJIBatteryState djiBatteryState) {
                    mView.showDJIBatteryState(djiBatteryState);
                    //将代理设置为接收电池聚合信息。
                    DJISampleApplication.getAircraftInstance().getBattery().setBatteryAggregationStateUpdatedCallback(new DJIBattery.DJIBatteryAggregationStateUpdatedCallback() {
                        @Override
                        public void onResult(DJIBatteryAggregationState djiBatteryAggregationState) {
                            mView.showDJIBatteryAggregationState(djiBatteryAggregationState);
                        }
                    });
                }
            });


        }
    }

    @Override
    public void getDJIBatteryLowCellVoltageOperation() {
        if(DJIModuleVerificationUtil.isBattery()) {

            //获取以mV为单位的1级单元电压阈值。
            DJISampleApplication.getAircraftInstance().getBattery().getLevel1CellVoltageThreshold(new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    mView.showLevel1CellVoltageThreshold(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });

            //获取以mV为单位的1级单元电压阈值。
            DJISampleApplication.getAircraftInstance().getBattery().getLevel2CellVoltageThreshold(new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    mView.showLevel2CellVoltageThreshold(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });

            //
            DJISampleApplication.getAircraftInstance().getBattery().getLevel1CellVoltageOperation(new DJICommonCallbacks.DJICompletionCallbackWith<DJIBatteryLowCellVoltageOperation>() {
                @Override
                public void onSuccess(DJIBatteryLowCellVoltageOperation djiBatteryLowCellVoltageOperation) {
                    mView.showLevel1CellVoltageperation(djiBatteryLowCellVoltageOperation);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            DJISampleApplication.getAircraftInstance().getBattery().getLevel2CellVoltageOperation(new DJICommonCallbacks.DJICompletionCallbackWith<DJIBatteryLowCellVoltageOperation>() {
                @Override
                public void onSuccess(DJIBatteryLowCellVoltageOperation djiBatteryLowCellVoltageOperation) {
                    mView.showLevel2CellVoltageperation(djiBatteryLowCellVoltageOperation);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });
        }
    }
}
