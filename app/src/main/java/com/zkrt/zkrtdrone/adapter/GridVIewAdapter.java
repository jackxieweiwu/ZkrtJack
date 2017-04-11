package com.zkrt.zkrtdrone.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.bean.exelBean;

import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseAdapter;

/**
 * Created by root on 17-3-30.
 */

public class GridVIewAdapter extends BaseAdapter<exelBean> {
    public GridVIewAdapter(Context context) {
        super(context);
    }

    @Override
    public int getContentView() {
        return R.layout.spurces_item;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onInitView(View view, int position) {
        exelBean log = getItem(position);
        TextView txt_lat = get(view, R.id.txt_lat);
        TextView txt_lng = get(view, R.id.txt_lng);
        TextView txt_co = get(view, R.id.txt_co);
        TextView txt_h2s = get(view, R.id.txt_h2s);
        TextView txt_nh3 = get(view, R.id.txt_nh3);
        TextView txt_co2 = get(view, R.id.txt_co2);
        TextView txt_time = get(view, R.id.txt_time);
        txt_lat.setText(log.getLat()+"");
        txt_lng.setText(log.getLog()+"");
        txt_co.setText(log.getGasValueOne()+"");
        txt_h2s.setText(log.getGasValueTwo()+"");
        txt_nh3.setText(log.getGasValueThree()+"");
        txt_co2.setText(log.getGasValueFour()+"");
        txt_time.setText(log.getTime()+"");
    }
}
