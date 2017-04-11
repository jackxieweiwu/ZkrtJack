package com.zkrt.zkrtdrone.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.until.DJIModuleVerificationUtil;

import dji.common.error.DJIError;
import dji.sdk.camera.DJIMedia;
import dji.sdk.camera.DJIMediaManager;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseAdapter;

/**
 * Created by jack_xie on 16-12-26.
 */

public class CameraAdapter extends BaseAdapter<DJIMedia> {
    private Context mContext;
    public CameraAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getContentView() {
        return R.layout.camera_file_item;
    }

    @Override
    public void onInitView(View view, int position) {
        DJIMedia media = getItem(position);

        ImageView img_camera_play = get(view, R.id.img_camera_play);
        ImageView img_camera_file_check = get(view, R.id.img_camera_file_check);
        ImageView img_camera_file_dialog = get(view, R.id.img_camera_file_dialog);
        ProgressBar progressbar_camera_file = get(view, R.id.progressbar_camera_file);

        if(DJIModuleVerificationUtil.isCameraModuleAvailable()){
            if (DJIModuleVerificationUtil.isMediaManagerAvailable() && media != null) {
                media.fetchThumbnail(new DJIMediaManager.CameraDownloadListener<Bitmap>() {
                    @Override
                    public void onStart() {
                        progressbar_camera_file.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onRateUpdate(long l, long l1, long l2) {

                    }

                    @Override
                    public void onProgress(long l, long l1) {
                        if(l == l1){
                            progressbar_camera_file.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        img_camera_file_dialog.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onFailure(DJIError djiError) {

                    }

                });
            }

        }else{

        }
    }
}
