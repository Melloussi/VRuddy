package com.app.vruddy.Objects;

public class VideoObject {
    private String videoId;
    private String title;
    private String by;
    private String time;
    private String date;
    private String views;
    private boolean badge;
    private String thumbnail;
    private String channelThumbnail;

    public VideoObject(String videoId, String title, String by, String time, String date, String views, boolean badge, String thumbnail, String channelThumbnail) {
        this.videoId = videoId;
        this.title = title;
        this.by = by;
        this.time = time;
        this.date = date;
        this.views = views;
        this.badge = badge;
        this.thumbnail = thumbnail;
        this.channelThumbnail = channelThumbnail;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }

    public String getBy() {
        return by;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getViews() {
        return views;
    }

    public boolean isBadge() {
        return badge;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getChannelThumbnail() {
        return channelThumbnail;
    }
}
