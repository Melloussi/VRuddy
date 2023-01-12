package com.app.vruddy.Data.database.InProgress.Updaters;

public class FileConverted {
    private String video_id;
    private boolean isConverted;

    public FileConverted(String video_id, boolean isConverted) {
        this.video_id = video_id;
        this.isConverted = isConverted;
    }
    public String getVideo_id() {
        return video_id;
    }

    public boolean isConverted() {
        return isConverted;
    }
}
