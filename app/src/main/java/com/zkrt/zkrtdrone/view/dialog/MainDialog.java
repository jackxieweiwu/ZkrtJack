package com.zkrt.zkrtdrone.view.dialog;

import android.content.Context;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zkrt.zkrtdrone.R;

import org.alex.dialog.base.BaseDialog;

import java.util.List;

/**
 * Created by jack_xie on 17-2-28.
 */
public class MainDialog extends BaseDialog<MainDialog> {
    private TextView txt_dialog_title;
    private TextView dialog_message;
    private Button btn_main_ok;
    private Button btn_main_no;

    public MainDialog(Context context) {
        super(context);
    }

    public MainDialog(Context context, int theme) {
        super(context, theme);
    }

    public void setOnBtn(){

    }
    @Override
    public int getLayoutRes() {
        return R.layout.main_dialog;
    }

    @Override
    public void onCreateData() {
        txt_dialog_title = (TextView) findViewById(R.id.txt_dialog_title);
        dialog_message = (TextView) findViewById(R.id.dialog_message);
        btn_main_ok = (Button) findViewById(R.id.btn_main_ok);
        btn_main_no = (Button) findViewById(R.id.btn_main_no);
        btn_main_ok.setOnClickListener(this);
        btn_main_no.setOnClickListener(this);
    }

    public void setText(String titName,String message){
        btn_main_no.setVisibility(View.GONE);
        txt_dialog_title.setText(titName);
        dialog_message.setText(message);
    }

    @Override
    public void onClick(View v, int id) {
        switch (v.getId()){
            case R.id.btn_main_ok:
                dismiss();
                break;
            case R.id.btn_main_no:
                dismiss();
                break;
        }
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {

    }
}
