package com.zkrt.zkrtdrone.view.myfragment.mapmvp;

import android.graphics.Bitmap;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.adapter.MissionAdapter;
import com.zkrt.zkrtdrone.base.BtnClick;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;
import com.zkrt.zkrtdrone.until.GeoUtils;
import com.zkrt.zkrtdrone.until.Time_Date;
import com.zkrt.zkrtdrone.until.UpdateFollowmeSimLocationThread;
import com.zkrt.zkrtdrone.view.dialog.DialogPopup;
import com.zkrt.zkrtdrone.view.dialog.FllowMeDialogPopup;
import com.zkrt.zkrtdrone.view.dialog.HotPoDialogPopup;
import com.zkrt.zkrtdrone.view.dialog.ProgressDialog;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.hand.HandFragment;
import com.zkrt.zkrtdrone.view.myfragment.waypoint.WaypointMission;
import com.zkrt.zkrtdrone.view.widget.AttitudeIndicator;
import com.zkrt.zkrtdrone.view.widget.RotateImageView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import butterknife.OnClick;
import dji.common.camera.DJICameraExposureParameters;
import dji.common.camera.DJICameraSettingsDef;
import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.flightcontroller.DJIFlightControllerFlightMode;
import dji.common.flightcontroller.DJILocationCoordinate2D;
import dji.common.remotecontroller.DJIRCGPSData;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.camera.DJIMedia;
import dji.sdk.missionmanager.DJICustomMission;
import dji.sdk.missionmanager.DJIFollowMeMission;
import dji.sdk.missionmanager.DJIHotPointMission;
import dji.sdk.missionmanager.DJIMission;
import dji.sdk.missionmanager.DJIMissionManager;
import dji.sdk.missionmanager.DJIPanoramaMission;
import dji.sdk.missionmanager.DJIWaypointMission;
import dji.sdk.products.DJIAircraft;
import dji.sdk.remotecontroller.DJIRemoteController;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseApplication;
import zkrtdrone.zkrt.com.maplib.base.BaseMap;
import zkrtdrone.zkrt.com.maplib.bean.LonglatLog;
import zkrtdrone.zkrt.com.maplib.bean.MissionLatLon;
import zkrtdrone.zkrt.com.maplib.until.Converter;
import zkrtdrone.zkrt.com.maplib.until.MapUtil;
import zkrtdrone.zkrt.com.maplib.until.location.LocationUtil;

/**
 * Created by jack_xie on 16-12-19.
 */

