package com.app.vruddy.database.Music;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface MusicDoa {
    @Insert
    void insert(Music music);

    @Query
            ("DELETE FROM music_table WHERE video_id = :id")
    void deleteById(String id);

    @Query
            ("DELETE FROM music_table")
    void deleteAll();

    @Query
            ("SELECT * FROM music_table")
    LiveData<List<Music>> getAllMusic();
}
