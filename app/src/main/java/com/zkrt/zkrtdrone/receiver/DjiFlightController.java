package com.zkrt.zkrtdrone.receiver;

import android.app.Activity;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;

/**
 * Created by root on 17-3-6.
 */

public class DjiFlightController {

    //起飞
    static public void TakeOffDrone(Activity activity){
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getFlightController().
                    takeOff(new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            /*if(djiError !=null){
                                new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("起飞失败!")
                                        .show();
                            }*/
                        }
                    });
        }
    }

    //降落
    static public void LandingDrone(Activity activity, statusString name){
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getFlightController().
                    autoLanding(new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(final DJIError djiError) {
                            if(djiError == null){
                                name.StringStatus("Landing");
                            }else{
                               /* new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("降落失败!")
                                        .show();*/
                            }
                        }
                    });
        }
    }

    //返航
    static public void GoHomeDrone(Activity activity, statusString name){
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
            DJISampleApplication.getAircraftInstance().getFlightController().
                    goHome(new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(final DJIError djiError) {
                            if(djiError == null){
                                name.StringStatus("GoHome");
                            }else{
                                /*new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("返航失败!")
                                        .show();*/
                            }
                        }
                    });
        }
    }

    //悬停
    static public void HoverDrone(Activity activity ,String modleStop){
        if(DJIModuleVerificationUtil.isFlightControllerAvailable()){
            if(("Landing").equals(modleStop)){
                DJISampleApplication.getAircraftInstance().getFlightController().cancelAutoLanding(new DJICommonCallbacks.DJICompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        Utils.setResultToToast(activity,djiError == null?"悬停":djiError.getDescription());
                    }
                });
            }else if(("GoHome").equals(modleStop)){
                DJISampleApplication.getAircraftInstance().getFlightController().cancelGoHome(new DJICommonCallbacks.DJICompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        Utils.setResultToToast(activity,djiError == null?"悬停":djiError.getDescription());
                        if(djiError!=null){
                            DJISampleApplication.getAircraftInstance().getFlightController().cancelAutoLanding(new DJICommonCallbacks.DJICompletionCallback() {
                                @Override
                                public void onResult(DJIError djiError) {
                                    Utils.setResultToToast(activity,djiError == null?"悬停":djiError.getDescription());
                                }
                            });
                        }
                    }
                });
            }
        }
    }


    public interface statusString{
        void StringStatus(String name);
    }
}
