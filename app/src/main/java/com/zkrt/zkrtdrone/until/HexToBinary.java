package com.zkrt.zkrtdrone.until;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.bean.DeviceCallBackData;
import com.zkrt.zkrtdrone.bean.DeviceCallBackTwo;
import com.zkrt.zkrtdrone.bean.DeviceCallback;
import com.zkrt.zkrtdrone.bean.Mephitis;
import com.zkrt.zkrtdrone.bean.Module;
import com.zkrt.zkrtdrone.bean.MoudleBean;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jack_xie on 2016/10/25.
 * 进制转换
 */

public class HexToBinary {
    //16进制转二进制
    public static String Hex2Binary(String hexString){
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++){
            tmp = "0000"
                    + Integer.toBinaryString(Integer.parseInt(hexString
                    .substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    //十六进制进行解析
    public static DeviceCallback analysisByte(String lendName){
        //第一个字段为1个字节
        String regex = ",|，|\\s+";
        String[] strAry = lendName.split(regex);
        if("EB".equalsIgnoreCase(strAry[0])){
            DeviceCallback deviceCallback = new DeviceCallback();
            deviceCallback.setStartCode(strAry[0]);
            deviceCallback.setVer(strAry[1]);
            deviceCallback.setSessionAck(strAry[2]);  //f
            deviceCallback.setPaddingEnc(strAry[3]);  //f
            deviceCallback.setCMD(strAry[4]);
            deviceCallback.setLength(strAry[5]);
            deviceCallback.setSeq(strAry[6]);
            deviceCallback.setAppID(strAry[7]+" "+strAry[8]+" "+strAry[9]); //appid
            deviceCallback.setUavID(strAry[10]+" "+strAry[11]+" "+
                    strAry[12]+" "+strAry[13]+" "+strAry[14]+" "+strAry[15]); //UavId
            deviceCallback.setCommand(strAry[16]);
            if(strAry[13].equals("65")){
                DeviceCallBackData deviceCallBackData = new DeviceCallBackData();
                //data 数据
                deviceCallBackData.setStatusTemperatureOne(strAry[17]);
                deviceCallBackData.setTemperatureOne(strAry[19]+" "+strAry[18]);
                deviceCallBackData.setStatusTemperatureTwo(strAry[20]);
                deviceCallBackData.setTemperatureTwo(strAry[22]+" "+strAry[21]);
                deviceCallBackData.setTemperatureLow(strAry[24]+" "+strAry[23]);
                deviceCallBackData.setTemperatureHight(strAry[26]+" "+strAry[25]);
                deviceCallBackData.setGas_Status(strAry[27]);
                deviceCallBackData.setGasNumOne(strAry[28]);
                deviceCallBackData.setGasValueOne(strAry[30]+" "+strAry[29]);
                deviceCallBackData.setGasNumTwo(strAry[31]);
                deviceCallBackData.setGasValueTwo(strAry[33]+" "+strAry[32]);
                deviceCallBackData.setGasNumThree(strAry[34]);
                deviceCallBackData.setGasValueThree(strAry[36]+" "+strAry[35]);
                deviceCallBackData.setGasNumFour(strAry[37]);
                deviceCallBackData.setGasValueFour(strAry[39]+" "+strAry[38]);
                deviceCallBackData.setDeviceStatus(strAry[42]+" "+strAry[41]+" "+strAry[40]);  //前低后高
                deviceCallBackData.setSetFeedback(strAry[45]+" "+strAry[44]+" "+strAry[43]);
                deviceCallBackData.setInsmodStatus(strAry[46]);
                deviceCallBackData.setDateTime(TimeUtil.getTimeDate());  //time
                deviceCallback.setDeviceCallBackData(deviceCallBackData);

            }else if(strAry[13].equals("66")){
                DeviceCallBackTwo deviceCallBackTwo = new DeviceCallBackTwo();
                deviceCallBackTwo.setObstacleDistance(strAry[18]+" "+strAry[17]);//  下方向障碍物距离 cm
                deviceCallBackTwo.setObstacleFrontBarrier(strAry[20]+" "+strAry[19]);//前方向障碍物距离
                deviceCallBackTwo.setObstacleRightBarrier(strAry[22]+" "+strAry[21]);//右方向障碍物距离
                deviceCallBackTwo.setObstacleQueenBarrier(strAry[24]+" "+strAry[23]);//后方向障碍物距离
                deviceCallBackTwo.setObstacleLeftBarrier(strAry[26]+" "+strAry[25]);//左方向障碍物距离
                deviceCallBackTwo.setObstacleEnabled(strAry[27]);//避障使能状态
                deviceCallBackTwo.setObstacleDistance(strAry[29]+" "+strAry[28]);//避障生效距离
                deviceCallBackTwo.setObstacleSpeed(strAry[31]+" "+strAry[30]);//避障速度  /10
                deviceCallBackTwo.setTripodStatus(strAry[32]);//脚架状态
                deviceCallBackTwo.setTripodDistance(strAry[33]);//脚架自动收放功能使能状态
                deviceCallBackTwo.setTripodAngle(strAry[35]+" "+strAry[34]);//脚架当前角度值
                deviceCallBackTwo.setTripodLangAngle(strAry[37]+" "+strAry[36]);//脚架自动降落角度的设置值
                deviceCallBackTwo.setTripodFewerAngle(strAry[39]+" "+strAry[38]);//脚架自动收起角度的设置值
                deviceCallBackTwo.setReserve(strAry[40]+" "+
                        strAry[41]+" "+strAry[42]+" "+strAry[43]+" "+strAry[44]+" "+strAry[45]+" "+strAry[46]);//备用
                deviceCallback.setDeviceCallBackTwo(deviceCallBackTwo);

            }
            deviceCallback.setCrc(strAry[48]+" "+strAry[47]);
            deviceCallback.setEndCode(strAry[49]);
            return deviceCallback;
        }
        return null;
    }

    //byte解析成16进制
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv).append(" ");
        }
        return stringBuilder.toString();
    }

