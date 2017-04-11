package com.zkrt.zkrtdrone.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.BaseDialog;

import butterknife.BindView;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.rxbean.UITask;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.rxutil.RxjavaUtil;

/**
 * Created by jack_xie on 17-1-9.
 */

public class ProgressDialog extends BaseDialog {
    private Context mContext;
    private int layoutId;
    @BindView(R.id.txt_name_progress) TextView txt_name_progress;

    public ProgressDialog(Context context) {
        super(context);
    }

    public ProgressDialog(Context context, int theme, int resLayout) {
        super(context, theme, resLayout);
        this.mContext = context;
        this.layoutId = resLayout;
    }

    public void setTextProgressDialog(String v){
        if(txt_name_progress == null) return;
        RxjavaUtil.doInUIThread(new UITask<String>() {
            @Override
            public void doInUIThread() {
                txt_name_progress.setText(v);
            }
        });
    }

    @Override
    protected void initView(View view) {

    }

    public void setShow(){
        this.show();
    }

    @Override
    public int getDialogFindById() {
        return layoutId;
    }
}
