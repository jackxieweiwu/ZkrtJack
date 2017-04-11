package zkrtdrone.zkrt.com.maplib.bean;

import com.amap.api.maps.model.Marker;

/**
 * Created by admin on 2016/11/18.
 */

public class MissionLatLon {
    private int number;
    private LonglatLog latLong;
    private int distance;  //gaodu
    private boolean bool;
    private Marker marker;
    private int rateNumber;  //sudo

    private int retentionNumber;//滞留
    private int rotateNumber;  //旋转
    private int numberRaiuds;  //Radius
    private boolean boolgroup;  //顺时针

    public int getNumberRaiuds() {

        return numberRaiuds;
    }

    public void setNumberRaiuds(int numberRaiuds) {
        this.numberRaiuds = numberRaiuds;
    }

    public MissionLatLon(int number, LonglatLog latLong, int distance, boolean bool, Marker marker, int rateNumber, int retentionNumber,
                         int rotateNumber, boolean boolgroup, int raiuds, String motion) {
        this.number = number;
        this.latLong = latLong;
        this.distance = distance;
        this.bool = bool;
        this.marker = marker;
        this.rateNumber = rateNumber;
        this.retentionNumber = retentionNumber;
        this.rotateNumber = rotateNumber;
        this.boolgroup = boolgroup;
        this.numberRaiuds = raiuds;
        this.motion = motion;

    }

    public int getRetentionNumber() {
        return retentionNumber;
    }

    public void setRetentionNumber(int retentionNumber) {
        this.retentionNumber = retentionNumber;
    }

    public int getRotateNumber() {
        return rotateNumber;
    }

    public void setRotateNumber(int rotateNumber) {
        this.rotateNumber = rotateNumber;
    }

    public boolean isBoolgroup() {
        return boolgroup;
    }

    public void setBoolgroup(boolean boolgroup) {
        this.boolgroup = boolgroup;
    }

    public int getRateNumber() {
        return rateNumber;
    }

    public void setRateNumber(int rateNumber) {
        this.rateNumber = rateNumber;
    }


    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }



    public String getMotion() {
        return motion;
    }

    public void setMotion(String motion) {
        this.motion = motion;
    }

    private String motion;//动作

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }



    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public LonglatLog getLatLong() {
        return latLong;
    }

    public void setLatLong(LonglatLog latLong) {
        this.latLong = latLong;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
