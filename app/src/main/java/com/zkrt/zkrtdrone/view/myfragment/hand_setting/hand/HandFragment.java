package com.zkrt.zkrtdrone.view.myfragment.hand_setting.hand;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;
import com.zkrt.zkrtdrone.until.Time_Date;
import com.zkrt.zkrtdrone.view.myfragment.camera.ModelCameraAndOne;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.SettingTabLayout;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.RemoteSettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.batterysetting.BatterySettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.commonsetting.CommonSettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.dronesetting.DroneSettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.hdsetting.HDSettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.holdersetting.HolderSettingFragment;
import com.zkrt.zkrtdrone.view.widget.XCSlideView;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import dji.common.airlink.DJISignalInformation;
import dji.common.battery.DJIBatteryState;
import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.flightcontroller.DJIGPSSignalStatus;
import dji.sdk.battery.DJIBattery;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseMvpFragment;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.DensityUtil;

import static com.zkrt.zkrtdrone.DJISampleApplication.mContext;
import static com.zkrt.zkrtdrone.DJISampleApplication.rightMenu;

/**
 * Created by jack_xie on 16-12-23.
 */
public class HandFragment extends BaseMvpFragment<HandPresenter,HandModel> implements HandContract.View{
    @BindView(R.id.base_img)
    ImageView base_img;
    @BindView(R.id.base_txt)
    TextView base_txt;
    @BindView(R.id.frag_status_bg)
    LinearLayout frag_status_bg;
    @BindView(R.id.txt_status)
    TextView txt_status;
    @BindView(R.id.txt_t) TextView txt_t;
    @BindView(R.id.txt_mode_values) TextView txt_mode_values;  //飞行器的模式
    @BindView(R.id.txt_satelite_num) TextView txt_satelite_num;  //卫星的数量
    @BindView(R.id.txt_signal_name) TextView txt_signal_name;  //遥控器的信号强度
    @BindView(R.id.txt_signal_name_hemi) TextView txt_signal_name_hemi;  //HDMI信号强度
    @BindView(R.id.img_gps_status) ImageView img_gps_status;  //GPS信号的质量
    @BindView(R.id.img_yao_status) ImageView img_yao_status;  //遥控的信号质量
    @BindView(R.id.img_hd_status) ImageView img_hd_status;  //图传的信号质量
    private XCSlideView mSlideViewRight;
    private int mScreenWidth = 0;
    private int numberStatus = 0;
    private FrameLayout frame_hand;
    private SettingTabLayout settingTabLayout;
    private ModelCameraAndOne modelCameraAndOne;

    @Override
    protected void init(Bundle savedInstanceState) {
        frame_hand = (FrameLayout) mActivity.findViewById(R.id.frame_hand);
        mScreenWidth = DensityUtil.getScreenWidthAndHeight(mContext)[0];
        mSlideViewRight = XCSlideView.create(mActivity, XCSlideView.Positon.RIGHT);
        if(settingTabLayout == null)settingTabLayout = new SettingTabLayout(getActivity());
        mSlideViewRight.setMenuView(getActivity(),
                settingTabLayout.getSettingView(LayoutInflater.from(getActivity()).inflate(R.layout.setting_layout,null)));
        settingTabLayout.addTableView();
        mSlideViewRight.setMenuWidth(mScreenWidth * 4 / 9);
        mSlideViewRight.mMaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
    }

    @OnClick(R.id.img_setting)
    public void setHandSetting(View v){
        if (!mSlideViewRight.isShow()) {
            mSlideViewRight.show();
            frame_hand.setVisibility(View.GONE);
            rightMenu = false;
            //deleteBattery();
        }
    }

    public void closeMenu(){
        if(mSlideViewRight.isShow()) {
            mSlideViewRight.dismiss();
            frame_hand.setVisibility(View.VISIBLE);
            rightMenu = true;
            setStartHandStatus();
        }
    }

    @Override
    protected int getFragmentViewId() {
        return R.layout.hand_layout;
    }

    @Override
    protected void getViewFindByid(View view) {
        super.getViewFindByid(view);
    }

