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
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;
import com.zkrt.zkrtdrone.view.widget.MySeekBar;

import java.util.HashSet;
import java.util.Iterator;

import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIIMUCalibrationOrientation;
import dji.common.flightcontroller.DJIIMUCalibrationStatus;
import dji.common.flightcontroller.DJIIMUMultiOrientationCalibrationStatus;
import dji.common.flightcontroller.DJIIMUState;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.flightcontroller.DJIFlightControllerDelegate;

/**
 * Created by jack_xie on 17-2-26.
 */

public class ImuDialog extends BaseDialog {
    private Activity activity;
    private ImageView img_imu_caommon;
    private ImageView img_clean_commpos;
    private TextView txt_imu_common;
    private MySeekBar time_contorl_seekbar;
    private Button btn_imu_start;
    private DJIIMUState DJIIMUState;
    private int number = 0;

    public ImuDialog(Context context) {
        super(context);
    }

    public ImuDialog(Context context, int theme, int resLayout) {
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
        txt_imu_common = (TextView) findViewById(R.id.txt_imu_common);
        img_clean_commpos = (ImageView) findViewById(R.id.img_clean_commpos);
        img_clean_commpos.setVisibility(View.VISIBLE);
        time_contorl_seekbar = (MySeekBar) findViewById(R.id.time_contorl_seekbar);
        time_contorl_seekbar.setMax(100);
        btn_imu_start = (Button) findViewById(R.id.btn_imu_start);

        img_clean_commpos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                });
            }
        });

        btn_imu_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
                    DJISampleApplication.getAircraftInstance().getFlightController().
                            startIMUCalibration(new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if(djiError == null){
                                int progrress =  DJIIMUState.getCalibrationProgress();
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        time_contorl_seekbar.setVisibility(View.VISIBLE);
                                        time_contorl_seekbar.setProgress(progrress);
                                    }
                                });
                                if(progrress == 100){
                                    DJISampleApplication.getAircraftInstance().getFlightController().setOnIMUStateChangedCallback(new DJIFlightControllerDelegate.FlightControllerIMUStateChangedCallback() {
                                        @Override
                                        public void onStateChanged(DJIIMUState djiimuState) {
                                            DJIIMUCalibrationStatus djiimuCalibrationStatus = djiimuState.getCalibrationStatus();
                                            if(djiimuCalibrationStatus.value() == 2){
                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dismiss();
                                                    }
                                                });
                                            }else if(djiimuCalibrationStatus.value() == 3){
                                                btn_imu_start.setText("重来");
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });

                }
            }
        });
    }

    public void setDJIIMUState(DJIIMUState DJIIMUState) {
        this.DJIIMUState = DJIIMUState;
    }
}
