package com.zkrt.zkrtdrone.base;


import com.amap.api.maps.model.LatLng;


/**
 * Created by jack_xie on 16-12-28.
 */

public interface BtnClick {
    public void clickOk(String MissMotion, int hightNumber, int rateNumber,int numbe_rreten,int numbe_rotate,boolean boolgroup,int raiuds);
    public void cleanWaypion(LatLng latLng,int Num);
    public void cancle();
}
