package com.zkrt.zkrtdrone.view.myfragment.mount;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.bean.DeviceCallback;
import com.zkrt.zkrtdrone.bean.Mephitis;
import com.zkrt.zkrtdrone.bean.Module;
import com.zkrt.zkrtdrone.until.HexToBinary;
import com.zkrt.zkrtdrone.view.dialog.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseMvpFragment;

import static com.zkrt.zkrtdrone.until.HandMapUtil.getKeyListBySet;
import static com.zkrt.zkrtdrone.until.HandMapUtil.getKeySetByMap;
import static com.zkrt.zkrtdrone.until.HandMapUtil.getListByMap;

/**
 * Created by jack_xie on 16-12-23.
 */

public class MountFragment extends BaseMvpFragment<MountPresenter,MountModel> implements
        MountContract.View {
    private CustomDialog dialog;
    @BindView(R.id.txt_temperature_one) TextView txt_temperature_one;
    @BindView(R.id.txt_temperature_two) TextView txt_temperature_two;
    @BindView(R.id.horiz_error) HorizontalBarChart mChart;
    private String shar_co,shar_h2s,shar_nh3,shar_co2;
    @BindView(R.id.txt_co_error) TextView txt_co_error;
    @BindView(R.id.txt_h2s_error) TextView txt_h2s_error;
    @BindView(R.id.txt_nh3_error) TextView txt_nh3_error;
    @BindView(R.id.txt_co2_error) TextView txt_co2_error;

    private LinearLayout linear_obstacle;
    private TextView txt_obstacle_left;
    private TextView txt_obstacle_right;
    private TextView txt_obstacle_front;
    private TextView txt_obstacle_back;
    //private boolean boolOne = false,boolTwo = false;
    private int numberOneState = -1,numberTwoState = -1;  //0:温度1异常  1:温度1超过上限  2:温度1超过下限 3:ok


    private int co_number=0,h2s_number=0,nh3_number=0,co2_number=0;
    private Map<String,String> map_co;
    private SharedPreferences sharedPreferences;
    private int colorOne = Color.WHITE,colorTwo = Color.WHITE;
    private float numValues1 = 0;
    private float numValues2 = 0;
    private RelativeLayout  mount_camera_linear;
    private RelativeLayout camera_mount_joy;
    private RelativeLayout  model_camera_rela;
    private FloatingActionButton fram_mount;
    private FrameLayout frame_mount;
    private Typeface tf;
    @Override
    protected int getFragmentViewId() {
        return R.layout.mount;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        map_co = new HashMap<String,String>();
        chatError();
        frame_mount = (FrameLayout) mActivity.findViewById(R.id.frame_mount);
    }

    public void setLinear(RelativeLayout  mount_camera_linears,RelativeLayout  model_camera_relas,
                          RelativeLayout camera_mount_joys){
        this.mount_camera_linear = mount_camera_linears;
        this.model_camera_rela = model_camera_relas;
        this.camera_mount_joy = camera_mount_joys;
    }

    public void chatError(){
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");
        mChart.setMaxVisibleValueCount(60);
        //mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        tf = Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Regular.ttf");
        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(tf);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGridLineWidth(0.3f);
        xl.setTextColor(Color.WHITE);

        YAxis yl = mChart.getAxisLeft();
        yl.setTypeface(tf);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setGridLineWidth(0.3f);
        yl.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        yl.setTextColor(Color.WHITE);
//        yl.setInverted(true);

        YAxis yr = mChart.getAxisRight();
        yr.setTypeface(tf);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        yr.setTextColor(Color.WHITE);
//        yr.setInverted(true);
        mChart.getRendererXAxis();

        setData();
        mChart.animateY(2500);
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
        l.setTextColor(Color.WHITE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void getViewFindByid(View view) {
        super.getViewFindByid(view);
        fram_mount = (FloatingActionButton) mActivity.findViewById(R.id.fram_mount);
        linear_obstacle = (LinearLayout) mActivity.findViewById(R.id.linear_obstacle);
        txt_obstacle_left = (TextView) mActivity.findViewById(R.id.txt_obstacle_left);
        txt_obstacle_right = (TextView) mActivity.findViewById(R.id.txt_obstacle_right);
        txt_obstacle_front = (TextView) mActivity.findViewById(R.id.txt_obstacle_front);
        txt_obstacle_back = (TextView) mActivity.findViewById(R.id.txt_obstacle_queen);

        fram_mount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog != null){
                    dialog.dismiss();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            }
        });
    }

    private void dialogUpdate(Module module) {
        if(module != null) {
            DJISampleApplication.DEVICE_TYPE_CAMERA = module.getDEVICE_TYPE_CAMERA();
            fram_mount.setVisibility(View.VISIBLE);
            if (module.getDEVICE_TYPE_3DMODELING() == 1) {
                model_camera_rela.setVisibility(View.VISIBLE);
                //mount_camera_linear.setVisibility(View.GONE);  //双光 三光
            } else {
                model_camera_rela.setVisibility(View.GONE);
            }
            if (module.getDEVICE_TYPE_CAMERA() == 1) {
                model_camera_rela.setVisibility(View.GONE);
                //mount_camera_linear.setVisibility(View.VISIBLE); 双光 三光
            } else {
                //mount_camera_linear.setVisibility(View.GONE); 双光 三光
            }
        }else{
            fram_mount.setVisibility(View.GONE);
            model_camera_rela.setVisibility(View.GONE);
            mount_camera_linear.setVisibility(View.GONE);
        }
    }

    private void setData() {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();
        if(map_co.size()<=0){
            return;
        }
        List<String> keyListBySet = getKeyListBySet(getKeySetByMap(map_co));
        List<String> keyList = getListByMap(map_co, true);
        List<String> valuesList = getListByMap(map_co, false);
        for (int i = 0; i < keyListBySet.size(); i++) {
            xVals.add(keyList.get(i));
            yVals1.add(new BarEntry(Float.parseFloat(valuesList.get(i)),i));
        }

        mChart.clear();
        BarDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setYVals(yVals1);
            set1.setValueTextColor(Color.WHITE);
            mChart.getData().setXVals(xVals);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "含量值");
            set1.setValueTextColor(Color.WHITE);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(xVals, dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(tf);
            mChart.setData(data);
        }
    }

    @Override
    public void showDialog(CustomDialog dialogs) {
        this.dialog = dialogs;
    }

    @Override
    public void showSharedPreferences(SharedPreferences sharedPreferencesa) {
        this.sharedPreferences = sharedPreferencesa;
         shar_co = sharedPreferences.getString("CO", "");
         shar_h2s = sharedPreferences.getString("H2S", "");
         shar_nh3 = sharedPreferences.getString("NH3", "");
         shar_co2 = sharedPreferences.getString("CO2", "");
        if(shar_co.isEmpty()) shar_co = "0";
        if(shar_h2s.isEmpty()) shar_h2s = "0";
        if(shar_nh3.isEmpty()) shar_nh3 = "0";
        if(shar_co2.isEmpty()) shar_co2 = "0";
    }

    @Override
    public void showMoudleNameImg(String[] moudleNames, TypedArray moudleImgs) {
        //this.moudleName = moudleNames;
        //this.moudleImg = moudleImgs;
    }

    @Override
    public void showDJIFlightControllerDelegate(byte[] bytes) {

    }

    @Override
    public void showDeviceCallbackMoudle(DeviceCallback deviceCallback, Module module, Mephitis mephitis) {
        if(deviceCallback != null){
            frame_mount.setVisibility(View.VISIBLE);
            dialogUpdate(module);
            String tempStatus = deviceCallback.getDeviceCallBackData().getStatusTemperatureOne();  //温度状态
            String tempStatusTwo = deviceCallback.getDeviceCallBackData().getStatusTemperatureTwo();  //温度状态
            String temOne = deviceCallback.getDeviceCallBackData().getTemperatureOne();  //温度
            String temTwo = deviceCallback.getDeviceCallBackData().getTemperatureTwo();  //温度
            String gas_Value1 = deviceCallback.getDeviceCallBackData().getGasValueOne();
            String gas_Value2 = deviceCallback.getDeviceCallBackData().getGasValueTwo();
            String gas_Value3 = deviceCallback.getDeviceCallBackData().getGasValueThree();
            String gas_Value4 = deviceCallback.getDeviceCallBackData().getGasValueFour();

            //温度模块
            if (module.getDEVICE_TYPE_TEMPERATURE() == 1) {
                numValues1 = HexToBinary.HexTo10IntegerShort(temOne);
                numValues2 = HexToBinary.HexTo10IntegerShort(temTwo);
                if ("FD".equalsIgnoreCase(tempStatus)) {
                    colorOne = Color.RED;numberOneState = 0;
                    DJISampleApplication.mainActivity.setTextLog("温度1异常",colorOne);
                } else if ("FC".equalsIgnoreCase(tempStatus)) {
                    colorOne = Color.YELLOW;numberOneState = 1;
                    DJISampleApplication.mainActivity.setTextLog("温度1超过上限",colorOne);
                } else if ("FB".equalsIgnoreCase(tempStatus)) {
                    colorOne = Color.YELLOW;numberOneState = 2;
                    DJISampleApplication.mainActivity.setTextLog("温度1超过下限",colorOne);
                } else if ("FE".equalsIgnoreCase(tempStatus)) {
                    colorOne = Color.WHITE; numberOneState = 3;
                }

                if ("FD".equalsIgnoreCase(tempStatusTwo)) {
                    colorTwo = Color.RED;numberTwoState = 0;
                    DJISampleApplication.mainActivity.setTextLog("温度2异常",colorTwo);
                } else if ("FC".equalsIgnoreCase(tempStatusTwo)) {
                    colorTwo = Color.YELLOW;numberTwoState = 1;
                    DJISampleApplication.mainActivity.setTextLog("温度2超过上限",colorTwo);
                } else if ("FB".equalsIgnoreCase(tempStatusTwo)) {
                    colorTwo = Color.YELLOW;numberTwoState = 2;
                    DJISampleApplication.mainActivity.setTextLog("温度2超过下限",colorTwo);
                } else if ("FE".equalsIgnoreCase(tempStatusTwo)) {
                    colorTwo = Color.WHITE; numberTwoState = 3;
                }
            }

            //气体模块
            map_co.clear();
            //if (module.getDEVICE_TYPE_GAS() == 1) {
                if (mephitis.getDEVICE_TYPE_GAS_CO() == 1) co_number = HexToBinary.HexTo10Integer(gas_Value1);
                else co_number = 0;
                if (mephitis.getDEVICE_TYPE_GAS_H2S() == 1) h2s_number = HexToBinary.HexTo10Integer(gas_Value2) / 100;
                else h2s_number = 0;
                if (mephitis.getDEVICE_TYPE_GAS_NH3() == 1) nh3_number = HexToBinary.HexTo10Integer(gas_Value3);
                else nh3_number = 0;
                if (mephitis.getDEVICE_TYPE_GAS_CO2() == 1) co2_number = HexToBinary.HexTo10Integer(gas_Value4);
                else co2_number = 0;
            //}

            map_co.put("CO", co_number + "");
            map_co.put("H2S", h2s_number + "");
            map_co.put("NH3", nh3_number + "");
            map_co.put("CO2", co2_number + "");

            if(txt_temperature_one != null) {
                if(module.getDEVICE_TYPE_TEMPERATURE() == 0){
                    txt_temperature_one.setText(0 + "");
                    txt_temperature_two.setText(0 + "");
                }else{
                    if(numberOneState == 0) txt_temperature_one.setText(0 + "");
                    if(numberTwoState == 0) txt_temperature_two.setText(0 + "");

                    if((numberOneState >0 && numberOneState<=3) && numberTwoState == 0){
                        txt_temperature_one.setText(numValues1+"");
                    }else if(numberOneState == 0 && (numberTwoState >0 && numberTwoState<=3)){
                        txt_temperature_two.setText(numValues2+"");
                    }else if((numberOneState >0 && numberOneState<=3) && (numberTwoState >0 && numberTwoState<=3)){
                        txt_temperature_one.setText(numValues1 > numValues2 ? numValues1 + "" : numValues2 + "");
                        txt_temperature_two.setText(numValues1 > numValues2 ? numValues1 + "" : numValues2 + "");
                    }
                    txt_temperature_one.setTextColor(colorOne);
                    txt_temperature_two.setTextColor(colorTwo);
                }

                txt_co_error.setText("CO: " + shar_co);
                txt_h2s_error.setText("H2S: " + shar_h2s);
                txt_nh3_error.setText("NH3: " + shar_nh3);
                txt_co2_error.setText("CO2: " + shar_co2);

                if (co_number > Integer.parseInt(shar_co)){
                    txt_co_error.setTextColor(Color.RED);
                    DJISampleApplication.mainActivity.setTextLog("CO超过上限",Color.RED);
                }
                else txt_co_error.setTextColor(Color.WHITE);

                if (h2s_number > Integer.parseInt(shar_h2s)){
                    txt_h2s_error.setTextColor(Color.RED);
                    DJISampleApplication.mainActivity.setTextLog("H2S超过上限",Color.RED);
                }
                else txt_h2s_error.setTextColor(Color.WHITE);

                if (nh3_number > Integer.parseInt(shar_nh3)){
                    txt_nh3_error.setTextColor(Color.RED);
                    DJISampleApplication.mainActivity.setTextLog("NH3超过上限",Color.RED);
                }
                else txt_nh3_error.setTextColor(Color.WHITE);

                if (co2_number > Integer.parseInt(shar_co2)){
                    txt_co2_error.setTextColor(Color.RED);
                    DJISampleApplication.mainActivity.setTextLog("CO2超过上限",Color.RED);
                }
                else txt_co2_error.setTextColor(Color.WHITE);
            }
            setData();
        }else{
            dialogUpdate(null);
            frame_mount.setVisibility(View.GONE);
        }
    }

    @Override
    public void showDeviceCallbackMoudle2(DeviceCallback deviceCallback) {
        if(deviceCallback != null){
            linear_obstacle.setVisibility(View.VISIBLE);
            //障碍
            txt_obstacle_left.setText(HexToBinary.HexTo10Integer(deviceCallback.getDeviceCallBackTwo().getObstacleLeftBarrier())+"CM");
            txt_obstacle_right.setText(HexToBinary.HexTo10Integer(deviceCallback.getDeviceCallBackTwo().getObstacleRightBarrier())+"CM");
            txt_obstacle_front.setText(HexToBinary.HexTo10Integer(deviceCallback.getDeviceCallBackTwo().getObstacleFrontBarrier())+"CM");
            txt_obstacle_back.setText(HexToBinary.HexTo10Integer(deviceCallback.getDeviceCallBackTwo().getObstacleQueenBarrier())+"CM");
            int obstacleDistance = HexToBinary.HexTo10Integer(deviceCallback.getDeviceCallBackTwo().getObstacleDistance());
            String name = "";
            if(HexToBinary.HexTo10Integer(deviceCallback.getDeviceCallBackTwo().getObstacleLeftBarrier())<=obstacleDistance) {
                name = "左边";
            }else if(HexToBinary.HexTo10Integer(deviceCallback.getDeviceCallBackTwo().getObstacleRightBarrier())<=obstacleDistance){
                name = "右边";
            }else if(HexToBinary.HexTo10Integer(deviceCallback.getDeviceCallBackTwo().getObstacleFrontBarrier())<=obstacleDistance){
                name = "前面";
            }else if(HexToBinary.HexTo10Integer(deviceCallback.getDeviceCallBackTwo().getObstacleQueenBarrier())<=obstacleDistance){
                name = "后面";
            }
            if(!name.isEmpty() && !name.equals(""))
            DJISampleApplication.mainActivity.setTextLog("小心"+name+"障碍物",Color.YELLOW);
        }else{
            linear_obstacle.setVisibility(View.GONE);
        }
    }

    public CustomDialog getDialog() {
        return dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(dialog != null) dialog.dismiss();
    }
}
