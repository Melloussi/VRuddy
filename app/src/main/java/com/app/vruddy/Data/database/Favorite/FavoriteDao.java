package com.app.vruddy.Data.database.Favorite;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Insert
    void insert(Favorite favorite);

    @Update
    void update(Favorite favorite);

    @Delete
    void delete(Favorite favorite);

    @Query("DELETE FROM favorite_video WHERE video_id = :id")
    void deleteById(String id);

    @Query("DELETE FROM favorite_video")
    void deleteAll();


    @Query("SELECT * FROM favorite_video")
    LiveData<List<Favorite>> getAllFavorites();
}
