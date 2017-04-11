package com.zkrt.zkrtdrone.view.dialog;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import com.shawnlin.numberpicker.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zkrt.zkrtdrone.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by jack_xie on 17-3-13.
 */

public class HotPoDialogPopup extends BasePopupWindow implements NumberPicker.OnValueChangeListener,View.OnClickListener{

    private NumberPicker number_hotpoint_relay_picker;
    private TextView ok_hotpoint;
    private TextView cancel_hotpoint;
    private TextView txt_hotpoint_mission_lat;
    private TextView txt_hotpoint_mission_lng;
    private EditText edit_radius_hotpoin;
    private EditText edit_angular_hotpoin;
    private RadioGroup group_clockwise;
    private int hightNumber;
    private setFollowMeBtn setFollowMeBtn;
    private boolean boolgroup = true;

    public HotPoDialogPopup(Activity context) {
        super(context);
        number_hotpoint_relay_picker= (NumberPicker) findViewById(R.id.number_hotpoint_relay_picker);
        ok_hotpoint= (TextView) findViewById(R.id.ok_hotpoint);
        cancel_hotpoint= (TextView) findViewById(R.id.cancel_hotpoint);
        group_clockwise= (RadioGroup) findViewById(R.id.group_clockwise);
        txt_hotpoint_mission_lat= (TextView) findViewById(R.id.txt_hotpoint_mission_lat);
        txt_hotpoint_mission_lng= (TextView) findViewById(R.id.txt_hotpoint_mission_lng);
        edit_radius_hotpoin= (EditText) findViewById(R.id.edit_radius_hotpoin);
        edit_angular_hotpoin= (EditText) findViewById(R.id.edit_angular_hotpoin);
        number_hotpoint_relay_picker.setOnValueChangedListener(this);
        ok_hotpoint.setOnClickListener(this);
        cancel_hotpoint.setOnClickListener(this);

        group_clockwise.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.hotpointlayout);
    }

    @Override
    public View initAnimaView() {
        return null;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()){
            case R.id.number_hotpoint_relay_picker:
                hightNumber = newVal;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok_hotpoint:
                String radius = edit_radius_hotpoin.getText().toString();
                String angularSpeed = edit_angular_hotpoin.getText().toString();
                setFollowMeBtn.btnOk(hightNumber,boolgroup,Integer.parseInt(radius),Integer.parseInt(angularSpeed));
                break;
            case R.id.cancel_hotpoint:
                setFollowMeBtn.btnNo();
                setPopuWindowDismiss();
                break;
        }
    }

    public void setDataLat(double droneLocationLat, double droneLocationLng, setFollowMeBtn setFollowMeBtns) {
        txt_hotpoint_mission_lat.setText(droneLocationLat+"");
        txt_hotpoint_mission_lng.setText(droneLocationLng+"");
        this.setFollowMeBtn = setFollowMeBtns;
    }

    public void setPopuWindowDismiss(){
        dismiss();
    }

    public interface setFollowMeBtn{
        void btnOk(int hightNumber,boolean boolgroup,int radius,int angularspeed);
        void btnNo();
    }
}
