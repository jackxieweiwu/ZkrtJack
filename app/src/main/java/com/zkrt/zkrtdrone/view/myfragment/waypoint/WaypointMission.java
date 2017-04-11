package com.zkrt.zkrtdrone.view.myfragment.waypoint;


import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.base.Utils;

import java.util.LinkedList;
import java.util.List;

import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.missionmanager.DJICustomMission;
import dji.sdk.missionmanager.DJIFollowMeMission;
import dji.sdk.missionmanager.DJIHotPointMission;
import dji.sdk.missionmanager.DJIMission;
import dji.sdk.missionmanager.DJIPanoramaMission;
import dji.sdk.missionmanager.DJIWaypoint;
import dji.sdk.missionmanager.DJIWaypointMission;
import dji.sdk.missionmanager.missionstep.DJIGoHomeStep;
import dji.sdk.missionmanager.missionstep.DJIGoToStep;
import dji.sdk.missionmanager.missionstep.DJIHotpointStep;
import dji.sdk.missionmanager.missionstep.DJIMissionStep;
import dji.sdk.missionmanager.missionstep.DJIShootPhotoStep;
import dji.sdk.missionmanager.missionstep.DJIStartRecordVideoStep;
import dji.sdk.missionmanager.missionstep.DJIStopRecordVideoStep;
import dji.sdk.missionmanager.missionstep.DJITakeoffStep;
import dji.sdk.missionmanager.missionstep.DJIWaypointStep;
import zkrtdrone.zkrt.com.maplib.bean.Gps;
import zkrtdrone.zkrt.com.maplib.bean.MissionLatLon;
import zkrtdrone.zkrt.com.maplib.until.Converter;

/**
 * Created by jack_xie on 16-12-28.
 */

public class WaypointMission {
    private static double droneLat;
    private static double droneLng;

    public static double getGpsDroneLat(){
        return droneLat;
    }

    public static double getGpsDroneLng(){
        return droneLng;
    }

    //将任务航点的转化成无人机的航点任务
    public static DJIMission getDroneWaypointTask(List<MissionLatLon> list,double peploeLocationLat,double peploeLocationLng){
        if (!Utils.checkGpsCoordinate(peploeLocationLat, peploeLocationLng)) {
            Utils.setResultToToast(DJISampleApplication.activity, "No home point!!!");
            return null;
        }
        DJIWaypointMission waypointMission = new DJIWaypointMission();
        waypointMission.maxFlightSpeed = 15;
        waypointMission.autoFlightSpeed = 10;
        waypointMission.needExitMissionOnRCSignalLost = false;  //确定在RC信号丢失时是否退出任务。
        waypointMission.needRotateGimbalPitch = true;  //确定在执行航点任务时飞机是否可以旋转万向节。
        //waypointMission.repeatNum = 0;  //地面站任务将重复多少次，这意味着如果mRepeatNum为1，飞机将在完成任务后返回第一个航点，并再次重复任务。
        //waypointMission.goFirstWaypointMode = DJIWaypointMission.DJIWaypointMissionGotoWaypointMode.PointToPoint;  //Safely

        List<DJIWaypoint> waypointsList = new LinkedList<>();
        for(MissionLatLon missionLatLon : list){
            Gps gps = Converter.gcj_To_Gps84(missionLatLon.getLatLong().getLatLng());
            DJIWaypoint northPoint = new DJIWaypoint(gps.getWgLat(), gps.getWgLon(), (float) missionLatLon.getDistance());
            //northPoint.actionRepeatTimes = 1;   //说明重复路点动作集的次数。默认值为一次。
            //northPoint.actionTimeoutInSeconds = 60; //设置为为航路点排除所有航路点行动的最大时间。如果执行航路点操作的时间超过时间设置，飞机将停止执行当前航点的航点操作，并将移动到下一个航点。此属性的值必须在[0，999]秒的范围内。默认值为60秒。
            northPoint.altitude = (float) missionLatLon.getDistance();  //到达航点时飞机的高度（米）。
            northPoint.speed = missionLatLon.getRateNumber(); //speed
            northPoint.turnMode = DJIWaypoint.DJIWaypointTurnMode.Clockwise;

            if(missionLatLon.getMotion().equals("无")){
                northPoint.addAction(new DJIWaypoint.DJIWaypointAction(DJIWaypoint.DJIWaypointActionType.Stay,0));
            }else
            if(missionLatLon.getMotion().equals("开始拍照")){
                northPoint.addAction(new DJIWaypoint.DJIWaypointAction(DJIWaypoint.DJIWaypointActionType.StartRecord, 0));
            }else
            if(missionLatLon.getMotion().equals("开始录像")){
                northPoint.addAction(new DJIWaypoint.DJIWaypointAction(DJIWaypoint.DJIWaypointActionType.StartRecord,0));
            }else
            if(missionLatLon.getMotion().equals("停止录像")){
                northPoint.addAction(new DJIWaypoint.DJIWaypointAction(DJIWaypoint.DJIWaypointActionType.StopRecord,0));
            }else
            if(missionLatLon.getMotion().equals("滞留")){
                northPoint.addAction(new DJIWaypoint.DJIWaypointAction(DJIWaypoint.DJIWaypointActionType.Stay,missionLatLon.getRetentionNumber()));
            }else
            if(missionLatLon.getMotion().equals("旋转")){
                northPoint.addAction(new DJIWaypoint.DJIWaypointAction(DJIWaypoint.DJIWaypointActionType.RotateAircraft,missionLatLon.getRotateNumber()));
            }else
            if(missionLatLon.getMotion().equals("云台角度")){
                northPoint.addAction(new DJIWaypoint.DJIWaypointAction(DJIWaypoint.DJIWaypointActionType.GimbalPitch,0));
            }
            waypointsList.add(northPoint);
        }
        waypointMission.finishedAction = DJIWaypointMission.DJIWaypointMissionFinishedAction.GoHome;
        waypointMission.addWaypoints(waypointsList);
        return waypointMission;
    }

