package com.app.vruddy.database.InProgress.Updaters;

public class AudioFail {
    private boolean isAudioFile;
    private String video_id;

    public AudioFail(boolean isAudioFile, String video_id) {
        this.isAudioFile = isAudioFile;
        this.video_id = video_id;
    }

    public boolean isAudioFile() {
        return isAudioFile;
    }

    public String getVideo_id() {
        return video_id;
    }
}
