package com.app.vruddy.database.InProgress.Updaters;

public class AudioProgress {
    private String video_id;
    private int progress;

    public AudioProgress(String video_id, int progress) {
        this.video_id = video_id;
        this.progress = progress;
    }

    public String getVideo_id() {
        return video_id;
    }

    public int getProgress() {
        return progress;
    }
}
