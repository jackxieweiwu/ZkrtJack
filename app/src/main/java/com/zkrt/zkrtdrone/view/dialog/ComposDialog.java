package com.zkrt.zkrtdrone.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.BaseDialog;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;
import com.zkrt.zkrtdrone.view.widget.MySeekBar;

import dji.common.error.DJIError;
import dji.common.flightcontroller.DJICompassCalibrationStatus;
import dji.common.util.DJICommonCallbacks;


/**
 * Created by jack_xie on 17-2-26.
 */

public class ComposDialog extends BaseDialog {
    private Activity activity;
    private ImageView img_imu_caommon;
    private ImageView img_clean_commpos;
    private TextView txt_imu_common;
    private MySeekBar time_contorl_seekbar;
    private Button btn_imu_start;
    private int number = 0;

    public ComposDialog(Context context) {
        super(context);
    }

    public ComposDialog(Context context, int theme, int resLayout) {
        super(context, theme, resLayout);
        this.activity = (Activity) context;
    }

    @Override
    public int getDialogFindById() {
        return R.layout.imu_layout;
    }

    @Override
    protected void initView(View view) {
        img_imu_caommon = (ImageView) findViewById(R.id.img_imu_caommon);
        img_clean_commpos = (ImageView) findViewById(R.id.img_clean_commpos);
        img_clean_commpos.setVisibility(View.VISIBLE);
        txt_imu_common = (TextView) findViewById(R.id.txt_imu_common);
        time_contorl_seekbar = (MySeekBar) findViewById(R.id.time_contorl_seekbar);
        btn_imu_start = (Button) findViewById(R.id.btn_imu_start);
        btn_imu_start.setVisibility(View.GONE);

        img_clean_commpos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DJIModuleVerificationUtil.isCompassAvailable()){
                    DJISampleApplication.getAircraftInstance().getFlightController().getCompass().
                            stopCompassCalibration(new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if(djiError == null){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismiss();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        setCommposStatus();
    }

    public void setCommposStatus(){
        if(DJIModuleVerificationUtil.isCompassAvailable()){
            DJICompassCalibrationStatus djiCompassCalibrationStatus =DJISampleApplication.getAircraftInstance().getFlightController().
                    getCompass().getCalibrationStatus();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(djiCompassCalibrationStatus.value() == 0){
                        img_imu_caommon.setBackgroundResource(R.mipmap.fpv_compass_a3_horizontal);
                        txt_imu_common.setText("水平旋转360度");
                    }

                    if(djiCompassCalibrationStatus.value() == 1){
                        img_imu_caommon.setBackgroundResource(R.mipmap.fpv_compass_a3_vertical);
                        txt_imu_common.setText("垂直旋转360度");
                    }

                    if(djiCompassCalibrationStatus.value() == 2){
                        dismiss();
                    }

                    if(djiCompassCalibrationStatus.value() == 3){
                        txt_imu_common.setText("指南针校准失败");
                    }
                }
            });
        }
    }
}
