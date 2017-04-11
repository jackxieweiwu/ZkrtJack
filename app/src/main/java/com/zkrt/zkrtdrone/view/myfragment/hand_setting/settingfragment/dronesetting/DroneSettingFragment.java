package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.dronesetting;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.fynn.switcher.Switch;
import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.bean.DeviceCallBackTwo;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;
import com.zkrt.zkrtdrone.until.HexToBinary;
import com.zkrt.zkrtdrone.view.myfragment.camera.ModelCameraAndOne;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIFlightFailsafeOperation;
import dji.common.flightcontroller.DJILocationCoordinate2D;
import dji.common.util.DJICommonCallbacks;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseMvpFragment;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.rxbean.CommonRxTask;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.rxbean.IOTask;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.rxutil.RxjavaUtil;

import static android.R.attr.inputType;
import static zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseApplication.activity;

/**
 * Created by jack_xie on 16-12-29.
 */
//<DroneSettingPresenter,DroneSettingModel>  DroneSettingContract.View,
public class DroneSettingFragment extends BaseMvpFragment
        implements Switch.OnCheckedChangeListener {
    @BindView(R.id.edit_setting_drone_gohome_hight) EditText edit_setting_drone_gohome_hight;
    @BindView(R.id.fly_ios) Switch fly_ios;
    @BindView(R.id.distance_ios_fly) Switch distance_ios_fly;
    @BindView(R.id.edit_setting_fly_gohome_hight) EditText edit_setting_fly_gohome_hight;
    @BindView(R.id.edit_setting_fly_gohome_distance) EditText edit_setting_fly_gohome_distance;
    @BindView(R.id.spinner_outof) Spinner spinner_outof;
    @BindView(R.id.rela_dis_number) RelativeLayout rela_dis_number;
    @BindView(R.id.txt_speedx) TextView txt_speedx;
    @BindView(R.id.speed_skeebar) SeekBar speed_skeebar;
    @BindView(R.id.switch_landing) Switch switch_landing;

    //
    @BindView(R.id.rela_obstacle) RelativeLayout rela_obstacle;
    @BindView(R.id.edit_setting_obstacle_distance) EditText edit_setting_obstacle_distance;
    @BindView(R.id.rela_obstacle_speed) RelativeLayout rela_obstacle_speed;
    @BindView(R.id.edit_setting_obstacle_speed) EditText edit_setting_obstacle_speed;


    @BindView(R.id.switch_obstacle) Switch switch_obstacle;  //是否避障
    @BindView(R.id.switch_tripod) Switch switch_tripod;  //脚架自动收放
    @BindView(R.id.switch_obstacle_setting_down) Switch switch_obstacle_setting_down;
    @BindView(R.id.rela_obstacle_calibration) RelativeLayout rela_obstacle_calibration;
    @BindView(R.id.rela_obstacle_correct) RelativeLayout rela_obstacle_correct;
    private DeviceCallBackTwo deviceCallBackTwo;
    private ModelCameraAndOne modelCameraAndOnes;
    private  int numbea = 0;
    @Override
    protected int getFragmentViewId() {
        return R.layout.setting_drone;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(activity, R.array.meses,R.layout.spinner_list_item);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        if(spinner_outof != null)spinner_outof.setAdapter(adapter);

        switch_obstacle.setOnCheckedChangeListener(this);
        fly_ios.setOnCheckedChangeListener(this);
        switch_tripod.setOnCheckedChangeListener(this);
        switch_obstacle_setting_down.setOnCheckedChangeListener(this);
        distance_ios_fly.setOnCheckedChangeListener(this);
        switch_landing.setOnCheckedChangeListener(this);
/*        edit_setting_drone_gohome_hight.setOnFocusChangeListener(this);
        edit_setting_fly_gohome_hight.setOnFocusChangeListener(this);
        edit_setting_fly_gohome_distance.setOnFocusChangeListener(this);*/

        if(DJIModuleVerificationUtil.isCompassAvailable()){
            float speedNumber = DJISampleApplication.getAircraftInstance().getFlightController().getCurrentState().getVelocityX();
            txt_speedx.setText((int)speedNumber+"");
            speed_skeebar.setProgress((int)speedNumber);
        }
    }

    //stop  校准 是否自动 脚架控制
    @OnClick(R.id.btn_tripod_stop)
    public void stopBtnTripod(View v){
        modelCameraAndOnes.setTripodNoOff(3,switch_tripod.isChecked(),"0");
    }

    //stop
    @OnClick(R.id.btn_tripod_up)
    public void upBtnTripod(View v){
        modelCameraAndOnes.setTripodNoOff(2,switch_tripod.isChecked(),"0");
    }

    //stop
    @OnClick(R.id.btn_tripod_dowm)
    public void downBtnTripod(View v){
        modelCameraAndOnes.setTripodNoOff(1,switch_tripod.isChecked(),"0");
    }

    @Override
    public void onCheckedChanged(Switch s, boolean isChecked) {
        if(!s.isPressed())return;
        switch (s.getId()){
            case R.id.distance_ios_fly:
                limitFar(isChecked);
                break;
            case R.id.fly_ios: //新手模式
                if(isChecked){
                    //限制高度
                    edit_setting_fly_gohome_hight.setText(30+"");
                    serDroneSettingHight(Float.parseFloat(edit_setting_fly_gohome_hight.getText().toString()));
                    //限制半径
                    edit_setting_fly_gohome_distance.setText(30+"");
                    setDroneSettingradius(Float.parseFloat(edit_setting_fly_gohome_distance.getText().toString()));
                    distance_ios_fly.setChecked(isChecked);
                    limitFar(isChecked);
                }else{
                    distance_ios_fly.setChecked(isChecked);
                    limitFar(isChecked);
                }
                break;

            case R.id.switch_landing:
                /*if(DJIModuleVerificationUtil.isCompassAvailable()){
                    DJISampleApplication.getAircraftInstance().getFlightController().getCurrentState().setNeedConfirmLanding(isChecked);
                }*/
                DJISampleApplication.bool = isChecked;
                break;

            case R.id.switch_obstacle:
                if(modelCameraAndOnes !=null &&
                        edit_setting_obstacle_distance !=null && rela_obstacle != null
                        && edit_setting_obstacle_speed !=null){
                    String cm = edit_setting_obstacle_distance.getText().toString()+"";
                    String cm_speed = edit_setting_obstacle_speed.getText().toString()+"";
                    modelCameraAndOnes.setObstacleNoOff(isChecked,cm,cm_speed);
                    if(isChecked){
                        //rela_obstacle.setVisibility(View.VISIBLE);
                        rela_obstacle_speed.setVisibility(View.VISIBLE);
                    }else {
                        //rela_obstacle.setVisibility(View.GONE);
                        rela_obstacle_speed.setVisibility(View.GONE);
                    }
                    //getDeviteData();
                }
                break;

            case R.id.switch_tripod:  //脚架  校准 是否自动 脚架控制
                if(modelCameraAndOnes !=null){
                    modelCameraAndOnes.setTripodNoOff(0,isChecked,"0");
                    if(rela_obstacle_calibration !=null) rela_obstacle_calibration.setVisibility(isChecked?View.GONE:View.VISIBLE);
                    if(rela_obstacle_correct !=null) rela_obstacle_correct.setVisibility(isChecked?View.GONE:View.VISIBLE);
                }
                break;

            case R.id.switch_obstacle_setting_down:
                int number = -1;
                if(isChecked) number = 2; else number = 1;
                modelCameraAndOnes.setTripodNoOff(0,switch_tripod.isChecked(),number+"");
                break;
        }
    }

    @Override
    protected void getViewFindByid(View view) {
        super.getViewFindByid(view);
        //获取设置数据
        if (DJIModuleVerificationUtil.isFlightControllerAvailable()) {
            DJISampleApplication.getAircraftInstance().getFlightController().getFlightFailsafeOperation(new DJICommonCallbacks.DJICompletionCallbackWith<DJIFlightFailsafeOperation>() {
                @Override
                public void onSuccess(DJIFlightFailsafeOperation djiFlightFailsafeOperation) {
                    int number = 0;
                    if (djiFlightFailsafeOperation.name().equals("GoHome")) {
                        number = 0;
                    }
                    if (djiFlightFailsafeOperation.name().equals("Hover ")) {
                        number = 1;
                    }

                    if (djiFlightFailsafeOperation.name().equals("Landing ")) {
                        number = 2;
                    }
                    int finalNumber = number;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            spinner_outof.setSelection(finalNumber);
                        }
                    });
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showError(djiError);
                }
            });
        }

        //获取来自飞机的返航高度
        if (DJIModuleVerificationUtil.isFlightControllerAvailable()) {
            DJISampleApplication.getAircraftInstance().getFlightController().
                    getGoHomeAltitude(new DJICommonCallbacks.DJICompletionCallbackWith<Float>() {
                @Override
                public void onSuccess(Float aFloat) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (edit_setting_drone_gohome_hight == null) return;
                            edit_setting_drone_gohome_hight.setText(aFloat + "");
                        }
                    });
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });
        }

        //获取来自飞机的最大飞行高度限制。
        if (DJIModuleVerificationUtil.isFlightControllerAvailable()) {
            DJISampleApplication.getAircraftInstance().getFlightController().getFlightLimitation().getMaxFlightHeight(
                    new DJICommonCallbacks.DJICompletionCallbackWith<Float>() {
                        @Override
                        public void onSuccess(Float aFloat) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    edit_setting_fly_gohome_hight.setText(aFloat + "");
                                }
                            });
                        }

                        @Override
                        public void onFailure(DJIError djiError) {
                            showError(djiError);
                        }
                    });
        }

        //获取来自飞机的最大飞行半径限制。
        if (DJIModuleVerificationUtil.isFlightControllerAvailable()) {
            DJISampleApplication.getAircraftInstance().getFlightController().getFlightLimitation().getMaxFlightRadius(
                    new DJICommonCallbacks.DJICompletionCallbackWith<Float>() {
                        @Override
                        public void onSuccess(Float aFloat) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    edit_setting_fly_gohome_distance.setText(aFloat + "");
                                }
                            });
                        }

                        @Override
                        public void onFailure(DJIError djiError) {
                            showError(djiError);
                        }
                    });
        }

        //获取最大飞行半径限制是否已启用。
        if (DJIModuleVerificationUtil.isFlightControllerAvailable()) {
            DJISampleApplication.getAircraftInstance().getFlightController().getFlightLimitation()
                    .getMaxFlightRadiusLimitationEnable(new DJICommonCallbacks.DJICompletionCallbackWith<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    distance_ios_fly.setChecked(aBoolean);
                                    if (aBoolean) rela_dis_number.setVisibility(View.VISIBLE);
                                    else rela_dis_number.setVisibility(View.GONE);
                                }
                            });
                        }

                        @Override
                        public void onFailure(DJIError djiError) {
                            showError(djiError);
                        }
                    });
        }


        getDeviteData();

        //设置飞机的飞行速度
        speed_skeebar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float speedNum = seekBar.getProgress();
                if(DJIModuleVerificationUtil.isCompassAvailable()){
                    DJISampleApplication.getAircraftInstance().getFlightController().getCurrentState().setVelocityX(speedNum);
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txt_speedx.setText((int)speedNum+"");
                    }
                });
            }
        });

        //设置当遥控器与飞机之间的连接丢失的故障保护。
        spinner_outof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    setFlightFailsafeOperation(DJIFlightFailsafeOperation.GoHome);
                if (position == 1)
                    setFlightFailsafeOperation(DJIFlightFailsafeOperation.Hover);
                if (position == 2)
                    setFlightFailsafeOperation(DJIFlightFailsafeOperation.Landing);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //设置飞机的最大飞行高度限制。
        edit_setting_fly_gohome_hight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edit_setting_fly_gohome_hight.requestFocus();
                handler.removeMessages(0);
                if (!TextUtils.isEmpty(s.toString())){
                    handler.sendEmptyMessageDelayed(0, 500);
                }
            }
        });

        //设置为该机最大飞行半径限制。
        edit_setting_fly_gohome_distance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edit_setting_fly_gohome_distance.requestFocus();
                handler.removeMessages(1);
                if (!TextUtils.isEmpty(s.toString())){
                    handler.sendEmptyMessageDelayed(1, 500);
                }
            }
        });

        //设置返航高度
        edit_setting_drone_gohome_hight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edit_setting_drone_gohome_hight.requestFocus();
                handler.removeMessages(2);
                if (!TextUtils.isEmpty(s.toString())){
                    handler.sendEmptyMessageDelayed(2, 500);
                }
            }
        });

        edit_setting_obstacle_distance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edit_setting_obstacle_distance.requestFocus();
                handler.removeMessages(3);
                if (!TextUtils.isEmpty(s.toString())){
                    handler.sendEmptyMessageDelayed(3, 500);
                }
            }
        });
        edit_setting_obstacle_speed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edit_setting_obstacle_speed.requestFocus();
                handler.removeMessages(4);
                if (!TextUtils.isEmpty(s.toString())){
                    handler.sendEmptyMessageDelayed(4, 500);
                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0:
                    String gohomeh = edit_setting_fly_gohome_hight.getText().toString();
                    if(!gohomeh.isEmpty()){
                        float gohomehight = Float.parseFloat(gohomeh);
                        if(gohomehight<20){
                            gohomehight = 20f;
                        } else if(gohomehight>500f){
                            gohomehight = 500f;
                        }else{
                            serDroneSettingHight(gohomehight);
                        }
                    }
                    break;
                case 1:
                    String gohomeDIstance =edit_setting_fly_gohome_distance.getText().toString();
                    if(!gohomeDIstance.isEmpty()){
                        float gohomeDistance =  Float.parseFloat(gohomeDIstance);
                        if(gohomeDistance<15){
                            gohomeDistance = 15f;
                        }else if(gohomeDistance>500f){
                            gohomeDistance = 500f;
                        }else{
                            setDroneSettingradius(gohomeDistance);
                        }
                    }
                    break;
                case 2:
                    String dronegphomehight = edit_setting_drone_gohome_hight.getText().toString();
                    if(!dronegphomehight.isEmpty()){
                        float drone_gohome_hight = Float.parseFloat(dronegphomehight);
                        if(drone_gohome_hight>500){
                            drone_gohome_hight = 500f;
                        } else if(drone_gohome_hight<20){
                            drone_gohome_hight = 20f;
                        }else{
                            if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
                                DJISampleApplication.getAircraftInstance().getFlightController().
                                        setGoHomeAltitude(drone_gohome_hight,
                                                new DJICommonCallbacks.DJICompletionCallback() {
                                                    @Override
                                                    public void onResult(DJIError djiError) {
                                                        showError(djiError);
                                                    }
                                                });
                            }
                        }
                    }
                    break;
                case 3:
                    if(modelCameraAndOnes !=null &&
                            edit_setting_obstacle_distance !=null && rela_obstacle != null
                            && edit_setting_obstacle_speed !=null){
                        String cm = edit_setting_obstacle_distance.getText().toString()+"";
                        String cm_speed = edit_setting_obstacle_speed.getText().toString()+"";
                        modelCameraAndOnes.setObstacleNoOff(switch_obstacle.isChecked(),cm,cm_speed);
                    }
                    break;
                case 4:
                    if(modelCameraAndOnes !=null &&
                            edit_setting_obstacle_distance !=null && rela_obstacle != null
                            && edit_setting_obstacle_speed !=null){
                        String cm = edit_setting_obstacle_distance.getText().toString()+"";
                        String cm_speed = edit_setting_obstacle_speed.getText().toString()+"";
                        modelCameraAndOnes.setObstacleNoOff(switch_obstacle.isChecked(),cm,cm_speed);
                    }
                    break;
            }
        }
    };

    private void getDeviteData(){
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(DJIModuleVerificationUtil.isFlightControllerAvailable()) {
                    deviceCallBackTwo = DJISampleApplication.deviceCallBackTwo;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(deviceCallBackTwo != null && numbea == 0){
                                numbea = 1;
                                int obstacleNumber =  HexToBinary.HexTo10Integer(deviceCallBackTwo.getObstacleEnabled());
                                int obstacleNumber2 =  HexToBinary.HexTo10Integer(deviceCallBackTwo.getTripodDistance());
                                int obstacleNumber3 =  HexToBinary.HexTo10Integer(deviceCallBackTwo.getTripodStatus());

                                edit_setting_obstacle_distance.setText(HexToBinary.HexTo10Integer(deviceCallBackTwo.getObstacleDistance())+"");
                                edit_setting_obstacle_speed.setText(Float.parseFloat(HexToBinary.HexTo10Integer(deviceCallBackTwo.getObstacleSpeed())+"")/10f+"");
                                if(switch_obstacle !=null)switch_obstacle.setChecked(obstacleNumber == 0?false:true);
                                if(rela_obstacle != null && rela_obstacle_speed != null) {
                                    //rela_obstacle.setVisibility(obstacleNumber == 0?View.GONE:View.VISIBLE);
                                    rela_obstacle_speed.setVisibility(obstacleNumber == 0?View.GONE:View.VISIBLE);
                                }

                                //
                                if(switch_tripod != null)switch_tripod.setChecked(obstacleNumber2 == 0?false:true);
                                if(rela_obstacle_correct != null)rela_obstacle_correct.setVisibility(obstacleNumber2 == 0?View.VISIBLE:View.GONE);
                                if(rela_obstacle_calibration !=null) rela_obstacle_calibration.setVisibility(obstacleNumber2 == 0?View.VISIBLE:View.GONE);
                                if(switch_obstacle_setting_down != null) switch_obstacle_setting_down.setChecked(obstacleNumber3 == 0?false:true);
                            }
                        }
                    });
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /*@Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            switch (v.getId()){
                case R.id.edit_setting_fly_gohome_hight:  //设置飞机的最大飞行高度限制。
                    edit_setting_fly_gohome_hight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_SEARCH) {//EditorInfo.IME_ACTION_SEARCH、EditorInfo.IME_ACTION_SEND等分别对应EditText的imeOptions属性
                                String gohomeh = edit_setting_fly_gohome_hight.getText().toString();
                                if(!gohomeh.isEmpty()){
                                    float gohomehight = Float.parseFloat(gohomeh);
                                    if(gohomehight<20){
                                        gohomehight = 20f;
                                    } else if(gohomehight>500f){
                                        gohomehight = 500f;
                                    }else{
                                        serDroneSettingHight(gohomehight);
                                    }
                                }
                            }
                            return false;
                        }
                    });
                    break;

                case R.id.edit_setting_fly_gohome_distance://设置为该机最大飞行半径限制。
                    edit_setting_fly_gohome_distance.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_SEARCH) {//EditorInfo.IME_ACTION_SEARCH、EditorInfo.IME_ACTION_SEND等分别对应EditText的imeOptions属性
                                //TODO回车键按下时要执行的操作
                                String gohomeDIstance =edit_setting_fly_gohome_distance.getText().toString();
                                if(!gohomeDIstance.isEmpty()){
                                    float gohomeDistance =  Float.parseFloat(gohomeDIstance);
                                    if(gohomeDistance<15){
                                        gohomeDistance = 15f;
                                    }else if(gohomeDistance>8000){
                                        gohomeDistance = 8000f;
                                    }else{
                                        setDroneSettingradius(gohomeDistance);
                                    }
                                }
                            }
                            return false;
                        }
                    });
                    break;

                case R.id.edit_setting_drone_gohome_hight://设置返航高度
                    edit_setting_drone_gohome_hight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_SEARCH) {//EditorInfo.IME_ACTION_SEARCH、EditorInfo.IME_ACTION_SEND等分别对应EditText的imeOptions属性
                                String dronegphomehight = edit_setting_drone_gohome_hight.getText().toString();
                                if(!dronegphomehight.isEmpty()){
                                    float drone_gohome_hight = Float.parseFloat(dronegphomehight);
                                    if(drone_gohome_hight>500){
                                        drone_gohome_hight = 500f;
                                    } else if(drone_gohome_hight<20){
                                        drone_gohome_hight = 20f;
                                    }else{
                                        if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
                                            DJISampleApplication.getAircraftInstance().getFlightController().
                                                    setGoHomeAltitude(drone_gohome_hight,
                                                            new DJICommonCallbacks.DJICompletionCallback() {
                                                                @Override
                                                                public void onResult(DJIError djiError) {
                                                                    showError(djiError);
                                                                }
                                                            });
                                        }
                                    }
                                }
                            }
                            return false;
                        }
                    });
                    break;

            }
        }
    }*/

    //设置飞行器的飞行高度
    private void serDroneSettingHight(float hight){
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getFlightController().getFlightLimitation()
                    .setMaxFlightHeight(hight, new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            showError(djiError);
                        }
                    });
        }
    }

    //设置飞行器的最大飞行半径
    private void setDroneSettingradius(float nameradius){
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getFlightController().
                    getFlightLimitation().setMaxFlightRadius(nameradius,
                    new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            showError(djiError);
                        }
                    });
        }
    }

    //限远
    private void limitFar(boolean isChecked){
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getFlightController().getFlightLimitation()
                    .setMaxFlightRadiusLimitationEnabled(isChecked,
                            new DJICommonCallbacks.DJICompletionCallback() {
                                @Override
                                public void onResult(DJIError djiError) {
                                    showError(djiError);
                                    if(djiError == null){
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(isChecked){
                                                    rela_dis_number.setVisibility(View.VISIBLE);
                                                }else{
                                                    rela_dis_number.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
        }
    }

    public void setFlightFailsafeOperation(DJIFlightFailsafeOperation goHome){
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getFlightController().setFlightFailsafeOperation(goHome, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    showError(djiError);
                }
            });
        }
    }

    @OnClick(R.id.img_gohome_setting_peplo)
    public void peopleGoHome(View v){
        //设定人当前的坐标为返航点
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getFlightController().setHomeLocation(
                    new DJILocationCoordinate2D(DJISampleApplication.peploLat,DJISampleApplication.peploLng),
                    new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            showError(djiError);
                        }
                    });
        }
    }

    @OnClick(R.id.img_gohome_setting_drone)
    public void droneGoHome(View v){
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getFlightController().
                    setHomeLocationUsingAircraftCurrentLocation(new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    showError(djiError);
                }
            });
        }
    }

    /*@Override
    public void showAdapter(ArrayAdapter adapter) {
        if(spinner_outof != null)spinner_outof.setAdapter(adapter);
    }

    @Override*/
    public void showError(DJIError djiError) {
        if(djiError != null){
            if(djiError != null)  Utils.setResultToToast(mActivity,djiError==null?"设置成功":djiError.getDescription());
        }
    }

    public void setModelCameraAndOne(ModelCameraAndOne modelCameraAndOne) {
        this.modelCameraAndOnes = modelCameraAndOne;
    }

    /*@Override
    public void showDJIFlightFailsafeOperation(DJIFlightFailsafeOperation djiFlightFailsafeOperation) {
        int number = 0;
        if(djiFlightFailsafeOperation.name().equals("GoHome")){
            number = 0;
        }
        if(djiFlightFailsafeOperation.name().equals("Hover ")){
            number = 1;
        }

        if(djiFlightFailsafeOperation.name().equals("Landing ")){
            number = 2;
        }
        int finalNumber = number;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(spinner_outof != null) spinner_outof.setSelection(finalNumber);
            }
        });
    }

    @Override
    public void showGoHomeHight(Float aFloat) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(edit_setting_fly_gohome_hight != null) edit_setting_drone_gohome_hight.setText(aFloat+"");
            }
        });
    }

    @Override
    public void showMaxFlightHeight(Float aFloat) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(edit_setting_fly_gohome_hight != null)edit_setting_fly_gohome_hight.setText(aFloat+"");
            }
        });
    }

    @Override
    public void showMaxFlightRadius(Float aFloat) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(edit_setting_fly_gohome_distance != null)edit_setting_fly_gohome_distance.setText(aFloat+"");
            }
        });
    }

    @Override
    public void showMaxFlightRadiusLimitationEnable(boolean bool) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(distance_ios_fly != null) distance_ios_fly.setChecked(bool);
                if(bool){
                    if(rela_dis_number != null)rela_dis_number.setVisibility(View.VISIBLE);
                }else{
                    if(rela_dis_number != null) rela_dis_number.setVisibility(View.GONE);
                }
            }
        });
    }*/
}


