package com.app.vruddy.Views.ViewManager;

import static com.app.vruddy.Data.Global.GlobalConstantVariables.HOME_FRAGMENT;
import static com.app.vruddy.Data.Global.GlobalConstantVariables.SEARCH_FRAGMENT;
import static com.app.vruddy.Data.Global.GlobalConstantVariables.WATCH_FRAGMENT;

import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.vruddy.R;
import com.app.vruddy.Views.Fragment.HomeFragment;
import com.app.vruddy.Views.Fragment.SearchFragment;
import com.app.vruddy.Views.Fragment.WatchFragment;

public class Manager {
    private final AppCompatActivity activity;
    private boolean isHidden = false;
    private boolean isMinimize = false;

    public Manager(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void fragmentManager(int flag, Fragment...fragments ){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (flag){
            case HOME_FRAGMENT:
                fragmentTransaction.add(R.id.homeFragment, new HomeFragment());
                fragmentTransaction.commit();
                break;
            case SEARCH_FRAGMENT:
                fragmentTransaction.add(R.id.searchFragment, new SearchFragment());
                fragmentTransaction.commit();
                break;
            case WATCH_FRAGMENT:
                if(fragments[0] != null){
                    fragmentTransaction.add(R.id.watchFragment, fragments[0]);
                    fragmentTransaction.commit();
                }
                break;
        }
    }

    public void manageFrameLayoutVisibility() {
        if (!isHidden) {
            if (isMinimize) {
                resizeWatchFragment(0);
                isMinimize = false;
            } else {
                FrameLayout frameLayout = activity.findViewById(R.id.watchFragment);
                frameLayout.setVisibility(View.GONE);
                isHidden = true;
            }
        } else {
            FrameLayout frameLayout = activity.findViewById(R.id.watchFragment);
            frameLayout.setVisibility(View.VISIBLE);
            isHidden = false;
        }
    }

    public void resizeWatchFragment(int progress) {
        FrameLayout frameLayout = activity.findViewById(R.id.watchFragment);
        if (progress == 1) {
            FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            frameLayout.setLayoutParams(fl);
            fl.gravity = Gravity.BOTTOM;
            frameLayout.requestLayout();
            System.out.println("-------- I Minimized");
            isMinimize = true;
        } else {
            FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            frameLayout.setLayoutParams(fl);
            frameLayout.requestLayout();
            System.out.println("------- I Maximized");
            isMinimize = false;
        }
    }
}
