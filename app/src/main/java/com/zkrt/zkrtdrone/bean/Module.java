package com.zkrt.zkrtdrone.bean;

/**
 * Created by admin on 2016/10/25.
 */

public class Module {
    private int DEVICE_TYPE_TEMPERATURE;  //温度模块
    private int DEVICE_TYPE_OBSTACLE;  //壁障模块
    private int DEVICE_TYPE_CONTROL;  //备用
    private int DEVICE_TYPE_CAMERA;  //相机模块
    private int DEVICE_TYPE_GAS;  //毒气模块
    private int DEVICE_TYPE_THROW;  //抛投模块
    private int DEVICE_TYPE_STANDBY;  //备用
    private int DEVICE_TYPE_PARACHUTE;  //降落伞
    private int DEVICE_TYPE_IRRADIATE;  //照射模块
    private int DEVICE_TYPE_MEGAPHONE;  //喊话模块
    private int DEVICE_TYPE_BATTERY;  //电池模块
    private int DEVICE_TYPE_3DMODELING;  //电池模块

    public int getDEVICE_TYPE_3DMODELING() {
        return DEVICE_TYPE_3DMODELING;
    }

    public void setDEVICE_TYPE_3DMODELING(int DEVICE_TYPE_3DMODELING) {
        this.DEVICE_TYPE_3DMODELING = DEVICE_TYPE_3DMODELING;
    }

    public Module() {

    }

    public Module(int DEVICE_TYPE_TEMPERATURE, int DEVICE_TYPE_OBSTACLE, int DEVICE_TYPE_CONTROL,
                  int DEVICE_TYPE_CAMERA, int DEVICE_TYPE_GAS, int DEVICE_TYPE_THROW,
                  int DEVICE_TYPE_STANDBY, int DEVICE_TYPE_PARACHUTE, int DEVICE_TYPE_IRRADIATE,
                  int DEVICE_TYPE_MEGAPHONE, int DEVICE_TYPE_BATTERY,int DEVICE_TYPE_3DMODELING) {
        this.DEVICE_TYPE_TEMPERATURE = DEVICE_TYPE_TEMPERATURE;
        this.DEVICE_TYPE_OBSTACLE = DEVICE_TYPE_OBSTACLE;
        this.DEVICE_TYPE_CONTROL = DEVICE_TYPE_CONTROL;
        this.DEVICE_TYPE_CAMERA = DEVICE_TYPE_CAMERA;
        this.DEVICE_TYPE_GAS = DEVICE_TYPE_GAS;
        this.DEVICE_TYPE_THROW = DEVICE_TYPE_THROW;
        this.DEVICE_TYPE_STANDBY = DEVICE_TYPE_STANDBY;
        this.DEVICE_TYPE_PARACHUTE = DEVICE_TYPE_PARACHUTE;
        this.DEVICE_TYPE_IRRADIATE = DEVICE_TYPE_IRRADIATE;
        this.DEVICE_TYPE_MEGAPHONE = DEVICE_TYPE_MEGAPHONE;
        this.DEVICE_TYPE_BATTERY = DEVICE_TYPE_BATTERY;
        this.DEVICE_TYPE_3DMODELING = DEVICE_TYPE_3DMODELING;
    }

    public int getDEVICE_TYPE_TEMPERATURE() {
        return DEVICE_TYPE_TEMPERATURE;
    }

    public void setDEVICE_TYPE_TEMPERATURE(int DEVICE_TYPE_TEMPERATURE) {
        this.DEVICE_TYPE_TEMPERATURE = DEVICE_TYPE_TEMPERATURE;
    }

    public int getDEVICE_TYPE_OBSTACLE() {
        return DEVICE_TYPE_OBSTACLE;
    }

    public void setDEVICE_TYPE_OBSTACLE(int DEVICE_TYPE_OBSTACLE) {
        this.DEVICE_TYPE_OBSTACLE = DEVICE_TYPE_OBSTACLE;
    }

    public int getDEVICE_TYPE_CONTROL() {
        return DEVICE_TYPE_CONTROL;
    }

    public void setDEVICE_TYPE_CONTROL(int DEVICE_TYPE_CONTROL) {
        this.DEVICE_TYPE_CONTROL = DEVICE_TYPE_CONTROL;
    }

    public int getDEVICE_TYPE_CAMERA() {
        return DEVICE_TYPE_CAMERA;
    }

    public void setDEVICE_TYPE_CAMERA(int DEVICE_TYPE_CAMERA) {
        this.DEVICE_TYPE_CAMERA = DEVICE_TYPE_CAMERA;
    }

    public int getDEVICE_TYPE_GAS() {
        return DEVICE_TYPE_GAS;
    }

    public void setDEVICE_TYPE_GAS(int DEVICE_TYPE_GAS) {
        this.DEVICE_TYPE_GAS = DEVICE_TYPE_GAS;
    }

    public int getDEVICE_TYPE_THROW() {
        return DEVICE_TYPE_THROW;
    }

    public void setDEVICE_TYPE_THROW(int DEVICE_TYPE_THROW) {
        this.DEVICE_TYPE_THROW = DEVICE_TYPE_THROW;
    }

    public int getDEVICE_TYPE_STANDBY() {
        return DEVICE_TYPE_STANDBY;
    }

    public void setDEVICE_TYPE_STANDBY(int DEVICE_TYPE_STANDBY) {
        this.DEVICE_TYPE_STANDBY = DEVICE_TYPE_STANDBY;
    }

    public int getDEVICE_TYPE_PARACHUTE() {
        return DEVICE_TYPE_PARACHUTE;
    }

    public void setDEVICE_TYPE_PARACHUTE(int DEVICE_TYPE_PARACHUTE) {
        this.DEVICE_TYPE_PARACHUTE = DEVICE_TYPE_PARACHUTE;
    }

    public int getDEVICE_TYPE_IRRADIATE() {
        return DEVICE_TYPE_IRRADIATE;
    }

    public void setDEVICE_TYPE_IRRADIATE(int DEVICE_TYPE_IRRADIATE) {
        this.DEVICE_TYPE_IRRADIATE = DEVICE_TYPE_IRRADIATE;
    }

    public int getDEVICE_TYPE_MEGAPHONE() {
        return DEVICE_TYPE_MEGAPHONE;
    }

    public void setDEVICE_TYPE_MEGAPHONE(int DEVICE_TYPE_MEGAPHONE) {
        this.DEVICE_TYPE_MEGAPHONE = DEVICE_TYPE_MEGAPHONE;
    }

    public int getDEVICE_TYPE_BATTERY() {
        return DEVICE_TYPE_BATTERY;
    }

    public void setDEVICE_TYPE_BATTERY(int DEVICE_TYPE_BATTERY) {
        this.DEVICE_TYPE_BATTERY = DEVICE_TYPE_BATTERY;
    }
}
