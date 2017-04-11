package com.zkrt.zkrtdrone.view.myfragment.camera.cameraFileSetting;

import android.os.Bundle;
import android.support.design.widget.CheckableImageButton;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;
import com.zkrt.zkrtdrone.view.dialog.CameraFileDialog;
import com.zkrt.zkrtdrone.view.myfragment.camera.cameraFileSetting.cameraAutoFragment.PhotoCametaFragment;
import com.zkrt.zkrtdrone.view.myfragment.camera.cameraFileSetting.cameraAutoFragment.SettingCametaFragment;
import com.zkrt.zkrtdrone.view.myfragment.camera.cameraFileSetting.cameraAutoFragment.VIdeoCameraFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.SettingTabLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import dji.common.camera.CameraSystemState;
import dji.common.camera.DJICameraExposureParameters;
import dji.common.camera.DJICameraSettingsDef;
import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.camera.DJICamera;
import dji.sdk.camera.DJIMedia;
import dji.sdk.camera.DJIMediaManager;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseMvpFragment;

/**
 * Created by jack_xie on 16-12-26.
 */

public class CameraFileFragment extends BaseMvpFragment<CameraFilePresenter,CameraFileModel> implements CameraFileContract.View {
    @BindView(R.id.linear_camera_auto) LinearLayout linear_camera_auto;
    @BindView(R.id.lineart_camera_setting_auto) LinearLayout lineart_camera_setting_auto;
    @BindView(R.id.img_play) CheckableImageButton img_play;
    @BindView(R.id.img_play_down) CheckableImageButton img_play_down;
    @BindView(R.id.camera_file)  CheckableImageButton camera_file;
    @BindView(R.id.txt_camera_time)  TextView txt_camera_time;
    //@BindView(R.id.camera_file_seekbar) SeekBar camera_file_seekbar;

    //Zoom
    @BindView(R.id.rela_camerafile_zoom) RelativeLayout rela_camerafile_zoom;
    @BindView(R.id.txt_camera_T) TextView txt_camera_T;
    @BindView(R.id.txt_camera_zoom) TextView txt_camera_zoom;
    @BindView(R.id.txt_camera_W) TextView txt_camera_W;
    @BindView(R.id.txt_camera_R) TextView txt_camera_R;
    @BindView(R.id.camera_file_zoom_img) ImageView camera_file_zoom_img;

    private TextView txt_camera_file_shutterspeed;
    private TextView txt_camera_file_ev;
    private TextView txt_camera_file_wb;
    private TextView txt_iso;  //相机的曝光率
    private TextView txt_p;  //相机分辨率
    //AE
    private ImageView img_camera_file_ae;
    private TextView txt_camera_file_ae;
    private TextView txt_camera_name;
    private LinearLayout linear_camera_file_ae;
    private LinearLayout linear_camera;
    // 定义FragmentTabHost对象
    @BindView(R.id.camera_setting_tabhost) FragmentTabHost camera_setting_tabhost;

