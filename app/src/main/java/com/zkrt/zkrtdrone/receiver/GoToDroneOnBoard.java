package com.zkrt.zkrtdrone.receiver;

import android.os.CountDownTimer;
import android.widget.EditText;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.bean.DeviceCallback;
import com.zkrt.zkrtdrone.log.FileClass;
import com.zkrt.zkrtdrone.log.crc.MAVLinkCRC;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;
import com.zkrt.zkrtdrone.until.HexToBinary;
import com.zkrt.zkrtdrone.view.dialog.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.rxbean.IOTask;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.rxbean.UITask;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.rxutil.RxjavaUtil;
import zkrtdrone.zkrt.com.maplib.App;

/**
 * Created by jack_xie on 17-2-13.
 */

public class GoToDroneOnBoard {
    private CustomDialog dialog;
    private  int numberList = 27;
    private int i = -1;
    public GoToDroneOnBoard(CustomDialog dialoga) {
        this.dialog = dialoga;
    }

    public void setGetPaoTou(int staut, int number, String nameVUA, EditText exit_low_value,
                             EditText exit_height_value,MAVLinkCRC mavLinkCRC,DeviceCallback mDeviceCallback,int no1,
                             int no2,int no7_1,int no7_2 ,int take_picture,int recording,boolean bol,int redQh,int colorQh,int modleQh,
                             int no72,String zoomStop,int no71){

        List<String> lists = new ArrayList<>();
        lists.add(mDeviceCallback.getStartCode());
        lists.add(mDeviceCallback.getVer());
        lists.add(mDeviceCallback.getSessionAck());
        lists.add(mDeviceCallback.getPaddingEnc());
        lists.add("00");
        lists.add(mDeviceCallback.getLength());
        lists.add(mDeviceCallback.getSeq());
        String[] strAppid= mDeviceCallback.getAppID().split(" ");
        lists.add(strAppid[0]);
        lists.add(strAppid[1]);
        lists.add(strAppid[2]);

        String[] strUavid= mDeviceCallback.getUavID().split(" ");
        lists.add(strUavid[0]);
        lists.add(strUavid[1]);
        lists.add(strUavid[2]);
        if(nameVUA.equals("ROP"))
            lists.add("06");
        if(nameVUA.equals("FLOOD_LIGHTING_POST"))
            lists.add("09");
        if(nameVUA.equals("Avoid_Temperature"))
            lists.add("01");
        if(nameVUA.equals("camera_mount"))
            lists.add("04");
        if(nameVUA.equals("camera_3d"))
            lists.add("0C");
        if(nameVUA.equals("OBSTACLE"))
            lists.add("64");
        if(nameVUA.equals("TRIPOD"))
            lists.add("64");
        lists.add(strUavid[4]);
        lists.add(strUavid[5]);
        lists.add(mDeviceCallback.getCommand());

        if(nameVUA.equals("camera_mount")){
            String no1str = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex(no1+"",false));
            lists.add(no1str.substring(0, 2));
            lists.add(no1str.substring(3, 5));

            String no2str = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex(no2+"",false));
            lists.add(no2str.substring(0, 2));
            lists.add(no2str.substring(3, 5));

            String no3str = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex("0",false));
            lists.add(no3str.substring(0, 2));
            lists.add(no3str.substring(3, 5));

            String no4str = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex("0",false));
            lists.add(no4str.substring(0, 2));
            lists.add(no4str.substring(3, 5));

// 9-10
            String no5str = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex(take_picture+"",false));
            lists.add(no5str.substring(0, 2));
            lists.add(no5str.substring(3, 5));

//11-12
            String no6str = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex(recording+"",false));
            lists.add(no6str.substring(0, 2));
            lists.add(no6str.substring(3, 5));
