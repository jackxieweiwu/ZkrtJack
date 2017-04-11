package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.holdersetting;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;

import butterknife.BindView;
import butterknife.OnClick;
import dji.common.error.DJIError;
import dji.common.gimbal.DJIGimbalAdvancedSettingsProfile;
import dji.common.gimbal.DJIGimbalAxis;
import dji.common.util.DJICommonCallbacks;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseMvpFragment;

/**
 * Created by jack_xie on 16-12-29.
 */

public class HolderSettingFragment extends BaseMvpFragment<HolderSettingPresenter,HolderSettingModel> implements HolderSettingContract.View {
    @BindView(R.id.seekbar_bolder_one)SeekBar seekbar_bolder_one;
    @BindView(R.id.txt_seekbar_one)TextView txt_seekbar_one;
    @BindView(R.id.seekbar_bolder_two)SeekBar seekbar_bolder_two;
    @BindView(R.id.txt_seekbar_two)TextView txt_seekbar_two;
    @BindView(R.id.seekbar_bolder_three)SeekBar seekbar_bolder_three;
    @BindView(R.id.txt_seekbar_three)TextView txt_seekbar_three;
    @BindView(R.id.spinner_holder_setting)Spinner spinner_holder_setting;
    @BindView(R.id.txt_holder_resetGimbal)TextView txt_holder_resetGimbal;

    @Override
    protected int getFragmentViewId() {
        return R.layout.setting_holder;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        seekbar_bolder_one.setMax(100);
        seekbar_bolder_two.setMax(100);
        seekbar_bolder_three.setMax(30);
    }

    @Override
    protected void getViewFindByid(View view) {
        super.getViewFindByid(view);
        setSeekbar(seekbar_bolder_one);
        setSeekbar(seekbar_bolder_two);
        setSeekbar(seekbar_bolder_three);

        if(DJIModuleVerificationUtil.isGimbalModuleAvailable()){
            //获得云台的设置
            DJISampleApplication.getProductInstance().getGimbal().getAdvancedSettingsProfile(new DJICommonCallbacks.DJICompletionCallbackWith<DJIGimbalAdvancedSettingsProfile>() {
                @Override
                public void onSuccess(DJIGimbalAdvancedSettingsProfile djiGimbalAdvancedSettingsProfile) {
                    spinner_holder_setting.setSelection(djiGimbalAdvancedSettingsProfile.value());
                }

                @Override
                public void onFailure(DJIError djiError) {
                    Utils.setResultToToast(mActivity,djiError==null?"获取成功":djiError.getDescription());
                }
            });

            //得到云台的基本参数getControllerSmoothingOnAxis
            DJISampleApplication.getProductInstance().getGimbal().getControllerSmoothingOnAxis(DJIGimbalAxis.Roll,
                    new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    seekbar_bolder_three.setProgress(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });
            DJISampleApplication.getProductInstance().getGimbal().getControllerDeadbandOnAxis(DJIGimbalAxis.Roll,
                    new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    seekbar_bolder_one.setProgress(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            DJISampleApplication.getProductInstance().getGimbal().getControllerSpeedOnAxis(DJIGimbalAxis.Yaw,
                    new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    seekbar_bolder_two.setProgress(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });
        }

        //设置云台的配置文件
        spinner_holder_setting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(DJIModuleVerificationUtil.isGimbalModuleAvailable()){
                    DJIGimbalAdvancedSettingsProfile djiGimbalAdvancedSettingsProfile = null;
                    if(position==0){
                        djiGimbalAdvancedSettingsProfile = DJIGimbalAdvancedSettingsProfile.Custom1;
                    }else if(position ==1){
                        djiGimbalAdvancedSettingsProfile = DJIGimbalAdvancedSettingsProfile.Custom2;
                    }else if(position ==1){
                        djiGimbalAdvancedSettingsProfile = DJIGimbalAdvancedSettingsProfile.Fast;
                    }else if(position ==1){
                        djiGimbalAdvancedSettingsProfile = DJIGimbalAdvancedSettingsProfile.Medium;
                    }else if(position ==1){
                        djiGimbalAdvancedSettingsProfile = DJIGimbalAdvancedSettingsProfile.Slow;
                    }{
                        djiGimbalAdvancedSettingsProfile = DJIGimbalAdvancedSettingsProfile.Unknown;
                    }
                    DJISampleApplication.getProductInstance().getGimbal().setAdvancedSettingsProfile(djiGimbalAdvancedSettingsProfile, new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            //Utils.setResultToToast(mActivity,djiError==null?"设置成功":djiError.getDescription());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //云台校准
    @OnClick(R.id.txt_holder_correct)
    public void holderCorrect(View v){
        if(DJIModuleVerificationUtil.isGimbalModuleAvailable()){
            DJISampleApplication.getProductInstance().getGimbal().startGimbalAutoCalibration(new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    Utils.setResultToToast(mActivity,djiError==null?"成功":djiError.getDescription());
                }
            });
        }
    }

    //恢复出厂设置
    @OnClick(R.id.txt_holder_reset)
    public void holderReset(View v){
        if(DJIModuleVerificationUtil.isGimbalModuleAvailable()){
            DJISampleApplication.getProductInstance().getGimbal().loadFactorySettings(new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    Utils.setResultToToast(mActivity,djiError==null?"成功":djiError.getDescription());
                }
            });
        }
    }

    //
    @OnClick(R.id.txt_holder_resetGimbal)
    public void resetGimbal(View v){
        if(DJIModuleVerificationUtil.isGimbalModuleAvailable()){
            DJISampleApplication.getProductInstance().getGimbal().resetGimbal(new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    Utils.setResultToToast(mActivity,djiError==null?"成功":djiError.getDescription());
                }
            });
        }
    }


    public void setSeekbar(SeekBar s){
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(s == seekbar_bolder_one)txt_seekbar_one.setText(progress+"");
                if(s == seekbar_bolder_two)txt_seekbar_two.setText(progress+"");
                if(s == seekbar_bolder_three)txt_seekbar_three.setText(progress+"");
            }

            @Override  //表示进度条刚开始拖动，开始拖动时候触发的操作
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override  // 停止拖动时候
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(DJIModuleVerificationUtil.isGimbalModuleAvailable()){
                    DJIGimbalAxis djigimbal;
                    if(s == seekbar_bolder_one){
                        djigimbal = DJIGimbalAxis.Yaw;
                    }else if(s == seekbar_bolder_two){
                        djigimbal = DJIGimbalAxis.Yaw;
                    }else if(s == seekbar_bolder_three){
                        djigimbal = DJIGimbalAxis.Yaw;
                    }
                    DJISampleApplication.getProductInstance().getGimbal().setControllerDeadbandOnAxis(DJIGimbalAxis.Yaw,
                            seekBar.getProgress(),
                            new DJICommonCallbacks.DJICompletionCallback() {
                                @Override
                                public void onResult(DJIError djiError) {
                                    Utils.setResultToToast(getContext(),djiError == null?"设置成功":djiError.getDescription());
                                }
                            });
                }
            }
        });
    }

    @Override
    public void showAdapter(ArrayAdapter adapter) {
        if(spinner_holder_setting!=null) spinner_holder_setting.setAdapter(adapter);
    }
}
