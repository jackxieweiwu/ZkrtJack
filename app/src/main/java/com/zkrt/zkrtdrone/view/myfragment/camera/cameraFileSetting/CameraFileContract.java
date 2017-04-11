package com.zkrt.zkrtdrone.view.myfragment.camera.cameraFileSetting;

import com.zkrt.zkrtdrone.view.myfragment.camera.cameraFileSetting.cameraAutoFragment.PhotoCametaFragment;
import com.zkrt.zkrtdrone.view.myfragment.camera.cameraFileSetting.cameraAutoFragment.SettingCametaFragment;
import com.zkrt.zkrtdrone.view.myfragment.camera.cameraFileSetting.cameraAutoFragment.VIdeoCameraFragment;

import dji.common.camera.DJICameraExposureParameters;
import dji.common.camera.DJICameraSettingsDef;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseModel;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BasePresenter;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseView;

/**
 * Created by jack_xie on 16/4/22.
 */
public interface CameraFileContract {
    interface Model extends BaseModel {
       /* PhotoCametaFragment getCameraFragmentPhoto();
        VIdeoCameraFragment getCameraFragmentVideo();
        SettingCametaFragment getSettingCameraFragment();*/
    }

    interface View extends BaseView {
        void showGetAELocK(Boolean aBoolean);//获取相机的AE（自动曝光）锁是否被锁定。
        void showWhiteBalanceAndColorTemperature(DJICameraSettingsDef.CameraWhiteBalance cameraWhiteBalance, Integer integer); //获取相机的白平衡和色温。
        void showCameraShutterSpeed(DJICameraSettingsDef.CameraShutterSpeed cameraShutterSpeed);//获取相机的快门速度。
        void showExposureCompensation(DJICameraSettingsDef.CameraExposureCompensation cameraExposureCompensation); //获取相机的曝光补偿。
        void showCmaeraDisplayname(String displayName); //获取相机的名称
        void showCameraUpdatedCurrentExposureValuesCallback(DJICameraExposureParameters djiCameraExposureParameters);  ////参数回调的相机更新值。

        void showAntiFlicker(DJICameraSettingsDef.CameraAntiFlicker cameraAntiFlicker);//获取相机的防闪烁。
        void showAperTure(DJICameraSettingsDef.CameraAperture cameraAperture); //获取光圈值
        void showAutoAEUnlockEnabled(Boolean aBoolean); //获取是否启用自动锁定自动锁定功能。
        void getCameraISO(DJICameraSettingsDef.CameraISO cameraISO);  //获取相机的ISO值。
        void showLensFocusMode(DJICameraSettingsDef.CameraLensFocusMode cameraLensFocusMode); ////获取镜头对焦模式。
        void showLensFocusRingValue(Integer integer); //获取镜头对焦环值。
        void showOpticalZoomFocalLength(Integer integer);  //获取变焦镜头焦距，单位为0.1mm。
        void showOpticalZoomScale(Float aFloat);  //获取范围[1,30]的当前光学缩放比例。
        void showOpticalZoomSpec(DJICameraSettingsDef.CameraOpticalZoomSpec cameraOpticalZoomSpec); //获取变焦镜头的规格。
        void showPhotoAEBParam(DJICameraSettingsDef.CameraPhotoAEBParam cameraPhotoAEBParam);  //获取相机的AEB捕获参数。
        void showPhotoQuality(DJICameraSettingsDef.CameraPhotoQuality cameraPhotoQuality);  //获取相机的照片质量。
        void showVideoFIleFormat(DJICameraSettingsDef.CameraVideoFileFormat cameraVideoFileFormat);  //获取相机的视频存储格式。

        void showExposureMode(DJICameraSettingsDef.CameraExposureMode cameraExposureMode); //获取相机的曝光模式。
        void showSharpness(DJICameraSettingsDef.CameraSharpness cameraSharpness);  //获取相机的锐度。
        void showCameraInstance(boolean digitalZoomScaleSupported); ////检查当前设备是否支持数字缩放。
        void showOpticalZoomSupported(boolean opticalZoomSupported);  //检查相机是否支持光学变焦。
        void showOrientantion(DJICameraSettingsDef.CameraOrientation cameraOrientation);  //设置相机的方向。
        void showLensFocusAssistantEnabled(Boolean aBoolean, Boolean aBoolean2);//确定镜头对焦辅助是否启用。
        void showVideoResolutionAndFrameRate(DJICameraSettingsDef.CameraVideoResolution cameraVideoResolution,  //获取相机的视频分辨率和帧速率值。
                                             DJICameraSettingsDef.CameraVideoFrameRate cameraVideoFrameRate);
        //void showGimbalPitch(float f); //获取轴上的物理控制器平滑值。

        //void getCameraFragment(PhotoCametaFragment photoCametaFragment,VIdeoCameraFragment vIdeoCameraFragment,SettingCametaFragment settingCametaFragment);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void getCameraFile();
    }
}
