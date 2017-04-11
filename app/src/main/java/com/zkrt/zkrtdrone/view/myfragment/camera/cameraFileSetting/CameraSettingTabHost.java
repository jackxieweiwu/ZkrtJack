package com.zkrt.zkrtdrone.view.myfragment.camera.cameraFileSetting;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.adapter.SettingCameraFragmentAdapter;
import com.zkrt.zkrtdrone.view.myfragment.camera.cameraFileSetting.cameraAutoFragment.PhotoCametaFragment;
import com.zkrt.zkrtdrone.view.myfragment.camera.cameraFileSetting.cameraAutoFragment.SettingCametaFragment;
import com.zkrt.zkrtdrone.view.myfragment.camera.cameraFileSetting.cameraAutoFragment.VIdeoCameraFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-4-4.
 */

public class CameraSettingTabHost {
    private View menuViewRight;
    private Context mContext;
    private TabLayout tab_title;
    private ViewPager vp_pager;
    private List<Fragment> list_fragment;
    private List<String> list_title;
    private String[] moudleName;
    private SettingCameraFragmentAdapter settingCameraFragmentAdapter;

    public CameraSettingTabHost(Activity mActivity) {
        this.mContext = mActivity;
        list_fragment = new ArrayList<>();
        list_title = new ArrayList<>();
    }

    public void setCameraSettingTabHost(PhotoCametaFragment photoCametaFragment, VIdeoCameraFragment vIdeoCameraFragment, SettingCametaFragment settingCametaFragment){
        //TypedArray icons = mContext.getResources().obtainTypedArray(R.array.setting_camera_img);
        moudleName = mContext.getResources().getStringArray(R.array.setting_camera_item);
        for (int i=0;i<moudleName.length;i++){
            String nameLable = moudleName[i];
            list_title.add(nameLable);
            if(i == 0){
                list_fragment.add(photoCametaFragment);
            }

            if(i == 1){
                list_fragment.add(vIdeoCameraFragment);
            }

            if(i == 2){
                list_fragment.add(settingCametaFragment);
            }
        }
    }

    public View getSettingView(View inflate){
        menuViewRight=inflate;
        return menuViewRight;
    }

    public void addTableView(){
        tab_title = (TabLayout)menuViewRight.findViewById(R.id.setting_tab_layout);
        vp_pager = (ViewPager)menuViewRight.findViewById(R.id.vp_pager);

        tab_title.setTabMode(TabLayout.MODE_SCROLLABLE);
        tab_title.setPadding(20,20,20,20);
        fragmentChange();
    }

    private void fragmentChange() {
        settingCameraFragmentAdapter = new SettingCameraFragmentAdapter(DJISampleApplication.fragmentManager,list_fragment, list_title);
        vp_pager.setAdapter(settingCameraFragmentAdapter);
        tab_title.setupWithViewPager(vp_pager);
    }
}
