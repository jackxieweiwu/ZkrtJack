package com.zkrt.zkrtdrone.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by jack_xie on 16-12-29.
 */

public class SettingFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> list_fragment;                         //fragment列表
    private List<String> list_Title;                              //tab名的列表

    public SettingFragmentAdapter(FragmentManager fm,List<Fragment>list_fragment,List<String> list_Title) {
        super(fm);
        this.list_fragment = list_fragment;
        this.list_Title = list_Title;
    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_fragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list_Title.get(position % list_Title.size());
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }
}
