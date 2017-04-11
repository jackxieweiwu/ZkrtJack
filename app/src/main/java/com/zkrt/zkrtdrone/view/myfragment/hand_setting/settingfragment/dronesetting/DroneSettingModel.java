package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.dronesetting;


import android.widget.ArrayAdapter;

import com.amap.api.location.AMapLocation;
import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.view.myfragment.mapmvp.MyMapFragment;

import zkrtdrone.zkrt.com.maplib.App;

import static zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseApplication.activity;

/**
 * Created by root on 17-2-21.
 */

public class DroneSettingModel implements DroneSettingContract.Model {

    @Override
    public ArrayAdapter getAdapter() {

        ArrayAdapter adapter = ArrayAdapter.createFromResource(activity, R.array.meses,R.layout.spinner_list_item);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        return adapter;
    }

}
