package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.commonsetting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fynn.switcher.Switch;
import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.adapter.SimpleAdapter;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.bean.exelBean;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;
import com.zkrt.zkrtdrone.view.dialog.ComposDialog;
import com.zkrt.zkrtdrone.view.dialog.ImuDialog;
import com.zkrt.zkrtdrone.view.dialog.SourcesDialog;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.hand.HandFragment;
import com.zkrt.zkrtdrone.view.widget.ListViewForScrollView;
import com.zkrt.zkrtdrone.view.widget.MySeekBar;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIIMUSensorState;
import dji.common.flightcontroller.DJIIMUState;
import dji.common.remotecontroller.DJIRCGimbalControlDirection;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.flightcontroller.DJIFlightControllerDelegate;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseMvpFragment;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.rxbean.CommonRxTask;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.rxutil.RxjavaUtil;

/**
 * Created by jack_xie on 16-12-29.
 */

public class CommonSettingFragment extends BaseMvpFragment  {
    @BindView(R.id.linear_setting_common) LinearLayout linear_setting_common;
    private SweetAlertDialog pDialog;
    //ACC
    @BindView(R.id.img_accelerometer1) ImageView img_accelerometer1;
    @BindView(R.id.img_accelerometer2) ImageView img_accelerometer2;
    @BindView(R.id.txt_accelerometer1) TextView txt_accelerometer1;
    @BindView(R.id.txt_accelerometer2) TextView txt_accelerometer2;
    @BindView(R.id.txt_imunumber) TextView txt_imunumber;
    @BindView(R.id.progress_accelerometer1) MySeekBar progress_accelerometer1;
    @BindView(R.id.progress_accelerometer2) MySeekBar progress_accelerometer2;

    //GYRO
    @BindView(R.id.img_gyroscope1) ImageView img_gyroscope1;
    @BindView(R.id.img_gyroscope2) ImageView img_gyroscope2;
    @BindView(R.id.txt_gyroscope1) TextView txt_gyroscope1;
    @BindView(R.id.txt_gyroscope2) TextView txt_gyroscope2;
    @BindView(R.id.progress_gyroscope1) MySeekBar progress_gyroscope1;
    @BindView(R.id.progress_gyroscope2) MySeekBar progress_gyroscope2;

    //指南针
    @BindView(R.id.img_compass1) ImageView img_compass1;
    @BindView(R.id.img_compass2) ImageView img_compass2;
    @BindView(R.id.txt_compass1) TextView txt_compass1;
    @BindView(R.id.txt_compass2) TextView txt_compass2;
    @BindView(R.id.txt_commpos_status) TextView txt_commpos_status;
    @BindView(R.id.progress_compass1) MySeekBar progress_compass1;
    @BindView(R.id.progress_compass2) MySeekBar progress_compass2;

    @BindView(R.id.lista) ListViewForScrollView lista;
    @BindView(R.id.img_rz_left) ImageView img_rz_left;
    //@BindView(R.id.lista2) ListViewForScrollView lista2;
    @BindView(R.id.linear_imu_compass) LinearLayout linear_imu_compass;

    //roomRc
    @BindView(R.id.btn_bolun_hangx) Button btn_bolun_hangx;
    @BindView(R.id.btn_bolun_roll) Button btn_bolun_roll;  //hg
    @BindView(R.id.btn_bolun_pitch) Button btn_bolun_pitch;  //fy

    //log excel
    @BindView(R.id.switch_landing2) Switch switch_landing2;
    @BindView(R.id.rela_log) RelativeLayout rela_log;

    private HandFragment handFragment;
    private ImuDialog imuDialog;
    private ComposDialog composDialog;
    private DJIIMUState djiimuState;
    private boolean comm = false;
    //private LogAdapter logAdapter;//logAdapter2;
    //private List<LogRz> listone,listtwo;
    //private ChartRzDialog chartRzDialog;
    private SimpleAdapter simpleAdapter;
    private SourcesDialog sourcesDialog;

