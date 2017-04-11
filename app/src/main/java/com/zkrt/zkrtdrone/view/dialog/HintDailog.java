package com.zkrt.zkrtdrone.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.HintBtnClick;

/**
 * Created by jack_xie on 16-12-28.
 */

public class HintDailog extends Dialog implements View.OnClickListener{

    private TextView cancel_hint;
    private TextView ok_hint;
    private TextView txt_string_values;
    private int layoutRes;
    private Context mContext;
    private HintBtnClick hintBtnClick;
    private String values_name;

    public HintDailog(Context context) {
        super(context);
        this.mContext = context;
    }

    public HintDailog(Context context,int resLayout){
        super(context);
        this.mContext = context;
        this.layoutRes=resLayout;
    }

    public HintDailog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public HintDailog(Context context, int theme, int resLayout){
        super(context, theme);
        this.mContext = context;
        this.layoutRes=resLayout;
    }
    public void setNameValue(String name){
        this.values_name = name;
        if(txt_string_values!=null)txt_string_values.setText(name+"");
    }

    public void setHintBtnIntfaces(HintBtnClick hintBtnClicka){
        hintBtnClick = hintBtnClicka;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_hinlt);
        cancel_hint = (TextView) findViewById(R.id.cancel_hint);
        ok_hint = (TextView) findViewById(R.id.ok_hint);
        txt_string_values = (TextView) findViewById(R.id.txt_string_values);
        cancel_hint.setOnClickListener(this);
        ok_hint.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_hint:
                setDialogMission();
                break;
            case R.id.ok_hint:
                setDialogMission();
                hintBtnClick.hintOk(true);
                break;
        }
    };

    public void setDialogMission(){
        dismiss();
    }
}