    private DJICameraSettingsDef.CameraMode cameraMode;
    private boolean isRecord = false;
    private boolean isStoringPhoto = false;
    private boolean isCameraOverHeated = false;
    private Timer timer = new Timer();
    private long timeCounter = 0;
    private long hours = 0;
    private long minutes = 0;
    private long seconds = 0;
    String time = "";
    private CameraFileDialog cameraFileDialog;
    private DJICameraSettingsDef.CameraOpticalZoomSpec cameraOpticalZoomSpecs;
    private TextureView texttureView;
    private ImageView fpvImageViews;
    private Boolean aeBool = false;   //Ae 设置相机的AE（自动曝光）锁定是否被锁定。
    private String cameraName = "NA";
    //private CameraSettingTabHost cameraSettingTabHost;
     // 定义一个布局
    private LayoutInflater layoutInflater;
    // 定义数组来存放Fragment界面
    private Class fragmentArray[] = { PhotoCametaFragment.class,VIdeoCameraFragment.class,
            SettingCametaFragment.class };
    // 定义数组来存放按钮图片
    private int mImageViewArray[] = { R.mipmap.camera_photo_highlight,
            R.mipmap.camera_video_highlight, R.mipmap.profile_edit_icon};
    // Tab选项卡的文字
    private String mTextviewArray[] = { "Camera", "Video", "Setting" };
    @Override
    protected int getFragmentViewId() {
        return R.layout.camera;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        layoutInflater = LayoutInflater.from(mActivity);
        /*if(cameraSettingTabHost ==null){
           // cameraSettingTabHost = new CameraSettingTabHost(mActivity);
        }*/
        //lineart_camera_setting_auto.addView(cameraSettingTabHost.getSettingView(LayoutInflater.from(getActivity()).inflate(R.layout.setting_layout,null)));
        //cameraSettingTabHost.addTableView();
        cameraFileDialog = new CameraFileDialog(getActivity(),R.style.customDialog,R.layout.camera_file);
        getCameraSt();
        txt_camera_file_shutterspeed = (TextView) mActivity.findViewById(R.id.txt_camera_file_shutterspeed);
        txt_camera_file_ae = (TextView) mActivity.findViewById(R.id.txt_camera_file_ae);
        txt_camera_name = (TextView) mActivity.findViewById(R.id.txt_camera_name);
        txt_p = (TextView) mActivity.findViewById(R.id.txt_p);
        txt_iso = (TextView) mActivity.findViewById(R.id.txt_iso);
        txt_camera_file_wb = (TextView) mActivity.findViewById(R.id.txt_camera_file_wb);
        txt_camera_file_ev = (TextView) mActivity.findViewById(R.id.txt_camera_file_ev);
        img_camera_file_ae = (ImageView) mActivity.findViewById(R.id.img_camera_file_ae);
        linear_camera_file_ae = (LinearLayout) mActivity.findViewById(R.id.linear_camera_file_ae);
        linear_camera = (LinearLayout) mActivity.findViewById(R.id.linear_camera);
        linear_camera_file_ae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AE lock or unlock  设置相机的AE（自动曝光）锁定是否被锁定。
                if(DJIModuleVerificationUtil.isCameraModuleAvailable()){
                    DJISampleApplication.getCameraInstance().setAELock(!aeBool, new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if(djiError ==null){
                                setAeBgGroubs(false);
                            }else{
                                setAeBgGroubs(true);
                            }
                        }
                    });
                }
            }
        });

        // 实例化TabHost对象，得到TabHost
        camera_setting_tabhost.setup(mActivity, DJISampleApplication.fragmentManager, R.id.realtabcontent);

        // 得到fragment的个数
        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = camera_setting_tabhost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            camera_setting_tabhost.addTab(tabSpec, fragmentArray[i], null);
            // 设置Tab按钮的背景
            camera_setting_tabhost.getTabWidget().getChildAt(i)
                    .setBackgroundResource(R.color.back_150);
        }

        getCameraListView();
        /*texttureView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:  //起始位置为
                        fpvImageViews.setVisibility(View.VISIBLE);
                        ViewGroup.MarginLayoutParams margin9 = new ViewGroup.MarginLayoutParams(
                                fpvImageViews.getLayoutParams());
                        RelativeLayout.LayoutParams layoutParams9 = new RelativeLayout.LayoutParams(margin9);
                        fpvImageViews.setLayoutParams(layoutParams9);
                        layoutParams9.leftMargin = (int) event.getX();
                        layoutParams9.topMargin = (int) event.getY();

                        break;
                }
                return true;
            }
        });*/


    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
    }

    //开始在指定方向以指定速度更改镜头的焦距
    private void startConZoom(){
        if(DJIModuleVerificationUtil.isCameraModuleAvailable()){
            DJISampleApplication.getCameraInstance().startContinuousOpticalZoom(DJICameraSettingsDef.OpticalZoomDirection.ZoomIn,
                    DJICameraSettingsDef.OpticalZoomSpeed.ModeratelySlow, new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if(djiError ==null){
                                setZoomStop();
                            }
                        }
                    });
        }
    }


    //Zoom
    @OnClick(R.id.txt_camera_T)
    public void cameraZoomT(View v){
        if(DJIModuleVerificationUtil.isCameraModuleAvailable()){
            DJISampleApplication.getCameraInstance().startContinuousOpticalZoom(DJICameraSettingsDef.OpticalZoomDirection.ZoomIn,
                    DJICameraSettingsDef.OpticalZoomSpeed.ModeratelySlow, new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if(djiError ==null){
                                setZoomStop();
                            }
                        }
                    });
        }
    }

    //被叫停止焦距改变
    private void setZoomStop(){
        if(DJIModuleVerificationUtil.isCameraModuleAvailable()){
            DJISampleApplication.getCameraInstance().stopContinuousOpticalZoom(new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {

                }
            });
        }
    }

    @OnClick(R.id.txt_camera_W)
    public void cameraZoomW(View v){
        if(DJIModuleVerificationUtil.isCameraModuleAvailable()){
            DJISampleApplication.getCameraInstance().startContinuousOpticalZoom(DJICameraSettingsDef.OpticalZoomDirection.ZoomOut,
                    DJICameraSettingsDef.OpticalZoomSpeed.ModeratelySlow, new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if(djiError ==null){
                                setZoomStop();
                            }
                        }
                    });
        }
    }

    //Zoom R
    @OnClick(R.id.txt_camera_R)
    public void cameraZoomR(View v){
        if(DJIModuleVerificationUtil.isCameraModuleAvailable()){
            DJISampleApplication.getCameraInstance().setOpticalZoomFocalLength(cameraOpticalZoomSpecs.minFocalLength, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {

                }
            });
        }
    }

    public void getCameraListView(){
        if (DJIModuleVerificationUtil.isCameraModuleAvailable()) {
            DJISampleApplication.getProductInstance().getCamera().getMediaManager().fetchMediaList(
                    new DJIMediaManager.CameraDownloadListener<ArrayList<DJIMedia>>() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onRateUpdate(long total, long current, long persize) {
                        }

                        @Override
                        public void onProgress(long l, long l1) {

                        }

                        @Override
                        public void onSuccess(ArrayList<DJIMedia> djiMedias) {
                            if (null != djiMedias) {
                                if (!djiMedias.isEmpty()){
                                    cameraFileDialog.setCameraList(djiMedias);
                                    Utils.setResultToToast(getContext(),djiMedias.size()+"----");
                                    // str = "Total Media files:"+djiMedias.size() +"\n"+ "Media 1: "+djiMedias.get(0).getFileName();
                                }else {
                                    Utils.setResultToToast(getContext(),"没有媒体在SD卡");
                                }

                            }
                        }

                        @Override
                        public void onFailure(DJIError djiError) {
                            Utils.setResultToToast(getContext(),djiError.getDescription());
                        }
                    }
            );
        }
    }

    private void getCameraSt(){
        if(DJIModuleVerificationUtil.isCameraModuleAvailable()){
            DJISampleApplication.getProductInstance().getCamera().
                    setDJICameraUpdatedSystemStateCallback(new DJICamera.CameraUpdatedSystemStateCallback() {
                        @Override
                        public void onResult(CameraSystemState cameraSystemState) {
                            if (null != cameraSystemState) {
                                cameraMode = cameraSystemState.getCameraMode();
                                isRecord = cameraSystemState.isRecording();
                                isStoringPhoto = cameraSystemState.isStoringPhoto();
                                isCameraOverHeated = cameraSystemState.isCameraOverHeated();
                            }
                        }
                    });
        }
    }

    //拍照
    private void captureAction(){
        if(DJISampleApplication.getProductInstance()==null) return;
        DJICameraSettingsDef.CameraMode cameraMode = DJICameraSettingsDef.CameraMode.ShootPhoto;
        if (DJIModuleVerificationUtil.isCameraModuleAvailable()) {
            DJICameraSettingsDef.CameraShootPhotoMode photoMode = DJICameraSettingsDef.CameraShootPhotoMode.Single;
            DJISampleApplication.getProductInstance().getCamera().startShootPhoto(photoMode,
                    new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError error) {
                    Utils.setResultToToast(getActivity(),error == null ? "Success" : error.getDescription());
                }
            });
        }
    }

    //转变  是拍照   还是录像
    private void switchCameraMode(DJICameraSettingsDef.CameraMode cameraMode,boolean bool){
        DJICamera camera = DJISampleApplication.getCameraInstance();
        if (camera != null) {
            camera.setCameraMode(cameraMode, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError error) {
                    if(error ==null){
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(bool){
                                    img_play.setBackgroundResource(R.drawable.button_camera);
                                }else{
                                    img_play.setBackgroundResource(R.mipmap.camera_controll_video_icon_start);
                                }
                            }
                        });
                    }
                    Utils.setResultToToast(getActivity(),error == null ? "Success" : error.getDescription());
                }
            });
        }
    }

    public boolean getCameraStatus(){
        if(isCameraOverHeated){
            Utils.setResultToToast(getContext(),"相机温度过高,请稍候...");
            return false;
        }
        if(isRecord){
            Utils.setResultToToast(getContext(),"正在录像,请稍候...");
            return false;
        }
        if(isStoringPhoto){
            Utils.setResultToToast(getContext(),"正在拍照,请稍候...");
            return false;
        }
        return true;
    }

    public void updateCameraMode(){
        if(cameraMode == DJICameraSettingsDef.CameraMode.RecordVideo){
            if(!getCameraStatus()){
                return;
            }
            switchCameraMode(DJICameraSettingsDef.CameraMode.ShootPhoto,true);
        }else{
            if(!getCameraStatus()){
                return;
            }
            switchCameraMode(DJICameraSettingsDef.CameraMode.RecordVideo,false);
        }
    }

    @OnClick(R.id.img_play_down)
    public void caDown(View v){//切换摄像 拍照模式
        getCameraSt();
        updateCameraMode();
    }

    //录像计时
    public void setTextPlay(){
        if (DJIModuleVerificationUtil.isCameraModuleAvailable()) {
            DJICameraSettingsDef.CameraMode cameraMode = DJICameraSettingsDef.CameraMode.RecordVideo;
            DJISampleApplication.getProductInstance().getCamera().startRecordVideo(
                    new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if (null == djiError) {
                                Utils.setResultToToast(getContext(), "Start record");
                                timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        timeCounter = timeCounter + 1;
                                        hours = TimeUnit.MILLISECONDS.toHours(timeCounter);
                                        minutes = TimeUnit.MILLISECONDS.toMinutes(timeCounter) - (hours * 60);
                                        seconds = TimeUnit.MILLISECONDS.toSeconds(timeCounter) - ((hours * 60 * 60) + (minutes * 60));
                                        time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                txt_camera_time.setVisibility(View.VISIBLE);
                                                txt_camera_time.setText(time);
                                            }
                                        });
                                    }
                                }, 0, 1);
                            }

                        }
                    }
            );
        }
    }

    @OnClick(R.id.img_play)
    public void startPlay(View v){
        getCameraSt();
        if(cameraMode == DJICameraSettingsDef.CameraMode.RecordVideo){
            if(isRecord){
                stopTextPlay();
            }else{
                setTextPlay();
            }
        }else if(cameraMode == DJICameraSettingsDef.CameraMode.ShootPhoto){
            captureAction();
        }
    }

    @OnClick(R.id.camera_setting)
    public void cameraSettingAuto(View v){
        lineart_camera_setting_auto.setVisibility(View.GONE);
        linear_camera_auto.setVisibility(linear_camera_auto.getVisibility() == View.GONE?View.VISIBLE:View.GONE);
    }

    @OnClick(R.id.camera_menu)
    public void cameraMenu(View v){
        linear_camera_auto.setVisibility(View.GONE);
        lineart_camera_setting_auto.setVisibility(lineart_camera_setting_auto.getVisibility() == View.GONE?View.VISIBLE:View.GONE);
    }

    //停止计时
    public void stopTextPlay(){
        if (DJIModuleVerificationUtil.isCameraModuleAvailable()) {
            DJISampleApplication.getProductInstance().getCamera().stopRecordVideo(
                    new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if(djiError == null){
                                timer.cancel();
                                timeCounter = 0;
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        img_play.setBackgroundResource(R.mipmap.camera_controll_video_icon_start);
                                        txt_camera_time.setVisibility(View.INVISIBLE);
                                        txt_camera_time.setText("00:00:00");
                                    }
                                });
                            }else{
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        img_play.setBackgroundResource(R.mipmap.camera_controll_video_icon_stop);
                                    }
                                });
                            }
                        }
                    }
            );
        }
    }



    @OnClick(R.id.camera_file)
    public void toCameraFile(View v){
        cameraFileDialog.show();
    }

    @Override  //获取相机的防闪烁。
    public void showAntiFlicker(DJICameraSettingsDef.CameraAntiFlicker cameraAntiFlicker) {

    }

    @Override  //获取光圈值
    public void showAperTure(DJICameraSettingsDef.CameraAperture cameraAperture) {

    }

    @Override  //获取是否启用自动锁定自动锁定功能。
    public void showAutoAEUnlockEnabled(Boolean aBoolean) {

    }

    @Override  //获取镜头对焦模式。
    public void showLensFocusMode(DJICameraSettingsDef.CameraLensFocusMode cameraLensFocusMode) {

    }

    @Override  //获取镜头对焦环值。
    public void showLensFocusRingValue(Integer integer) {

    }

    @Override  //获取变焦镜头焦距，单位为0.1mm。
    public void showOpticalZoomFocalLength(Integer integer) {
    }

    @Override  //获取范围[1,30]的当前光学缩放比例。
    public void showOpticalZoomScale(Float aFloat) {
        if(txt_camera_zoom !=null){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txt_camera_zoom.setText(aFloat+"x");
                }
            });
        }
    }

    @Override  //获取相机的AEB捕获参数。
    public void showPhotoAEBParam(DJICameraSettingsDef.CameraPhotoAEBParam cameraPhotoAEBParam) {

    }

    @Override  //获取相机的照片质量。
    public void showPhotoQuality(DJICameraSettingsDef.CameraPhotoQuality cameraPhotoQuality) {

    }

    @Override  //获取相机的视频存储格式。
    public void showVideoFIleFormat(DJICameraSettingsDef.CameraVideoFileFormat cameraVideoFileFormat) {

    }

    @Override  //获取相机的曝光模式。
    public void showExposureMode(DJICameraSettingsDef.CameraExposureMode cameraExposureMode) {

    }

    @Override  //获取相机的锐度。
    public void showSharpness(DJICameraSettingsDef.CameraSharpness cameraSharpness) {

    }

    @Override  //检查相机是否支持光学变焦。
    public void showOpticalZoomSupported(boolean opticalZoomSupported) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(rela_camerafile_zoom !=null){
                    if(opticalZoomSupported && (!cameraName.equals("NA") &&(
                            cameraName.equals("X5") ||
                                    cameraName.equals("Z30") ||
                                    cameraName.equals("X5R")))){
                        rela_camerafile_zoom.setVisibility(View.VISIBLE);
                    }else{
                        rela_camerafile_zoom.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override  //参数回调的相机更新值。
    public void showCameraUpdatedCurrentExposureValuesCallback(DJICameraExposureParameters djiCameraExposureParameters) {
        if(txt_camera_file_ev != null){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(txt_iso !=null) txt_iso.setText(djiCameraExposureParameters.getISO().name());
                    if(txt_camera_file_shutterspeed !=null)txt_camera_file_shutterspeed.setText(djiCameraExposureParameters.getShutterSpeed().name());
                    txt_camera_file_ev.setText(djiCameraExposureParameters.getExposureCompensation().name());
                }
            });
        }
    }

    @Override  //获取相机的视频分辨率和帧速率值。
    public void showVideoResolutionAndFrameRate(DJICameraSettingsDef.CameraVideoResolution cameraVideoResolution,
                                                DJICameraSettingsDef.CameraVideoFrameRate cameraVideoFrameRate) {
        if(txt_p != null){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int cameraResolution = cameraVideoResolution.value();
                    int cameraFrameRate = cameraVideoFrameRate.value();
                    String name = "";
                    if(cameraFrameRate == 0) name = "23FPS";
                    if(cameraFrameRate == 1) name = "24FPS";
                    if(cameraFrameRate == 2) name = "25FPS";
                    if(cameraFrameRate == 3) name = "29FPS";
                    if(cameraFrameRate == 4) name = "30FPS";
                    if(cameraFrameRate == 5) name = "47FPS";
                    if(cameraFrameRate == 6) name = "48FPS";
                    if(cameraFrameRate == 7) name = "50FPS";
                    if(cameraFrameRate == 8) name = "59FPS";
                    if(cameraFrameRate == 9) name = "60FPS";
                    if(cameraFrameRate == 10) name = "96FPS";
                    if(cameraFrameRate == 11) name = "100FPS";
                    if(cameraFrameRate == 12) name = "120FPS";
                    if(cameraResolution <= 9 && cameraResolution>= 5){
                        txt_p.setText("4K"+"/"+name);
                    }else if(cameraResolution <5 && cameraResolution>+2){
                        txt_p.setText("2K"+"/"+name);
                    }else if(cameraResolution >=0 && cameraResolution<2){
                        txt_p.setText("1080"+"/"+name);
                    }
                }
            });
        }
    }


    /*@Override
    public void getCameraFragment(PhotoCametaFragment photoCametaFragment, VIdeoCameraFragment vIdeoCameraFragment, SettingCametaFragment settingCametaFragment) {
       *//* if(cameraSettingTabHost ==null){
            //cameraSettingTabHost = new CameraSettingTabHost(mActivity);
        }*//*
        //cameraSettingTabHost.setCameraSettingTabHost(photoCametaFragment,vIdeoCameraFragment,settingCametaFragment);
    }*/

    @Override //获取相机的白平衡和色温。
    public void showWhiteBalanceAndColorTemperature(DJICameraSettingsDef.CameraWhiteBalance cameraWhiteBalance, Integer integer) {
        if(txt_camera_file_wb!=null){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txt_camera_file_wb.setText(cameraWhiteBalance.name());
                }
            });
        }
    }

    @Override  //获取相机的快门速度。
    public void showCameraShutterSpeed(DJICameraSettingsDef.CameraShutterSpeed cameraShutterSpeed) {
        if(txt_camera_file_shutterspeed !=null){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txt_camera_file_shutterspeed.setText(cameraShutterSpeed.name());
                }
            });
        }
    }

    @Override //获取相机的曝光补偿。
    public void showExposureCompensation(DJICameraSettingsDef.CameraExposureCompensation cameraExposureCompensation) {
        if(txt_camera_file_ev != null){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txt_camera_file_ev.setText(cameraExposureCompensation.name());
                }
            });
        }
    }

    @Override   //获取相机的AE（自动曝光）锁是否被锁定。
    public void showGetAELocK(Boolean aBoolean) {
        if(img_camera_file_ae != null){
            setAeBgGroubs(aBoolean);
        }
    }


    private void setAeBgGroubs(boolean boolAe){
        this.aeBool = boolAe;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(boolAe) {
                    img_camera_file_ae.setBackgroundResource(R.mipmap.camera_controll_aelock_lock);
                    txt_camera_file_ae.setTextColor(getResources().getColor(R.color.user_icon_1));
                }else{
                    img_camera_file_ae.setBackgroundResource(R.mipmap.camera_controll_aelock_unlock);
                    txt_camera_file_ae.setTextColor(getResources().getColor(R.color.user_icon_default_white));
                }
            }
        });
    }

    @Override //获取相机的名称
    public void showCmaeraDisplayname(String displayName) {
        this.cameraName = displayName;
        if(txt_camera_name != null){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txt_camera_name.setText(displayName);
                }
            });
        }
    }

    @Override  //设置相机的方向。
    public void showOrientantion(DJICameraSettingsDef.CameraOrientation cameraOrientation) {
        //Utils.setResultToToast(mActivity,cameraOrientation.name()+"***/");
    }

    @Override  //确定镜头对焦辅助是否启用。
    public void showLensFocusAssistantEnabled(Boolean aBoolean, Boolean aBoolean2) {

    }

    @Override  //检查当前设备是否支持数字缩放。
    public void showCameraInstance(boolean digitalZoomScaleSupported) {

    }

   /* @Override  //获取轴上的物理控制器平滑值。俯仰
    public void showGimbalPitch(float f) {
        if(camera_file_seekbar !=null){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    camera_file_seekbar.setProgress((int) f);
                    Utils.setResultToToast(mActivity,"俯仰"+f);
                }
            });
        }
    }*/



    @Override  //获取变焦镜头的规格。
    public void showOpticalZoomSpec(DJICameraSettingsDef.CameraOpticalZoomSpec cameraOpticalZoomSpec) {
        this.cameraOpticalZoomSpecs = cameraOpticalZoomSpec;
    }



    @Override  //获取相机的ISO值。
    public void getCameraISO(DJICameraSettingsDef.CameraISO cameraISO) {
        /*if(txt_iso !=null){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txt_iso.setText(cameraISO.name());
                }
            });
        }*/
    }

    public void setFpv(TextureView fpv) {
        this.texttureView = fpv;
    }

    public void setFpvImageView(ImageView fpvImageView) {
        this.fpvImageViews = fpvImageView;
    }
}
