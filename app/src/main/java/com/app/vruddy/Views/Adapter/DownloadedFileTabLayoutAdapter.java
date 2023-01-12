package com.app.vruddy.Views.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.vruddy.Views.Fragment.MusicFragment;
import com.app.vruddy.Views.Fragment.VideoFragment;
import com.app.vruddy.Views.Fragment.inProgressFragment;

//FragmentPagerAdapter
public class DownloadedFileTabLayoutAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitles = new String[]{"Progress", "Music", "Video"};

    public DownloadedFileTabLayoutAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new inProgressFragment();
                break;
            case 1:
                fragment = new MusicFragment();
                break;
            case 2:
                fragment = new VideoFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
