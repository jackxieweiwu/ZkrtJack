package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;
import com.zkrt.zkrtdrone.view.dialog.TimeDialog;

import butterknife.BindView;
import butterknife.OnClick;
import dji.common.error.DJIError;
import dji.common.remotecontroller.DJIRCControlChannel;
import dji.common.remotecontroller.DJIRCControlMode;
import dji.common.remotecontroller.DJIRCControlStyle;
import dji.common.util.DJICommonCallbacks;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseMvpFragment;
/**
 * Created by jack_xie on 16-12-29.
 */

public class RemoteSettingFragment extends BaseMvpFragment {
    @BindView(R.id.txt_remote_version) TextView txt_remote_version;
    @BindView(R.id.txt_seekbar_remote) TextView txt_seekbar_remote;
    @BindView(R.id.edit_remote_name) EditText edit_remote_name;
    @BindView(R.id.edit_remote_pwd) EditText edit_remote_pwd;
    @BindView(R.id.seekar_remote) SeekBar seekar_remote;
    @BindView(R.id.spinner_rocker_mode) Spinner spinner_rocker_mode;
    @BindView(R.id.img_rocker_mode) ImageView img_rocker_mode;
    private TimeDialog timeDialog;
    @Override
    protected int getFragmentViewId() {
        return R.layout.setting_remote;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        seekar_remote.setMax(10);
        timeDialog = new TimeDialog(mActivity,R.style.set_dialogs);
    }

    @Override
    protected void getViewFindByid(View view) {
        super.getViewFindByid(view);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(),R.array.remote_setting,R.layout.spinner_list_item);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner_rocker_mode.setAdapter(adapter);

        //获取遥控器名称
        if(DJIModuleVerificationUtil.isRemoteControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getRemoteController().getVersion(new DJICommonCallbacks.DJICompletionCallbackWith<String>() {
                @Override
                public void onSuccess(String s) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt_remote_version.setText(s);
                        }
                    });
                }

                @Override
                public void onFailure(DJIError djiError) {
                }
            });

            DJISampleApplication.getAircraftInstance().getRemoteController().getRCName(new DJICommonCallbacks.DJICompletionCallbackWith<String>() {
                @Override
                public void onSuccess(String s) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            edit_remote_name.setText(s);
                        }
                    });
                }

                @Override
                public void onFailure(DJIError djiError) {
                }
            });

            //获取遥控器密码
            DJISampleApplication.getAircraftInstance().getRemoteController().getRCPassword(new DJICommonCallbacks.DJICompletionCallbackWith<String>() {
                @Override
                public void onSuccess(String s) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            edit_remote_pwd.setText(s);
                        }
                    });
                }

                @Override
                public void onFailure(DJIError djiError) {
                }
            });

            //获取遥控器左上方拨轮的速度
            DJISampleApplication.getAircraftInstance().getRemoteController().getRCWheelControlGimbalSpeed(new DJICommonCallbacks.DJICompletionCallbackWith<Short>() {
                @Override
                public void onSuccess(Short aShort) {
                    final int muber  = aShort.intValue()/10;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seekar_remote.setProgress(muber);
                            txt_seekbar_remote.setText(muber+"");
                        }
                    });
                }

                @Override
                public void onFailure(DJIError djiError) {
                    Utils.setResultToToast(mActivity,djiError==null?"获取成功":djiError.getDescription());
                }
            });

            //获取遥控器的操作习惯
            DJISampleApplication.getAircraftInstance().getRemoteController().getRCControlMode(
                    new DJICommonCallbacks.DJICompletionCallbackWith<DJIRCControlMode>() {
                @Override
                public void onSuccess(DJIRCControlMode djircControlMode) {
                    int number =-1;
                    if(djircControlMode.controlStyle.name().equals("American")) number = 0;
                    if(djircControlMode.controlStyle.name().equals("Chinese")) number = 1;
                    if(djircControlMode.controlStyle.name().equals("Japanese")) number = 2;
                    int finalNumber = number;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            spinner_rocker_mode.setSelection(finalNumber);
                            if(finalNumber == 0) img_rocker_mode.setBackgroundResource(R.mipmap.setting_ui_rc_slave);
                            if(finalNumber == 1) img_rocker_mode.setBackgroundResource(R.mipmap.setting_ui_rc_china);
                            if(finalNumber == 2) img_rocker_mode.setBackgroundResource(R.mipmap.setting_ui_rc_japan);
                        }
                    });
                }

                @Override
                public void onFailure(DJIError djiError) {
                    Utils.setResultToToast(mActivity,djiError==null?"获取成功":djiError.getDescription());
                }
            });

            //设置遥控器操作习惯
            spinner_rocker_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    DJIRCControlStyle american = DJIRCControlStyle.American;
                    int number = -1;
                    if(position == 0){
                        american = DJIRCControlStyle.American;
                        number = 0;
                    }
                    if(position == 1){
                        american = DJIRCControlStyle.Chinese;
                        number = 1;
                    }

                    if(position == 2){
                        american = DJIRCControlStyle.Japanese;
                        number = 2;
                    }

                    int finalNumber = number;
                    DJISampleApplication.getAircraftInstance().getRemoteController().setRCControlMode(new DJIRCControlMode(american,new DJIRCControlChannel[number]) , new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            Utils.setResultToToast(getContext(),djiError == null?"操作成功":djiError.getDescription());
                            if(djiError == null){
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        spinner_rocker_mode.setSelection(finalNumber);
                                        if(finalNumber == 0) img_rocker_mode.setBackgroundResource(R.mipmap.setting_ui_rc_slave);
                                        if(finalNumber == 1) img_rocker_mode.setBackgroundResource(R.mipmap.setting_ui_rc_china);
                                        if(finalNumber == 2) img_rocker_mode.setBackgroundResource(R.mipmap.setting_ui_rc_japan);
                                    }
                                });
                            }
                        }
                    });
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        //设置遥控器左上方拨轮速度
        seekar_remote.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txt_seekbar_remote.setText(progress+"");
            }

            @Override  //表示进度条刚开始拖动，开始拖动时候触发的操作
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override  // 停止拖动时候
            public void onStopTrackingTouch(SeekBar seekBar) {
                String number = seekBar.getProgress()*10+"";
                if(DJIModuleVerificationUtil.isRemoteControllerAvailable()){
                    DJISampleApplication.getAircraftInstance().getRemoteController().setRCWheelControlGimbalSpeed(Short.parseShort(number), new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            Utils.setResultToToast(getContext(),djiError == null?"操作成功":djiError.getDescription());
                        }
                    });
                }
            }
        });
    }

    @OnClick(R.id.txt_remote_frequency)
    public void remoteFreQuency(View v){
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getRemoteController().enterRCPairingMode(new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if(djiError == null){
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timeDialog.showDialog();
                            }
                        });
                    }
                }
            });
        }
    }
}
