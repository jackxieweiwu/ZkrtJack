package com.zkrt.zkrtdrone.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.base.BaseDialog;
import com.zkrt.zkrtdrone.base.HintBtnClick;
import com.zkrt.zkrtdrone.base.Utils;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;

import butterknife.BindView;
import butterknife.OnClick;
import dji.common.error.DJIError;
import dji.common.remotecontroller.DJIRCToAircraftPairingState;
import dji.common.util.DJICommonCallbacks;

/**
 * Created by jack_xie on 2017/1/2.
 */

public class TimeDialog extends BaseDialog {
    private Activity mContext;
    TextView txt_remote_dialog;
    TextView txt_id_remote;

    public TimeDialog(Activity context) {
        super(context);
        this.mContext = context;
    }

    public TimeDialog(Activity context,int theme){
        super(context,theme,0);
        this.mContext = context;
    }

    @Override
    protected void initView(View view) {
        txt_remote_dialog = (TextView) view.findViewById(R.id.txt_remote_dialog);
        txt_id_remote = (TextView) view.findViewById(R.id.txt_id_remote);
        txt_remote_dialog.setText(mContext.getResources().getString(R.string.remote_dialog));
        txt_id_remote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textName = txt_id_remote.getText().toString();
                if(textName.equals("取消")){
                    if(DJIModuleVerificationUtil.isRemoteControllerAvailable()){
                        canclent();
                    }
                }else{
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismiss();
                        }
                    });
                }
            }
        });
    }

    private void canclent(){
        DJISampleApplication.getAircraftInstance().getRemoteController().
                exitRCPairingMode(new DJICommonCallbacks.DJICompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        if(djiError == null){
                            dissmiondialog(true);
                        }
                    }
                });
    }

    private void dissmiondialog(boolean bool){
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(bool){
                    dismiss();
                }else{
                    show();
                }
            }
        });
    }
    public void showDialog(){
        dissmiondialog(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(DJIModuleVerificationUtil.isRemoteControllerAvailable()){
                    DJISampleApplication.getAircraftInstance().getRemoteController().
                            getRCToAircraftPairingState(new DJICommonCallbacks.DJICompletionCallbackWith<DJIRCToAircraftPairingState>() {
                                @Override
                                public void onSuccess(DJIRCToAircraftPairingState djircToAircraftPairingState) {
                                    mContext.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(djircToAircraftPairingState.value()==0){
                                                Utils.setResultToToast(getContext(),"对频失败");
                                                dismiss();
                                                canclent();
                                            }else if(djircToAircraftPairingState.value() == 1){
                                                dismiss();
                                                Utils.setResultToToast(getContext(),"对频中,但无法找到对象...");
                                                canclent();
                                            }else if(djircToAircraftPairingState.value() == 2){
                                                dismiss();
                                                Utils.setResultToToast(getContext(),"对频成功");
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(DJIError djiError) {

                                }
                            });
                }
            }
        },60000);
    }

    @Override
    public int getDialogFindById() {
        return R.layout.time_dailog;
    }
}
