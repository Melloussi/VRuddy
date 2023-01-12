package com.app.vruddy.Data.database.InProgress.Updaters;

public class FailFile {
    private boolean file;
    private String video_id;

    public FailFile(boolean file, String video_id) {
        this.file = file;
        this.video_id = video_id;
    }

    public boolean isFile() {
        return file;
    }

    public String getVideo_id() {
        return video_id;
    }
}