    @Override
    protected int getFragmentViewId() {
        return R.layout.setting_common;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        //chartRzDialog = new ChartRzDialog(mActivity,R.style.customDialog,0);
        handFragment = (HandFragment) DJISampleApplication.fragmentManager.
                findFragmentByTag("HandFragment");
        imuDialog = new ImuDialog(mActivity,R.style.set_dialogs,0);
        composDialog = new ComposDialog(mActivity,R.style.set_dialogs,0);
        composDialog.setCanceledOnTouchOutside(false);
        imuDialog.setCanceledOnTouchOutside(false);
        //logAdapter = new LogAdapter(mActivity);
        //logAdapter2 = new LogAdapter(mActivity);
        //lista.setAdapter(logAdapter);
        //lista2.setAdapter(logAdapter2);
        //listone = CommonSettingModel.getListItemsName();
        simpleAdapter = new SimpleAdapter(mActivity);
        lista.setAdapter(simpleAdapter);
        sourcesDialog = new SourcesDialog(mActivity);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new SweetAlertDialog(mActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("生成Excel或预览统计图?")
                        .setContentText("一天之内的各项气体值的数据统计分析!")
                        .setCancelText("生成Excel")
                        .setConfirmText("预览统计图")
                        .showCancelButton(true)
                        .showContentText(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                startGetData(true,position);
                                sDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                //new DownloadTask(false,simpleAdapter.getItem(position),"statistics").execute();
                                startGetData(false,position);
                                sweetAlertDialog.cancel();
                            }
                        })
                        .show();

            }
        });

        switch_landing2.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch s, boolean isChecked) {
                if(rela_log !=null) {
                    rela_log.setVisibility(isChecked?View.GONE:View.VISIBLE);
                    DJISampleApplication.bool = isChecked;
                }
            }
        });
    }

    @Override
    protected void getViewFindByid(View view) {
        super.getViewFindByid(view);
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getFlightController().setOnIMUStateChangedCallback(new DJIFlightControllerDelegate.FlightControllerIMUStateChangedCallback() {
                @Override
                public void onStateChanged(DJIIMUState djiimuState) {
                    showDJiOnIMUStateChangedCallback(djiimuState);
                }
            });
        }

        if(DJIModuleVerificationUtil.isRemoteControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getRemoteController().getRCControlGimbalDirection(new DJICommonCallbacks.DJICompletionCallbackWith<DJIRCGimbalControlDirection>() {
                @Override
                public void onSuccess(DJIRCGimbalControlDirection djircGimbalControlDirection) {
                    showRccontrolGimnalDirection(djircGimbalControlDirection);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });
        }
    }

    private void startGetData(boolean bool, int pos){
        sourcesDialog.show();
        new DownloadTask(false,simpleAdapter.getItem(pos),"excel",bool).execute();  //true excel
    }

    private class DownloadTask extends AsyncTask<String,Integer,List<exelBean>>{
        private boolean bool;
        private boolean bool2;
        private String boos;
        private String time2;

        public DownloadTask(boolean b,String time,String bools,boolean b2) {
            this.bool = b;
            this.bool2 = b2;
            this.boos = bools;
            this.time2 = time;
        }

        @Override
        protected List<exelBean> doInBackground(String... params) {
            List<exelBean> allNews;
            if(bool){
                allNews = DataSupport.findAll(exelBean.class);
            }else{
                allNews = DataSupport.where("time2 = ?", time2).find(exelBean.class);
            }
            return allNews;
        }

        @Override
        protected void onPostExecute(List<exelBean> exelBeen) {
            super.onPostExecute(exelBeen);
            if(pDialog != null) pDialog.dismiss();
            if(bool){
                setStartSqlSources(exelBeen);
            }else{
                if(("excel").equals(boos)){
                    sourcesDialog.setData(exelBeen,time2,bool2);
                    sourcesDialog.updateView();
                }
            }
        }
    }

    private void setStartSqlSources(List<exelBean> exelBeen){
        List<String> stringList = new ArrayList<>();
        RxjavaUtil.executeRxTask(new CommonRxTask<Object>() {
            @Override
            public void doInIOThread() {
                for(exelBean s: exelBeen){
                    if(Collections.frequency(stringList, s.getTime2()) < 1) stringList.add(s.getTime2());
                }
            }

            @Override
            public void doInUIThread() {
                simpleAdapter.addAll(stringList);
                simpleAdapter.notifyDataSetChanged();

                if(lista.getVisibility() != View.VISIBLE){  // || lista2.getVisibility() != View.VISIBLE
                    if(lista.getVisibility() == View.GONE){
                        lista.setVisibility(View.VISIBLE);
                        linear_imu_compass.setVisibility(View.GONE);
                        img_rz_left.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @OnClick(R.id.btn_bolun_hangx)
    public void setBtnGimaedHx(View v){
        if(DJIModuleVerificationUtil.isRemoteControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getRemoteController().setRCControlGimbalDirection(DJIRCGimbalControlDirection.Yaw, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setBtnStaus(2);
                        }
                    });
                }
            });
        }
    }

    @OnClick(R.id.btn_bolun_pitch)
    public void setBtnGimaedfy(View v){
        if(DJIModuleVerificationUtil.isRemoteControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getRemoteController().setRCControlGimbalDirection(DJIRCGimbalControlDirection.Pitch, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setBtnStaus(0);
                        }
                    });
                }
            });
        }
    }

    @OnClick(R.id.btn_bolun_roll)
    public void setBtnGimaedRl(View v){
        if(DJIModuleVerificationUtil.isRemoteControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getRemoteController().setRCControlGimbalDirection(DJIRCGimbalControlDirection.Roll, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setBtnStaus(1);
                        }
                    });
                }
            });
        }
    }

    private void setBtnStaus(int ia){
        if(btn_bolun_hangx != null){
            if(ia == 0){
                btn_bolun_pitch.setBackgroundColor(mActivity.getResources().getColor(R.color.colorAccent));
                btn_bolun_hangx.setBackgroundColor(mActivity.getResources().getColor(R.color.green));
                btn_bolun_roll.setBackgroundColor(mActivity.getResources().getColor(R.color.green));
            }else if(ia == 1){
                btn_bolun_roll.setBackgroundColor(mActivity.getResources().getColor(R.color.colorAccent));
                btn_bolun_hangx.setBackgroundColor(mActivity.getResources().getColor(R.color.green));
                btn_bolun_pitch.setBackgroundColor(mActivity.getResources().getColor(R.color.green));
            }else if(ia == 2){
                btn_bolun_hangx.setBackgroundColor(mActivity.getResources().getColor(R.color.colorAccent));
                btn_bolun_roll.setBackgroundColor(mActivity.getResources().getColor(R.color.green));
                btn_bolun_pitch.setBackgroundColor(mActivity.getResources().getColor(R.color.green));
            }
        }
    }

    @OnClick(R.id.txt_holder_correct3)
    public void clickresetGimnal(View v){
        if(DJIModuleVerificationUtil.isGimbalModuleAvailable()){
            DJISampleApplication.getProductInstance().getGimbal().resetGimbal(new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    Utils.setResultToToast(mActivity,djiError==null?"成功":djiError.getDescription());
                }
            });
        }
    }

    @OnClick(R.id.rela_log) //日志文件
    public void logBtn(View v){
        pDialog = new
                SweetAlertDialog(DJISampleApplication.activity, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在整理数据...");
        pDialog.show();
        pDialog.setCancelable(false);
        new DownloadTask(true,"a","aa",false).execute();
    }

    @OnClick(R.id.img_rz_left)
    public void backLogBtn(View v){  //lista2.getVisibility() == View.GONE &&
        if(lista.getVisibility() == View.VISIBLE
                && linear_imu_compass.getVisibility() == View.GONE){
            linear_imu_compass.setVisibility(View.VISIBLE);
            img_rz_left.setVisibility(View.GONE);
            lista.setVisibility(View.GONE);
            //lista2.setVisibility(View.GONE);
        }
        /*else if(lista2.getVisibility() == View.VISIBLE && lista.getVisibility() == View.GONE
                && linear_imu_compass.getVisibility() == View.GONE){
            linear_imu_compass.setVisibility(View.GONE);
            lista.setVisibility(View.VISIBLE);
            lista2.setVisibility(View.GONE);
        }*/
    }

    //云台校准
    @OnClick(R.id.txt_holder_correct2)
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
    @OnClick(R.id.txt_holder_reset2)
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

    //校准罗盘
    @OnClick(R.id.txt_btn_Calibrate_compass)
    public void compassOnclick(View view){
        if(DJIModuleVerificationUtil.isCompassAvailable()){
            DJISampleApplication.getAircraftInstance().getFlightController().getCompass().
                    startCompassCalibration(new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if(djiError == null){
                                comm = true;
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        composDialog.show();
                                    }
                                });
                            }
                        }
                    });
        }
    }

    //校准IMU
    @OnClick(R.id.txt_btn_Calibrate_imu)
    public void calibrationIMU(View v){
        if(DJIModuleVerificationUtil.isCompassAvailable()){
            imuDialog.setDJIIMUState(djiimuState);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imuDialog.show();
                }
            });
        }
    }

    //@Override
    public void showDJiOnIMUStateChangedCallback(DJIIMUState djiimuState) {
        this.djiimuState = djiimuState;
        if(comm)composDialog.setCommposStatus();
       mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //txt_commpos_status.setText(djiCompass.getCalibrationStatus().);
                if(txt_imunumber !=null) txt_imunumber.setText(DJISampleApplication.getAircraftInstance().getFlightController().getNumberOfIMUs()+"");
               /* progress_accelerometer1.setProgress(djiimuState.getSubIMUState().length*10);
                progress_accelerometer2.setProgress(djiimuState.getSubIMUState().length*10);
                progress_gyroscope1.setProgress(djiimuState.getSubIMUState().length*10);
                progress_gyroscope2.setProgress(djiimuState.getSubIMUState().length*10);*/
            }
        });
        if(djiimuState.getSubIMUState() != null)
        hintHandMessage(djiimuState);
    }

    //@Override
    public void showDjiError(DJIError djiError) {
        Utils.setResultToToast(mActivity,djiError==null?"0k":djiError.getDescription());
    }

    //@Override
    public void showRccontrolGimnalDirection(DJIRCGimbalControlDirection djircGimbalControlDirection) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setBtnStaus(djircGimbalControlDirection.value());
            }
        });
    }

    //提示
    private void hintHandMessage(DJIIMUState djiimuState){
        DJIIMUSensorState djiimuSensorState = djiimuState.getAccelerometerStatus();
        if(djiimuSensorState ==null) return;
        int imuStatusnum = djiimuSensorState.value();
        if(imuStatusnum == 9){
            setMessage(1,"IMU传感器的偏置值较大，飞机不能起飞，需要进行IMU校准");
        }
        if(imuStatusnum == 8){
            setMessage(1,"IMU传感器的偏置值为中等，飞机可以安全起飞");
        }
        if(imuStatusnum == 7){
            setMessage(1,"IMU传感器的偏置值为中等，飞机可以安全起飞");
        }
        if(imuStatusnum == 4){
            setMessage(1,"IMU传感器有数据异常");
        }
        if(imuStatusnum == 1){
            setMessage(1,"IMU传感器与飞行控制器断开连接");
        }
        if(imuStatusnum == 6){
            setMessage(1,"IMU传感器并不是静态的，这架飞机可能不是足够稳定");
        }
        if(imuStatusnum == 5){
            setMessage(1,"IMU传感器升温");
        }
        if(imuStatusnum == 2){
            setMessage(1,"IMU传感器校准");
        }
        if(imuStatusnum == 3){
            setMessage(1,"校准IMU传感器失败");
        }
    }

    private void setMessage(int number , String name){
        if(handFragment != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handFragment.setBgBreathe(1);
                    handFragment.setStatus(name);
                }
            });
        }
    }
}