    //跟随航点任务
    public static DJIMission FollowInitMission(double peploeLocationLat,double peploeLocationLng,int hight) {
        if (!Utils.checkGpsCoordinate(peploeLocationLat, peploeLocationLng)) {
            Utils.setResultToToast(DJISampleApplication.activity, "No home point!!!");
            return null;
        }
        DJIFollowMeMission djiFollowMeMission = new DJIFollowMeMission(peploeLocationLat, peploeLocationLng);
        djiFollowMeMission.followmeAltitude = hight;
        DJIFollowMeMission.updateFollowMeCoordinate(peploeLocationLat, peploeLocationLng, new DJICommonCallbacks.DJICompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {

            }
        });
        return djiFollowMeMission;
    }

    //环绕热点
    public static DJIMission hotpoInitMission(double peploeLocationLat, double peploeLocationLng, int hightNumber, boolean boolgroup, int radius, int angularspeed) {
        if (!Utils.checkGpsCoordinate( peploeLocationLat, peploeLocationLng)) {
            Utils.setResultToToast(DJISampleApplication.activity, "No home point!!!");
            return null;
        }
        DJIHotPointMission hotPointMission = new DJIHotPointMission(peploeLocationLat, peploeLocationLng);
        hotPointMission.radius = radius;
        hotPointMission.isClockwise = true;
        hotPointMission.isClockwise = boolgroup;
        /*hotPointMission.startPoint = DJIHotPointMission.DJIHotPointStartPoint.Nearest;*/
        hotPointMission.altitude = hightNumber;
        hotPointMission.angularVelocity = angularspeed;
        return hotPointMission;
    }

    //360度全景模式
    protected DJIMission FullCircleinitMission() {
        return new DJIPanoramaMission(DJIPanoramaMission.DJIPanoramaMode.FullCircle);
    }

    //180度全景模式。
    protected DJIMission HalfCircleinitMission() {
        return new DJIPanoramaMission(DJIPanoramaMission.DJIPanoramaMode.HalfCircle);
    }
}
