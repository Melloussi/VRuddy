package com.app.vruddy.database.InProgress.Updaters;

public class FileId {
    private String video_id;
    private int downloadFileId;

    public FileId(String video_id, int downloadFileId) {
        this.video_id = video_id;
        this.downloadFileId = downloadFileId;
    }

    public String getVideo_id() {
        return video_id;
    }

    public int getDownloadFileId() {
        return downloadFileId;
    }
}
