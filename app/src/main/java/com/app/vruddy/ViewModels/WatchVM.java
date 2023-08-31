package com.app.vruddy.ViewModels;

import androidx.lifecycle.ViewModel;

import com.google.android.exoplayer2.SimpleExoPlayer;

public class WatchVM extends ViewModel {
    private SimpleExoPlayer exoPlayer;
    private int test;

    public SimpleExoPlayer getExoPlayer() {
        return exoPlayer;
    }

    public void setExoPlayer(SimpleExoPlayer exoPlayer) {
        this.exoPlayer = exoPlayer;
    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }
}
