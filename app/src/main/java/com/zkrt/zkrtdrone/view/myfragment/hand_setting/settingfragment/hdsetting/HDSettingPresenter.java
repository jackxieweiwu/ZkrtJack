package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.hdsetting;


import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;

import java.util.ArrayList;

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
import dji.sdk.airlink.DJIAuxLink;
import dji.sdk.airlink.DJILBAirLink;


/**
 * Created by jack_xie on 17-2-21.
 */

public class HDSettingPresenter extends HDSettingContract.Presenter{
    @Override
    public void onAttached() {
        getGetChannelBandwidth();
    }

    @Override
    public void getGetChannelBandwidth() {
        if(DJIModuleVerificationUtil.isLBAirlinkAvailable()) {
            //如果Lightbridge 2设备支持双编码模式。
            mView.showDualEncodeModePercentageBoolean(DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().isDualEncodeModeSupported());

            //如果支持辅助视频输出。
            mView.showSecondaryVideoOutputSupported(DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().isSecondaryVideoOutputSupported());

            //设置DJILB空气链接更新所有通道干扰的回调函数
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().setDJILBAirLinkUpdatedAllChannelInterferenceCallback(new DJILBAirLink.DJILBAirLinkUpdatedAllChannelInterferenceCallback() {
                @Override
                public void onResult(ChannelInterference[] channelInterferences) {
                    mView.showDJiLbairLinkUpdateAllChannelInterFerenceCalback(channelInterferences);
                }
            });

//确定是否启用遥控器上的辅助视频输出。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getSecondaryVideoOutputEnabled(new DJICommonCallbacks.DJICompletionCallbackWith<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    mView.showSecondaryVIdeoOutputEnable(aBoolean);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });

            //获取空中链路的当前下行信道。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getChannel(new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    mView.showDjiChannel(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });

            //获取下行信道选择模式。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getChannelSelectionMode(new DJICommonCallbacks.DJICompletionCallbackWith<ChannelSelectionMode>() {
                @Override
                public void onSuccess(ChannelSelectionMode channelSelectionMode) {
                    mView.showChannelSelectionMode(channelSelectionMode);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });

            //设置用于更新FPV视频带宽更改的代理回调。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().setDJILBAirLinkFPVBandwidthPercentChangedCallback(new DJILBAirLink.DJILBAirLinkFPVBandwidthPercentChangedCallback() {
                @Override
                public void onResult(float v) {
                    mView.showFPVBandwidthPer(v);
                }
            });

            //获取当前下行数据速率（吞吐量）。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getDataRate(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkDataRate>() {
                @Override
                public void onSuccess(LBAirLinkDataRate lbAirLinkDataRate) {
                    mView.showDataRate(lbAirLinkDataRate);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });

            //确定OSD是否覆盖在视频Feed上。
            /*DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getDisplayOSDEnabled(new DJICommonCallbacks.DJICompletionCallbackWith<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    mView.showDisplayOSDEnabled(aBoolean);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });*/

            //获取FPV视频质量与延迟偏好。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getFPVQualityLatency(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkFPVVideoQualityLatency>() {
                @Override
                public void onSuccess(LBAirLinkFPVVideoQualityLatency lbAirLinkFPVVideoQualityLatency) {
                    mView.showFPVQualityLatency(lbAirLinkFPVVideoQualityLatency);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });

            //当启用双编码模式时，获取Lightbridge 2模块上的AV和HMDI输入之间的计算能力和带宽平衡。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getDualEncodeModePercentage(new DJICommonCallbacks.DJICompletionCallbackWith<Float>() {
                @Override
                public void onSuccess(Float aFloat) {
                    mView.showDualEncodeModePercentage(aFloat);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });


            //获取专用于FPV摄像机的下行链路视频带宽百分比
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getFPVVideoBandwidthPercent(new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    mView.showFPVVideoBandwidthPercent(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });

            //获取Lightbridge 2编码模式。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getEncodeMode(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkEncodeMode>() {
                @Override
                public void onSuccess(LBAirLinkEncodeMode lbAirLinkEncodeMode) {
                    mView.showEncodeMode(lbAirLinkEncodeMode);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });



            //获取遥控器上的辅助视频输出端口。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getSecondaryVideoOutputPort(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkSecondaryVideoOutputPort>() {
                @Override
                public void onSuccess(LBAirLinkSecondaryVideoOutputPort lbAirLinkSecondaryVideoOutputPort) {
                    mView.showSecondaryVideoOutputPort(lbAirLinkSecondaryVideoOutputPort);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });

            //获取遥控器HDMI视频端口输出视频格式。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getHDMIOutputFormat(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkSecondaryVideoFormat>() {
                @Override
                public void onSuccess(LBAirLinkSecondaryVideoFormat lbAirLinkSecondaryVideoFormat) {
                    mView.showHDMIOutputFormat(lbAirLinkSecondaryVideoFormat);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });

            //获取遥控器SDI视频端口输出视频格式。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getSDIOutputFormat(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkSecondaryVideoFormat>() {
                @Override
                public void onSuccess(LBAirLinkSecondaryVideoFormat lbAirLinkSecondaryVideoFormat) {
                    mView.showSDIOutputFormat(lbAirLinkSecondaryVideoFormat);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });

            //获取LB空中链路频带
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getFrequencyBand(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkFrequencyBand>() {
                @Override
                public void onSuccess(LBAirLinkFrequencyBand lbAirLinkFrequencyBand) {
                    mView.showFrequncyBand(lbAirLinkFrequencyBand);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });

            //得到频带通道范围内
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getFrequencyBandChannelRange(new DJICommonCallbacks.DJICompletionCallbackWith<FrequencyBandChannelRange>() {
                @Override
                public void onSuccess(FrequencyBandChannelRange frequencyBandChannelRange) {
                    mView.showFrequncybandCHannelRange(frequencyBandChannelRange);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });



            //获取当前区域中产品支持的频段。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getSupportedFrequencyBands(new DJICommonCallbacks.DJICompletionCallbackWith<LBAirLinkFrequencyBand[]>() {
                @Override
                public void onSuccess(LBAirLinkFrequencyBand[] lbAirLinkFrequencyBands) {
                    mView.showSupportedFrequencyBands(lbAirLinkFrequencyBands);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });

            //获取将由视频数据接收的视频数据。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getVideoDataChannel(new DJICommonCallbacks.DJICompletionCallbackWith<VideoDataChannel>() {
                @Override
                public void onSuccess(VideoDataChannel videoDataChannel) {
                    mView.showVIdeoDataChannel(videoDataChannel);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });

            //得到的视频信号强度
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().getVideoSignalStrength(new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    mView.showVideoSingnalStrength(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    mView.showDjiError(djiError);
                }
            });

            //设置代理回调以监听板载天线信号的更新状态。
            DJISampleApplication.getAircraftInstance().getAirLink().getLBAirLink().setDJILBAirLinkUpdatedLightbridgeModuleSignalInformationCallback(new DJILBAirLink.DJILBAirLinkUpdatedLightbridgeModuleSignalInformationCallback() {
                @Override
                public void onResult(ArrayList<DJISignalInformation> arrayList) {
                    mView.showDJISignalInformation(arrayList);
                }
            });
        }
    }
}
