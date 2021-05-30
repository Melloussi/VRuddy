package com.app.vruddy.database.InProgress.Updaters;

public class FileStatue {
    private String video_id;
    private boolean isPaused;

    public FileStatue(String video_id, boolean isPaused) {
        this.video_id = video_id;
        this.isPaused = isPaused;
    }

    public String getVideo_id() {
        return video_id;
    }

    public boolean isPaused() {
        return isPaused;
    }
}
