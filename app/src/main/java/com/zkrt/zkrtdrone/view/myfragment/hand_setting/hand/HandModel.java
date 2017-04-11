package com.zkrt.zkrtdrone.view.myfragment.hand_setting.hand;

//import com.app.annotation.apt.Instance;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.RemoteSettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.batterysetting.BatterySettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.commonsetting.CommonSettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.dronesetting.DroneSettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.hdsetting.HDSettingFragment;
import com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.holdersetting.HolderSettingFragment;

/**
 * Created by jack_xie on 16/5/2.
 */
//@Instance
public class HandModel implements HandContract.Model {

    @Override
    public BatterySettingFragment getBatterySetting() {
        return new BatterySettingFragment();
    }

    @Override
    public CommonSettingFragment getCommonSetting() {
        return new CommonSettingFragment();
    }

    @Override
    public DroneSettingFragment getDroneSetting() {
        return new DroneSettingFragment();
    }

    @Override
    public HolderSettingFragment getHolderSetting() {
        return new HolderSettingFragment();
    }

    @Override
    public HDSettingFragment getHDsetting() {
        return new HDSettingFragment();
    }

    @Override
    public RemoteSettingFragment getRemoteSetting() {
        return new RemoteSettingFragment();
    }
}
