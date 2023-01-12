package com.app.vruddy.Data.database.Video;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface VideoDoa {
    @Insert
    void insert(Video video);

    @Query
            ("DELETE FROM video_table WHERE video_id = :id")
    void deleteById(String id);

    @Query
            ("DELETE FROM video_table")
    void deleteAll();

    @Query
            ("SELECT * FROM video_table")
    LiveData<List<Video>> getAllVideos();
}