//

            String finalZoomStop = zoomStop;
            RxjavaUtil.doInIOThread(new IOTask<Object>() {
                @Override
                public void doInIOThread() {
                    FileClass.method1(App.app.getPackageName() + "/log/test/","zoomStop:"+ finalZoomStop +"\t\n"+
                            "Minno71:"+no71+"\t\n"+"no7_1:"+no7_1+"\t\n"+"Maxno72:"+no72+"\t\n"+"no7_2:"+no72+"\t\n");
                }
            });
            if(zoomStop.equals("stopMax")){
                String no7str_2 = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex(no72+"",false));
                lists.add(no7str_2.substring(0, 2));
            }else {
                String no7str_3 = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex(no7_2 + "", false));
                lists.add(no7str_3.substring(0, 2));
            }

            if(zoomStop.equals("stopMin")){
                String no7str_1 = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex(no71+"",false));
                lists.add(no7str_1.substring(0, 2));
            }else {
                String no7str_4 = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex(no7_1 + "", false));
                lists.add(no7str_4.substring(0, 2));
            }

            String no8str = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex("0",false));
            lists.add(no8str.substring(0, 2));
            lists.add(no8str.substring(3, 5));

            //红外切换
            String no9str = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex(redQh+"",false));
            lists.add(no9str.substring(0, 2));

            //颜色切换
            String no10str = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex(colorQh+"",false));
            lists.add(no10str.substring(0, 2));
            //模式切换
            String no11str = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex(modleQh+"",false));
            lists.add(no11str.substring(0, 2));

            for (int i = 0; i < 10; i++) {
                lists.add("00");
            }

            lists.add("0"+number);
        }else {

            if (nameVUA.equals("ROP")) {
                numberList = 27;
                if (staut == 1) {
                    lists.add("0" + number);
                    lists.add("ee");
                    lists.add("ee");
                }
                if (staut == 2) {
                    lists.add("ee");
                    lists.add("0" + number);
                    lists.add("ee");
                }
                if (staut == 3) {
                    lists.add("ee");
                    lists.add("ee");
                    lists.add("0" + number);
                }
            }

            if (nameVUA.equals("FLOOD_LIGHTING_POST")) {
                numberList = 29;
                lists.add("0" + number);
            }

            if (nameVUA.equals("Avoid_Temperature")) {
                numberList = 26;
                String low = exit_low_value.getText().toString();
                String height = exit_height_value.getText().toString();
                if (low.isEmpty()) low = "0";
                if (height.isEmpty()) height = "0";
                byte[] bytesa = HexToBinary.intengerTo16Hex((int)(Float.parseFloat(low)*10));
                String heightbytestr = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex(height,true));
                lists.add(HexToBinary.bytesToHexString(bytesa).substring(0,2));
                lists.add(HexToBinary.bytesToHexString(bytesa).substring(3,5));

                lists.add(heightbytestr.substring(0, 2));
                lists.add(heightbytestr.substring(3, 5));
            }

            if(nameVUA.equals("camera_3d")){
                numberList = 29;
                lists.add("0" + number);
            }

            if(nameVUA.equals("OBSTACLE")){
                numberList = 24;
                lists.add("0" + 1);
                lists.add("0" + number);
                if(zoomStop.isEmpty()) zoomStop = "0";
                String lowbytestr = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex(zoomStop,false));
                lists.add(lowbytestr.substring(0, 2));
                lists.add(lowbytestr.substring(3, 5));

                String lowbytestr2 = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex(staut+"",false));
                lists.add(lowbytestr2.substring(0, 2));
                lists.add(lowbytestr2.substring(3, 5));
            }

            if(nameVUA.equals("TRIPOD")){
                numberList = 22;
                lists.add("0" + 2);
                lists.add("0" + number);

                String lowbytestr = HexToBinary.bytesToHexString(HexToBinary.intengerTo16Hex("0",false));
                lists.add(lowbytestr.substring(0, 2));
                lists.add(lowbytestr.substring(3, 5));

                lists.add(lowbytestr.substring(0, 2));
                lists.add(lowbytestr.substring(3, 5));

                lists.add("0" + staut);  //jiaozhun
                lists.add("0" + Integer.parseInt(zoomStop));
            }

            for (int i = 0; i < numberList; i++) {
                lists.add("00");
            }
        }

        byte[] bytes_end = HexToBinary.HexString2Bytes(mDeviceCallback.getEndCode());
        byte[] bytes = new byte[lists.size()];
        byte[] bytes_length = new byte[lists.size()+3];
        for (int i=0;i<lists.size();i++){
            String name = lists.get(i);
            byte[] bytes1 = HexToBinary.HexString2Bytes(name.toString());
            bytes[i] = bytes1[0];
            bytes_length[i] = bytes1[0];
            bytes1.clone();
        }
        int numberbyte = mavLinkCRC.crc_calculate(bytes);

        int crc_b = (numberbyte & 0xff);
        int crc_c = ((numberbyte>>8) & 0xff);

        bytes_length[47] = (byte)crc_b;
        bytes_length[48] = (byte)crc_c;
        bytes_length[49] = bytes_end[0];
        bytes_end.clone();
        bytes.clone();

        if(!DJIModuleVerificationUtil.isFlightControllerAvailable()) return;
        DJISampleApplication.getAircraftInstance().getFlightController().sendDataToOnboardSDKDevice(bytes_length,
                new DJICommonCallbacks.DJICompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        if(djiError == null){
                            if(bol) {
                                if(nameVUA.equals("Avoid_Temperature")){
                                    RxjavaUtil.doInUIThread(new UITask<String>() {
                                        @Override
                                        public void doInUIThread() {
                                            dialog.serViewVisibility();
                                        }
                                    });
                                }

                                if (("ROP").equals(nameVUA) || "FLOOD_LIGHTING_POST".equals(nameVUA)) {

                                    final SweetAlertDialog pDialog = new
                                            SweetAlertDialog(DJISampleApplication.activity, SweetAlertDialog.PROGRESS_TYPE)
                                            .setTitleText("发送...");
                                    pDialog.show();
                                    pDialog.setCancelable(false);
                                    new CountDownTimer(800 * 2, 1) {
                                        public void onTick(long millisUntilFinished) {
                                            // you can change the progress bar color by ProgressHelper every 800 millis
                                            i++;
                                            switch (i){
                                                case 0:
                                                    pDialog.getProgressHelper().setBarColor(App.getResources().getColor(R.color.blue_btn_bg_color));
                                                    break;
                                                case 1:
                                                    pDialog.getProgressHelper().setBarColor(App.getResources().getColor(R.color.material_deep_teal_50));
                                                    break;
                                                case 2:
                                                    pDialog.getProgressHelper().setBarColor(App.getResources().getColor(R.color.success_stroke_color));
                                                    break;
                                                case 3:
                                                    pDialog.getProgressHelper().setBarColor(App.getResources().getColor(R.color.material_deep_teal_20));
                                                    break;
                                                case 4:
                                                    pDialog.getProgressHelper().setBarColor(App.getResources().getColor(R.color.material_blue_grey_80));
                                                    break;
                                                case 5:
                                                    pDialog.getProgressHelper().setBarColor(App.getResources().getColor(R.color.warning_stroke_color));
                                                    break;
                                                case 6:
                                                    pDialog.getProgressHelper().setBarColor(App.getResources().getColor(R.color.success_stroke_color));
                                                    break;
                                            }
                                        }

                                        public void onFinish() {
                                            i = -1;
                                            pDialog.dismiss();
                                        }
                                    }.start();
                                }
                            }
                        }
                    }
                });
    }
}
