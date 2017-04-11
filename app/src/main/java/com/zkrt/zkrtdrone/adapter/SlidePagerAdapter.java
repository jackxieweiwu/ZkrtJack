package com.zkrt.zkrtdrone.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by jack_xie on 17-1-17.
 */

public class SlidePagerAdapter extends FragmentPagerAdapter {
    public SlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        /*if (position == 0)
            return new RootFragment();
        else
            return new StaticFragment();*/
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
