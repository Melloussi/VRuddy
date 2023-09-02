package com.app.vruddy.ViewModels;

import androidx.lifecycle.ViewModel;

import com.app.vruddy.Data.Background.AsyncTask.getRelatedVideoData;
import com.app.vruddy.Models.Objects.VideoObject;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.List;

public class WatchVM extends ViewModel {
    private SimpleExoPlayer exoPlayer;
    private int test;
    private List<VideoObject> videoData;

    private getRelatedVideoData relatedVideoData;

    private Boolean loadNewData = true;

    private long watchedTime;

    private boolean playState;

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

    public List<VideoObject> getVideoData() {
        return videoData;
    }

    public void setVideoData(List<VideoObject> videoData) {
        this.videoData = videoData;
    }

    public Boolean getLoadNewData() {
        return loadNewData;
    }

    public void setLoadNewData(Boolean loadNewData) {
        this.loadNewData = loadNewData;
    }

    public getRelatedVideoData getRelatedVideoData() {
        return relatedVideoData;
    }

    public void setRelatedVideoData(getRelatedVideoData relatedVideoData) {
        this.relatedVideoData = relatedVideoData;
    }

    public long getWatchedTime() {
        return watchedTime;
    }

    public void setWatchedTime(long watchedTime) {
        this.watchedTime = watchedTime;
    }

    public boolean isPlayState() {
        return playState;
    }

    public void setPlayState(boolean playState) {
        this.playState = playState;
    }
}
