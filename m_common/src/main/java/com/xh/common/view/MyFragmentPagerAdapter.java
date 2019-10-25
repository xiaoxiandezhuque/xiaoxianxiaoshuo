package com.xh.common.view;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by BHKJ on 2016/8/19.
 */

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;
    private String[] mTitles;

    public MyFragmentPagerAdapter(List<Fragment> fragments, FragmentManager fm) {
        super(fm);
        mFragments = fragments;
    }

    public MyFragmentPagerAdapter(List<Fragment> fragments, FragmentManager fm, String[] titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles != null ? mTitles[position] : "";
    }
}
