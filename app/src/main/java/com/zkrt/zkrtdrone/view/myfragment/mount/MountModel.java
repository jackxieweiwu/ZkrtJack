package com.zkrt.zkrtdrone.view.myfragment.mount;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.TypedArray;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.bean.Module;
import com.zkrt.zkrtdrone.until.HexToBinary;
import com.zkrt.zkrtdrone.view.dialog.CustomDialog;
import com.zkrt.zkrtdrone.view.myfragment.camera.ModelCameraAndOne;
import zkrtdrone.zkrt.com.maplib.App;

import static zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseApplication.activity;

/**
 * Created by jack_xie on 17-2-21.
 */

public class MountModel implements MountContract.Model {
    @Override
    public String[] getMoudleName() {
        return  activity.getResources().getStringArray(R.array.moudle_item);
    }

    @Override
    public TypedArray getMoudleImg() {
        return activity.getResources().obtainTypedArray(R.array.moudle_img);
    }

    @Override
    public CustomDialog getDialog() {
        return new CustomDialog(activity, R.style.customDialog, R.layout.moudle,
                HexToBinary.setGetMoudle(getMoudleName(),getMoudleImg(),new Module(),activity));
    }

    /*@Override
    public ModelCameraAndOne getModelCameraAndOne(CustomDialog dialog) {
        ModelCameraAndOne modelCameraAndOne = (ModelCameraAndOne) DJISampleApplication.fragmentManager.
                findFragmentByTag("ModelCameraAndOne");
        modelCameraAndOne.changeDialog(dialog);
        return modelCameraAndOne;
    }*/

    @Override
    public SharedPreferences getSharePreferences() {
        return App.app.getSharedPreferences("gases", Activity.MODE_PRIVATE);
    }
}
