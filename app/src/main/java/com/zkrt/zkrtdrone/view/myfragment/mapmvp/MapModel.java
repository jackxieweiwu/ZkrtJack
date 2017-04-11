package com.zkrt.zkrtdrone.view.myfragment.mapmvp;

import android.view.View;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;

/**
 * Created by jack_xie on 16/5/2.
 */
//@Instance
public class MapModel implements MapContract.Model {

    @Override
    public View getDroneMap() {
        return View.inflate(DJISampleApplication.activity, R.layout.view_drone, null);
    }
}
