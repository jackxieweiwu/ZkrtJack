package com.zkrt.zkrtdrone.view.myfragment.camera.cameraFileSetting;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;

import dji.common.camera.DJICameraExposureParameters;
import dji.common.camera.DJICameraSettingsDef;
import dji.common.error.DJIError;
import dji.common.gimbal.DJIGimbalAxis;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.camera.DJICamera;

/**
 * Created by jack_xie on 16/4/22.
 */
//@Instance
public class CameraFilePresenter extends CameraFileContract.Presenter {
    @Override
    public void onAttached() {
        getCameraFile();
    }

    @Override
    void getCameraFile() {
        //mView.getCameraFragment(mModel.getCameraFragmentPhoto(),mModel.getCameraFragmentVideo(),mModel.getSettingCameraFragment());

        if(DJIModuleVerificationUtil.isCameraModuleAvailable()){
            //获取相机的名称
            mView.showCmaeraDisplayname(DJISampleApplication.getCameraInstance().getDisplayName());
            //获取相机的防闪烁。
            DJISampleApplication.getCameraInstance().getAntiFlicker(new DJICommonCallbacks.DJICompletionCallbackWith<DJICameraSettingsDef.CameraAntiFlicker>() {
                @Override
                public void onSuccess(DJICameraSettingsDef.CameraAntiFlicker cameraAntiFlicker) {
                    mView.showAntiFlicker(cameraAntiFlicker);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取光圈值。
            DJISampleApplication.getCameraInstance().getAperture(new DJICommonCallbacks.DJICompletionCallbackWith<DJICameraSettingsDef.CameraAperture>() {
                @Override
                public void onSuccess(DJICameraSettingsDef.CameraAperture cameraAperture) {
                    mView.showAperTure(cameraAperture);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取是否启用自动锁定自动锁定功能。
            DJISampleApplication.getCameraInstance().getAutoAEUnlockEnabled(new DJICommonCallbacks.DJICompletionCallbackWith<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    mView.showAutoAEUnlockEnabled(aBoolean);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取相机的ISO值。
            DJISampleApplication.getCameraInstance().getISO(new DJICommonCallbacks.DJICompletionCallbackWith<DJICameraSettingsDef.CameraISO>() {
                @Override
                public void onSuccess(DJICameraSettingsDef.CameraISO cameraISO) {
                    mView.getCameraISO(cameraISO);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取镜头对焦模式。
            DJISampleApplication.getCameraInstance().getLensFocusMode(new DJICommonCallbacks.DJICompletionCallbackWith<DJICameraSettingsDef.CameraLensFocusMode>() {
                @Override
                public void onSuccess(DJICameraSettingsDef.CameraLensFocusMode cameraLensFocusMode) {
                    mView.showLensFocusMode(cameraLensFocusMode);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取镜头对焦环值。
            DJISampleApplication.getCameraInstance().getLensFocusRingValue(new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    mView.showLensFocusRingValue(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取变焦镜头焦距，单位为0.1mm。
            DJISampleApplication.getCameraInstance().getOpticalZoomFocalLength(new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    mView.showOpticalZoomFocalLength(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取变焦镜头的规格。
            DJISampleApplication.getCameraInstance().getOpticalZoomSpec(new DJICommonCallbacks.DJICompletionCallbackWith<DJICameraSettingsDef.CameraOpticalZoomSpec>() {
                @Override
                public void onSuccess(DJICameraSettingsDef.CameraOpticalZoomSpec cameraOpticalZoomSpec) {
                    mView.showOpticalZoomSpec(cameraOpticalZoomSpec);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取相机的AEB捕获参数。
            DJISampleApplication.getCameraInstance().getPhotoAEBParam(new DJICommonCallbacks.DJICompletionCallbackWith<DJICameraSettingsDef.CameraPhotoAEBParam>() {
                @Override
                public void onSuccess(DJICameraSettingsDef.CameraPhotoAEBParam cameraPhotoAEBParam) {
                    mView.showPhotoAEBParam(cameraPhotoAEBParam);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取相机的照片质量。
            DJISampleApplication.getCameraInstance().getPhotoQuality(new DJICommonCallbacks.DJICompletionCallbackWith<DJICameraSettingsDef.CameraPhotoQuality>() {
                @Override
                public void onSuccess(DJICameraSettingsDef.CameraPhotoQuality cameraPhotoQuality) {
                    mView.showPhotoQuality(cameraPhotoQuality);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //  获取相机的视频存储格式。
            DJISampleApplication.getCameraInstance().getVideoFileFormat(new DJICommonCallbacks.DJICompletionCallbackWith<DJICameraSettingsDef.CameraVideoFileFormat>() {
                @Override
                public void onSuccess(DJICameraSettingsDef.CameraVideoFileFormat cameraVideoFileFormat) {
                    mView.showVideoFIleFormat(cameraVideoFileFormat);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取相机的曝光模式。
            DJISampleApplication.getCameraInstance().getExposureMode(new DJICommonCallbacks.DJICompletionCallbackWith<DJICameraSettingsDef.CameraExposureMode>() {
                @Override
                public void onSuccess(DJICameraSettingsDef.CameraExposureMode cameraExposureMode) {
                    mView.showExposureMode(cameraExposureMode);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取相机的锐度。
            DJISampleApplication.getCameraInstance().getSharpness(new DJICommonCallbacks.DJICompletionCallbackWith<DJICameraSettingsDef.CameraSharpness>() {
                @Override
                public void onSuccess(DJICameraSettingsDef.CameraSharpness cameraSharpness) {
                    mView.showSharpness(cameraSharpness);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //检查当前设备是否支持数字缩放。
            mView.showCameraInstance(DJISampleApplication.getCameraInstance().isDigitalZoomScaleSupported());

            //检查相机是否支持光学变焦。
            mView.showOpticalZoomSupported(DJISampleApplication.getCameraInstance().isOpticalZoomSupported());


            //获取范围[1,30]的当前光学缩放比例。
            DJISampleApplication.getCameraInstance().getOpticalZoomScale(new DJICommonCallbacks.DJICompletionCallbackWith<Float>() {
                @Override
                public void onSuccess(Float aFloat) {
                    mView.showOpticalZoomScale(aFloat);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //设置相机的方向。
            DJISampleApplication.getCameraInstance().getOrientation(new DJICommonCallbacks.DJICompletionCallbackWith<DJICameraSettingsDef.CameraOrientation>() {
                @Override
                public void onSuccess(DJICameraSettingsDef.CameraOrientation cameraOrientation) {
                    mView.showOrientantion(cameraOrientation);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //确定镜头对焦辅助是否启用。
            DJISampleApplication.getCameraInstance().getLensFocusAssistantEnabled(new DJICommonCallbacks.DJICompletionCallbackWithTwoParam<Boolean, Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean, Boolean aBoolean2) {
                    mView.showLensFocusAssistantEnabled(aBoolean,aBoolean2);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取相机的视频分辨率和帧速率值。
            DJISampleApplication.getCameraInstance().getVideoResolutionAndFrameRate(new DJICommonCallbacks.DJICompletionCallbackWithTwoParam<DJICameraSettingsDef.CameraVideoResolution, DJICameraSettingsDef.CameraVideoFrameRate>() {
                @Override
                public void onSuccess(DJICameraSettingsDef.CameraVideoResolution cameraVideoResolution, DJICameraSettingsDef.CameraVideoFrameRate cameraVideoFrameRate) {
                    mView.showVideoResolutionAndFrameRate(cameraVideoResolution,cameraVideoFrameRate);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //参数回调的相机更新值。
            DJISampleApplication.getCameraInstance().setCameraUpdatedCurrentExposureValuesCallback(new DJICamera.CameraUpdatedCurrentExposureValuesCallback() {
                @Override
                public void onResult(DJICameraExposureParameters djiCameraExposureParameters) {
                    mView.showCameraUpdatedCurrentExposureValuesCallback(djiCameraExposureParameters);
                }
            });

            //获取相机的视频分辨率和帧速率值。
            DJISampleApplication.getCameraInstance().getVideoResolutionAndFrameRate(new DJICommonCallbacks.DJICompletionCallbackWithTwoParam<DJICameraSettingsDef.CameraVideoResolution, DJICameraSettingsDef.CameraVideoFrameRate>() {
                @Override
                public void onSuccess(DJICameraSettingsDef.CameraVideoResolution cameraVideoResolution, DJICameraSettingsDef.CameraVideoFrameRate cameraVideoFrameRate) {
                    mView.showVideoResolutionAndFrameRate(cameraVideoResolution,cameraVideoFrameRate);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取相机的白平衡和色温。
            DJISampleApplication.getCameraInstance().getWhiteBalanceAndColorTemperature(new DJICommonCallbacks.DJICompletionCallbackWithTwoParam<DJICameraSettingsDef.CameraWhiteBalance, Integer>() {
                @Override
                public void onSuccess(DJICameraSettingsDef.CameraWhiteBalance cameraWhiteBalance, Integer integer) {
                    mView.showWhiteBalanceAndColorTemperature(cameraWhiteBalance,integer);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取相机的快门速度。
            DJISampleApplication.getCameraInstance().getShutterSpeed(new DJICommonCallbacks.DJICompletionCallbackWith<DJICameraSettingsDef.CameraShutterSpeed>() {
                @Override
                public void onSuccess(DJICameraSettingsDef.CameraShutterSpeed cameraShutterSpeed) {
                    mView.showCameraShutterSpeed(cameraShutterSpeed);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取相机的曝光补偿。
            DJISampleApplication.getCameraInstance().getExposureCompensation(new DJICommonCallbacks.DJICompletionCallbackWith<DJICameraSettingsDef.CameraExposureCompensation>() {
                @Override
                public void onSuccess(DJICameraSettingsDef.CameraExposureCompensation cameraExposureCompensation) {
                    mView.showExposureCompensation(cameraExposureCompensation);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

            //获取相机的AE（自动曝光）锁是否被锁定。
            DJISampleApplication.getCameraInstance().getAELock(new DJICommonCallbacks.DJICompletionCallbackWith<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    mView.showGetAELocK(aBoolean);
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });

        }
    }
}
