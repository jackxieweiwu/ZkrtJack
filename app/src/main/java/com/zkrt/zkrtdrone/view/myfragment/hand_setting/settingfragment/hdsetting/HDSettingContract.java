package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.hdsetting;


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
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseModel;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BasePresenter;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseView;

/**
 * Created by jack_xie on 17-2-21.
 */

public interface HDSettingContract {
    interface Model extends BaseModel {

    }

    interface View extends BaseView {
        void showDjiError(DJIError djiError);
        void showDjiChannel(Integer integer);  //获取空中链路的当前下行信道。
        void showChannelSelectionMode(ChannelSelectionMode channelSelectionMode);  //获取下行信道选择模式。
        void showDataRate(LBAirLinkDataRate lbAirLinkDataRate); //获取当前下行数据速率（吞吐量）。
        //void showDisplayOSDEnabled(Boolean aBoolean); //确定OSD是否覆盖在视频Feed上。
        void showDualEncodeModePercentage(Float aFloat); //当启用双编码模式时，获取Lightbridge 2模块上的AV和HMDI输入之间的计算能力和带宽平衡。
        void showEncodeMode(LBAirLinkEncodeMode lbAirLinkEncodeMode); //获取Lightbridge 2编码模式。
        void showFPVQualityLatency(LBAirLinkFPVVideoQualityLatency lbAirLinkFPVVideoQualityLatency); //获取FPV视频质量与延迟偏好。
        void showFPVVideoBandwidthPercent(Integer integer); //获取专用于FPV摄像机的下行链路视频带宽百分比
        void showFrequncyBand(LBAirLinkFrequencyBand lbAirLinkFrequencyBand); //获取LB空中链路频带
        void showFrequncybandCHannelRange(FrequencyBandChannelRange frequencyBandChannelRange); //得到频带通道范围内
        void showHDMIOutputFormat(LBAirLinkSecondaryVideoFormat lbAirLinkSecondaryVideoFormat); //获取遥控器HDMI视频端口输出视频格式。
        void showSDIOutputFormat(LBAirLinkSecondaryVideoFormat lbAirLinkSecondaryVideoFormat); //获取遥控器SDI视频端口输出视频格式。
        void showSecondaryVIdeoOutputEnable(Boolean aBoolean); //确定是否启用遥控器上的辅助视频输出。
        void showSecondaryVideoOutputPort(LBAirLinkSecondaryVideoOutputPort lbAirLinkSecondaryVideoOutputPort); //获取遥控器上的辅助视频输出端口。
        void showSupportedFrequencyBands(LBAirLinkFrequencyBand[] lbAirLinkFrequencyBands); //获取当前区域中产品支持的频段。
        void showVIdeoDataChannel(VideoDataChannel videoDataChannel);  //获取将由视频数据接收的视频数据。
        void showVideoSingnalStrength(Integer integer);  //得到的视频信号强度
        void showDualEncodeModePercentageBoolean(boolean dualEncodeModeSupported);//如果Lightbridge 2设备支持双编码模式。
        void showSecondaryVideoOutputSupported(boolean secondaryVideoOutputSupported); //如果支持辅助视频输出。
        void showFPVBandwidthPer(float v);////设置用于更新FPV视频带宽更改的代理回调。
        void showDJiLbairLinkUpdateAllChannelInterFerenceCalback(ChannelInterference[] channelInterferences); //设置DJILB空气链接更新所有通道干扰的回调函数
        void showDJISignalInformation(ArrayList<DJISignalInformation> arrayList);  //设置代理回调以监听板载天线信号的更新状态。
    }
    abstract class Presenter extends BasePresenter<HDSettingContract.Model, HDSettingContract.View> {
        public abstract void getGetChannelBandwidth();
    }
}
