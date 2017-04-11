package com.zkrt.zkrtdrone.view.myfragment.camera;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;

import butterknife.BindView;
import dji.common.error.DJIError;
import dji.common.product.Model;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.airlink.DJILBAirLink;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.camera.DJICamera;
import dji.sdk.codec.DJICodecManager;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseMvpFragment;

/**
 * Created by jack_xie on 17-2-28.
 */

public class CameraFPV extends BaseMvpFragment implements TextureView.SurfaceTextureListener{
    @BindView(R.id.texture_video_previewer_surface) TextureView texture_video_previewer_surface;
    @BindView(R.id.img_fpv_zoom) ImageView img_fpv_zoom;
    @BindView(R.id.linear_temperature) LinearLayout linear_temperature;
    @BindView(R.id.txt_temperature) TextView txt_temperature;
    private DJICamera.CameraReceivedVideoDataCallback mReceivedVideoDataCallback = null;
    private DJILBAirLink.DJIOnReceivedVideoCallback mOnReceivedVideoCallback = null;
    private DJIBaseProduct product;
    private DJICamera mCamera;
    private DJICodecManager mCodecManager;

    @Override
    protected int getFragmentViewId() {
        return R.layout.view_fpv_display;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    protected void onProductChange() {
        initPreviewer();
    }

    @Override
    public void onResume() {
        super.onResume();
        initPreviewer();
        //onProductChange();
    }

    @Override
    public void onPause() {
        uninitPreviewer();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mCodecManager != null) {
            mCodecManager.destroyCodec();
        }
        super.onDestroyView();
    }

    private void initPreviewer() {
        product = DJISampleApplication.getProductInstance();
        mReceivedVideoDataCallback = new DJICamera.CameraReceivedVideoDataCallback() {
            @Override
            public void onResult(byte[] videoBuffer, int size) {
                if(mCodecManager != null){
                    mCodecManager.sendDataToDecoder(videoBuffer, size);
                }
            }
        };

        mOnReceivedVideoCallback = new DJILBAirLink.DJIOnReceivedVideoCallback() {
            @Override
            public void onResult(byte[] videoBuffer, int size) {
                if(mCodecManager != null){
                    mCodecManager.sendDataToDecoder(videoBuffer, size);
                }
            }
        };

        if (null == product || !product.isConnected()) {
            mCamera = null;
            Utils.setResultToToast(mActivity,"连接出现异常");
        } else {
            if (null != texture_video_previewer_surface) {
                texture_video_previewer_surface.setSurfaceTextureListener(this);
            }

            if (!product.getModel().equals(Model.UnknownAircraft)) {
                mCamera = product.getCamera();
                if (mCamera != null) {
                    mCamera.setDJICameraReceivedVideoDataCallback(mReceivedVideoDataCallback);
                }
            } else {
                if (null != product.getAirLink()) {
                    if (null != product.getAirLink().getLBAirLink()) {
                        product.getAirLink().getLBAirLink().setDJIOnReceivedVideoCallback(mOnReceivedVideoCallback);
                    }
                }
            }
        }

        //
        if(DJIModuleVerificationUtil.isCameraModuleAvailable()){
            if(DJISampleApplication.getProductInstance().getCamera().isThermalImagingCamera()) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        linear_temperature.setVisibility(DJISampleApplication.getProductInstance().getCamera().isThermalImagingCamera() ? View.VISIBLE : View.GONE);
                    }
                });
            }



            DJISampleApplication.getProductInstance().getCamera().getThermalTemperatureData(new DJICommonCallbacks.DJICompletionCallbackWith<Float>() {
                @Override
                public void onSuccess(Float aFloat) {

                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });
        }
    }

    private void uninitPreviewer() {
        if (mCamera != null) {
            mCamera.setDJICameraReceivedVideoDataCallback(null);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (mCodecManager == null) {
            mCodecManager = new DJICodecManager(mActivity, surface, width, height);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        if (mCodecManager != null) {
            mCodecManager.cleanSurface();
            mCodecManager = null;
            mCodecManager = new DJICodecManager(mActivity, surface, width, height);
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        surface.release();
        if (mCodecManager != null) {
            mCodecManager.cleanSurface();
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    public TextureView getFPV() {
        return texture_video_previewer_surface;
    }

    public ImageView getFPVImageView() {
        return img_fpv_zoom;
    }

}
