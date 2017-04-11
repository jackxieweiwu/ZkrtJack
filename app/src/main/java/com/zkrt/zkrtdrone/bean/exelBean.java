package com.zkrt.zkrtdrone.bean;


import org.litepal.crud.DataSupport;


/**
 * Created by jack_xie on 17-3-29.
 */
public class exelBean extends DataSupport {
    private double lat;

    private double log;

    private int GasValueOne;

    private int GasValueTwo;

    private int GasValueThree;

    private int GasValueFour;

    private String time;

    private String time2;

    public exelBean(double lat, double log, int gasValueOne, int gasValueTwo, int gasValueThree, int gasValueFour, String time, String time2) {
        this.lat = lat;
        this.log = log;
        GasValueOne = gasValueOne;
        GasValueTwo = gasValueTwo;
        GasValueThree = gasValueThree;
        GasValueFour = gasValueFour;
        this.time = time;
        this.time2 = time2;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public exelBean() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }

    public int getGasValueOne() {
        return GasValueOne;
    }

    public void setGasValueOne(int gasValueOne) {
        GasValueOne = gasValueOne;
    }

    public int getGasValueTwo() {
        return GasValueTwo;
    }

    public void setGasValueTwo(int gasValueTwo) {
        GasValueTwo = gasValueTwo;
    }

    public int getGasValueThree() {
        return GasValueThree;
    }

    public void setGasValueThree(int gasValueThree) {
        GasValueThree = gasValueThree;
    }

    public int getGasValueFour() {
        return GasValueFour;
    }

    public void setGasValueFour(int gasValueFour) {
        GasValueFour = gasValueFour;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
