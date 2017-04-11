package com.zkrt.zkrtdrone.until;


import com.zkrt.zkrtdrone.base.Utils;

import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.missionmanager.DJIFollowMeMission;
import dji.sdk.missionmanager.DJIMission;

/**
 * Created by jack_xie on 2016/11/30.
 */

public class UpdateFollowmeSimLocationThread extends Thread {
    private DJIMission mDJIMission = null;
    private double mHomeLatitude = 181;
    private double mHomeLongitude = 181;

    private boolean mIsRunning = false;
    private boolean mIsPausing = false;

    float clock = 0;
    float radius = 6378137;
    private double tarPosLat;
    private double tarPosLon;
    private double tgtPosX;
    private double tgtPosY;

    public UpdateFollowmeSimLocationThread(DJIMission mission, double homeLat, double homeLng) {
        super();
        mDJIMission = mission;
        this.mHomeLatitude = homeLat;
        this.mHomeLongitude = homeLng;
    }

    @Override
    public void run() {
        if (
                mDJIMission == null ||
                        mDJIMission.getMissionType() != DJIMission.DJIMissionType.Followme ||
                        !Utils.checkGpsCoordinate(mHomeLatitude, mHomeLongitude)
                )
            return;
        mIsRunning = true;


        while (mIsRunning && clock < 1800) {
            tgtPosX = 5 * Math.sin(clock / 10 * 0.5);
            tgtPosY = 5 * Math.cos(clock / 10 * 0.5);
            tarPosLat = Utils.Radian(mHomeLatitude) + tgtPosX / radius;
            tarPosLon = Utils.Radian(mHomeLongitude) + tgtPosY  / radius / Math.cos(Utils.Radian(mHomeLatitude));
            DJIFollowMeMission.updateFollowMeCoordinate(
                    Utils.Degree(tarPosLat), Utils.Degree(tarPosLon), new DJICommonCallbacks.DJICompletionCallback() {

                        @Override
                        public void onResult(DJIError error) {
                        }
                    });
            if (!mIsPausing)
                clock ++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRunning() {
        mIsRunning = false;
    }

    public void setIsPause(boolean isPaused) {
        mIsPausing = isPaused;
    }
}
