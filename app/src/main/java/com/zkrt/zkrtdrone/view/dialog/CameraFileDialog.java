package com.zkrt.zkrtdrone.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.GridView;

import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.adapter.CameraAdapter;
import com.zkrt.zkrtdrone.base.BaseDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dji.sdk.camera.DJIMedia;

/**
 * Created by jack_xie on 16-12-26.
 */

public class CameraFileDialog extends BaseDialog {

    @BindView(R.id.grid_camera_file)
    GridView grid_camera_file;
    private Context mContext;
    private CameraAdapter cameraAdapter;
    private int res_Layout;
    private List<DJIMedia> list;

    public CameraFileDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setCameraList(ArrayList<DJIMedia> djiMedias_list){
        this.list = djiMedias_list;
        cameraAdapter.setList(list);
    }

    public CameraFileDialog(Context context,int theme, int resLayout) {
        super(context,theme, resLayout);
        this.mContext = context;
        this.res_Layout = resLayout;
        cameraAdapter = new CameraAdapter(getContext());
    }

    protected CameraFileDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    public int getDialogFindById() {
        return res_Layout;
    }

    @OnClick(R.id.camera_clean_btn)
    public void clanelClean(View v){
        this.dismiss();
    }
}
