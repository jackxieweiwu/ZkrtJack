package com.zkrt.zkrtdrone.view.myfragment.mount;

import android.content.res.TypedArray;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.bean.DeviceCallback;
import com.zkrt.zkrtdrone.bean.Mephitis;
import com.zkrt.zkrtdrone.bean.Module;
import com.zkrt.zkrtdrone.bean.MoudleBean;
import com.zkrt.zkrtdrone.bean.exelBean;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;
import com.zkrt.zkrtdrone.until.HexToBinary;
import com.zkrt.zkrtdrone.view.dialog.CustomDialog;

import java.util.List;

import dji.sdk.flightcontroller.DJIFlightControllerDelegate;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.rxbean.CommonRxTask;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.rxbean.UITask;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.rxutil.RxjavaUtil;

import static com.zkrt.zkrtdrone.DJISampleApplication.deviceCallBackTwo;
import static com.zkrt.zkrtdrone.until.TimeUtil.getTimeDate;
import static com.zkrt.zkrtdrone.until.TimeUtil.getTimeDate2;
import static zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseApplication.activity;

/**
 * Created by jack_xie on 17-2-21.
 */

public class MountPresenter extends MountContract.Presenter{
    private CustomDialog dialog;
    private String[] moudleName;
    private TypedArray moudleImg;
    private int dialogNumber = 0;
    private DeviceCallback deviceCallback;
    private Module module;
    private Mephitis mephitis;
    private String[] as;
    private List<MoudleBean> list;

    @Override
    public void onAttached() {
        getshowDJIFlightControllerDelegate();
    }

    @Override
    public void getshowDJIFlightControllerDelegate() {
        moudleName = mModel.getMoudleName();
        moudleImg = mModel.getMoudleImg();
        dialog = mModel.getDialog();
        //mModel.getModelCameraAndOne(dialog);
        mView.showDialog(dialog);

        if(!DJIModuleVerificationUtil.isFlightControllerAvailable()) return;
        DJISampleApplication.getAircraftInstance().getFlightController().setReceiveExternalDeviceDataCallback(
                new DJIFlightControllerDelegate.FlightControllerReceivedDataFromExternalDeviceCallback() {
                    @Override
                    public void onResult(byte[] bytes) {
                        mView.showSharedPreferences(mModel.getSharePreferences());
                        //mView.showMoudleNameImg(moudleName,moudleImg);
                        RxjavaUtil.executeRxTask(new CommonRxTask<Object>() {
                            @Override
                            public void doInIOThread() {
                                deviceCallback = HexToBinary.analysisByte(HexToBinary.bytesToHexString(bytes));
                                /*deviceCallback.setDroneGps(new DroneGps(new LatLng(DJISampleApplication.Lat,DJISampleApplication.log),
                                        DJISampleApplication.HH));*/

                                as= deviceCallback.getUavID().split(" ");
                                //获取飞行器的一些信息
                                if(deviceCallback !=null) {
                                    if(as[3].equals("65")) {
                                        //FileClass.saveFile(FileClass.gsonData(deviceCallback));
                                        module = HexToBinary.HexOneMode(
                                                HexToBinary.Hex2Binary(deviceCallback.getDeviceCallBackData().getDeviceStatus().replace(" ", "")));
                                        String gasStatus = deviceCallback.getDeviceCallBackData().getGas_Status();  //气体状态
                                        mephitis = HexToBinary.getDuqi(HexToBinary.Hex2Binary(gasStatus.replace(" ", "")));
                                    }else if(as[3].equals("66")){
                                        deviceCallBackTwo = deviceCallback.getDeviceCallBackTwo();
                                    }
                                }
                                list = HexToBinary.setGetMoudle(moudleName, moudleImg, module, activity);
                            }

                            @Override
                            public void doInUIThread() {
                                if(as[3].equals("65")){
                                    mView.showDeviceCallbackMoudle(deviceCallback, module, mephitis);
                                    if (dialogNumber == 0) {
                                        dialog.setDevideCall(HexToBinary.HexTo10IntegerShort(deviceCallback.getDeviceCallBackData().getTemperatureLow())+"",
                                                +HexToBinary.HexTo10IntegerShort(deviceCallback.getDeviceCallBackData().getTemperatureHight())+"");
                                        if (dialog.getEdtextLow() != null) dialogNumber = 1;
                                    }
                                    dialog.setDeviceBack(deviceCallback, module,list);

                                    if(DJISampleApplication.bool) {
                                        exelBean e = new exelBean();
                                        e.setLat(DJISampleApplication.Lat);
                                        e.setLog(DJISampleApplication.log);
                                        e.setGasValueOne(HexToBinary.HexTo10Integer(deviceCallback.getDeviceCallBackData().getGasValueOne()));
                                        e.setGasValueTwo(HexToBinary.HexTo10Integer(deviceCallback.getDeviceCallBackData().getGasValueTwo())/100);
                                        e.setGasValueThree(HexToBinary.HexTo10Integer(deviceCallback.getDeviceCallBackData().getGasValueThree()));
                                        e.setGasValueFour(HexToBinary.HexTo10Integer(deviceCallback.getDeviceCallBackData().getGasValueFour()));
                                        e.setTime(getTimeDate());
                                        e.setTime2(getTimeDate2());
                                        e.save();
                                    }
                                }else{
                                    mView.showDeviceCallbackMoudle2(deviceCallback);
                                }
                                //deviceCallback = null;
                            }
                        });
                    }
                });
    }
}
