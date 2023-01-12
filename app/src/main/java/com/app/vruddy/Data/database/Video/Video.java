package com.app.vruddy.Data.database.Video;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "video_table")
public class Video {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private boolean badge;
    private String title;
    private String by;
    private String time_line;
    private String thumbnail;
    private String video_id;

    public Video(boolean badge, String title, String by, String time_line, String thumbnail, String video_id) {
        this.badge = badge;
        this.title = title;
        this.by = by;
        this.time_line = time_line;
        this.thumbnail = thumbnail;
        this.video_id = video_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isBadge() {
        return badge;
    }

    public String getTitle() {
        return title;
    }

    public String getBy() {
        return by;
    }

    public String getTime_line() {
        return time_line;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getVideo_id() {
        return video_id;
    }
}
