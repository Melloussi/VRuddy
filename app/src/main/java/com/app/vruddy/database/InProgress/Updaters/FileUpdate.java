package com.app.vruddy.database.InProgress.Updaters;

public class FileUpdate {
    private String id;
    private int progress;
    private double total_size;
    private double current_size;
    private String downloadUrl;
    private String audioUrl;

    public FileUpdate(String id, int progress, double total_size, double current_size, String downloadUrl, String audioUrl) {
        this.id = id;
        this.progress = progress;
        this.total_size = total_size;
        this.current_size = current_size;
        this.downloadUrl = downloadUrl;
        this.audioUrl = audioUrl;
    }

    public String getId() {
        return id;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public int getProgress() {
        return progress;
    }

    public double getTotal_size() {
        return total_size;
    }

    public double getCurrent_size() {
        return current_size;
    }
}
