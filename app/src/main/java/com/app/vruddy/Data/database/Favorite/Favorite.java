package com.app.vruddy.Data.database.Favorite;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.app.vruddy.Models.Objects.VideoObject;

//Favorite video
@Entity(tableName = "favorite_video")
public class Favorite {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String by;
    private String views;
    private String date;
    private String time_line;
    private boolean badge;
    private String thumbnail;
    private String channel_image;
    private String video_id;

    public Favorite(String title, String by, String views, String date, String time_line, boolean badge, String thumbnail, String channel_image, String video_id) {
        this.title = title;
        this.by = by;
        this.views = views;
        this.date = date;
        this.time_line = time_line;
        this.badge = badge;
        this.thumbnail = thumbnail;
        this.channel_image = channel_image;
        this.video_id = video_id;
    }

    public Favorite(VideoObject videoObject) {
        this.title = videoObject.getTitle();
        this.by = videoObject.getBy();
        this.views = videoObject.getViews();
        this.date = videoObject.getDate();
        this.time_line = videoObject.getTime();
        this.badge = videoObject.isBadge();
        this.thumbnail = videoObject.getThumbnail();
        this.channel_image = videoObject.getChannelThumbnail();
        this.video_id = videoObject.getVideoId();
    }

    //Setter
    public void setId(int id) {
        this.id = id;
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBy() {
        return by;
    }

    public String getViews() {
        return views;
    }

    public String getDate() {
        return date;
    }

    public String getTime_line() {
        return time_line;
    }

    public boolean isBadge() {
        return badge;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getChannel_image() {
        return channel_image;
    }

    public String getVideo_id() {
        return video_id;
    }
}
