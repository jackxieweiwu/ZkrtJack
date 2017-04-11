package com.zkrt.zkrtdrone.view.myfragment.mount;


import android.content.SharedPreferences;
import android.content.res.TypedArray;

import com.zkrt.zkrtdrone.bean.DeviceCallback;
import com.zkrt.zkrtdrone.bean.Mephitis;
import com.zkrt.zkrtdrone.bean.Module;
import com.zkrt.zkrtdrone.view.dialog.CustomDialog;
import com.zkrt.zkrtdrone.view.myfragment.camera.ModelCameraAndOne;

import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseModel;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BasePresenter;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseView;

/**
 * Created by jack_xie on 17-2-21.
 */

public interface MountContract {
    interface Model extends BaseModel {
        String[] getMoudleName();
        TypedArray getMoudleImg();
        CustomDialog getDialog();
        //ModelCameraAndOne getModelCameraAndOne(CustomDialog dialog);
        SharedPreferences getSharePreferences();
    }

    interface View extends BaseView {
        void showDialog(CustomDialog dialog);
        void showSharedPreferences(SharedPreferences sharedPreferences);
        void showMoudleNameImg(String[] moudleName, TypedArray moudleImg);
        void showDJIFlightControllerDelegate(byte[] bytes);
        void showDeviceCallbackMoudle(DeviceCallback deviceCallback, Module module, Mephitis mephitis);
        void showDeviceCallbackMoudle2(DeviceCallback deviceCallback);

    }

    abstract class Presenter extends BasePresenter<MountContract.Model, MountContract.View> {
        //public abstract void getShowMoudleNameImg();
        //public abstract void getshowSharedPreferences();
        //public abstract void getshowDialog();
        public abstract void getshowDJIFlightControllerDelegate();
    }
}