    // 从十六进制字符串到字节数组转换
    public static byte[] HexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    private static int parse(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }

    //解析24位字符  是否有有哪些是1
    public static Module HexOneMode(String name){
        Module module = new Module();
        for(int i=-1; i<=name.lastIndexOf("1");++i){
            i=name.indexOf("1",i);
            module =  setMoudleNum(24-i,module);
        }
        return module;
    }

    //输入值
    public static Module setMoudleNum(int i,Module module){
        if(i == 1) module.setDEVICE_TYPE_TEMPERATURE(1);   //温度模块
        if(i == 2) module.setDEVICE_TYPE_OBSTACLE(1);   //壁障模块
        if(i == 3) module.setDEVICE_TYPE_CONTROL(1);   //备用
        if(i == 4) module.setDEVICE_TYPE_CAMERA(1);   //相机模块
        if(i == 5) module.setDEVICE_TYPE_GAS(1);   //毒气模块
        if(i == 6) module.setDEVICE_TYPE_THROW(1);   //抛投模块
        if(i == 7) module.setDEVICE_TYPE_STANDBY(1);   //备用
        if(i == 8) module.setDEVICE_TYPE_PARACHUTE(1);   //降落伞
        if(i == 9) module.setDEVICE_TYPE_IRRADIATE(1);   //照射模块
        if(i == 10) module.setDEVICE_TYPE_MEGAPHONE(1);   //喊话模块
        if(i == 11) module.setDEVICE_TYPE_BATTERY(1);   //电池模块
        if(i == 12) module.setDEVICE_TYPE_3DMODELING(1);   //3DModel
        return module;
    }

    //解析毒气模块的四个探头是否是正常
    public static Mephitis getDuqi(String name){
        Mephitis mephitis = new Mephitis();
        for(int i=-1; i<=name.lastIndexOf("1");++i){
            i=name.indexOf("1",i);
            mephitis =  getToMephitis(8-i,mephitis);
        }
        return mephitis;
    }

    //毒气输入值
    public static Mephitis getToMephitis(int i , Mephitis mephitis){
        if(i == 1) mephitis.setDEVICE_TYPE_GAS_CO(1);
        if(i == 2) mephitis.setDEVICE_TYPE_GAS_H2S(1);
        if(i == 3) mephitis.setDEVICE_TYPE_GAS_NH3(1);
        if(i == 4) mephitis.setDEVICE_TYPE_GAS_CO2(1);
        return mephitis;
    }

    public static int HexTo10Integer(String name){
        return Integer.parseInt(name.replace(" ",""),16);
    }

    public static float HexTo10IntegerShort(String name){
        short s = (short) (Integer.parseInt(name.replace(" ",""),16));
        return (float)s/10f;
    }

    public static byte[] intengerTo16Hex(String number,boolean bool) {
        float n = Float.parseFloat(number);
        if(bool) n = n*10f;
        int nn = (int) n;
        byte[] b = new byte[4];
        b[0] = (byte) (nn & 0xff);
        b[1] = (byte) (nn >> 8 & 0xff);
       /* b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);*/
        return b;
    }