    private void deleteBattery(){
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()) {
            DJISampleApplication.getProductInstance().getBattery().setBatteryStateUpdateCallback(null);
        }
    }

    private void setStartHandStatus(){
        if(DJIModuleVerificationUtil.isBattery()){
            DJISampleApplication.getProductInstance().getBattery().setBatteryStateUpdateCallback(
                    new DJIBattery.DJIBatteryStateUpdateCallback() {
                        @Override
                        public void onResult(DJIBatteryState djiBatteryState) {
                            showDJIBatteryState(djiBatteryState);
                        }
                    });
        }
    }

    public void setGpsToast(String namem, int color, int img){
        Utils.setResultToToast2(mContext,namem,color,img);
    }

    public void setStatus(String name){
        if(txt_status!=null)txt_status.setText(name);
    }

    public void setBgBreathe(int number){
        if(frag_status_bg ==null) return;
        if(isAdded()) {
            if (number == 0) {
                frag_status_bg.setBackgroundColor(mActivity.getResources().getColor(R.color.user_icon_6));
            } else if (number == 1) {
                frag_status_bg.setBackgroundColor(Color.YELLOW);
            } else {
                frag_status_bg.setBackgroundColor(Color.RED);
            }
        }
    }

    @Override
    public void showDJIBatteryState(DJIBatteryState djiBatteryState) {

        BigDecimal b = new BigDecimal(djiBatteryState.getCurrentVoltage()/1000f);
        float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

        DJIFlightControllerCurrentState djiFlightControllerCurrentState = DJISampleApplication.getAircraftInstance().getFlightController().getCurrentState();
        DJIGPSSignalStatus gpsStstus =  djiFlightControllerCurrentState.getGpsSignalStatus();
        DJISampleApplication.getAircraftInstance().getFlightController().getCurrentState();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(DJISampleApplication.getAircraftInstance().getFlightController().getCompass().hasError()){
                    setStatus("罗盘错误，需要校准");
                    setBgBreathe(1);
                }

                if(djiFlightControllerCurrentState == null) return;
                float hs = djiFlightControllerCurrentState.getVelocityZ();
                if(djiFlightControllerCurrentState.getAttitude().roll >= 30 ||
                        djiFlightControllerCurrentState.getAttitude().roll<=-30
                        || djiFlightControllerCurrentState.getAttitude().pitch>= 30 ||
                        djiFlightControllerCurrentState.getAttitude().pitch<= -30) {
                    setStatus("飞行器倾斜过大!");
                    setBgBreathe(1);
                }else {
                    if(hs>=10) {
                        setStatus("飞行器下降速度过快!");
                        setBgBreathe(1);
                    }
                }

                if(djiFlightControllerCurrentState == null) return;
                /*if(djiFlightControllerCurrentState.getGpsSignalStatus().toString().equals("None")) {
                    setGpsToast("没有GPS信号!", Color.RED, 0);
                }*/
                if(txt_t == null) return;
                txt_t.setText(Time_Date.cal(djiFlightControllerCurrentState.getFlightTime()));
                txt_mode_values.setText(djiFlightControllerCurrentState.getFlightModeString()+"");
                txt_satelite_num.setText((int)djiFlightControllerCurrentState.getSatelliteCount()+"");

                if (base_txt == null) return;
                base_txt.setText(f1+"V");
                if(f1<DJISampleApplication.lowone && f1>DJISampleApplication.lowtwo){
                    base_img.setBackgroundResource(R.mipmap.osd_electric_warning);
                    setStatus("低电量警告！");
                    numberStatus = 0;
                    setBgBreathe(1);
                }else if(f1<DJISampleApplication.lowtwo){
                    base_img.setBackgroundResource(R.mipmap.osd_electric_low);
                    setStatus("低电量危险！");
                    setBgBreathe(2);
                    numberStatus = 0;
                }else if(f1>DJISampleApplication.lowone){
                    base_img.setBackgroundResource(R.mipmap.osd_electric_btn_normal);
                    setStatus("正常!");
                    numberStatus = 1;
                    setBgBreathe(0);
                }

                if(gpsStstus != null){
                    if(gpsStstus.value() == 0){
                        if(numberStatus == 1) {
                            setStatus("无GPS信号");
                            setBgBreathe(1);
                            img_gps_status.setBackgroundResource(R.mipmap.osd_singal_level0);
                        }
                    }else if(gpsStstus.value() == 1) {
                        setStatus("GPS信号非常弱"); setBgBreathe(1);
                        if(img_gps_status !=null) img_gps_status.setBackgroundResource(R.mipmap.osd_singal_level1);
                    }else if(gpsStstus.value() == 2){
                        setStatus("GPS信号很弱!"); setBgBreathe(1);
                        if(img_gps_status !=null) img_gps_status.setBackgroundResource(R.mipmap.osd_singal_level2);
                    }else if(gpsStstus.value() == 3) {
                        if(img_gps_status !=null) img_gps_status.setBackgroundResource(R.mipmap.osd_singal_level3);
                    }else if(gpsStstus.value() == 4){
                        if(img_gps_status !=null) img_gps_status.setBackgroundResource(R.mipmap.osd_singal_level4);
                    }else if(gpsStstus.value() == 5){
                        if(img_gps_status !=null) img_gps_status.setBackgroundResource(R.mipmap.osd_singal_level5);
                    }
                }else{
                    if(DJISampleApplication.aBoolean){
                        setStatusError("与飞控连接断开!");
                    }
                }
            }
        });
    }

    public void setStatusError(String name){
        if(base_img !=null) {
            base_img.setBackgroundResource(R.mipmap.osd_electric_warning);
            img_gps_status.setBackgroundResource(R.mipmap.osd_singal_level0);
            img_yao_status.setBackgroundResource(R.mipmap.osd_singal_level0);
            img_hd_status.setBackgroundResource(R.mipmap.osd_singal_level0);

            txt_signal_name.setText(0 + "");
            txt_signal_name_hemi.setText(0 + "");
            base_txt.setText(0.00 + "V");
            setStatus(name);
            numberStatus = 1;
            setBgBreathe(2);
        }
    }

    @Override
    public void showDJISignalInformation(ArrayList<DJISignalInformation> arrayList) {
        final int numberPer_bundler = arrayList.get(0).getPercent();
                            /*final int numberhdPer_bundler = arrayList.get(1).getPercent();
                            final int numberPow = arrayList.get(0).getPower();
                            final int numberhdPow = arrayList.get(1).getPower();*/

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (txt_signal_name == null) return;
                txt_signal_name.setText(numberPer_bundler + "");
                txt_signal_name_hemi.setText(numberPer_bundler + "");
                if (numberPer_bundler > 0 && numberPer_bundler <= 10) {
                    img_yao_status.setBackgroundResource(R.mipmap.osd_singal_level0);
                    img_hd_status.setBackgroundResource(R.mipmap.osd_singal_level0);
                }

                if (numberPer_bundler > 10 && numberPer_bundler <= 20) {
                    img_yao_status.setBackgroundResource(R.mipmap.osd_singal_level1);
                    img_hd_status.setBackgroundResource(R.mipmap.osd_singal_level1);
                }

                if (numberPer_bundler > 20 && numberPer_bundler <= 40) {
                    img_yao_status.setBackgroundResource(R.mipmap.osd_singal_level2);
                    img_hd_status.setBackgroundResource(R.mipmap.osd_singal_level2);
                }

                if (numberPer_bundler > 40 && numberPer_bundler <= 60) {
                    img_yao_status.setBackgroundResource(R.mipmap.osd_singal_level3);
                    img_hd_status.setBackgroundResource(R.mipmap.osd_singal_level3);
                }

                if (numberPer_bundler > 60 && numberPer_bundler <= 80) {
                    img_yao_status.setBackgroundResource(R.mipmap.osd_singal_level4);
                    img_hd_status.setBackgroundResource(R.mipmap.osd_singal_level4);
                }

                if (numberPer_bundler > 80 && numberPer_bundler <= 100) {
                    img_yao_status.setBackgroundResource(R.mipmap.osd_singal_level5);
                    img_hd_status.setBackgroundResource(R.mipmap.osd_singal_level5);
                }

            }
        });
    }


    @Override
    public void showSettingFragment(BatterySettingFragment batterySettingFragment, CommonSettingFragment commonSettingFragment,
                                    DroneSettingFragment droneSettingFragment, HolderSettingFragment holderSettingFragment,
                                    HDSettingFragment hdSettingFragment, RemoteSettingFragment remoteSettingFragment) {
        if(settingTabLayout == null)settingTabLayout = new SettingTabLayout(mActivity);
        settingTabLayout.setSettingFragment(batterySettingFragment,
                commonSettingFragment,
                droneSettingFragment,
                hdSettingFragment,remoteSettingFragment,modelCameraAndOne);
    }

    public void setModelCameraAndOne(ModelCameraAndOne modelCameraAndOne) {
        this.modelCameraAndOne = modelCameraAndOne;
    }
}
