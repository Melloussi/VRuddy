package com.app.vruddy.Data.database.InProgress;

import com.app.vruddy.Data.database.InProgress.Updaters.AudioFail;
import com.app.vruddy.Data.database.InProgress.Updaters.AudioId;
import com.app.vruddy.Data.database.InProgress.Updaters.FailFile;
import com.app.vruddy.Data.database.InProgress.Updaters.FileConverted;
import com.app.vruddy.Data.database.InProgress.Updaters.IsDownloadComplete;
import com.app.vruddy.Data.database.InProgress.Updaters.FileUpdate;
import com.app.vruddy.Data.database.InProgress.Updaters.AudioProgress;
import com.app.vruddy.Data.database.InProgress.Updaters.FileId;

public interface FileHandler {
    void update(FileUpdate fileUpdate);
    void updateDownloadFileId(FileId fileId);
    void updateAudioFileId(AudioId audioId);
    void updateAudioProgressById(AudioProgress audioProgress);
    void updateDownloadStatue(IsDownloadComplete isDownloadComplete);
    void updateConvertById(FileConverted fileConverted);
    void updateFailFile(FailFile failFile);
    void updateAudioFail(AudioFail audioFail);
    void deleteFileById(String video_id);
}
