package com.app.vruddy.Data.database.InProgress.Updaters;

public class AudioId {
    private String video_id;
    private int audioId;

    public AudioId(String video_id, int audioId) {
        this.video_id = video_id;
        this.audioId = audioId;
    }

    public String getVideo_id() {
        return video_id;
    }

    public int getAudioId() {
        return audioId;
    }
}
