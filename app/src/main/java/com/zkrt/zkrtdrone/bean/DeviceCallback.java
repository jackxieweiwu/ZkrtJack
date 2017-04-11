package com.zkrt.zkrtdrone.bean;

/**
 * Created by jack_xie on 2016/10/14.
 * 实体类
 */

public class DeviceCallback {
    private String startCode;  //固定值
    private String ver;         //协议版本
    private String sessionAck;    //f 前7位：表示通信过程 第8位：值 0：数据帧，1：应答帧􀀁
    private String paddingEnc;   //f
    private String CMD;         //f  APP_TO_UAV	上位机到下位机 值固定为：0    UAV_TO_APP 	下位机到上位机 值固定为：1
    private String length;     //字节长度
    private String appID;      //app的id
    private String uavID;      //飞机id
    private String command;    //保留字段，值为0
    private DeviceCallBackData deviceCallBackData;  //data心跳包数据
    private DeviceCallBackTwo deviceCallBackTwo;  //data 心跳包数据2

    public DeviceCallBackTwo getDeviceCallBackTwo() {
        return deviceCallBackTwo;
    }

    public void setDeviceCallBackTwo(DeviceCallBackTwo deviceCallBackTwo) {
        this.deviceCallBackTwo = deviceCallBackTwo;
    }

    private String crc;  //
    private String endCode;  //固定值BE
    private String seq;  //帧序列号，值自增
    private DroneGps droneGps;

    public DeviceCallback(String startCode, String ver, String sessionAck, String paddingEnc, String CMD, String length, String appID, String uavID, String command, DeviceCallBackData deviceCallBackData, DeviceCallBackTwo deviceCallBackTwo, String crc, String endCode, String seq, DroneGps droneGps) {
        this.startCode = startCode;
        this.ver = ver;
        this.sessionAck = sessionAck;
        this.paddingEnc = paddingEnc;
        this.CMD = CMD;
        this.length = length;
        this.appID = appID;
        this.uavID = uavID;
        this.command = command;
        this.deviceCallBackData = deviceCallBackData;
        this.deviceCallBackTwo = deviceCallBackTwo;
        this.crc = crc;
        this.endCode = endCode;
        this.seq = seq;
        this.droneGps = droneGps;
    }

    public DroneGps getDroneGps() {
        return droneGps;
    }

    public void setDroneGps(DroneGps droneGps) {
        this.droneGps = droneGps;
    }

    public DeviceCallback() {
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getStartCode() {
        return startCode;
    }

    public void setStartCode(String startCode) {
        this.startCode = startCode;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getSessionAck() {
        return sessionAck;
    }

    public void setSessionAck(String sessionAck) {
        this.sessionAck = sessionAck;
    }

    public String getPaddingEnc() {
        return paddingEnc;
    }

    public void setPaddingEnc(String paddingEnc) {
        this.paddingEnc = paddingEnc;
    }

    public String getCMD() {
        return CMD;
    }

    public void setCMD(String CMD) {
        this.CMD = CMD;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getUavID() {
        return uavID;
    }

    public void setUavID(String uavID) {
        this.uavID = uavID;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public DeviceCallBackData getDeviceCallBackData() {
        return deviceCallBackData;
    }

    public void setDeviceCallBackData(DeviceCallBackData deviceCallBackData) {
        this.deviceCallBackData = deviceCallBackData;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public String getEndCode() {
        return endCode;
    }

    public void setEndCode(String endCode) {
        this.endCode = endCode;
    }
}
