package com.app.vruddy.database.InProgress.Updaters;

public class IsDownloadComplete {
    private String video_id;
    private boolean isDownloadComplete;

    public IsDownloadComplete(String video_id, boolean isDownloadComplete) {
        this.video_id = video_id;
        this.isDownloadComplete = isDownloadComplete;
    }

    public String getVideo_id() {
        return video_id;
    }

    public boolean isDownloadComplete() {
        return isDownloadComplete;
    }
}
