package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.holdersetting;

import android.widget.ArrayAdapter;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.view.myfragment.mount.MountContract;

import zkrtdrone.zkrt.com.maplib.App;

/**
 * Created by root on 17-2-21.
 */

public class HolderSettingModel implements HolderSettingContract.Model {

    @Override
    public ArrayAdapter getAdapter() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(App.app, R.array.holder_setting,R.layout.spinner_list_item);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        return adapter;
    }
}
