package com.henley.appmanage.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.henley.appmanage.fragment.AppInfoFragment;

import java.util.List;

/**
 * @author Henley
 * @date 2017/4/17 17:05
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {

    private List<String> titles;
    private List<AppInfoFragment> fragments;

    public FragmentAdapter(@NonNull FragmentManager fm, @NonNull List<String> titles, @NonNull List<AppInfoFragment> fragments) {
        super(fm);
        this.titles = titles;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }
}
