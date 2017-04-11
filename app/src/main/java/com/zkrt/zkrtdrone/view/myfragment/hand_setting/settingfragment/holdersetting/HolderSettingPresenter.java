package com.zkrt.zkrtdrone.view.myfragment.hand_setting.settingfragment.holdersetting;

/**
 * Created by jack_xie on 17-2-21.
 */

public class HolderSettingPresenter extends HolderSettingContract.Presenter{
    @Override
    public void onAttached() {
        getShowAdapter();
    }

    @Override
    public void getShowAdapter() {
        mView.showAdapter(mModel.getAdapter());
    }
}
