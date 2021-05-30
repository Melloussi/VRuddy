package com.app.vruddy.database.InProgress;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FileDoa {
    @Insert
    void insert(InProgressFile inProgressFile);

    @Update
    void update(InProgressFile inProgressFile);

    @Query("DELETE FROM downloading_table WHERE video_id = :id")
    void deleteById(String id);

    @Query("DELETE FROM downloading_table")
    void deleteAll();

    @Query("UPDATE downloading_table SET progress = :progress, total_size = :total_size, current_size = :current_size, downloadUrl = :downloadUrl, audioUrl = :audioUrl WHERE video_id = :id;")
    void updateById(String id, int progress, double total_size, double current_size, String downloadUrl, String audioUrl);

    @Query("UPDATE downloading_table SET isPaused = :isPaused WHERE video_id = :id;")
    void updateFileStatueById(String id, boolean isPaused);

    @Query("UPDATE downloading_table SET isFileFail = :isFileFail WHERE video_id = :id;")
    void updateFailFileIdById(String id, boolean isFileFail);

    @Query("UPDATE downloading_table SET isAudioFail = :isAudioFail WHERE video_id = :id;")
    void updateFailAudioIdById(String id, boolean isAudioFail);

    @Query("UPDATE downloading_table SET isConverted = :isConverted WHERE video_id = :id;")
    void updateConvertById(String id, boolean isConverted);

    @Query("UPDATE downloading_table SET downloadFileId = :fileDownloadId WHERE video_id = :id;")
    void updateDownloadFileIdById(String id, int fileDownloadId);

    @Query("UPDATE downloading_table SET audioFileId = :audioFileId WHERE video_id = :id;")
    void updateAudioDownloadIdById(String id, int audioFileId);

    @Query("UPDATE downloading_table SET progress = :progress WHERE video_id = :video_id;")
    void updateAudioProgressById(String video_id, int progress);

    @Query("UPDATE downloading_table SET isFileCompleted = :isComplete WHERE video_id = :id;")
    void updateIsDownloadComplete(String id, boolean isComplete);

    @Query("SELECT * FROM downloading_table")
    LiveData<List<InProgressFile>> getAllFiles();
}
