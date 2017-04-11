package com.zkrt.zkrtdrone.bean;

import com.amap.api.maps.model.LatLng;

/**
 * Created by jack_xie on 17-2-27.
 */

public class DroneGps {
    private LatLng latLng;
    private double altt;

    public DroneGps() {
    }

    public DroneGps(LatLng latLng, double altt) {
        this.latLng = latLng;
        this.altt = altt;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public double getAltt() {
        return altt;
    }

    public void setAltt(double altt) {
        this.altt = altt;
    }
}
