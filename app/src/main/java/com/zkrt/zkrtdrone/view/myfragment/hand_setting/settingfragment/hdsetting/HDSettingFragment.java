package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.hdsetting;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.fynn.switcher.Switch;
import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.adapter.ReoutStatusRecyleViewAdapter;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import dji.common.LBAirLinkEncodeMode;
import dji.common.LBAirLinkSecondaryVideoFormat;
import dji.common.VideoDataChannel;
import dji.common.airlink.ChannelInterference;
import dji.common.airlink.ChannelSelectionMode;
import dji.common.airlink.DJISignalInformation;
import dji.common.airlink.FrequencyBandChannelRange;
import dji.common.airlink.LBAirLinkDataRate;
import dji.common.airlink.LBAirLinkFPVVideoQualityLatency;
import dji.common.airlink.LBAirLinkFrequencyBand;
import dji.common.airlink.LBAirLinkSecondaryVideoOutputPort;
import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.airlink.DJILBAirLink;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseMvpFragment;

/**
 * Created by jack_xie on 16-12-29.
 */  //<HDSettingPresenter,HDSettingModel> implements HDSettingContract.View
public class HDSettingFragment extends BaseMvpFragment{
    @BindView(R.id.recy_list_route) RecyclerView recy_list_route;
    @BindView(R.id.txt_camera_hd_motion)TextView txt_camera_hd_motion;
    @BindView(R.id.txt_camera_hd_hdmi)TextView txt_camera_hd_hdmi;
    @BindView(R.id.txt_camera_sdi)TextView txt_camera_sdi;
    @BindView(R.id.txt_camera_hd_custom)TextView txt_camera_hd_custom;
    @BindView(R.id.txt_camera_mode_quality)TextView txt_camera_mode_quality;  //画质
    @BindView(R.id.txt_camera_mode_delay)TextView txt_camera_mode_delay;
    @BindView(R.id.spinner_camera_hd) Spinner spinner_camera_hd;
    @BindView(R.id.spinner_camera_format) Spinner spinner_camera_format;
    @BindView(R.id.spinner_setting_video_mode) Spinner spinner_setting_video_mode;  //视频输出模式
    @BindView(R.id.spinner_setting_app_mode) Spinner spinner_setting_app_mode;
    @BindView(R.id.setting_hd_camera_seek) SeekBar setting_hd_camera_seek;
    @BindView(R.id.setting_bandwidth_seek) SeekBar setting_bandwidth_seek;
    @BindView(R.id.txt_number_quality) TextView txt_number_quality;
    @BindView(R.id.txt_number_bandwidth) TextView txt_number_bandwidth;
    @BindView(R.id.rela_xinhao_one)RelativeLayout rela_xinhao_one;
    @BindView(R.id.rela_xinhao_two)RelativeLayout rela_xinhao_two;
    @BindView(R.id.switch_ios_camera)Switch switch_ios_camera;
    @BindView(R.id.switch_ios_camera_ext)Switch switch_ios_camera_ext;
    @BindView(R.id.txt_rssi_number_one)TextView txt_rssi_number_one;
    @BindView(R.id.txt_rssi_number_two)TextView txt_rssi_number_two;
    @BindView(R.id.txt_rssi_number_three)TextView txt_rssi_number_three;
    @BindView(R.id.txt_rssi_number_four)TextView txt_rssi_number_four;
    @BindView(R.id.ext_rela)RelativeLayout ext_rela;
    @BindView(R.id.rela_video_out)RelativeLayout rela_video_out;
    @BindView(R.id.rela_voide_outmode)RelativeLayout rela_voide_outmode;
    @BindView(R.id.linear_video_in)LinearLayout linear_video_in;

    private int numberMoble = -1;  //0 Single
    private String[] numberName = {"4Mbps(3km)","6Mbps(2km)","8Mbps(1.5km)","10Mbps(0.7km)"};
    private LBAirLinkDataRate[] lbdata = {LBAirLinkDataRate.Bandwidth4Mbps,LBAirLinkDataRate.Bandwidth6Mbps,
            LBAirLinkDataRate.Bandwidth8Mbps,LBAirLinkDataRate.Bandwidth10Mbps};
    private ArrayAdapter<String> appintputMoudle;
    private ArrayAdapter<String> videoIntputMoudle;
    private ReoutStatusRecyleViewAdapter mliveRoomRecyclerViewAdapter;
    private ChannelInterference[] channelInterferences_relay;
    private int sumRoutatus = -1;

    //LB  EXT 频段百分比
    private int lbExt=0;
    //HDMI AV  频段百分比
    private float hdmiAv = 0f;
    private int txt_camera_hd_hdminunmer = 0;

