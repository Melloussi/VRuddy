package com.app.vruddy.Data.database.InProgress;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "downloading_table")
public class InProgressFile {
    @PrimaryKey(autoGenerate = true)
    private int id;
    final private int downloadFileId;
    final private int audioFileId;
    private int pause;
    private int resume;
    final private int retry;
    final private boolean isConverted;
    private boolean isPaused;
    private boolean isFileCompleted;
    private boolean isFileFail;
    private boolean isAudioFail;
    private boolean badge;
    private int progress;
    private long total_size;
    private long current_size;
    private String file_type;
    private String title;
    private String by;
    private String time_line;
    private String thumbnail;
    private String video_id;
    private String downloadUrl;
    private String audioUrl;

    public InProgressFile(int downloadFileId, int audioFileId, int pause, int resume, int retry, boolean isConverted, boolean isPaused, boolean isFileCompleted, boolean isFileFail, boolean isAudioFail, boolean badge, int progress, long total_size, long current_size, String file_type, String title, String by, String time_line, String thumbnail, String video_id, String downloadUrl, String audioUrl) {

        this.downloadFileId = downloadFileId;
        this.audioFileId = audioFileId;
        this.pause = pause;
        this.resume = resume;
        this.retry = retry;
        this.isConverted = isConverted;
        this.isPaused = isPaused;
        this.isFileCompleted = isFileCompleted;
        this.isFileFail = isFileFail;
        this.isAudioFail = isAudioFail;
        this.badge = badge;
        this.progress = progress;
        this.total_size = total_size;
        this.current_size = current_size;
        this.file_type = file_type;
        this.title = title;
        this.by = by;
        this.time_line = time_line;
        this.thumbnail = thumbnail;
        this.video_id = video_id;
        this.downloadUrl = downloadUrl;
        this.audioUrl = audioUrl;
    }

    public static boolean areContentsTheSame(InProgressFile oldData, InProgressFile newData) {
        if (oldData.getId() != newData.getId()){
            return false;
        }if (oldData.getPause() != newData.getPause()){
            return false;
        }if (oldData.getResume() != newData.getResume()){
            return false;
        }if (oldData.getProgress() != newData.getProgress()){
            return false;
        }if (oldData.getTotal_size() != newData.getTotal_size()){
            return false;
        }if (oldData.getCurrent_size() != newData.getCurrent_size()){
            return false;
        }if (oldData.getFile_type() != newData.getFile_type()){
            return false;
        }if (oldData.getTitle() != newData.getTitle()){
            return false;
        }if (oldData.getBy() != newData.getBy()){
            return false;
        }if (oldData.getTime_line() != newData.getTime_line()){
            return false;
        }if (oldData.badge != newData.badge){
            return false;
        }if (oldData.getThumbnail() != newData.getThumbnail()){
            return false;
        }if (oldData.getVideo_id() != newData.getVideo_id()){
            return false;
        }
        return true;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public void setPause(int pause) {
        this.pause = pause;
    }

    public void setResume(int resume) {
        this.resume = resume;
    }

    public boolean isFileCompleted() {
        return isFileCompleted;
    }

    public boolean isFileFail() {
        return isFileFail;
    }

    public boolean isAudioFail() {
        return isAudioFail;
    }

    public boolean isConverted() {
        return isConverted;
    }

    public int getRetry() {
        return retry;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public int getDownloadFileId() {
        return downloadFileId;
    }

    public int getAudioFileId() {
        return audioFileId;
    }

    public int getId() {
        return id;
    }

    public int getProgress() {
        return progress;
    }

    public long getTotal_size() {
        return total_size;
    }

    public long getCurrent_size() {
        return current_size;
    }

    public String getFile_type() {
        return file_type;
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

    public boolean isBadge() {
        return badge;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getVideo_id() {
        return video_id;
    }

    public int getPause() {
        return pause;
    }

    public int getResume() {
        return resume;
    }
}