public class MyMapFragment extends BaseMap<MapPresenter,MapModel> implements
        DJICommonCallbacks.DJICompletionCallback,MapContract.View,
        DJIMissionManager.MissionProgressStatusCallback,MissionAdapter.OnRecyclerViewItemClickListener{
    public DJIFlightControllerFlightMode flightState;
    private Marker droneMarker = null;
    private boolean bool = false;
    private TextView txt_h;
    private AttitudeIndicator img_hud;
    private TextView txt_d;
    private TextView txt_vs;  //速度
    private TextView txt_hs;   //飞机的速度
    private TextView txt_main_way_m;
    private ImageView img_record_hs;
    private int img = R.mipmap.flight_record_unsort;
    private DJIMission djiMission;
    private MissionAdapter missionAdapter;
    private RecyclerView mai_recy_waypion;

    private DialogPopup popup;
    private UpdateFollowmeSimLocationThread mUpdateSimLocateThread = null;
    public DJIMissionManager mMissionManager;
    private int lastTargetWPIndex = -1;//任务进展状态
    private ProgressDialog progressDialog;
    private Marker markerpop;
    private boolean boolpop = false;
    private RotateImageView compassDroneOmage;
    private HandFragment handFragment;
    private FllowMeDialogPopup fllowMeDialogPopup;
    private HotPoDialogPopup hotPoDialogPopup;

    @Override
    public void onResult(DJIError djiError) {
        Utils.setResultToToast(mActivity,"Execution finished: " + (djiError == null ? "Success" : djiError.getDescription()));
    }

    @Override
    public void showDjiBaseProdouct(DJIFlightControllerCurrentState djiFlightControllerCurrentState) {
        float y = djiFlightControllerCurrentState.getVelocityY();
        float x = djiFlightControllerCurrentState.getVelocityX();
        float z;
        if(y<0) y = y*-1;
        if(x<0) x = x*-1;
        if(x<y) z = y; else z = x;
        float hs = djiFlightControllerCurrentState.getVelocityZ();
        if(hs<0.0f){hs=hs*-1; img = R.mipmap.flight_record_sort_min;}else if(hs>0.0f){img = R.mipmap.flight_record_sort_max;}else{img = R.mipmap.flight_record_unsort;}
        float finalHs = hs;
        float finalZ = z;
        float hh = djiFlightControllerCurrentState.getAircraftLocation().getAltitude();
        DJISampleApplication.HH = hh;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(txt_h != null){
                    if(compassDroneOmage != null)compassDroneOmage.setAttitude(djiFlightControllerCurrentState.getAttitude().yaw);
                    img_hud.setAttitude(djiFlightControllerCurrentState.getAttitude().roll,djiFlightControllerCurrentState.getAttitude().pitch,
                            djiFlightControllerCurrentState.getAttitude().yaw);

                    txt_h.setText(hh+"M");
                    txt_hs.setText(finalHs +"M/s");
                    txt_vs.setText(finalZ +"M/s");
                    img_record_hs.setBackgroundResource(img);
                    txt_d.setText(GeoUtils.getDistance(djiFlightControllerCurrentState.getHomeLocation().getLongitude(),
                            djiFlightControllerCurrentState.getHomeLocation().getLatitude(),
                            djiFlightControllerCurrentState.getAircraftLocation().getCoordinate2D().getLongitude(),
                            djiFlightControllerCurrentState.getAircraftLocation().getCoordinate2D().getLatitude())+"M");
                }
            }
        });

        if(aMap == null) return;
        LatLng latLonPoint = Converter.getGPStoAmap(new LatLng(djiFlightControllerCurrentState.getAircraftLocation().getLatitude(),
                djiFlightControllerCurrentState.getAircraftLocation().getLongitude()),mActivity);
        DJISampleApplication.Lat = droneLocationLat = latLonPoint.latitude;
        DJISampleApplication.log = droneLocationLng = latLonPoint.longitude;
        flightState = djiFlightControllerCurrentState.getFlightMode();

        if(bool){ //指定跟随飞机
            MapUtil.moveTo(aMap,new LatLng(droneLocationLat,droneLocationLng),true);
        }
        setDroneGPS(droneLocationLat,droneLocationLng);
        startDronePolay();
    }

    @Override
    public void showDroneBitMap(Bitmap bitmap) {
        if(bitmap == null) return;
        LatLng pos = new LatLng(droneLocationLat, droneLocationLng);
        setAmapCirCle(pos);
        BitmapDescriptor iconBitmap = BitmapDescriptorFactory.fromBitmap(bitmap);
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pos);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.icon(iconBitmap);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (droneMarker != null) {
                    droneMarker.remove();
                }
                if (MapUtil.checkGpsCoordinates(droneLocationLat, droneLocationLng)) {
                    droneMarker = aMap.addMarker(markerOptions);
                }
            }
        });
    }

    //Navigation in the product is unsupported
    //The estimated time for the mission is too long
    //Distance between the aiccraf and mobile phone is beyond acceptable limit(must be lower than 20000m)

    @Override
    public void showRCGps(DJIRemoteController djiRemoteController, DJIRCGPSData djircgpsData) {
        //LocationUtil.wgsToGcj(amapLocation, false);
        /*Utils.setResultToToast(mActivity,"遥控器看到的GPS卫星数"+djircgpsData.satelliteCount+
                "遥控器的GPS纬度值（以度为单位）"+djircgpsData.latitude+
        "遥控器的GPS经度值（以度为单位）"+djircgpsData.longitude+
        "GPS数据有效。"+djircgpsData.isValid+
        "GPS位置的误差幅度（以米为单位）"+djircgpsData.accuracy+
        "遥控器在东方向的速度（米/秒）。"+djircgpsData.speedEast+
                "遥控器在北方的速度（米/秒）。"+djircgpsData.speedNorth);*/
    }

    @Override
    public void mapOnclick() {
        super.mapOnclick();
        if(handFragment !=null)handFragment.closeMenu();
    }

    public void addMinssionPolay(LatLng point, Marker marker, int number, boolean boolges){
        if(boolges){
            MissionLatLon missionLatLon = new MissionLatLon(number,new LonglatLog(point,amapLocationa.getAddress(),
                    Time_Date.getDateTimeFile()),15,false,marker,10,0,0,true,0,"无");
            if(missionLatLon != null){
                mBiMarkersMap.put(number,marker);
                wayMissionList.add(missionLatLon);
                wayMapPlayout(getResources().getColor(R.color.aaa));
                if(missionAdapter != null) missionAdapter.setListMission(wayMissionList);
                CalculationDistance();
            }
        }else missionItemWindow(point, marker,number,true,"无");
    }

    //清理地图航线
    public void clearWarPoily(){
        for(MissionLatLon missionLatLon:wayMissionList){
            missionLatLon.getMarker().remove();
        }
        cleanPolyLineMarkLatlan();
        if(missionAdapter != null) missionAdapter.setListMission(wayMissionList);
        setWayDistance();
    }

    public void setBoolFolowDrone(boolean boolDrone){
        this.bool = boolDrone;
    }

    @Override
    protected void initView() {
        txt_h = (TextView) mActivity.findViewById(R.id.txt_h);
        img_hud = (AttitudeIndicator) mActivity.findViewById(R.id.img_hud);
        txt_d = (TextView) mActivity.findViewById(R.id.txt_d);
        txt_vs = (TextView) mActivity.findViewById(R.id.txt_vs);
        txt_hs = (TextView) mActivity.findViewById(R.id.txt_hs);
        txt_main_way_m = (TextView) mActivity.findViewById(R.id.txt_main_way_m);
        img_record_hs = (ImageView) mActivity.findViewById(R.id.img_record_hs);
        mai_recy_waypion = (RecyclerView) mActivity.findViewById(R.id.mai_recy_waypion);

        progressDialog = new ProgressDialog(mActivity,R.style.customDialog,R.layout.view_dialog);
        missionAdapter = new MissionAdapter(getContext());
        missionAdapter.setOnItemClickListener(this);
        popup = new DialogPopup(mActivity);
        fllowMeDialogPopup = new FllowMeDialogPopup(mActivity);
        hotPoDialogPopup = new HotPoDialogPopup(mActivity);
        popup.onCreatePopupView().setOnKeyListener(backListener);

        mai_recy_waypion.setAdapter(missionAdapter);
        mai_recy_waypion.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mai_recy_waypion.setLayoutManager(linearLayoutManager);
        mai_recy_waypion.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onItemClick(View view, MissionLatLon data) {
        missionItemWindow(data.getLatLong().getLatLng(),data.getMarker(),data.getNumber(),false,data.getMotion());
        MapUtil.moveTo(aMap,data.getLatLong().getLatLng(),true);
    }

    public void missionItemWindow(LatLng point, Marker marker,int number,boolean bools,String motion){
        this.markerpop = marker;
        this.boolpop = bools;
        if(amapLocationa != null) {

            popup.setData(point, number, new BtnClick() {
                @Override
                public void clickOk(String MissMotionss, int hightNumber, int rateNumber, int numbe_rreten,
                                    int numbe_rotate, boolean boolgroup, int raiuds) {
                    if (!MissMotionss.isEmpty()) {
                        MissionLatLon missionLatLon = new MissionLatLon(number,
                                new LonglatLog(point, amapLocationa.getAddress(),
                                        Time_Date.getDateTimeFile()), hightNumber, false, marker,
                                rateNumber, numbe_rreten, numbe_rotate, boolgroup, raiuds, MissMotionss);
                        if (missionLatLon != null) {
                            mBiMarkersMap.put(number, marker);
                            popup.popDismiss();
                            wayMissionList.add(missionLatLon);
                            latLnglist.add(point);
                            wayMapPlayout(getResources().getColor(R.color.aaa));
                            if (missionAdapter != null)
                                missionAdapter.setListMission(wayMissionList);
                            CalculationDistance();
                        }
                    }
                }

                @Override
                public void cleanWaypion(LatLng ll, int munway) {  //删除航点
                    List<MissionLatLon> listway = wayMissionList;
                    List<MissionLatLon> listMiss = new ArrayList<MissionLatLon>();
                    latLnglist.remove(ll);
                    mBiMarkersMap.removeKey(munway);
                    for (int i = 0; i < listway.size(); i++) {
                        MissionLatLon missionLat = listway.get(i);
                        if (missionLat.getLatLong().getLatLng() == ll) {
                            wayMissionList.get(i).getMarker().destroy();
                            wayMissionList.get(i).getMarker().remove();
                            wayMissionList.remove(i);
                        }

                    }
                    for (int ii = 0; ii < wayMissionList.size(); ii++) {
                        MissionLatLon missionway = wayMissionList.get(ii);
                        missionway.getMarker().remove();
                        missionway.setMarker(MapUtil.customMarker(aMap, mActivity, missionway.getLatLong().getLatLng(), ii + 1 + ""));
                        missionway.setNumber(ii + 1);
                        listMiss.add(missionway);
                    }
                    updatePolyLine();
                    wayMissionList.clear();
                    wayMissionList = listMiss;
                    wayMapPlayout(getResources().getColor(R.color.aaa));
                    if (missionAdapter != null) missionAdapter.setListMission(wayMissionList);
                    CalculationDistance();
                }

                @Override
                public void cancle() {
                    if (boolpop) {
                        marker.remove();
                    }
                }
            }, boolpop, motion);
            popup.showPopupWindow();
        }else{
            Utils.setResultToToast(mActivity,"定位数据获取中");
        }
    }

    private View.OnKeyListener backListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if(boolpop){
                    markerpop.remove();
                }
                return true;
            }
            return false;
        }
    };

    @Override
    public void missionProgressStatus(DJIMission.DJIMissionProgressStatus djiMissionProgressStatus) {
        if (djiMissionProgressStatus == null) return;
        if (djiMissionProgressStatus instanceof DJIWaypointMission.DJIWaypointMissionStatus) {

            for(MissionLatLon missionLatLon:wayMissionList){
                if(((DJIWaypointMission.DJIWaypointMissionStatus) djiMissionProgressStatus).getTargetWaypointIndex() == 0){
                    if(((DJIWaypointMission.DJIWaypointMissionStatus) djiMissionProgressStatus).isWaypointReached()){
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                missionAdapter.setMissionIndex(wayMissionList.size()-1);
                                missionAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
                /*if(missionLatLon.getNumber() == ((DJIWaypointMission.DJIWaypointMissionStatus) djiMissionProgressStatus).getTargetWaypointIndex()){
                    if(((DJIWaypointMission.DJIWaypointMissionStatus) djiMissionProgressStatus).isWaypointReached()){
                        int number = ((DJIWaypointMission.DJIWaypointMissionStatus) djiMissionProgressStatus).getTargetWaypointIndex()-1;
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                missionAdapter.setMissionIndex(number);
                                missionAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }*/
            }

            //途中修改飞行器速度
            DJIWaypointMission.setAutoFlightSpeed(10, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError error) {
                }
            });

            if (((DJIWaypointMission.DJIWaypointMissionStatus) djiMissionProgressStatus).getTargetWaypointIndex() == 2 && lastTargetWPIndex == 1) {
                DJIWaypointMission.setAutoFlightSpeed(10, new DJICommonCallbacks.DJICompletionCallback() {
                    @Override
                    public void onResult(DJIError error) {
                        Utils.setResultToToast(mActivity, "Set auto flight speed: " + (error == null ? "Success" : error.getDescription()));
                    }
                });
            }
            lastTargetWPIndex = ((DJIWaypointMission.DJIWaypointMissionStatus) djiMissionProgressStatus).getTargetWaypointIndex();
        }else if (djiMissionProgressStatus instanceof DJIFollowMeMission.DJIFollowMeMissionStatus ||
                        djiMissionProgressStatus instanceof DJIHotPointMission.DJIHotPointMissionStatus ||
                        djiMissionProgressStatus instanceof DJIPanoramaMission.DJIPanoramaMissionStatus ||
                        djiMissionProgressStatus instanceof DJICustomMission.DJICustomMissionProgressStatus
                ) {
            //Utils.addLineToSB(pushSB, null, "Received " + djiMissionProgressStatus.getClass().getSimpleName());
        }else {
            //DJIError err = djiMissionProgressStatus.getError();
            //Utils.addLineToSB(pushSB, "Previous mission result", err == null ? "Success" : err.getDescription());
        }
    }

    public void setWayDistance(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt_main_way_m.setText("航距:"+new BigDecimal(numberCalcu).
                        setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()+"M");
                missionAdapter.setMissionIndex(-1);
                missionAdapter.notifyDataSetChanged();
            }
        });
    }

    public void initMissionManager() {
        mMissionManager = DJIMissionManager.getInstance();
        if (mMissionManager != null) {
            mMissionManager.setMissionProgressStatusCallback(this);
        }

        if (DJISampleApplication.getProductInstance() instanceof DJIAircraft &&
                        !Utils.checkGpsCoordinate(peploeLocationLat, peploeLocationLng)) {
            final CountDownLatch cdl = new CountDownLatch(1);
            DJISampleApplication.getAircraftInstance().getFlightController().getHomeLocation(new DJICommonCallbacks.DJICompletionCallbackWith<DJILocationCoordinate2D>() {

                @Override
                public void onSuccess(DJILocationCoordinate2D t) {
                    peploeLocationLat = t.getLatitude();
                    peploeLocationLng = t.getLongitude();
                }

                @Override
                public void onFailure(DJIError error) {
                    cdl.countDown();
                }
            });
            try {
                cdl.await(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!Utils.checkGpsCoordinate(peploeLocationLat, peploeLocationLng)) {
                Utils.setResultToToast(mActivity, "家坐标没有设置…");
                return;
            }
        }
    }

    //上传航点  准备
    public void prepareWayPointMission(){
        initMissionManager();
        djiMission = null;
        djiMission = WaypointMission.getDroneWaypointTask(wayMissionList,peploeLocationLat,peploeLocationLng);
        startUpMission();
        //djiMission = WaypointMission.initMission(peploeLocationLat,peploeLocationLng);

        /*singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        Thread.sleep(1000);
                        mMissionManager.prepareMission(djiMission, new DJIMission.DJIMissionProgressHandler() {
                            @Override
                            public void onProgress(DJIMission.DJIProgressType type, float progress) {
                                if(progressDialog !=null) progressDialog.setShow();
                            }
                        }, new DJICommonCallbacks.DJICompletionCallback() {
                            @Override
                            public void onResult(DJIError error) {
                                if (error == null) {
                                    Utils.setResultToToast(mActivity, "上传成功，请开始任务！");
                                } else {
                                    Utils.setResultToToast(mActivity, "Prepare: " + error.getDescription());
                                }
                                if(progressDialog !=null)progressDialog.dismiss();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });*/
    }

    //上传跟随
    public void startFllowMeMission(){
        initMissionManager();
        fllowMeDialogPopup.setDataLat(droneLocationLat,droneLocationLng, new FllowMeDialogPopup.setFollowMeBtn() {
            @Override
            public void btnOk(int hightNumber) {
                djiMission = null;
                djiMission = WaypointMission.FollowInitMission(peploeLocationLat,peploeLocationLng,hightNumber);
                startUpMission();
                fllowMeDialogPopup.popuwindowdimmi();
            }

            @Override
            public void btnNo() {
                fllowMeDialogPopup.popuwindowdimmi();
            }
        });
        fllowMeDialogPopup.showPopupWindow();
    }

    //上传环绕
    public void startHotPotMission(){
        initMissionManager();
        hotPoDialogPopup.setDataLat(droneLocationLat,droneLocationLng, new HotPoDialogPopup.setFollowMeBtn() {
            @Override
            public void btnOk(int hightNumber, boolean boolgroup, int radius, int angularspeed) {
                djiMission = null;
                djiMission = WaypointMission.hotpoInitMission(peploeLocationLat,peploeLocationLng,
                        hightNumber,boolgroup,radius,angularspeed);
                startUpMission();
                hotPoDialogPopup.setPopuWindowDismiss();
            }

            @Override
            public void btnNo() {
                hotPoDialogPopup.setPopuWindowDismiss();
            }
        });
        hotPoDialogPopup.showPopupWindow();
    }

    private void startUpMission(){
        if (djiMission == null) {
            Utils.setResultToToast(mActivity, "请选择一个任务类型...");
        }
        if(mMissionManager == null){
            Utils.setResultToToast(mActivity,"任务管理器为空");
            return;
        }

        //开始上传航点信息
        if(progressDialog !=null) progressDialog.setShow();
        mMissionManager.prepareMission(djiMission, new DJIMission.DJIMissionProgressHandler() {
            @Override
            public void onProgress(DJIMission.DJIProgressType type, float progress) {

            }
        }, new DJICommonCallbacks.DJICompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                if (error == null) {
                    Utils.setResultToToast(mActivity, "上传成功，请开始任务！");
                } else {
                    Utils.setResultToToast(mActivity, "Prepare: " + error.getDescription());
                }
                if(progressDialog !=null)progressDialog.dismiss();
            }
        });
    }

    //开始任务
    public void startWaypointMission(){
        initMissionManager();
        if (djiMission != null) {
            if(mMissionManager == null){
                Utils.setResultToToast(mActivity,"MissionManager is null");
                return;
            }
            mMissionManager.setMissionExecutionFinishedCallback(new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError error) {
                    Utils.setResultToToast(mActivity, "Mission executing result: " + (error == null ? "Success" : error.getDescription()));
                    if (djiMission instanceof DJIPanoramaMission) {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ((DJIPanoramaMission) djiMission).getPanoramaMediaFile(new DJICommonCallbacks.DJICompletionCallbackWith<DJIMedia>() {
                            @Override
                            public void onSuccess(DJIMedia djiMedia) {
                                Utils.setResultToToast(mActivity, "Index: " + djiMedia.getId());
                            }

                            @Override
                            public void onFailure(DJIError djiError) {
                                Utils.setResultToToast(mActivity, "Get panorama media file failed!");
                            }
                        });
                    }
                }
            });
        }

        //For the panorama mission, there will be no callback in some cases, we will fix it in next version.
        mMissionManager.startMissionExecution(new DJICommonCallbacks.DJICompletionCallback() {
            @Override
            public void onResult(DJIError mError) {
                if (mError == null) {
                    if (djiMission instanceof DJIFollowMeMission && mUpdateSimLocateThread == null) {
                        mUpdateSimLocateThread = new UpdateFollowmeSimLocationThread(djiMission,
                                peploeLocationLat,peploeLocationLng);
                        mUpdateSimLocateThread.start();
                    }
                } else {
                    Utils.setResultToToast(mActivity, "Start: " + mError.getDescription());
                }
            }
        });
    }

    //结束任务
    public void clearMapMinssion(){
        initMissionManager();
        if(mMissionManager == null){
            Utils.setResultToToast(mActivity,"MissionManager is null");
            return;
        }
        mMissionManager.stopMissionExecution(new DJICommonCallbacks.DJICompletionCallback() {
            @Override
            public void onResult(DJIError mError) {
                if (mError == null) {
                    Utils.setResultToToast(mActivity, "Success!");
                    if (mUpdateSimLocateThread != null) {
                        mUpdateSimLocateThread.stopRunning();
                        mUpdateSimLocateThread = null;
                    }
                } else {
                    Utils.setResultToToast(mActivity, "Stop: " + mError.getDescription());
                }
            }
        });
    }

    //暂停任务
    public void droneMissionPause(){
        initMissionManager();
        if(mMissionManager == null){
            Utils.setResultToToast(mActivity,"MissionManager is null");
            return;
        }
        mMissionManager.pauseMissionExecution(new DJICommonCallbacks.DJICompletionCallback() {
            @Override
            public void onResult(DJIError mError) {
                if (mError == null) {
                    Utils.setResultToToast(mActivity, "Success!");
                    if (mUpdateSimLocateThread != null) {
                        mUpdateSimLocateThread.setIsPause(true);
                    }
                } else {
                    Utils.setResultToToast(mActivity, "Pause: " + mError.getDescription());
                }
            }
        });
    }

    //继续任务
    public void droneMissionResune(){
        initMissionManager();
        if(mMissionManager == null){
            Utils.setResultToToast(mActivity,"MissionManager is null");
            return;
        }
        mMissionManager.resumeMissionExecution(new DJICommonCallbacks.DJICompletionCallback() {

            @Override
            public void onResult(DJIError mError) {
                if (mError == null) {
                    Utils.setResultToToast(mActivity, "Success!");
                    if (mUpdateSimLocateThread != null) {
                        mUpdateSimLocateThread.setIsPause(false);
                    }
                } else {
                    Utils.setResultToToast(mActivity, "Resume" + mError.getDescription());
                }
            }
        });
    }

    public void setCompassDroneOmage(RotateImageView compassDroneOmage, HandFragment handFragments) {
        this.compassDroneOmage = compassDroneOmage;
        this.handFragment = handFragments;
    }
}
