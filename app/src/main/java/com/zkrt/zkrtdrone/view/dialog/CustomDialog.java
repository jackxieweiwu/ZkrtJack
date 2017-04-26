package com.zkrt.zkrtdrone.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.SwitchCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.adapter.MoudleAdapter;
import com.zkrt.zkrtdrone.base.BaseDialog;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.bean.DeviceCallback;
import com.zkrt.zkrtdrone.bean.Module;
import com.zkrt.zkrtdrone.bean.MoudleBean;
import com.zkrt.zkrtdrone.log.crc.MAVLinkCRC;
import com.zkrt.zkrtdrone.receiver.GoToDroneOnBoard;
import com.zkrt.zkrtdrone.until.HexToBinary;
import java.util.List;


/**
 * Created by jack_xie on 2016/10/18.
 */

public class CustomDialog extends BaseDialog implements AdapterView.OnItemClickListener,View.OnTouchListener,
        View.OnClickListener,View.OnFocusChangeListener {
    private int layoutRes;
    private Activity context;
    private GridView dlg_grid;
    private Button btn_clear;
    private Button btn_ok;
    private Button btn_temper_ok;
    private Button btn_temper_clean;
    private Button btn_zhao_clean;
    private int positions;
    private MoudleAdapter moudleAdapter;
    private ImageView img_btn;
    private DeviceCallback mDeviceCallback;
    private EditText edit_co;
    private EditText edit_h2s;
    private EditText edit_nh3;
    private EditText edit_co2;
    private EditText exit_low_value;
    private EditText exit_height_value;
    private SwitchCompat btn_floater_one_ios;
    private SwitchCompat btn_floater_two_ios;
    private SwitchCompat btn_floater_three_ios;
    private SwitchCompat btn_floater_zhao_ios2;
    private Button btn_paotou_clean;
    private List<MoudleBean> list_bean;
    private MAVLinkCRC mavLinkCRC;
    private SharedPreferences sharedPreferences;
    private LinearLayout linear_gases;
    private LinearLayout mount_search;
    private LinearLayout mount_jettision;
    private LinearLayout relay_temper;
    private GoToDroneOnBoard goToDroneOnBoard;
    private String low,height;
    private int no1 = 1500;
    private int no2 = 1500;
    private int no7_1 = 0;
    private int no7_2 = 0;
    private int take_picture = 0;
    private int recording = 0;
    private int redQh = 0;
    private int colorQh = 0;
    private int moudleQh = 0;
    private int no72 = 0;
    private int no71 = 0;

    public CustomDialog(Context context, int customDialog, int moudle) {
        super(context);
        this.context = (Activity) context;
    }

    public CustomDialog(Context context, int theme, int resLayout, List<MoudleBean> moudleBeen) {
        super(context, theme, resLayout);
        this.context = (Activity) context;
        this.layoutRes=resLayout;
        this.list_bean = moudleBeen;
        mavLinkCRC = new MAVLinkCRC();
        sharedPreferences= getContext().getSharedPreferences("gases",Activity.MODE_PRIVATE);
        goToDroneOnBoard = new GoToDroneOnBoard(this);
    }

    public void setDevideCall(String lows, String heights){
        this.low = lows;
        this.height = heights;
        if(exit_low_value != null){
            exit_low_value.setText(low);
            exit_height_value.setText(height);
        }
    }

    public EditText getEdtextLow(){
        return exit_low_value;
    }

    public void setDeviceBack(DeviceCallback deviceCallback, Module module, List<MoudleBean> listMoudleBean){
        this.mDeviceCallback = deviceCallback;
        String modleStatus = HexToBinary.Hex2Binary(deviceCallback.getDeviceCallBackData().getSetFeedback().replace(" ", ""));
        //检测回馈的指令是否执行
        if(moudleAdapter != null) moudleAdapter.addAll(listMoudleBean);
        if(module.getDEVICE_TYPE_IRRADIATE() == 1){  //照射模块
            if(btn_floater_zhao_ios2 !=null) {
                btn_floater_zhao_ios2.setChecked(("1").equals(modleStatus.substring(20, 21))?true:false);
            }
        }

        if(module.getDEVICE_TYPE_THROW() == 1){  //抛投模块
            if(btn_floater_one_ios !=null) btn_floater_one_ios.setChecked(modleStatus.substring(23,24).equals("1")?true:false);
            if(btn_floater_two_ios !=null) btn_floater_two_ios.setChecked(modleStatus.substring(22,23).equals("1")?true:false);
            if(btn_floater_three_ios !=null) btn_floater_three_ios.setChecked(modleStatus.substring(21,22).equals("1")?true:false);
        }
    }

    @Override
    protected void initView(View view) {
        dlg_grid = (GridView) findViewById(R.id.grid_moudle);
        linear_gases = (LinearLayout) findViewById(R.id.linear_gases);
        mount_search = (LinearLayout) findViewById(R.id.mount_search);
        mount_jettision = (LinearLayout) findViewById(R.id.mount_jettision);
        relay_temper = (LinearLayout) findViewById(R.id.relay_temper);
        img_btn = (ImageView) findViewById(R.id.img_btn);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_temper_ok = (Button) findViewById(R.id.btn_temper_ok);
        btn_temper_clean = (Button) findViewById(R.id.btn_temper_clean);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_ok.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        edit_co = (EditText) findViewById(R.id.edit_co);
        edit_h2s = (EditText) findViewById(R.id.edit_h2s);
        edit_nh3 = (EditText) findViewById(R.id.edit_nh3);
        edit_co2 = (EditText) findViewById(R.id.edit_co2);
        exit_low_value = (EditText) findViewById(R.id.exit_low_value);
        exit_height_value = (EditText) findViewById(R.id.exit_height_value);
        exit_low_value.setOnFocusChangeListener(this);
        exit_height_value.setOnFocusChangeListener(this);
        edit_co.setText(sharedPreferences.getString("CO", ""));
        edit_h2s.setText(sharedPreferences.getString("H2S", ""));
        edit_nh3.setText(sharedPreferences.getString("NH3", ""));
        edit_co2.setText(sharedPreferences.getString("CO2", ""));
        btn_floater_one_ios = (SwitchCompat) findViewById(R.id.btn_floater_one_ios);
        btn_floater_two_ios = (SwitchCompat) findViewById(R.id.btn_floater_two_ios);
        btn_floater_three_ios = (SwitchCompat) findViewById(R.id.btn_floater_three_ios);
        btn_paotou_clean = (Button) findViewById(R.id.btn_paotou_clean);
        btn_zhao_clean = (Button) findViewById(R.id.btn_zhao_clean);
        btn_floater_zhao_ios2 = (SwitchCompat) findViewById(R.id.btn_floater_zhao_ios2);
        btn_zhao_clean.setOnClickListener(this);
        btn_floater_zhao_ios2.isPressed();

        btn_paotou_clean.setOnClickListener(this);
        dlg_grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        moudleAdapter = new MoudleAdapter(context);
        dlg_grid.setAdapter(moudleAdapter);
        moudleAdapter.clear();
        moudleAdapter.addAll(list_bean);
        dlg_grid.setOnItemClickListener(this);
        img_btn.setOnClickListener(this);
        btn_temper_ok.setOnClickListener(this);
        btn_temper_clean.setOnClickListener(this);

        btn_floater_one_ios.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!btn_floater_one_ios.isPressed())return;
                if(mDeviceCallback!=null)setRoling(1,isChecked,true);
            }
        });
        btn_floater_two_ios.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!btn_floater_two_ios.isPressed())return;
                if(mDeviceCallback!=null)setRoling(2,isChecked,true);
            }
        });
        btn_floater_three_ios.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!btn_floater_three_ios.isPressed())return;
                if(mDeviceCallback!=null)setRoling(3,isChecked,true);
            }
        });
        btn_floater_zhao_ios2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!btn_floater_zhao_ios2.isPressed())return;
                if(mDeviceCallback!=null) setExposure(isChecked,true);
            }
        });
    }

    @Override
    public int getDialogFindById() {
        return R.layout.moudle;
    }

    private MoudleAdapter ad;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ad = (MoudleAdapter) parent.getAdapter();
        positions = position;
        MoudleBean moudleBean = moudleAdapter.getItem(position);
        if (moudleBean.getStatus()) {
            if("有毒气体".equals(moudleBean.getName())){
                linear_gases.setVisibility(View.VISIBLE);
                dlg_grid.setVisibility(View.INVISIBLE);
                mount_search.setVisibility(View.INVISIBLE);
                mount_jettision.setVisibility(View.INVISIBLE);
                relay_temper.setVisibility(View.INVISIBLE);
            }

            if("抛投".equals(moudleBean.getName())){
                linear_gases.setVisibility(View.INVISIBLE);
                dlg_grid.setVisibility(View.INVISIBLE);
                relay_temper.setVisibility(View.INVISIBLE);
                mount_search.setVisibility(View.INVISIBLE);
                mount_jettision.setVisibility(View.VISIBLE);
            }

            if("探照灯".equals(moudleBean.getName())){
                linear_gases.setVisibility(View.INVISIBLE);
                dlg_grid.setVisibility(View.INVISIBLE);
                mount_search.setVisibility(View.VISIBLE);
                relay_temper.setVisibility(View.INVISIBLE);
                mount_jettision.setVisibility(View.INVISIBLE);
            }

            if("避温".equals(moudleBean.getName())){
                linear_gases.setVisibility(View.INVISIBLE);
                dlg_grid.setVisibility(View.INVISIBLE);
                mount_search.setVisibility(View.INVISIBLE);
                relay_temper.setVisibility(View.VISIBLE);
                mount_jettision.setVisibility(View.INVISIBLE);
            }

        } else {
            Utils.setResultToToast(context, "未挂载该模块...");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                ad.setNotifyDataChange(positions);
                break;
            case MotionEvent.ACTION_MOVE:
                ad.setNotifyDataChange(-1);
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_btn:
                dismiss();
                serViewVisibility();
                break;
            case R.id.btn_clear:
            case R.id.btn_temper_clean:
            case R.id.btn_zhao_clean:
            case R.id.btn_paotou_clean:
                serViewVisibility();
                break;
            case R.id.btn_ok:
                String name_co = edit_co.getText().toString();
                String name_h2s = edit_h2s.getText().toString();
                String name_nh3 = edit_nh3.getText().toString();
                String name_co2 = edit_co2.getText().toString();

                if(name_co.isEmpty()) name_co = "0";
                if(name_h2s.isEmpty()) name_h2s = "0";
                if(name_nh3.isEmpty()) name_nh3 = "0";
                if(name_co2.isEmpty()) name_co2 = "0";

                SharedPreferences.Editor editor = DJISampleApplication.mySharedPreferences.edit();
                editor.putString("CO", name_co);
                editor.putString("H2S", name_h2s);
                editor.putString("NH3", name_nh3);
                editor.putString("CO2", name_co2);
                editor.commit();
                Utils.setResultToToast(getContext(),"设置成功");
                serViewVisibility();
                break;

            case R.id.btn_temper_ok:
                serGotoOnboardData(0,1,"Avoid_Temperature",true,"");
                break;
        }
    }

    public void serViewVisibility(){
        linear_gases.setVisibility(View.INVISIBLE);
        dlg_grid.setVisibility(View.VISIBLE);
        mount_search.setVisibility(View.INVISIBLE);
        relay_temper.setVisibility(View.INVISIBLE);
        mount_jettision.setVisibility(View.INVISIBLE);
    }

    //抛投
    public void setRoling(int number,boolean bool,boolean bol){
        if(bool)serGotoOnboardData(number,1,"ROP",bol,"");
        else serGotoOnboardData(number,0,"ROP",bol,"");
    }

    //照射
    public void setExposure(boolean bool,boolean bol){
        if(bool)serGotoOnboardData(0,1,"FLOOD_LIGHTING_POST",bol,"");
        else serGotoOnboardData(0,0,"FLOOD_LIGHTING_POST",bol,"");
    }

    //避障
    public void setObtacle(boolean bool,String cm,int speed){
        if(bool)serGotoOnboardData(speed,1,"OBSTACLE",false,cm);
        else serGotoOnboardData(speed,0,"OBSTACLE",false,cm);
    }

    //jiaojia
    public void setTripoda(int number,boolean bool,String name){
        if(bool)serGotoOnboardData(number,1,"TRIPOD",false,name);
        else serGotoOnboardData(number,0,"TRIPOD",false,name);
    }

    public void serGotoOnboardData(int one ,int two ,String name,boolean bol,String zoomStop) {
        if (mDeviceCallback != null) {
            goToDroneOnBoard.setGetPaoTou(one, two, name, exit_low_value,
                    exit_height_value, mavLinkCRC, mDeviceCallback, no1, no2, no7_1, no7_2,
                    take_picture, recording, bol, redQh, colorQh, moudleQh, no72, zoomStop, no71);
        }
    }

    public void serDataValues(int one ,int two ,String name,boolean bol,String zoomStop,
                              int take_pictures,int recordings,int no7_2s,int no7_1s,int no1s,int no2s,
                              int redQhs,int colorQhs,int modleQhs,int no72s,int no71s){
        this.take_picture = take_pictures;
        this.recording = recordings;
        this.no7_2 = no7_2s;
        this.no7_1 = no7_1s;
        this.no1 = no1s;
        this.no2 = no2s;
        this.redQh = redQhs;
        this.colorQh = colorQhs;
        this.moudleQh = modleQhs;
        this.no72 = no72s;
        this.no71 = no71s;

        goToDroneOnBoard.setGetPaoTou(one,two,name,exit_low_value,
                exit_height_value, mavLinkCRC, mDeviceCallback,no1,no2,no7_1,no7_2,
                take_picture,recording,bol,redQh,colorQh,moudleQh,no72,zoomStop,no71);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
            low = exit_low_value.getText().toString()+"";
            height = exit_height_value.getText().toString()+"";
            switch (v.getId()){
                case R.id.exit_height_value:
                    if(height.isEmpty() || height.equals("")){ Utils.setResultToToast(context,"不可为空"); return;}
                    if(Float.parseFloat(height)<60f){
                        Utils.setResultToToast(context,"上限最低为60");
                        exit_height_value.setText("60");
                    }
                    if(Float.parseFloat(height)>1000f){
                        Utils.setResultToToast(context,"上限最高为1000");
                        exit_height_value.setText("1000");
                    }
                    break;
                case R.id.exit_low_value:
                    if(low.isEmpty() || low.equals("")) {Utils.setResultToToast(context,"不可为空");return;}
                    if(Float.parseFloat(low)<-45f) {
                        Utils.setResultToToast(context,"下限最低为-45");
                        exit_height_value.setText("-45");
                    }
                    if(Float.parseFloat(low)>60f){
                        Utils.setResultToToast(context,"下限最高为60");
                        exit_height_value.setText("60");
                    }
                    break;
            }
        }
    }
}