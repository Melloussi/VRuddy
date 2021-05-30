package com.app.vruddy.database.InProgress;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.vruddy.database.InProgress.Updaters.AudioFail;
import com.app.vruddy.database.InProgress.Updaters.AudioId;
import com.app.vruddy.database.InProgress.Updaters.FailFile;
import com.app.vruddy.database.InProgress.Updaters.FileConverted;
import com.app.vruddy.database.InProgress.Updaters.IsDownloadComplete;
import com.app.vruddy.database.InProgress.Updaters.FileUpdate;
import com.app.vruddy.database.InProgress.Updaters.AudioProgress;
import com.app.vruddy.database.InProgress.Updaters.FileId;
import com.app.vruddy.database.InProgress.Updaters.FileStatue;

import java.util.List;

public class FileViewModel extends AndroidViewModel {
    private FileRepository fileRepository;
    private LiveData<List<InProgressFile>> allFiles;

    public FileViewModel(@NonNull Application application) {
        super(application);
        fileRepository = new FileRepository(application);
        allFiles = fileRepository.getAllFiles();
    }
    public void insertFile(InProgressFile inProgressFile){
        fileRepository.insert(inProgressFile);
    }

    public void updateFile(InProgressFile inProgressFile){
        fileRepository.update(inProgressFile);
    }

    public void deleteFileById(String id){
        fileRepository.deleteById(id);
    }

    public void updateFileById(FileUpdate fileUpdate){
        fileRepository.updateById(fileUpdate);
    }

    public void updateFileStatueById(FileStatue fileStatue){
        fileRepository.updateFileStatueById(fileStatue);
    }

    public void updateDownloadFileIdById(FileId fileId){
        fileRepository.updateDownloadFileIdById(fileId);
    }

    public void updateAudioFileId(AudioId audioId){
        fileRepository.updateAudioFileId(audioId);
    }

    public void updateConvertById(FileConverted fileConverted){
        fileRepository.updateConvertById(fileConverted);
    }

    public void updateFailFileIdById(FailFile failFile){
        fileRepository.updateFailFileIdById(failFile);
    }

    public void updateAudioFail(AudioFail audioFail){
        fileRepository.updateAudioFailIdById(audioFail);
    }

    public void updateAudioProgressById(AudioProgress audioProgress){
        fileRepository.updateAudioProgressById(audioProgress);
    }

    public void updateDownloadStatueById(IsDownloadComplete isDownloadComplete){
        fileRepository.updateDownloadStatueById(isDownloadComplete);
    }

    public void deleteAll(){
        fileRepository.deleteAll();
    }

    public LiveData<List<InProgressFile>> getAllFiles(){
        return allFiles;
    }
}
