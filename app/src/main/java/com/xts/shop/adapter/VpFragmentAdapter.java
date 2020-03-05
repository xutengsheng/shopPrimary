package com.xts.shop.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class VpFragmentAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> mFragments;
    private final ArrayList<String> mTitles;

    public VpFragmentAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> fragments, ArrayList<String> titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }

    //页面
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    //页面的数量
    @Override
    public int getCount() {
        return mFragments.size();
    }

    //viewpager配合TabLayout+framgnet使用的时候,可以委托adapter去帮我们参加tabLayout的tab
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
