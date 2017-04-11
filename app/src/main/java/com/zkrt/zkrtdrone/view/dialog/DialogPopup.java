package com.zkrt.zkrtdrone.view.dialog;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.shawnlin.numberpicker.NumberPicker;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.BtnClick;
import com.zkrt.zkrtdrone.base.HintBtnClick;

import java.util.Arrays;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by jack_xie on 16-12-28.
 */

public class DialogPopup extends BasePopupWindow implements View.OnClickListener,NumberPicker.OnValueChangeListener {
    private TextView ok;
    private TextView cancel;
    private TextView txt_mission_number;
    private TextView clean_waypion;
    private TextView txt_mission_lat;
    private TextView txt_mission_lng;
    //private TextView txt_mission_h;
    private Spinner mission_spinner;
    private BtnClick btnClicka;
    private HintDailog hint_dailog;
    private NumberPicker number_relay_picker;  //高度
    private NumberPicker number_rotate_picker;  //速度
    private int rateNumber = 0;
    private int hightNumber = 15;
    private LatLng latLngdialog;
    private int MissNum;
    private String MissMotion;

    //
    private EditText edit_retention;  //滞留
    private EditText edit_surround_radius;  //radious
    private EditText edit_rotate;  //旋转
    private RadioGroup radiogroup_way;  //旋转 顺时针
    private boolean boolgroup = true;
    private LinearLayout linear_surround_radius;
    private RelativeLayout relay_sudo;
    private RelativeLayout relay_rotate;

