package com.zkrt.zkrtdrone.bean;

/**
 * Created by jack_xie on 2016/10/25.
 * 检测毒气模块的单独探头是否是正常的
 */

public class Mephitis {
    private int DEVICE_TYPE_GAS_CO;
    private int DEVICE_TYPE_GAS_H2S;
    private int DEVICE_TYPE_GAS_NH3;
    private int DEVICE_TYPE_GAS_CO2;

    public Mephitis() {
    }

    public Mephitis(int DEVICE_TYPE_GAS_CO, int DEVICE_TYPE_GAS_H2S, int DEVICE_TYPE_GAS_NH3, int DEVICE_TYPE_GAS_CO2) {
        this.DEVICE_TYPE_GAS_CO = DEVICE_TYPE_GAS_CO;
        this.DEVICE_TYPE_GAS_H2S = DEVICE_TYPE_GAS_H2S;
        this.DEVICE_TYPE_GAS_NH3 = DEVICE_TYPE_GAS_NH3;
        this.DEVICE_TYPE_GAS_CO2 = DEVICE_TYPE_GAS_CO2;
    }

    public int getDEVICE_TYPE_GAS_CO() {
        return DEVICE_TYPE_GAS_CO;
    }

    public void setDEVICE_TYPE_GAS_CO(int DEVICE_TYPE_GAS_CO) {
        this.DEVICE_TYPE_GAS_CO = DEVICE_TYPE_GAS_CO;
    }

    public int getDEVICE_TYPE_GAS_H2S() {
        return DEVICE_TYPE_GAS_H2S;
    }

    public void setDEVICE_TYPE_GAS_H2S(int DEVICE_TYPE_GAS_H2S) {
        this.DEVICE_TYPE_GAS_H2S = DEVICE_TYPE_GAS_H2S;
    }

    public int getDEVICE_TYPE_GAS_NH3() {
        return DEVICE_TYPE_GAS_NH3;
    }

    public void setDEVICE_TYPE_GAS_NH3(int DEVICE_TYPE_GAS_NH3) {
        this.DEVICE_TYPE_GAS_NH3 = DEVICE_TYPE_GAS_NH3;
    }

    public int getDEVICE_TYPE_GAS_CO2() {
        return DEVICE_TYPE_GAS_CO2;
    }

    public void setDEVICE_TYPE_GAS_CO2(int DEVICE_TYPE_GAS_CO2) {
        this.DEVICE_TYPE_GAS_CO2 = DEVICE_TYPE_GAS_CO2;
    }
}
