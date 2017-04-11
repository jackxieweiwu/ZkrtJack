package com.zkrt.zkrtdrone.bean;

/**
 * Created by root on 17-3-5.
 */

public class ComAssis {
    private int upOrDow;
    private int leftOrRight;

    private int oneBtn;
    private int twoBtn;
    private int threeBtn;
    private int fourBtn;
    private int fiveBtn;
    private int sixBtn;
    private int sevenBtn;
    private int nightBtn;
    private int zoom;



    public ComAssis(int upOrDow, int leftOrRight, int oneBtn, int twoBtn, int threeBtn, int fourBtn, int fiveBtn, int sixBtn, int sevenBtn, int nightBtn, int zoom) {

        this.upOrDow = upOrDow;
        this.leftOrRight = leftOrRight;
        this.oneBtn = oneBtn;
        this.twoBtn = twoBtn;
        this.threeBtn = threeBtn;
        this.fourBtn = fourBtn;
        this.fiveBtn = fiveBtn;
        this.sixBtn = sixBtn;
        this.sevenBtn = sevenBtn;
        this.nightBtn = nightBtn;
        this.zoom = zoom;
    }

    public int getUpOrDow() {
        return upOrDow;
    }

    public void setUpOrDow(int upOrDow) {
        this.upOrDow = upOrDow;
    }

    public int getLeftOrRight() {
        return leftOrRight;
    }

    public void setLeftOrRight(int leftOrRight) {
        this.leftOrRight = leftOrRight;
    }

    public int getOneBtn() {
        return oneBtn;
    }

    public void setOneBtn(int oneBtn) {
        this.oneBtn = oneBtn;
    }

    public int getTwoBtn() {
        return twoBtn;
    }

    public void setTwoBtn(int twoBtn) {
        this.twoBtn = twoBtn;
    }

    public int getThreeBtn() {
        return threeBtn;
    }

    public void setThreeBtn(int threeBtn) {
        this.threeBtn = threeBtn;
    }

    public int getFourBtn() {
        return fourBtn;
    }

    public void setFourBtn(int fourBtn) {
        this.fourBtn = fourBtn;
    }

    public int getFiveBtn() {
        return fiveBtn;
    }

    public void setFiveBtn(int fiveBtn) {
        this.fiveBtn = fiveBtn;
    }

    public int getSixBtn() {
        return sixBtn;
    }

    public void setSixBtn(int sixBtn) {
        this.sixBtn = sixBtn;
    }

    public int getSevenBtn() {
        return sevenBtn;
    }

    public void setSevenBtn(int sevenBtn) {
        this.sevenBtn = sevenBtn;
    }

    public int getNightBtn() {
        return nightBtn;
    }

    public void setNightBtn(int nightBtn) {
        this.nightBtn = nightBtn;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }
}
