package com.zkrt.zkrtdrone.view.layout.main;

import android.animation.Animator;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.Base64;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zhy.android.percent.support.PercentRelativeLayout;
import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.bean.AssistBean;
import com.zkrt.zkrtdrone.bean.ComAssis;
import com.zkrt.zkrtdrone.bean.ComBean;
import com.zkrt.zkrtdrone.bean.exelBean;
import com.zkrt.zkrtdrone.comassis.serialport_help.MyFunc;
import com.zkrt.zkrtdrone.comassis.serialport_help.SerialHelper;
import com.zkrt.zkrtdrone.receiver.DjiFlightController;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;
import com.zkrt.zkrtdrone.until.HexToBinary;
import com.zkrt.zkrtdrone.view.myfragment.camera.CameraFPV;
import com.zkrt.zkrtdrone.view.myfragment.camera.ModelCameraAndOne;
import com.zkrt.zkrtdrone.view.myfragment.camera.cameraFileSetting.CameraFileFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.hand.HandFragment;
import com.zkrt.zkrtdrone.view.myfragment.mount.MountFragment;
import com.zkrt.zkrtdrone.view.myfragment.mapmvp.MyMapFragment;
import com.zkrt.zkrtdrone.view.widget.RotateImageView;
import com.zkrt.zkrtdrone.view.widget.compass.CompassView2;

import org.litepal.tablemanager.Connector;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidParameterException;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnLongClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseMvpActivity;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.rxbean.IOTask;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.rxbean.UITask;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.DensityUtil;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.rxutil.RxjavaUtil;

import static com.zkrt.zkrtdrone.until.TimeUtil.getTimeDate2;

/**
 * Created by jack_xie on 16-12-22.
 */

public class MainActivity extends BaseMvpActivity<MainPresenter, MainModel> implements MainContract.View, SensorEventListener {
    @BindView(R.id.fram_mount)
    FloatingActionButton fram_mount;
    @BindView(R.id.coordinatorMain)
    PercentRelativeLayout coordinatorMain;
    @BindView(R.id.percenlinear)
    PercentRelativeLayout percenlinear;
    @BindView(R.id.frame_mount)
    FrameLayout frame_mount;
    @BindView(R.id.compass_drone)
    CompassView2 compass_drone;
    @BindView(R.id.fpv_view2)
    FrameLayout fpv_view2;
    @BindView(R.id.frame_map)
    FrameLayout frame_map;

    /*@BindView(R.id.btn_fllow_me_minssion)FloatingActionButton btn_fllow_me_minssion;
    @BindView(R.id.btn_hopt_minssion)FloatingActionButton btn_hopt_minssion;*/
    //@BindView(R.id.btn_up_minssions)FloatingActionButton btn_up_minssions;
    //@BindView(R.id.btn_clean_minssions)FloatingActionButton btn_clean_minssions;

    @BindView(R.id.btn_clean_minssion)
    FloatingActionButton btn_clean_minssion;
    @BindView(R.id.btn_up_minssion)
    FloatingActionButton btn_up_minssion;