    @Override
    protected int getFragmentViewId() {
        return R.layout.setting_hd_camera;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        switch_ios_camera_ext.isPressed();
        switch_ios_camera.isPressed();
        setting_hd_camera_seek.setMax(3);
        setting_bandwidth_seek.setMax(100);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(),R.array.route,R.layout.spinner_list_item);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner_camera_hd.setAdapter(adapter);

        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.camera_framt,R.layout.spinner_list_item);
        adapter2.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner_camera_format.setAdapter(adapter2);

        //app输出模式
       /* appintputMoudle = ArrayAdapter.createFromResource(getContext(),R.array.appintput_modle_LB,R.layout.spinner_list_item);*/
        appintputMoudle = new ArrayAdapter<String>(mActivity,R.layout.spinner_list_item, getSourceAdapterList(R.array.appintput_modle_LB));
        appintputMoudle.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner_setting_app_mode.setAdapter(appintputMoudle);

        //VIdeo输出模式
        /*videoIntputMoudle = ArrayAdapter.createFromResource(getContext(),R.array.videointput_modle_LB,R.layout.spinner_list_item);*/
        videoIntputMoudle = new ArrayAdapter<String>(mActivity,R.layout.spinner_list_item, getSourceAdapterList(R.array.videointput_modle_LB));
        videoIntputMoudle.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner_setting_video_mode.setAdapter(appintputMoudle);

        spinner_camera_hd.isPressed();
        spinner_setting_video_mode.isPressed();
        spinner_setting_app_mode.isPressed();
        setting_bandwidth_seek.isPressed();
        setting_hd_camera_seek.isPressed();
        spinner_camera_format.isPressed();
        if(channelInterferences_relay == null) channelInterferences_relay = new ChannelInterference[8];
        mliveRoomRecyclerViewAdapter = new ReoutStatusRecyleViewAdapter(mActivity,channelInterferences_relay);
        recy_list_route.setAdapter(mliveRoomRecyclerViewAdapter);
        recy_list_route.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recy_list_route.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void getViewFindByid(View view) {
        super.getViewFindByid(view);
        if(DJIModuleVerificationUtil.isLBAirlinkAvailable()) {
            //如果Lightbridge 2设备支持双编码模式。
            showDualEncodeModePercentageBoolean(DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().isDualEncodeModeSupported());

            //如果支持辅助视频输出。
            showSecondaryVideoOutputSupported(DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().isSecondaryVideoOutputSupported());

            //设置DJILB空气链接更新所有通道干扰的回调函数
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().setDJILBAirLinkUpdatedAllChannelInterferenceCallback(new DJILBAirLink.DJILBAirLinkUpdatedAllChannelInterferenceCallback() {
                @Override
                public void onResult(ChannelInterference[] channelInterferences) {
                    showDJiLbairLinkUpdateAllChannelInterFerenceCalback(channelInterferences);
                }
            });

//确定是否启用遥控器上的辅助视频输出。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getSecondaryVideoOutputEnabled(new DJICommonCallbacks.DJICompletionCallbackWith<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    showSecondaryVIdeoOutputEnable(aBoolean);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //获取空中链路的当前下行信道。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getChannel(new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    showDjiChannel(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //获取下行信道选择模式。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getChannelSelectionMode(new DJICommonCallbacks.DJICompletionCallbackWith<ChannelSelectionMode>() {
                @Override
                public void onSuccess(ChannelSelectionMode channelSelectionMode) {
                    showChannelSelectionMode(channelSelectionMode);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //设置用于更新FPV视频带宽更改的代理回调。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().setDJILBAirLinkFPVBandwidthPercentChangedCallback(new DJILBAirLink.DJILBAirLinkFPVBandwidthPercentChangedCallback() {
                @Override
                public void onResult(float v) {
                    showFPVBandwidthPer(v);
                }
            });

            //获取当前下行数据速率（吞吐量）。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getDataRate(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkDataRate>() {
                @Override
                public void onSuccess(LBAirLinkDataRate lbAirLinkDataRate) {
                    showDataRate(lbAirLinkDataRate);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //确定OSD是否覆盖在视频Feed上。
            /*DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getDisplayOSDEnabled(new DJICommonCallbacks.DJICompletionCallbackWith<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    showDisplayOSDEnabled(aBoolean);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });*/

            //获取FPV视频质量与延迟偏好。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getFPVQualityLatency(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkFPVVideoQualityLatency>() {
                @Override
                public void onSuccess(LBAirLinkFPVVideoQualityLatency lbAirLinkFPVVideoQualityLatency) {
                    showFPVQualityLatency(lbAirLinkFPVVideoQualityLatency);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //当启用双编码模式时，获取Lightbridge 2模块上的AV和HMDI输入之间的计算能力和带宽平衡。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getDualEncodeModePercentage(new DJICommonCallbacks.DJICompletionCallbackWith<Float>() {
                @Override
                public void onSuccess(Float aFloat) {
                    showDualEncodeModePercentage(aFloat);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });


            //获取专用于FPV摄像机的下行链路视频带宽百分比
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getFPVVideoBandwidthPercent(new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    showFPVVideoBandwidthPercent(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //获取Lightbridge 2编码模式。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getEncodeMode(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkEncodeMode>() {
                @Override
                public void onSuccess(LBAirLinkEncodeMode lbAirLinkEncodeMode) {
                    showEncodeMode(lbAirLinkEncodeMode);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });



            //获取遥控器上的辅助视频输出端口。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getSecondaryVideoOutputPort(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkSecondaryVideoOutputPort>() {
                @Override
                public void onSuccess(LBAirLinkSecondaryVideoOutputPort lbAirLinkSecondaryVideoOutputPort) {
                    showSecondaryVideoOutputPort(lbAirLinkSecondaryVideoOutputPort);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //获取遥控器HDMI视频端口输出视频格式。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getHDMIOutputFormat(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkSecondaryVideoFormat>() {
                @Override
                public void onSuccess(LBAirLinkSecondaryVideoFormat lbAirLinkSecondaryVideoFormat) {
                    showHDMIOutputFormat(lbAirLinkSecondaryVideoFormat);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //获取遥控器SDI视频端口输出视频格式。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getSDIOutputFormat(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkSecondaryVideoFormat>() {
                @Override
                public void onSuccess(LBAirLinkSecondaryVideoFormat lbAirLinkSecondaryVideoFormat) {
                    showSDIOutputFormat(lbAirLinkSecondaryVideoFormat);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //获取LB空中链路频带
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getFrequencyBand(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkFrequencyBand>() {
                @Override
                public void onSuccess(LBAirLinkFrequencyBand lbAirLinkFrequencyBand) {
                    showFrequncyBand(lbAirLinkFrequencyBand);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //得到频带通道范围内
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getFrequencyBandChannelRange(new DJICommonCallbacks.DJICompletionCallbackWith<FrequencyBandChannelRange>() {
                @Override
                public void onSuccess(FrequencyBandChannelRange frequencyBandChannelRange) {
                    showFrequncybandCHannelRange(frequencyBandChannelRange);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });



            //获取当前区域中产品支持的频段。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getSupportedFrequencyBands(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkFrequencyBand[]>() {
                @Override
                public void onSuccess(LBAirLinkFrequencyBand[] lbAirLinkFrequencyBands) {
                    showSupportedFrequencyBands(lbAirLinkFrequencyBands);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //获取将由视频数据接收的视频数据。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getVideoDataChannel(new DJICommonCallbacks.DJICompletionCallbackWith<VideoDataChannel>() {
                @Override
                public void onSuccess(VideoDataChannel videoDataChannel) {
                    showVIdeoDataChannel(videoDataChannel);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //得到的视频信号强度
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getVideoSignalStrength(new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    showVideoSingnalStrength(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //设置代理回调以监听板载天线信号的更新状态。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().setDJILBAirLinkUpdatedLightbridgeModuleSignalInformationCallback(new DJILBAirLink.DJILBAirLinkUpdatedLightbridgeModuleSignalInformationCallback() {
                @Override
                public void onResult(ArrayList<DJISignalInformation> arrayList) {
                    showDJISignalInformation(arrayList);
                }
            });
        }



        //设置遥控器上的辅助视频输出端口。
        txt_camera_hd_hdmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DJIModuleVerificationUtil.isLBAirlinkAvailable()){
                    DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().setSecondaryVideoOutputPort(LBAirLinkSecondaryVideoOutputPort.HDMI, new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if(djiError == null){
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txt_camera_hd_hdmi.setTextColor(Color.RED);
                                        txt_camera_hd_hdminunmer = 0;
                                        txt_camera_sdi.setTextColor(Color.WHITE);
                                    }
                                });
                            }
                        }
                    });
                }
                //new HDSettingPresenter().getGetChannelBandwidth();
                getDataTime();
            }
        });

        txt_camera_sdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DJIModuleVerificationUtil.isLBAirlinkAvailable()){
                    DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().setSecondaryVideoOutputPort(LBAirLinkSecondaryVideoOutputPort.SDI, new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if(djiError == null) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txt_camera_sdi.setTextColor(Color.RED);
                                        txt_camera_hd_hdminunmer = 1;
                                        txt_camera_hd_hdmi.setTextColor(Color.WHITE);
                                    }
                                });
                            }
                        }
                    });
                }
                //new HDSettingPresenter().getGetChannelBandwidth();
                getDataTime();
            }
        });

        //图传质量
        setting_hd_camera_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(txt_number_quality !=null)txt_number_quality.setText(numberName[progress]);  //numberName[finalNumber]
                    }
                });
            }

            @Override  //表示进度条刚开始拖动，开始拖动时候触发的操作
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override  // 停止拖动时候
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(DJIModuleVerificationUtil.isLBAirlinkAvailable()) {
                    DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink()
                            .setDataRate(LBAirLinkDataRate.find(seekBar.getProgress()+1), new DJICommonCallbacks.DJICompletionCallback() {
                                @Override
                                public void onResult(DJIError djiError) {
                                    Utils.setResultToToast(getContext(), djiError == null ? "操作成功" : djiError.getDescription());
                                }
                            });
                }
            }
        });

        //带宽分配
        setting_bandwidth_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(numberMoble == 0){ //singe
                    txt_number_bandwidth.setText("LB:"+progress+"%EXT:"+(100-progress)+"%");
                }
                if(numberMoble == 1){
                    txt_number_bandwidth.setText("HDMI:"+progress+"%AV:"+(100-progress)+"%");
                }
            }
            @Override  //表示进度条刚开始拖动，开始拖动时候触发的操作
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override  // 停止拖动时候
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(numberMoble == 0 || seekBar.getProgress() == 100){
                    ext_rela.setVisibility(View.GONE);
                }else {
                    ext_rela.setVisibility(View.VISIBLE);
                }

                if(numberMoble == 1){
                    //双编码模式时
                    if (DJIModuleVerificationUtil.isLBAirlinkAvailable()) {
                        DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().
                                setDualEncodeModePercentage((float) (seekBar.getProgress() * 0.01), new DJICommonCallbacks.DJICompletionCallback() {
                                    @Override
                                    public void onResult(DJIError djiError) {
                                        Utils.setResultToToast(getContext(), djiError == null ? "操作成功" : djiError.getDescription());
                                    }
                                });
                    }
                }else if(numberMoble == 0){
                    if(DJIModuleVerificationUtil.isLBAirlinkAvailable()){
                        DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().
                                setFPVVideoBandwidthPercent(seekBar.getProgress(), new DJICommonCallbacks.DJICompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {

                            }
                        });
                    }
                }
            }
        });

        //开启EXT端口
        switch_ios_camera_ext.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch s, boolean isChecked) {
                if(isChecked){
                    if(DJIModuleVerificationUtil.isLBAirlinkAvailable()){
                        DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().setEncodeMode(LBAirLinkEncodeMode.Dual, new DJICommonCallbacks.DJICompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {
                                if(djiError ==null){//HDMI
                                    numberMoble = 1;
                                    switch_ios_camera_ext.setChecked(isChecked);
                                    getDataTime();

                                }
                            }
                        });}
                }else {
                    if(DJIModuleVerificationUtil.isLBAirlinkAvailable()){
                        DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().setEncodeMode(LBAirLinkEncodeMode.Single, new DJICommonCallbacks.DJICompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {  //lb
                                if(djiError == null){  //LB
                                    numberMoble = 0;
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            switch_ios_camera_ext.setChecked(!isChecked);
                                        }
                                    });
                                    getDataTime();
                                }
                            }
                        });}
                }
            }
        });

        //设置是否有辅助视频输出
        switch_ios_camera.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch s, boolean isChecked) {
                if(DJIModuleVerificationUtil.isLBAirlinkAvailable()){
                    DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().
                            setSecondaryVideoOutputEnabled(isChecked, new DJICommonCallbacks.DJICompletionCallback() {
                                @Override
                                public void onResult(DJIError djiError) {
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(djiError == null){
                                                switch_ios_camera.setChecked(isChecked);
                                                if(isChecked){
                                                    linear_video_in.setVisibility(View.VISIBLE);
                                                }else{
                                                    linear_video_in.setVisibility(View.GONE);
                                                }
                                            }
                                        }
                                    });
                                    Utils.setResultToToast(getContext(), djiError == null ? "操作成功" : djiError.getDescription());
                                }
                            });
                }
            }
        });

        //选择信道
        spinner_camera_hd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(DJIModuleVerificationUtil.isLBAirlinkAvailable()){
                    DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().setChannel(position + 1, new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //选择app输出模式
        spinner_setting_app_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(DJIModuleVerificationUtil.isLBAirlinkAvailable()){
                    int numbera = -1;
                    if(numberMoble == 0){  //lb
                        numbera = position;
                    }else if(numberMoble == 1){  //hdmi
                        numbera = position+2;
                    }
                    DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().setVideoDataChannel(VideoDataChannel.find(numbera), new DJICommonCallbacks.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            //new HDSettingPresenter().getGetChannelBandwidth();
                            getDataTime();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //视频输出分辨率
        spinner_camera_format.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(DJIModuleVerificationUtil.isLBAirlinkAvailable()){
                    if(txt_camera_hd_hdminunmer == 0){ //hd
                        DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().setHDMIOutputFormat(LBAirLinkSecondaryVideoFormat.find(position), new DJICommonCallbacks.DJICompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {

                            }
                        });
                    }else if(txt_camera_hd_hdminunmer == 1){  // sdi
                        DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().setSDIOutputFormat(LBAirLinkSecondaryVideoFormat.find(position), new DJICommonCallbacks.DJICompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    //自动选择信到   ----
    @OnClick(R.id.txt_camera_hd_motion)
    public void motionOnclick(View v){
        if(DJIModuleVerificationUtil.isLBAirlinkAvailable()){
            DJISampleApplication.getProductInstance().getAirLink().getLBAirLink().setChannelSelectionMode(ChannelSelectionMode.Auto, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(djiError == null){
                                txt_camera_hd_motion.setTextColor(Color.RED);
                                txt_camera_hd_custom.setTextColor(Color.WHITE);
                                rela_xinhao_one.setVisibility(View.GONE);
                                rela_xinhao_two.setVisibility(View.GONE);
                            }
                        }
                    });
                    Utils.setResultToToast(mActivity,djiError == null?"设置成功":djiError.getDescription());
                }
            });
        }
    }

    //自定义选择信道
    @OnClick(R.id.txt_camera_hd_custom)
    public void customOnclick(View v){
        if(DJIModuleVerificationUtil.isLBAirlinkAvailable()){
            DJISampleApplication.getProductInstance().getAirLink().getLBAirLink().setChannelSelectionMode(ChannelSelectionMode.Manual, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(djiError == null){
                                txt_camera_hd_custom.setTextColor(Color.RED);
                                txt_camera_hd_motion.setTextColor(Color.WHITE);
                                rela_xinhao_one.setVisibility(View.VISIBLE);
                                rela_xinhao_two.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            });
        }
    }

    //设置高画质
    @OnClick(R.id.txt_camera_mode_quality)
    public void modeQualityOnclick(View v){
        if(DJIModuleVerificationUtil.isLBAirlinkAvailable()){
            DJISampleApplication.getProductInstance().getAirLink().getLBAirLink().
                    setFPVQualityLatency(LBAirLinkFPVVideoQualityLatency.HighQuality, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if(djiError == null){
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txt_camera_mode_quality.setTextColor(Color.RED);
                                txt_camera_mode_delay.setTextColor(Color.WHITE);
                            }
                        });
                    }
                }
            });
        }
    }

    //设置低延迟
    @OnClick(R.id.txt_camera_mode_delay)
    public void modeDelayOnclick(View v){
        if(DJIModuleVerificationUtil.isLBAirlinkAvailable()){
            DJISampleApplication.getProductInstance().getAirLink().getLBAirLink().
                    setFPVQualityLatency(LBAirLinkFPVVideoQualityLatency.LowLatency, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if(djiError == null){
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txt_camera_mode_quality.setTextColor(Color.WHITE);
                                txt_camera_mode_delay.setTextColor(Color.RED);
                            }
                        });
                    }
                }
            });
        }
    }

    //@Override  //获取空中链路的当前下行信道。
    public void showDjiChannel(Integer integer) {
        //setToastMessage("获取空中链路的当前下行信道"+integer);
        sumRoutatus = integer;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner_camera_hd.setSelection(integer);
            }
        });
    }

    //@Override  //获取下行信道选择模式。
    public void showChannelSelectionMode(ChannelSelectionMode channelSelectionMode) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(channelSelectionMode.value() ==0){
                    txt_camera_hd_motion.setTextColor(Color.RED);
                    txt_camera_hd_custom.setTextColor(Color.WHITE);
                    rela_xinhao_one.setVisibility(View.GONE);
                    rela_xinhao_two.setVisibility(View.GONE);
                }
                if(channelSelectionMode.value() ==1){
                    txt_camera_hd_motion.setTextColor(Color.WHITE);
                    txt_camera_hd_custom.setTextColor(Color.RED);
                    rela_xinhao_one.setVisibility(View.VISIBLE);
                    rela_xinhao_two.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //@Override //获取当前下行数据速率（吞吐量）。
    public void showDataRate(LBAirLinkDataRate lbAirLinkDataRate) {
        //setToastMessage("获取当前下行数据速率（吞吐量）。"+lbAirLinkDataRate.name());
        int number = -1;
        if(lbAirLinkDataRate.name().equals("Bandwidth10Mbps")) number = 3;
        if(lbAirLinkDataRate.name().equals("Bandwidth4Mbps")) number = 0;
        if(lbAirLinkDataRate.name().equals("Bandwidth6Mbps")) number = 1;
        if(lbAirLinkDataRate.name().equals("Bandwidth8Mbps")) number = 2;
        int finalNumber = number;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(finalNumber > -1) {
                    setting_hd_camera_seek.setProgress(finalNumber);
                    if (txt_number_quality != null)
                        txt_number_quality.setText(numberName[finalNumber]);  //numberName[finalNumber]
                }
            }
        });
    }

    //@Override  //当启用双编码模式时，获取Lightbridge 2模块上的AV和HMDI输入之间的计算能力和带宽平衡。
    public void showDualEncodeModePercentage(Float aFloat) {
        hdmiAv = aFloat;
    }

    //@Override  //获取Lightbridge 2编码模式。
    public void showEncodeMode(LBAirLinkEncodeMode lbAirLinkEncodeMode) {
        //setToastMessage("获取Lightbridge 2编码模式。"+lbAirLinkEncodeMode.name());
        numberMoble = lbAirLinkEncodeMode.value();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(numberMoble == 1) {  //hdmi
                    switch_ios_camera_ext.setChecked(true);  //启用
                }else if(numberMoble == 0){
                    switch_ios_camera_ext.setChecked(false);
                }
                updateHdmiAvLbExt();
            }
        });
    }

    private void updateHdmiAvLbExt(){
        if(setting_bandwidth_seek !=null) {
            appintputMoudle.clear();
            if (numberMoble == 1) {
                setting_bandwidth_seek.setProgress((int) hdmiAv);
                txt_number_bandwidth.setText("HDMI:" + hdmiAv + "%AV:" + (100 - hdmiAv) + "%");
                appintputMoudle.addAll(getSourceAdapterList(R.array.appintput_modle_AV));
            } else if (numberMoble == 0) {
                setting_bandwidth_seek.setProgress(lbExt);
                txt_number_bandwidth.setText("LB:" + lbExt + "%EXT:" + (100 - lbExt) + "%");
                appintputMoudle.addAll(getSourceAdapterList(R.array.appintput_modle_LB));
            }
        }
        appintputMoudle.notifyDataSetChanged();
    }

    private ArrayList<String>  getSourceAdapterList(int i){
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(Arrays.asList(getResources().getStringArray(i)));
        return list;
    }

    //@Override  //获取FPV视频质量与延迟偏好。
    public void showFPVQualityLatency(LBAirLinkFPVVideoQualityLatency lbAirLinkFPVVideoQualityLatency) {
        //setToastMessage("//获取FPV视频质量与延迟偏好。"+lbAirLinkFPVVideoQualityLatency.name());
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(lbAirLinkFPVVideoQualityLatency.value() == 0){
                    txt_camera_mode_quality.setTextColor(Color.RED);
                    txt_camera_mode_delay.setTextColor(Color.WHITE);
                }
                if(lbAirLinkFPVVideoQualityLatency.value() == 1){
                    txt_camera_mode_quality.setTextColor(Color.WHITE);
                    txt_camera_mode_delay.setTextColor(Color.RED);
                }
            }
        });
    }

    //@Override  //获取专用于FPV摄像机的下行链路视频带宽百分比
    public void showFPVVideoBandwidthPercent(Integer integer) {
        //setToastMessage("获取专用于FPV摄像机的下行链路视频带宽百分比"+integer);
        //lbExt = integer;
    }

    //@Override  //获取LB空中链路频带
    public void showFrequncyBand(LBAirLinkFrequencyBand lbAirLinkFrequencyBand) {
        //setToastMessage("获取LB空中链路频带"+lbAirLinkFrequencyBand.name());
    }

    //@Override  //得到频带通道范围内
    public void showFrequncybandCHannelRange(FrequencyBandChannelRange frequencyBandChannelRange) {
        //setToastMessage("得到频带通道范围内"+frequencyBandChannelRange.getStartIndex()+"*/*"+frequencyBandChannelRange.getEndIndex());
    }

    //@Override  //获取遥控器HDMI视频端口输出视频格式
    public void showHDMIOutputFormat(LBAirLinkSecondaryVideoFormat lbAirLinkSecondaryVideoFormat) {
        //setToastMessage("获取遥控器HDMI视频端口输出视频格式"+lbAirLinkSecondaryVideoFormat.name());
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner_camera_format.setSelection(lbAirLinkSecondaryVideoFormat.value());
            }
        });
    }

    //@Override  //获取遥控器SDI视频端口输出视频格式。
    public void showSDIOutputFormat(LBAirLinkSecondaryVideoFormat lbAirLinkSecondaryVideoFormat) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner_camera_format.setSelection(lbAirLinkSecondaryVideoFormat.value());
            }
        });
    }

    //@Override  //确定是否启用遥控器上的辅助视频输出
    public void showSecondaryVIdeoOutputEnable(Boolean aBoolean) {
        //setToastMessage("确定是否启用遥控器上的辅助视频输出"+aBoolean);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch_ios_camera.setChecked(aBoolean);
                if(aBoolean){
                    linear_video_in.setVisibility(View.VISIBLE);
                }else {
                    linear_video_in.setVisibility(View.GONE);
                }
            }
        });
    }

    //@Override   //获取遥控器上的辅助视频输出端口
    public void showSecondaryVideoOutputPort(LBAirLinkSecondaryVideoOutputPort lbAirLinkSecondaryVideoOutputPort) {
        //setToastMessage("获取遥控器上的辅助视频输出端口"+lbAirLinkSecondaryVideoOutputPort.name());
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(lbAirLinkSecondaryVideoOutputPort.value() == 0){
                    txt_camera_hd_hdmi.setTextColor(Color.RED);
                    txt_camera_hd_hdminunmer = 0;
                    txt_camera_sdi.setTextColor(Color.WHITE);
                }
                if(lbAirLinkSecondaryVideoOutputPort.value()==1){
                    txt_camera_sdi.setTextColor(Color.RED);
                    txt_camera_hd_hdminunmer = 1;
                    txt_camera_hd_hdmi.setTextColor(Color.WHITE);
                }
            }
        });
    }

    //@Override   //获取当前区域中产品支持的频段
    public void showSupportedFrequencyBands(LBAirLinkFrequencyBand[] lbAirLinkFrequencyBands) {
        //setToastMessage("获取当前区域中产品支持的频段"+lbAirLinkFrequencyBands[0].name());
    }

    //@Override  //获取将由视频数据接收的视频数据
    public void showVIdeoDataChannel(VideoDataChannel videoDataChannel) {
        //setToastMessage("获取将由视频数据接收的视频数据"+videoDataChannel.name());
        int number =  videoDataChannel.value();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                appintputMoudle.clear();
                if(numberMoble == 0){  //LB
                    appintputMoudle.addAll(getSourceAdapterList(R.array.appintput_modle_LB));
                    spinner_setting_app_mode.setSelection(number);
                }else if(numberMoble == 1){
                    appintputMoudle.addAll(getSourceAdapterList(R.array.appintput_modle_AV));
                    spinner_setting_app_mode.setSelection(number-2);
                }
                appintputMoudle.notifyDataSetChanged();
            }
        });
    }

    //@Override  //得到的视频信号强度
    public void showVideoSingnalStrength(Integer integer) {
        //setToastMessage("得到的视频信号强度"+integer);
    }

    //@Override  //如果Lightbridge 2设备支持双编码模式
    public void showDualEncodeModePercentageBoolean(boolean dualEncodeModeSupported) {
        //setToastMessage("如果Lightbridge 2设备支持双编码模式"+dualEncodeModeSupported);
        /*mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(rela_video_out !=null){
                    if(dualEncodeModeSupported){
                        rela_video_out.setVisibility(View.VISIBLE);
                    }else rela_video_out.setVisibility(View.GONE);
                }
            }
        });*/
    }

    //@Override  //如果支持辅助视频输出
    public void showSecondaryVideoOutputSupported(boolean secondaryVideoOutputSupported) {
        //setToastMessage("如果支持辅助视频输出"+secondaryVideoOutputSupported);
    }

    //@Override //设置用于更新FPV视频带宽更改的代理回调。
    public void showFPVBandwidthPer(float v) {
        lbExt = (int) v;
    }

    //@Override  //设置DJILB空气链接更新所有通道干扰的回调函数
    public void showDJiLbairLinkUpdateAllChannelInterFerenceCalback(ChannelInterference[] channelInterferences) {
        //List<ChannelInterference> listChannelInterference =  Arrays.asList(channelInterferences);
        channelInterferences_relay = channelInterferences;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mliveRoomRecyclerViewAdapter !=null && channelInterferences.length>0) {
                    mliveRoomRecyclerViewAdapter.initData(channelInterferences,sumRoutatus);
                    mliveRoomRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    //@Override   //设置代理回调以监听板载天线信号的更新状态。
    public void showDJISignalInformation(ArrayList<DJISignalInformation> arrayList) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(arrayList.size()>=2){
                    if(txt_rssi_number_one !=null)txt_rssi_number_one.setText(arrayList.get(0).getPower()+"dBm");
                    if(txt_rssi_number_three !=null)txt_rssi_number_three.setText(arrayList.get(1).getPower()+"dBm");
                }
            }
        });
    }

    //@Override
    public void showDjiError(DJIError djiError) {
        if(djiError != null)setToastMessage(djiError.getDescription());
    }

    private void setToastMessage(String name){
        Utils.setResultToToast(mActivity,name);
    }

    private void getDataTime(){
        if(DJIModuleVerificationUtil.isLBAirlinkAvailable()) {
            //如果Lightbridge 2设备支持双编码模式。
            showDualEncodeModePercentageBoolean(DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().isDualEncodeModeSupported());

            //如果支持辅助视频输出。
            showSecondaryVideoOutputSupported(DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().isSecondaryVideoOutputSupported());

           /* //设置DJILB空气链接更新所有通道干扰的回调函数
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().setDJILBAirLinkUpdatedAllChannelInterferenceCallback(new DJILBAirLink.DJILBAirLinkUpdatedAllChannelInterferenceCallback() {
                @Override
                public void onResult(ChannelInterference[] channelInterferences) {
                    showDJiLbairLinkUpdateAllChannelInterFerenceCalback(channelInterferences);
                }
            });*/

        //确定是否启用遥控器上的辅助视频输出。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getSecondaryVideoOutputEnabled(new DJICommonCallbacks.DJICompletionCallbackWith<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    showSecondaryVIdeoOutputEnable(aBoolean);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //获取空中链路的当前下行信道。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getChannel(new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    showDjiChannel(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //获取下行信道选择模式。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getChannelSelectionMode(new DJICommonCallbacks.DJICompletionCallbackWith<ChannelSelectionMode>() {
                @Override
                public void onSuccess(ChannelSelectionMode channelSelectionMode) {
                    showChannelSelectionMode(channelSelectionMode);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //设置用于更新FPV视频带宽更改的代理回调。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().setDJILBAirLinkFPVBandwidthPercentChangedCallback(new DJILBAirLink.DJILBAirLinkFPVBandwidthPercentChangedCallback() {
                @Override
                public void onResult(float v) {
                    showFPVBandwidthPer(v);
                }
            });

            //获取当前下行数据速率（吞吐量）。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getDataRate(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkDataRate>() {
                @Override
                public void onSuccess(LBAirLinkDataRate lbAirLinkDataRate) {
                    showDataRate(lbAirLinkDataRate);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //获取FPV视频质量与延迟偏好。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getFPVQualityLatency(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkFPVVideoQualityLatency>() {
                @Override
                public void onSuccess(LBAirLinkFPVVideoQualityLatency lbAirLinkFPVVideoQualityLatency) {
                    showFPVQualityLatency(lbAirLinkFPVVideoQualityLatency);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //当启用双编码模式时，获取Lightbridge 2模块上的AV和HMDI输入之间的计算能力和带宽平衡。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getDualEncodeModePercentage(new DJICommonCallbacks.DJICompletionCallbackWith<Float>() {
                @Override
                public void onSuccess(Float aFloat) {
                    showDualEncodeModePercentage(aFloat);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });


            //获取专用于FPV摄像机的下行链路视频带宽百分比
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getFPVVideoBandwidthPercent(new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    showFPVVideoBandwidthPercent(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //获取Lightbridge 2编码模式。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getEncodeMode(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkEncodeMode>() {
                @Override
                public void onSuccess(LBAirLinkEncodeMode lbAirLinkEncodeMode) {
                    showEncodeMode(lbAirLinkEncodeMode);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });



            //获取遥控器上的辅助视频输出端口。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getSecondaryVideoOutputPort(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkSecondaryVideoOutputPort>() {
                @Override
                public void onSuccess(LBAirLinkSecondaryVideoOutputPort lbAirLinkSecondaryVideoOutputPort) {
                    showSecondaryVideoOutputPort(lbAirLinkSecondaryVideoOutputPort);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //获取遥控器HDMI视频端口输出视频格式。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getHDMIOutputFormat(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkSecondaryVideoFormat>() {
                @Override
                public void onSuccess(LBAirLinkSecondaryVideoFormat lbAirLinkSecondaryVideoFormat) {
                    showHDMIOutputFormat(lbAirLinkSecondaryVideoFormat);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

            //获取遥控器SDI视频端口输出视频格式。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getSDIOutputFormat(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkSecondaryVideoFormat>() {
                @Override
                public void onSuccess(LBAirLinkSecondaryVideoFormat lbAirLinkSecondaryVideoFormat) {
                    showSDIOutputFormat(lbAirLinkSecondaryVideoFormat);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });


            //获取将由视频数据接收的视频数据。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getVideoDataChannel(new DJICommonCallbacks.DJICompletionCallbackWith<VideoDataChannel>() {
                @Override
                public void onSuccess(VideoDataChannel videoDataChannel) {
                    showVIdeoDataChannel(videoDataChannel);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    showDjiError(djiError);
                }
            });

        }
    }
}
