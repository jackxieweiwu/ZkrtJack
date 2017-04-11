package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.dronesetting;

import android.widget.ArrayAdapter;

import com.amap.api.location.AMapLocation;
import com.zkrt.zkrtdrone.view.myfragment.mapmvp.MyMapFragment;

import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIFlightFailsafeOperation;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseModel;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BasePresenter;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseView;

/**
 * Created by jack_xie on 17-2-21.
 */

public interface DroneSettingContract {
    interface Model extends BaseModel {
        ArrayAdapter getAdapter();
    }

    interface View extends BaseView {
        void showAdapter(ArrayAdapter adapter);
        void showError(DJIError djiError);
        void showDJIFlightFailsafeOperation(DJIFlightFailsafeOperation djiFlightFailsafeOperation);
        void showGoHomeHight(Float aFloat);   //获取来自飞机的返航高度
        void showMaxFlightHeight(Float aFloat);  ////获取来自飞机的最大飞行高度限制。
        void showMaxFlightRadius(Float aFloat);  //获取来自飞机的最大飞行半径限制。
        void showMaxFlightRadiusLimitationEnable(boolean bool);  //获取最大飞行半径限制是否已启用。
    }

    abstract class Presenter extends BasePresenter<DroneSettingContract.Model, DroneSettingContract.View> {
        public abstract void getDJIFlightFailsafeOperation();
        public abstract void getGoHomeHight();
        public abstract void getMaxFlightHeight();
        public abstract void getMaxFlightRadius();
        public abstract void getMaxFlightRadiusLimitationEnable();
    }
}