    @BindView(R.id.compass_img_drone)
    RotateImageView compass_img_drone;
    @BindView(R.id.linear_localtion_peplo_drone)
    LinearLayout linear_localtion_peplo_drone;
    @BindView(R.id.linear_check)
    LinearLayout linear_check;
    @BindView(R.id.frame_camera)
    FrameLayout frame_camera;
    private String modleStop = "aa";
    /*@BindView(R.id.camera_linear)
    PercentLinearLayout camera_linear;*/
    @BindView(R.id.frame_way_btn)
    FrameLayout frame_way_btn;
    @BindView(R.id.way_btn)
    LinearLayout way_btn;
    @BindView(R.id.btn_takeoff_land)
    FloatingActionButton btn_takeoff_land;
    @BindView(R.id.btn_opention_Suspen)
    FloatingActionButton btn_opention_Suspen;
    @BindView(R.id.btn_opention_gohome)
    FloatingActionButton btn_opention_gohome;
    @BindView(R.id.btn_land_option)
    FloatingActionButton btn_land_option;
    @BindView(R.id.txt_log_a) TextView txt_log_a;
    @BindView(R.id.linear_camera) LinearLayout linear_camera;
    private MyMapFragment myMapFragment;
    private SerialControl ComA;
    //SerialPortFinder mSerialPortFinder;//串口设备搜索
    AssistBean AssistData;//用于界面数据序列化和反序列化
    private ModelCameraAndOne modelCameraAndOne;
    private CameraFileFragment cameraFile;
    private CameraFPV cameraFPV;
    private int screenWidth;
    private int screenHeigh;
    private SensorManager sensorManager;
    private HandFragment handFragment;
    //定义转过的角度
    private float currentDegree = 0f;
    ///-------------------
    private int take = 0;
    private int rec = 0;
    private int takeoff = 0;
    private int landing = 0;
    private int hover = 0;
    private int go_home = 0;
    private int cameraMap = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        RxjavaUtil.doInUIThread(new UITask<Object>() {
            @Override
            public void doInUIThread() {
                fbtButtonCircle(savedInstanceState);
            }
        });
        DJISampleApplication.mainActivity = this;
        int[] widhHight = DensityUtil.getScreenWidthAndHeight(this);
        screenWidth = widhHight[0];
        screenHeigh = widhHight[1];

