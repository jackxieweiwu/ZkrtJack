package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.holdersetting;

import android.widget.ArrayAdapter;

import dji.common.error.DJIError;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseModel;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BasePresenter;
import zkrtdrone.zkrt.com.jackmvpmoudle.base.BaseView;

/**
 * Created by jack_xie on 17-2-21.
 */

public interface HolderSettingContract {
    interface Model extends BaseModel {
        ArrayAdapter getAdapter();
    }

    interface View extends BaseView {
        void showAdapter(ArrayAdapter adapter);
    }

    abstract class Presenter extends BasePresenter<HolderSettingContract.Model, HolderSettingContract.View> {
        public abstract void getShowAdapter();
    }
}
