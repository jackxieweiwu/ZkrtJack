package com.zkrt.zkrtdrone.view.myfragment.hand_setting;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zkrt.zkrtdrone.DJISampleApplication;
import com.zkrt.zkrtdrone.R;
import com.zkrt.zkrtdrone.adapter.SettingFragmentAdapter;
import com.zkrt.zkrtdrone.view.myfragment.camera.ModelCameraAndOne;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.RemoteSettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.batterysetting.BatterySettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.commonsetting.CommonSettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.dronesetting.DroneSettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.hdsetting.HDSettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.offlinemap.OffLineMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack_xie on 16-12-29.
 */

public class SettingTabLayout {
    private View menuViewRight;
    private Context mContext;
    private String[] moudleName;
    //private TypedArray moudleImg;
    private TabLayout tab_title;
    private ViewPager vp_pager;
    private List<Fragment> list_fragment;
    private List<String> list_title;
    private SettingFragmentAdapter settingFragmentadapter;

    public SettingTabLayout(Activity mActivity) {
        this.mContext = mActivity;
        //moudleImg = mContext.getResources().obtainTypedArray(R.array.setting_img);
        list_fragment = new ArrayList<>();
        list_title = new ArrayList<>();

    }

    public void setSettingFragment(BatterySettingFragment batterySettingFragment,
                                   CommonSettingFragment commonSettingFragment,
                                   DroneSettingFragment droneSettingFragment,
                                   HDSettingFragment hdSettingFragment,
                                   RemoteSettingFragment remoteSettingFragment, ModelCameraAndOne modelCameraAndOne){
        moudleName = mContext.getResources().getStringArray(R.array.setting_item);
        droneSettingFragment.setModelCameraAndOne(modelCameraAndOne);
        for (int i=0;i<moudleName.length;i++){
            String nameLable = moudleName[i];
            /*if(nameLable.equals("云台") && DJIModuleVerificationUtil.isGimbalModuleAvailable()){
                list_title.add(nameLable);
                list_fragment.add(new HolderSettingFragment());
            }*/

            if(nameLable.equals("飞机")){
                list_fragment.add(droneSettingFragment);
                list_title.add(nameLable);
            }

            if(nameLable.equals("遥控")){
                list_fragment.add(remoteSettingFragment);
                list_title.add(nameLable);
            }

            if(nameLable.equals("相机")){
                list_fragment.add(hdSettingFragment);
                list_title.add(nameLable);
            }

            if(nameLable.equals("电池")){
                list_fragment.add(batterySettingFragment);
                list_title.add(nameLable);
            }

            if(nameLable.equals("通用")){
                list_fragment.add(commonSettingFragment);
                list_title.add(nameLable);
            }

            if(nameLable.equals("Map")){
                list_fragment.add(new OffLineMap());
                list_title.add(nameLable);
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
        settingFragmentadapter = new SettingFragmentAdapter(DJISampleApplication.fragmentManager,list_fragment, list_title);
        vp_pager.setAdapter(settingFragmentadapter);
        tab_title.setupWithViewPager(vp_pager);
    }
}