        ComA = new SerialControl();
        AssistData = getAssistData();
        setControls();
        //生成数据库
        SQLiteDatabase db = Connector.getDatabase();
    }

    private void setControls() {
        AssistData.setTxtMode(true);
        // mSerialPortFinder= new SerialPortFinder();
        //ComA.setPort("/dev/ttymxc2");
        ComA.setPort("/dev/ttyS1");
        ComA.setBaudRate("9600");
        OpenComPort(ComA);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //获取触发event的传感器类型
        switch (sensorEvent.sensor.getType()) {
            /*case Sensor.TYPE_ORIENTATION:
                float degree = sensorEvent.values[0];
                //获取绕Z轴转过的角度
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        compass_drone.setAngle((int) -degree);
                    }
                });
                break;*/

            case Sensor.TYPE_ORIENTATION:
                float degree = sensorEvent.values[0];
                RotateAnimation ra = new RotateAnimation(currentDegree, -degree
                        , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                ra.setDuration(200);
                compass_drone.startAnimation(ra);
                currentDegree = -degree;
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    private class SerialControl extends SerialHelper {
        public SerialControl() {
        }

        @Override
        protected void onDataReceived(final ComBean ComRecData) {
            RxjavaUtil.doInIOThread(new IOTask<Object>() {
                @Override
                public void doInIOThread() {
                    goneExitText(MyFunc.HexToString(MyFunc.ByteArrToHex(ComRecData.bRec)));
                }
            });
        }
    }

    private void CloseComPort(SerialHelper ComPort) {
        if (ComPort != null) {
            ComPort.close();
        }
    }

    private void goneExitText(ComAssis comAssis) {
        if (comAssis != null) {
            //起飞
            if (comAssis.getOneBtn() == 1 && takeoff == 0) {
                takeoff = 1;
                DjiFlightController.TakeOffDrone(MainActivity.this);
            } else if (comAssis.getOneBtn() == 0 && takeoff == 1) {
                takeoff = 0;
            }
            //降落
            if (comAssis.getTwoBtn() == 1 && landing == 0) {
                landing = 1;
                DjiFlightController.LandingDrone(MainActivity.this, new DjiFlightController.statusString() {
                    @Override
                    public void StringStatus(String name) {
                        modleStop = name;
                    }
                });
            } else if (comAssis.getTwoBtn() == 0 && landing == 1) {
                landing = 0;
            }

            //返航
            if (comAssis.getThreeBtn() == 1 && go_home == 0) {
                go_home = 1;
                DjiFlightController.GoHomeDrone(MainActivity.this, new DjiFlightController.statusString() {
                    @Override
                    public void StringStatus(String name) {
                        modleStop = name;
                    }
                });
            } else if (comAssis.getThreeBtn() == 0 && go_home == 1) {
                go_home = 0;
            }

            //悬停
            if (comAssis.getFourBtn() == 1 && hover == 0) {
                hover = 1;
                DjiFlightController.HoverDrone(MainActivity.this, modleStop);
            } else if (comAssis.getFourBtn() == 0 && hover == 1) {
                hover = 0;
            }

            //拍照
            if (comAssis.getFiveBtn() == 1 && take == 0) {
                take = 1;
                if (modelCameraAndOne != null) modelCameraAndOne.setTakePhoto(false);
            } else if (comAssis.getFiveBtn() == 0 && take == 1) {
                take = 0;
            }

            //录像
            if (comAssis.getSixBtn() == 1 && rec == 0) {
                rec = 1;
                if (modelCameraAndOne != null) modelCameraAndOne.setRecVideo(false);
            } else if (comAssis.getSixBtn() == 0 && rec == 1) {
                rec = 0;
            }

            //抛投
            if (modelCameraAndOne != null) modelCameraAndOne.setPaoTou(comAssis.getSevenBtn());

            //照射
            if (modelCameraAndOne != null)
                modelCameraAndOne.setZhaoShe(comAssis.getNightBtn(), false);

            //变焦
            if (modelCameraAndOne != null)
                modelCameraAndOne.setComAssisZoom(comAssis.getZoom(), false);

            //遥感
            if (modelCameraAndOne != null)
                modelCameraAndOne.setRemote(comAssis.getUpOrDow(), comAssis.getLeftOrRight());
        }
    }

    private void OpenComPort(SerialHelper ComPort) {
        try {
            ComPort.open();
        } catch (SecurityException e) {
            Utils.setResultToToast(this, "打开串口失败:没有串口读/写权限!");
        } catch (IOException e) {
            Utils.setResultToToast(this, "打开串口失败:未知错误!");
        } catch (InvalidParameterException e) {
            Utils.setResultToToast(this, "打开串口失败:参数错误!");
        }
    }

    private AssistBean getAssistData() {
        SharedPreferences msharedPreferences = getSharedPreferences("ComAssistant", Context.MODE_PRIVATE);
        AssistBean AssistData = new AssistBean();
        try {
            String personBase64 = msharedPreferences.getString("AssistData", "");
            byte[] base64Bytes = Base64.decode(personBase64.getBytes(), 0);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            AssistData = (AssistBean) ois.readObject();
            return AssistData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AssistData;
    }

    @OnClick(R.id.img_zoom_out)
    public void setZoomout(View v) {
        myMapFragment.goToZoom(0);
    }

    @OnClick(R.id.img_zoom_in)
    public void setZoomin(View v) {
        myMapFragment.goToZoom(1);
    }

    @OnClick(R.id.frag_test)
    public void setOnlickPeplo(View v) {
        if (linear_localtion_peplo_drone.getVisibility() == View.VISIBLE || linear_check.getVisibility() == View.VISIBLE) {
            linear_localtion_peplo_drone.setVisibility(View.GONE);
            linear_check.setVisibility(View.GONE);
        } else linear_localtion_peplo_drone.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.frame_peplo)
    public void setPeplo(View v) {
        if (myMapFragment != null) {
            linear_localtion_peplo_drone.setVisibility(View.GONE);
            myMapFragment.goToPepleOrDrone(0);
        }
    }

    @OnClick(R.id.frame_drone)
    public void setOnlickdrone(View v) {
        if (myMapFragment != null) {
            linear_localtion_peplo_drone.setVisibility(View.GONE);
            myMapFragment.goToPepleOrDrone(1);
/*
            SweetAlertDialog pDialog = new
                    SweetAlertDialog(DJISampleApplication.activity, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("获取定位信息中...");
            pDialog.show();
            pDialog.setCancelable(false);*/
            /*do{
                myMapFragment.goToPepleOrDrone(1);
            }while(myMapFragment.getAmapLocation() == null);*/
            /*if(myMapFragment.getAmapLocation() != null){
                pDialog.dismiss();
            }*/
        }
    }

    //长按定位
    @OnLongClick(R.id.frag_test)
    public boolean goToLongLocationFollowDrone() {
        linear_check.setVisibility(View.VISIBLE);
        return true;
    }

    //跟随飞行器的checkBox
    @OnCheckedChanged(R.id.check_drone)
    public void goToCheckBoxFollowDrone(boolean checked) {
        if (myMapFragment == null) return;
        myMapFragment.setBoolFolowDrone(checked);
        linear_check.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_opention_way)
    public void wayBtnOption(View view) {
        if (frame_way_btn.getVisibility() == View.GONE) {
            myMapFragment.setIsAddWay(true);
            frame_way_btn.setVisibility(View.VISIBLE);
            way_btn.setVisibility(View.GONE);
            //myMapFragment.enableGestureDetection();
        }
    }

    @OnClick(R.id.btn_clean_minssion)
    public void onCleanWay(View view) {
        if (frame_way_btn.getVisibility() == View.VISIBLE) {
            myMapFragment.setIsAddWay(false);
            frame_way_btn.setVisibility(View.GONE);
            way_btn.setVisibility(View.VISIBLE);
            //myMapFragment.disableGestureDetection();
        }
    }

    /*//跟随
    @OnClick(R.id.btn_fllow_me_minssion)
    public void onFllowMeWay(View v){
        myMapFragment.startFllowMeMission();
        btn_up_minssions.setVisibility(View.VISIBLE);
        btn_clean_minssions.setVisibility(View.VISIBLE);
        btn_fllow_me_minssion.setVisibility(View.VISIBLE);
    }

    //环绕
    @OnClick(R.id.btn_hopt_minssion)
    public void onHotPoCicrlWay(View v){
        myMapFragment.startHotPotMission();
        btn_up_minssions.setVisibility(View.VISIBLE);
        btn_clean_minssions.setVisibility(View.VISIBLE);
        btn_hopt_minssion.setVisibility(View.GONE);
    }

    //终止任务
    @OnClick(R.id.btn_clean_minssions)
    public void onMissionClean(View v){
        myMapFragment.clearMapMinssion();
        btn_clean_minssions.setVisibility(View.GONE);
        btn_up_minssions.setVisibility(View.GONE);
        btn_hopt_minssion.setVisibility(View.VISIBLE);
        btn_fllow_me_minssion.setVisibility(View.VISIBLE);
    }

    //start follow任务
    @OnClick(R.id.btn_up_minssions)
    public void onUpmission(View v){
        myMapFragment.startWaypointMission();
        btn_up_minssions.setVisibility(View.VISIBLE);
        btn_clean_minssions.setVisibility(View.VISIBLE);
    }
*/
    //返航
    @OnClick(R.id.btn_opention_gohome)
    public void gotoHomeStart(View v) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("是否确定返航？")
                .setContentText("执行返航后飞行器将返回返航点")
                .setCancelText("取消")
                .setConfirmText("确定")
                .showCancelButton(true).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
                DjiFlightController.GoHomeDrone(MainActivity.this, new DjiFlightController.statusString() {
                    @Override
                    public void StringStatus(String name) {
                        modleStop = name;
                    }
                });
            }
        }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.cancel();
            }
        })
                .show();
    }

    @OnClick(R.id.btn_land_option)
    public void onLandAuto(View v) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("是否确定降落？")
                .setContentText("执行降落后飞行器将缓慢降落当前位置")
                .setCancelText("取消")
                .setConfirmText("确定")
                .showCancelButton(true).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
                DjiFlightController.LandingDrone(MainActivity.this, new DjiFlightController.statusString() {
                    @Override
                    public void StringStatus(String name) {
                        modleStop = name;
                    }
                });
            }
        })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    @OnClick(R.id.btn_takeoff_land)
    public void onTakeOffLanding(View v) {
        takeOffDrones();
    }

    private void takeOffDrones() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("是否确定起飞？")
                .setContentText("在起飞前请确认一切准备就绪")
                .setCancelText("取消")
                .setConfirmText("确定")
                .showCancelButton(true).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
                DjiFlightController.TakeOffDrone(MainActivity.this);
            }
        }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.cancel();
            }
        }).show();
    }

    @OnClick(R.id.btn_opention_Suspen)
    public void onOpenSuspen(View v) {
        DjiFlightController.HoverDrone(MainActivity.this, modleStop);
    }

    @OnClick(R.id.img_earaser)
    public void clearWay(View view) {
        myMapFragment.clearWarPoily();
    }

    //设置地图的样式   是标准  还是卫星
    @OnClick(R.id.fram_maptype)
    public void goToMapType() {
        if (myMapFragment == null) return;
        int mapType = myMapFragment.getMapType();
        if (mapType != 0) {
            if (mapType == AMap.MAP_TYPE_NORMAL) {
                myMapFragment.goToMapType(AMap.MAP_TYPE_SATELLITE);
            } else if (mapType == AMap.MAP_TYPE_SATELLITE) {
                myMapFragment.goToMapType(AMap.MAP_TYPE_NORMAL);
            }
        } else {
            Utils.setResultToToast(this, "地图类型获取失败");
        }
    }

    //准备上传航点
    @OnClick(R.id.btn_up_minssion)
    public void onUpDroneMission(View v) {
        myMapFragment.prepareWayPointMission();
    }

    //开始任务
    @OnClick(R.id.btn_start_minssion)
    public void onStartDroneMission(View v) {
        myMapFragment.startWaypointMission();
    }

    //停止任务
    @OnClick(R.id.btn_stop_minssion)
    public void onStopDroneMission(View v) {
        myMapFragment.clearMapMinssion();
    }

    //暂停任务
    @OnClick(R.id.btn_pusae_minssion)
    public void onPuaseDroneMission(View v) {
        myMapFragment.droneMissionPause();
    }

    //继续任务
    @OnClick(R.id.btn_resume_minssion)
    public void onResumeDroneMission(View v) {
        myMapFragment.droneMissionResune();
    }

    //视频隐藏与显示
    @OnClick(R.id.map_camera)
    public void clickFPV(View v) {
        if (cameraMap == 0) {
            cameraMap = 1;
            PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(frame_map.getWidth(), frame_map.getHeight());
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            fpv_view2.setLayoutParams(layoutParams);
            frame_map.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth, screenHeigh));

            ViewGroup vg2 = (ViewGroup) frame_map.getParent();
            vg2.bringChildToFront(vg2.getChildAt(vg2.indexOfChild(frame_map)));
            vg2.invalidate();

            ViewGroup vg = (ViewGroup) fpv_view2.getParent();
            vg.bringChildToFront(vg.getChildAt(vg.indexOfChild(fpv_view2)));
            vg.invalidate();
        } else if (cameraMap == 1) {
            cameraMap = 0;
            PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(fpv_view2.getWidth(), fpv_view2.getHeight());
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            frame_map.setLayoutParams(layoutParams);
            fpv_view2.setLayoutParams(new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            ViewGroup vg2 = (ViewGroup) fpv_view2.getParent();
            vg2.bringChildToFront(vg2.getChildAt(vg2.indexOfChild(fpv_view2)));
            vg2.invalidate();

            ViewGroup vg = (ViewGroup) frame_map.getParent();
            vg.bringChildToFront(vg.getChildAt(vg.indexOfChild(frame_map)));
            vg.invalidate();
        }
        ViewGroup vg3 = (ViewGroup) percenlinear.getParent();
        vg3.bringChildToFront(vg3.getChildAt(vg3.indexOfChild(percenlinear)));
        vg3.invalidate();
    }

    @Override
    public void showHandFragment(HandFragment fragment) {
        this.handFragment = fragment;
        getSupportFragmentManager().beginTransaction().
                replace(R.id.frame_hand, fragment, "HandFragment").commit();
    }

    @Override
    public void showMountFragment(MountFragment fragment) {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.frame_mount, fragment, "MountFragment").commit();
    }

    @Override
    public void showCameraFPV(CameraFPV cameraFPVs) {
        this.cameraFPV = cameraFPVs;
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fpv_view2, cameraFPVs, "CameraFPV").commit();
    }

    @Override
    public void showMyMapFragment(MyMapFragment fragment) {
        this.myMapFragment = fragment;
        getSupportFragmentManager().beginTransaction().
                replace(R.id.frame_map, fragment, "MyMapFragment").commit();
        myMapFragment.setCompassDroneOmage(compass_img_drone, handFragment);
    }

    @Override
    public void showCameraFileFragment(CameraFileFragment fragment, ModelCameraAndOne modelCameraAndOnes) {
        this.cameraFile = fragment;
        this.modelCameraAndOne = modelCameraAndOnes;
        if (handFragment != null) handFragment.setModelCameraAndOne(modelCameraAndOne);
        cameraFile.setFpv(cameraFPV.getFPV());
        cameraFile.setFpvImageView(cameraFPV.getFPVImageView());
        getSupportFragmentManager().beginTransaction().
                replace(R.id.frame_camera, modelCameraAndOnes, "ModelCameraAndOne").commit();
        /*if(DJIModuleVerificationUtil.isCameraModuleAvailable())
            if(DJISampleApplication.getCameraInstance().isConnected()){
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_camera,fragment).commit();*/
            /*camera_linear.setVisibility(View.VISIBLE);
            else camera_linear.setVisibility(View.GONE);*/
        //}
    }

    @Override
    public void showFlightControllerState(DJIFlightControllerCurrentState djiFlightControllerCurrentState) {
        //如果电机打开。
        boolean bool = djiFlightControllerCurrentState.areMotorsOn();
        float atti = djiFlightControllerCurrentState.getAircraftLocation().getAltitude();

                    /*//确定的最大飞行高度已达到。
                    DJIFlightLimitation djiFlightLimitation = DJISampleApplication.getAircraftInstance().getFlightController().getFlightLimitation();
                    boolean boola = djiFlightLimitation.hasReachedMaxFlightHeight();
                    if(boola) Utils.setResultToToast(MainActivity.this,"已到达最高的飞行高度");

                    //确定的最大飞行半径已达到。
                    boolean boolaa = djiFlightLimitation.hasReachedMaxFlightRadius();
                    if(boolaa) Utils.setResultToToast(MainActivity.this,"已到达最远的飞行半径");*/

        //如果飞机回家。
        if (djiFlightControllerCurrentState.isGoingHome()) {
            if (modleStop.equals("GoHome"))
                Utils.setResultToToast(MainActivity.this, "开始返航");
            else if (modleStop.equals("Landing"))
                Utils.setResultToToast(MainActivity.this, "开始降落");
        }

        if (cameraFile != null && modelCameraAndOne != null) {
            if (DJIModuleVerificationUtil.isCameraModuleAvailable() && DJISampleApplication.getCameraInstance().isConnected()) {
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_camera, cameraFile, "CameraFileFragment").commit();
                if(linear_camera!=null) linear_camera.setVisibility(View.VISIBLE);
                if(frame_camera!=null) frame_camera.setVisibility(View.VISIBLE);
            } else if(DJISampleApplication.DEVICE_TYPE_CAMERA == 1){
                if(linear_camera!=null) linear_camera.setVisibility(View.GONE);
                if(frame_camera!=null) frame_camera.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_camera, modelCameraAndOne, "ModelCameraAndOne").commit();
            }else{
                if(linear_camera!=null) linear_camera.setVisibility(View.GONE);
                if(frame_camera!=null) frame_camera.setVisibility(View.GONE);
            }
        }

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //检查板载SDK设备是否可用
                /*if(frame_mount !=null){
                    if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
                        if(!DJIModuleVerificationUtil.isOnboardSDKDeviceAvailable()){
                            frame_mount.setVisibility(View.GONE);
                            fram_mount.setVisibility(View.GONE);
                            modelCameraAndOne.getCameraLinear().setVisibility(View.GONE);
                            modelCameraAndOne.getCamerarela().setVisibility(View.GONE);
                        }else{
                            frame_mount.setVisibility(View.VISIBLE);
                            fram_mount.setVisibility(View.VISIBLE);
                        }
                    }
                }*/
                if (btn_takeoff_land != null) {
                    //如果飞机飞行。
                    if (atti > 0 && bool) {
                        btn_takeoff_land.setVisibility(View.GONE);
                        btn_land_option.setVisibility(View.VISIBLE);
                        btn_opention_gohome.setVisibility(View.VISIBLE);
                        btn_opention_Suspen.setVisibility(View.VISIBLE);

                        //跟随 环绕
                        /*btn_fllow_me_minssion.setVisibility(View.VISIBLE);
                        btn_hopt_minssion.setVisibility(View.VISIBLE);*/
                    } else {
                        btn_takeoff_land.setVisibility(View.VISIBLE);
                        btn_land_option.setVisibility(View.GONE);
                        btn_opention_gohome.setVisibility(View.GONE);
                        btn_opention_Suspen.setVisibility(View.GONE);

                        //跟随 环绕
                        /*btn_fllow_me_minssion.setVisibility(View.GONE);
                        btn_hopt_minssion.setVisibility(View.GONE);*/
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null)
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                    SensorManager.SENSOR_DELAY_FASTEST);  //SENSOR_DELAY_GAME
    }

    @Override
    protected void onPause() {
        CloseComPort(ComA);
        if (sensorManager != null) sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (sensorManager != null) sensorManager.unregisterListener(this);
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    protected void onDestroy() {
        CloseComPort(ComA);
        //取消注册
        if (sensorManager != null) sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void showSensorManager(SensorManager sensorManagers) {
        this.sensorManager = sensorManagers;
    }

    @Override
    public void showRemoteBool(boolean bool) {
        if (!bool || !DJISampleApplication.aBoolean) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(handFragment !=null)handFragment.setStatusError("与遥控器断开链接");
                    if (frame_mount != null) frame_mount.setVisibility(View.GONE);
                    if (fram_mount != null) fram_mount.setVisibility(View.GONE);
                    if (modelCameraAndOne != null)
                        modelCameraAndOne.getCameraLinear().setVisibility(View.GONE);
                }
            });
        }
    }

    public void setTextLog(String name, int color) {
        RxjavaUtil.doInIOThread(new IOTask<Object>() {
            @Override
            public void doInIOThread() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txt_log_a.setVisibility(View.VISIBLE);
                        txt_log_a.setText(name + "");
                        txt_log_a.setBackgroundColor(color);
                    }
                });
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txt_log_a.setVisibility(View.GONE);
            }
        },3000);
    }

    private void fbtButtonCircle(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (savedInstanceState == null) {
                coordinatorMain.setVisibility(View.INVISIBLE);
                ViewTreeObserver viewTreeObserver = coordinatorMain.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            circularRevealTransition(); //
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                coordinatorMain.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                coordinatorMain.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        }
                    });
                }
            }
        }
    }

    private void circularRevealTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int X = 9 * coordinatorMain.getWidth() / 10;
            int Y = 9 * coordinatorMain.getHeight() / 10;
            int Duration = 800;
            float finalRadius = Math.max(coordinatorMain.getWidth(), coordinatorMain.getHeight());
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(coordinatorMain, X, Y, 0, finalRadius);
            circularReveal.setDuration(Duration);
            coordinatorMain.setVisibility(View.VISIBLE);
            circularReveal.start();
        }
    }
}