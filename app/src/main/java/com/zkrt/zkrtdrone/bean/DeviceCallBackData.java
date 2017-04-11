package com.zkrt.zkrtdrone.bean;

/**
 * Created by admin on 2016/10/14.
 */

public class DeviceCallBackData {
    private String statusTemperatureOne;   //温度  1状态
    private String TemperatureOne;   //温度   温度1值为获取值除10，单位℃
    private String statusTemperatureTwo;  //温度状态 2
    private String TemperatureTwo;  //温度   温度1值为获取值除10，单位℃
    private String TemperatureLow;   //温度下限值为获取值除10，单位℃
    private String TemperatureHight;   //温度上限值为获取值除10，单位℃
    private String gas_Status;   //Gas_Status	11	第1位：气体1的状态  第2位：气体2的状态  第3位：气体3的状态  第4位：气体4的状态  所有位值默认为0，表示异常；值为1，表示正常
    private String gasNumOne;    //  气体名称 CO
    private String gasValueOne;  //   气体值
    private String gasNumTwo;  //   H2S，实际值为收到值除100
    private String gasValueTwo;  //
    private String gasNumThree;  //  NH3，
    private String gasValueThree;  //
    private String gasNumFour;  //  CO2
    private String gasValueFour;  //
    private String deviceStatus;  //  标识设备是否在线
    private String setFeedback;  //  设置反馈
    private String insmodStatus;  //管理单元电压值
    private String dateTime;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public DeviceCallBackData() {
    }

    public DeviceCallBackData(String statusTemperatureOne, String temperatureOne, String statusTemperatureTwo, String temperatureTwo, String temperatureLow, String temperatureHight, String gas_Status, String gasNumOne, String gasValueOne, String gasNumTwo, String gasValueTwo, String gasNumThree, String gasValueThree, String gasNumFour, String gasValueFour, String deviceStatus, String setFeedback, String insmodStatus, String dateTime) {
        this.statusTemperatureOne = statusTemperatureOne;
        TemperatureOne = temperatureOne;
        this.statusTemperatureTwo = statusTemperatureTwo;
        TemperatureTwo = temperatureTwo;
        TemperatureLow = temperatureLow;
        TemperatureHight = temperatureHight;
        this.gas_Status = gas_Status;
        this.gasNumOne = gasNumOne;
        this.gasValueOne = gasValueOne;
        this.gasNumTwo = gasNumTwo;
        this.gasValueTwo = gasValueTwo;
        this.gasNumThree = gasNumThree;
        this.gasValueThree = gasValueThree;
        this.gasNumFour = gasNumFour;
        this.gasValueFour = gasValueFour;
        this.deviceStatus = deviceStatus;
        this.setFeedback = setFeedback;
        this.insmodStatus = insmodStatus;
        this.dateTime = dateTime;
    }

    public String getStatusTemperatureOne() {
        return statusTemperatureOne;
    }

    public void setStatusTemperatureOne(String statusTemperatureOne) {
        this.statusTemperatureOne = statusTemperatureOne;
    }

    public String getTemperatureOne() {
        return TemperatureOne;
    }

    public void setTemperatureOne(String temperatureOne) {
        TemperatureOne = temperatureOne;
    }

    public String getStatusTemperatureTwo() {
        return statusTemperatureTwo;
    }

    public void setStatusTemperatureTwo(String statusTemperatureTwo) {
        this.statusTemperatureTwo = statusTemperatureTwo;
    }

    public String getTemperatureTwo() {
        return TemperatureTwo;
    }

    public void setTemperatureTwo(String temperatureTwo) {
        TemperatureTwo = temperatureTwo;
    }

    public String getTemperatureLow() {
        return TemperatureLow;
    }

    public void setTemperatureLow(String temperatureLow) {
        TemperatureLow = temperatureLow;
    }

    public String getTemperatureHight() {
        return TemperatureHight;
    }

    public void setTemperatureHight(String temperatureHight) {
        TemperatureHight = temperatureHight;
    }

    public String getGas_Status() {
        return gas_Status;
    }

    public void setGas_Status(String gas_Status) {
        this.gas_Status = gas_Status;
    }

    public String getGasNumOne() {
        return gasNumOne;
    }

    public void setGasNumOne(String gasNumOne) {
        this.gasNumOne = gasNumOne;
    }

    public String getGasValueOne() {
        return gasValueOne;
    }

    public void setGasValueOne(String gasValueOne) {
        this.gasValueOne = gasValueOne;
    }

    public String getGasNumTwo() {
        return gasNumTwo;
    }

    public void setGasNumTwo(String gasNumTwo) {
        this.gasNumTwo = gasNumTwo;
    }

    public String getGasValueTwo() {
        return gasValueTwo;
    }

    public void setGasValueTwo(String gasValueTwo) {
        this.gasValueTwo = gasValueTwo;
    }

    public String getGasNumThree() {
        return gasNumThree;
    }

    public void setGasNumThree(String gasNumThree) {
        this.gasNumThree = gasNumThree;
    }

    public String getGasValueThree() {
        return gasValueThree;
    }

    public void setGasValueThree(String gasValueThree) {
        this.gasValueThree = gasValueThree;
    }

    public String getGasNumFour() {
        return gasNumFour;
    }

    public void setGasNumFour(String gasNumFour) {
        this.gasNumFour = gasNumFour;
    }

    public String getGasValueFour() {
        return gasValueFour;
    }

    public void setGasValueFour(String gasValueFour) {
        this.gasValueFour = gasValueFour;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getSetFeedback() {
        return setFeedback;
    }

    public void setSetFeedback(String setFeedback) {
        this.setFeedback = setFeedback;
    }

    public String getInsmodStatus() {
        return insmodStatus;
    }

    public void setInsmodStatus(String insmodStatus) {
        this.insmodStatus = insmodStatus;
    }
}
