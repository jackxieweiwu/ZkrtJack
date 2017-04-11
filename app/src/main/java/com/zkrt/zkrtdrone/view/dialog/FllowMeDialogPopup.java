package com.zkrt.zkrtdrone.view.dialog;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import com.shawnlin.numberpicker.NumberPicker;
import android.widget.TextView;

import com.zkrt.zkrtdrone.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by jack_xie on 17-3-13.
 */

public class FllowMeDialogPopup extends BasePopupWindow implements NumberPicker.OnValueChangeListener,View.OnClickListener{

    private NumberPicker number_follow_relay_picker;
    private TextView ok_follow;
    private TextView cancel_follow;
    private TextView txt_follow_mission_lat;
    private TextView txt_follow_mission_lng;
    private int hightNumber;
    private setFollowMeBtn setFollowMeBtn;

    public FllowMeDialogPopup(Activity context) {
        super(context);
        number_follow_relay_picker= (NumberPicker) findViewById(R.id.number_follow_relay_picker);
        ok_follow= (TextView) findViewById(R.id.ok_follow);
        cancel_follow= (TextView) findViewById(R.id.cancel_follow);
        txt_follow_mission_lng= (TextView) findViewById(R.id.txt_follow_mission_lng);
        txt_follow_mission_lat= (TextView) findViewById(R.id.txt_follow_mission_lat);
        number_follow_relay_picker.setOnValueChangedListener(this);
        ok_follow.setOnClickListener(this);
        cancel_follow.setOnClickListener(this);
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
        return createPopupById(R.layout.fllowmepopuwindow);
    }

    @Override
    public View initAnimaView() {
        return null;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()){
            case R.id.number_follow_relay_picker:
                hightNumber = newVal;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok_follow:
                setFollowMeBtn.btnOk(hightNumber);
                break;
            case R.id.cancel_follow:
                setFollowMeBtn.btnNo();
                popuwindowdimmi();
                break;
        }
    }

    public void setDataLat(double droneLocationLat, double droneLocationLng, setFollowMeBtn setFollowMeBtns) {
        txt_follow_mission_lat.setText(droneLocationLat+"");
        txt_follow_mission_lng.setText(droneLocationLng+"");
        this.setFollowMeBtn = setFollowMeBtns;
    }

    public void popuwindowdimmi(){
        dismiss();
    }

    public interface setFollowMeBtn{
        void btnOk(int hightNumber);
        void btnNo();
    }
}