    public static byte[] intengerTo16Hex(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
       /* b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);*/
        return b;
    }


    //解析模块是否挂载
    public static List<MoudleBean> setGetMoudle(String[] moudleName, TypedArray moudleImg, Module module, Context context){
        List<MoudleBean> list_bean = new LinkedList<>();
        for(int i=0;i<moudleName.length;i++){
            Drawable drawable = null;
            boolean bool = false;
            if(i == 0){  //电池
                if(module.getDEVICE_TYPE_BATTERY() == 1){
                    drawable = ContextCompat.getDrawable(context,R.mipmap.pic_battery_onling);
                    bool = true;
                }else{
                    drawable = moudleImg.getDrawable(i);
                    bool = false;
                }
            }else if(i == 1){//喊话
                if(module.getDEVICE_TYPE_MEGAPHONE() == 1){
                    drawable = ContextCompat.getDrawable(context,R.mipmap.pic_horn_online);
                    bool = true;
                }else{
                    drawable = moudleImg.getDrawable(i);
                    bool = false;
                }
            }else if(i == 2){//抛投
                if(module.getDEVICE_TYPE_THROW() == 1){
                    drawable = ContextCompat.getDrawable(context,R.mipmap.pic_toss_online);
                    bool = true;
                }else{
                    drawable = moudleImg.getDrawable(i);
                    bool = false;
                }
            }else if(i == 3){//有毒气体
                if(module.getDEVICE_TYPE_GAS() == 1){
                    drawable = ContextCompat.getDrawable(context,R.mipmap.pic_gas_online);
                    bool = true;
                }else{
                    drawable = moudleImg.getDrawable(i);
                    bool = false;
                }
            }else if(i == 4){//降落伞
                if(module.getDEVICE_TYPE_PARACHUTE() == 1){
                    drawable = ContextCompat.getDrawable(context,R.mipmap.pic_parachute_online);
                    bool = true;
                }else{
                    drawable = moudleImg.getDrawable(i);
                    bool = false;
                }
            }else if(i == 5){//避障
                if(module.getDEVICE_TYPE_OBSTACLE() == 1){
                    drawable = ContextCompat.getDrawable(context,R.mipmap.pic_tof_online);
                    bool = true;
                }else{
                    drawable = moudleImg.getDrawable(i);
                    bool = false;
                }
            }else if(i == 6){//避温
                if(module.getDEVICE_TYPE_TEMPERATURE() == 1){
                    drawable = ContextCompat.getDrawable(context,R.mipmap.pic_temperature_online);
                    bool = true;
                }else{
                    drawable = moudleImg.getDrawable(i);
                    bool = false;
                }
            }else if(i == 7){//双光红外
                if(false){  //module.getDEVICE_TYPE_THROW() == 1
                    drawable = ContextCompat.getDrawable(context,R.mipmap.pic_infrared_online);
                    bool = true;
                }else{
                    drawable = moudleImg.getDrawable(i);
                    bool = false;
                }
            }else if(i == 8){//三维成像
                if(module.getDEVICE_TYPE_3DMODELING() == 1){  //module.getDEVICE_TYPE_THROW() == 1
                    drawable = ContextCompat.getDrawable(context,R.mipmap.pic_3d_online);
                    bool = true;
                }else{
                    drawable = moudleImg.getDrawable(i);
                    bool = false;
                }
            }else if(i == 9){//爆闪
                if(false){  //module.getDEVICE_TYPE_THROW() == 1
                    drawable = ContextCompat.getDrawable(context,R.mipmap.pic_multispectral_online);
                    bool = true;
                }else{
                    drawable = moudleImg.getDrawable(i);
                    bool = false;
                }
            }else if(i == 10){//探照灯
                if(module.getDEVICE_TYPE_IRRADIATE() == 1){
                    drawable = ContextCompat.getDrawable(context,R.mipmap.pic_searchlight_online);
                    bool = true;
                }else{
                    drawable = moudleImg.getDrawable(i);
                    bool = false;
                }
            }else if(i == 11){
                if(module.getDEVICE_TYPE_CAMERA() == 1){
                    drawable = ContextCompat.getDrawable(context,R.mipmap.pic_radar_online);
                    bool = true;
                }else{
                    drawable = moudleImg.getDrawable(i);
                    bool = false;
                }
            }
            list_bean.add(new MoudleBean(drawable,moudleName[i],bool));
        }
        return list_bean;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