    private String items[];
    public DialogPopup(Activity context) {
        super(context);
        hint_dailog = new HintDailog(context, R.style.customDialog, R.layout.dialog_hinlt);
        ok= (TextView) findViewById(R.id.ok);
        cancel= (TextView) findViewById(R.id.cancel);
        relay_sudo= (RelativeLayout) findViewById(R.id.relay_sudo);
        relay_rotate= (RelativeLayout) findViewById(R.id.relay_rotate);
        txt_mission_number= (TextView) findViewById(R.id.txt_mission_number);
        linear_surround_radius= (LinearLayout) findViewById(R.id.linear_surround_radius);
        edit_surround_radius= (EditText) findViewById(R.id.edit_surround_radius);
        clean_waypion= (TextView) findViewById(R.id.clean_waypion);
        txt_mission_lat= (TextView) findViewById(R.id.txt_mission_lat);
        txt_mission_lng= (TextView) findViewById(R.id.txt_mission_lng);
        //txt_mission_h= (TextView) findViewById(R.id.txt_mission_h);
        mission_spinner= (Spinner) findViewById(R.id.mission_spinner);
        number_relay_picker= (NumberPicker) findViewById(R.id.number_relay_picker);
        number_rotate_picker= (NumberPicker) findViewById(R.id.number_rotate_picker);
        edit_retention= (EditText) findViewById(R.id.edit_retention);  //滞留
        edit_rotate= (EditText) findViewById(R.id.edit_rotate);//旋转
        radiogroup_way= (RadioGroup) findViewById(R.id.radiogroup_way);// 顺时针
        number_relay_picker.setOnValueChangedListener(this);
        number_rotate_picker.setOnValueChangedListener(this);
        setViewClickListener(this,ok,cancel,clean_waypion);
        items = context.getResources().getStringArray(R.array.mission_way);

        radiogroup_way.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getId()){
                    case R.id.radio_clockwise://顺时针
                        boolgroup = true;
                        break;
                    case R.id.radio_anticlockwise:
                        boolgroup = false;
                        break;
                }
            }
        });
    }

    public void setData(LatLng latLng, int MissionNumber, BtnClick btnClick, boolean bool, String motion){
        latLngdialog = latLng;
        MissNum = MissionNumber;
        MissMotion = motion;
        setDataMission();
        btnClicka = btnClick;

        if(!bool){
            clean_waypion.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
        }else {
            clean_waypion.setVisibility(View.GONE);
            cancel.setVisibility(View.VISIBLE);
        }
    }

    public void setDataMission(){
        if(latLngdialog == null) return;
        txt_mission_number.setText(MissNum+"");
        txt_mission_lat.setText(latLngdialog.latitude+"");
        txt_mission_lng.setText(latLngdialog.longitude+"");
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(),R.array.mission_way,R.layout.spinner_list_item);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        mission_spinner. setAdapter(adapter);
        mission_spinner.setSelection(thanMissionWay(MissMotion));
        mission_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = "";
                if(position == 0) name = "无";
                /*if(position == 1) name = "起飞";*/
                /*if(position == 1) name = "旋转";*/
                if(position == 1) name = "开始拍照";
                if(position == 2) name = "开始录像";
                if(position == 3) name = "停止录像";
                if(position == 4) {
                    name = "滞留";
                    relay_sudo.setVisibility(View.VISIBLE);
                }else{
                    relay_sudo.setVisibility(View.GONE);
                }
                if(position == 5) {
                    name = "旋转";
                    relay_rotate.setVisibility(View.VISIBLE);
                }else{
                    relay_rotate.setVisibility(View.GONE);
                }

                /*if(position == 4) {
                    name = "热点环绕";
                    linear_surround_radius.setVisibility(View.VISIBLE);
                }else{
                    linear_surround_radius.setVisibility(View.GONE);
                }*/
                /*if(position == 5) name = "原地滞留";*/
                MissMotion= name;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //比对当前的任务是那些
    public int thanMissionWay(String motion){
        int number = 0;
        List list = Arrays.asList(items);
        for(int i=0;i<list.size();i++){
            String name = list.get(i).toString();
            if(motion.equals(name)){
                number = i;
            }
        }
        return number;
    }

    @Override
    protected Animation initShowAnimation() {
        AnimationSet set=new AnimationSet(false);
        /*Animation shakeAnima=new RotateAnimation(0,15,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        shakeAnima.setInterpolator(new CycleInterpolator(5));
        shakeAnima.setDuration(400);
        set.addAnimation(getDefaultAlphaAnimation());
        set.addAnimation(shakeAnima);*/
        return set;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.popupwindow_layout);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok:
                int numbe_rreten = 0; //滞留
                int numbe_rotate = 0;
                String nameretention = edit_retention.getText().toString()+"";
                String namerotate = edit_rotate.getText().toString()+"";
                if(nameretention.isEmpty() && nameretention.equals("")) {
                    numbe_rreten = 0;
                }else numbe_rreten = Integer.parseInt(nameretention);

                if(namerotate.isEmpty() && namerotate.equals("")){
                    numbe_rotate = 0;
                }else numbe_rotate = Integer.parseInt(namerotate);

                //radius
                int raiuds = 0;
                String numberRadius =  edit_surround_radius.getText().toString()+"";
                if(numberRadius.isEmpty() && numberRadius.equals("")){
                    raiuds = 0;
                }else raiuds = Integer.parseInt(numberRadius);
                btnClicka.clickOk(MissMotion,hightNumber,rateNumber,
                        numbe_rreten,numbe_rotate,boolgroup,raiuds);
                break;
            case R.id.cancel:
                btnClicka.cancle();
                popDismiss();
                break;
            case R.id.clean_waypion:
                popDismiss();
                hint_dailog.setNameValue("确定要删除该航点吗？");
                hint_dailog.setHintBtnIntfaces(new HintBtnClick() {
                    @Override
                    public void hintOk(boolean bool) {
                        if(bool) btnClicka.cleanWaypion(latLngdialog,MissNum);
                        else btnClicka.cleanWaypion(null,-1);
                    }
                });
                hint_dailog.show();
                break;
            default:
                break;
        }
    }

    public void popDismiss(){
        dismiss();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()){
            case R.id.number_relay_picker:
                hightNumber = newVal;
                break;
            case R.id.number_rotate_picker:
                rateNumber = newVal;
                break;
        }
    }
}

