package com.zkrt.zkrtdrone.bean;

/**
 * Created by jack_xie on 17-3-26.
 */

public class DeviceCallBackTwo {
    private String obstacleDownBarrier;  //  下方向障碍物距离 cm
    private String obstacleFrontBarrier;   //前方向障碍物距离
    private String obstacleRightBarrier;   //右方向障碍物距离
    private String obstacleQueenBarrier;   //后方向障碍物距离
    private String obstacleLeftBarrier;   //左方向障碍物距离
    private String obstacleEnabled;   //避障使能状态
    private String obstacleDistance;   //避障生效距离
    private String obstacleSpeed;   //避障速度
    private String tripodStatus;   //脚架状态
    private String tripodDistance;   //脚架自动收放功能使能状态
    private String tripodAngle;   //脚架当前角度值
    private String tripodLangAngle;   //脚架自动降落角度的设置值

    public String getObstacleSpeed() {
        return obstacleSpeed;
    }

    public void setObstacleSpeed(String obstacleSpeed) {
        this.obstacleSpeed = obstacleSpeed;
    }

    private String tripodFewerAngle;   //脚架自动收起角度的设置值
    private String reserve;   //备用

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getObstacleQueenBarrier() {
        return obstacleQueenBarrier;
    }

    public void setObstacleQueenBarrier(String obstacleQueenBarrier) {
        this.obstacleQueenBarrier = obstacleQueenBarrier;
    }

    public DeviceCallBackTwo() {
    }

    public String getObstacleDownBarrier() {
        return obstacleDownBarrier;
    }

    public void setObstacleDownBarrier(String obstacleDownBarrier) {
        this.obstacleDownBarrier = obstacleDownBarrier;
    }

    public String getObstacleFrontBarrier() {
        return obstacleFrontBarrier;
    }

    public void setObstacleFrontBarrier(String obstacleFrontBarrier) {
        this.obstacleFrontBarrier = obstacleFrontBarrier;
    }

    public String getObstacleRightBarrier() {
        return obstacleRightBarrier;
    }

    public void setObstacleRightBarrier(String obstacleRightBarrier) {
        this.obstacleRightBarrier = obstacleRightBarrier;
    }

    public String getObstacleLeftBarrier() {
        return obstacleLeftBarrier;
    }

    public void setObstacleLeftBarrier(String obstacleLeftBarrier) {
        this.obstacleLeftBarrier = obstacleLeftBarrier;
    }

    public String getObstacleEnabled() {
        return obstacleEnabled;
    }

    public void setObstacleEnabled(String obstacleEnabled) {
        this.obstacleEnabled = obstacleEnabled;
    }

    public String getObstacleDistance() {
        return obstacleDistance;
    }

    public void setObstacleDistance(String obstacleDistance) {
        this.obstacleDistance = obstacleDistance;
    }

    public String getTripodStatus() {
        return tripodStatus;
    }

    public void setTripodStatus(String tripodStatus) {
        this.tripodStatus = tripodStatus;
    }

    public String getTripodDistance() {
        return tripodDistance;
    }

    public void setTripodDistance(String tripodDistance) {
        this.tripodDistance = tripodDistance;
    }

    public String getTripodAngle() {
        return tripodAngle;
    }

    public void setTripodAngle(String tripodAngle) {
        this.tripodAngle = tripodAngle;
    }

    public String getTripodLangAngle() {
        return tripodLangAngle;
    }

    public void setTripodLangAngle(String tripodLangAngle) {
        this.tripodLangAngle = tripodLangAngle;
    }

    public String getTripodFewerAngle() {
        return tripodFewerAngle;
    }

    public void setTripodFewerAngle(String tripodFewerAngle) {
        this.tripodFewerAngle = tripodFewerAngle;
    }
}
