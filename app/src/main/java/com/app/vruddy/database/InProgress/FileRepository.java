package com.app.vruddy.database.InProgress;

import android.app.Application;
import android.os.AsyncTask;

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

public class FileRepository {
    private FileDoa fileDoa;
    private LiveData<List<InProgressFile>> allFiles;

    public FileRepository(Application application) {
        FileDatabase database = FileDatabase.getInstance(application);
        fileDoa = database.fileDoa();
        allFiles = fileDoa.getAllFiles();
    }

    public void insert(InProgressFile inProgressFile) {
        new InsertFileAsyncTask(fileDoa).execute(inProgressFile);
    }

    public void update(InProgressFile inProgressFile) {
        new updateFileAsyncTask(fileDoa).execute(inProgressFile);
    }

    public void deleteById(String id) {
        new FileRepository.deleteFileByIdAsyncTask(fileDoa).execute(id);
    }

    public void updateById(FileUpdate fileUpdate) {
        new updateFileByIdAsyncTask(fileDoa).execute(fileUpdate);
    }

    public void updateFileStatueById(FileStatue fileStatue) {
        new updateFileStatueByIdAsyncTask(fileDoa).execute(fileStatue);
    }

    public void updateDownloadFileIdById(FileId fileId) {
        new updateDownloadFileIdByIdAsyncTask(fileDoa).execute(fileId);
    }

    public void updateAudioFileId(AudioId audioId) {
        new updateAudioDownloadFileIdByIdAsyncTask(fileDoa).execute(audioId);
    }

    public void updateFailFileIdById(FailFile failFile) {
        new updateFailByIdAsyncTask(fileDoa).execute(failFile);
    }

    public void updateConvertById(FileConverted fileConverted) {
        new updateConvertedByIdAsyncTask(fileDoa).execute(fileConverted);
    }

    public void updateAudioFailIdById(AudioFail audioFail) {
        new updateAudioFailByIdAsyncTask(fileDoa).execute(audioFail);
    }

    public void updateAudioProgressById(AudioProgress audioProgress) {
        new updateAudioProgressByIdAsyncTask(fileDoa).execute(audioProgress);
    }

    public void updateDownloadStatueById(IsDownloadComplete isDownloadComplete) {
        new updateDownloadStatueByIdAsyncTask(fileDoa).execute(isDownloadComplete);
    }

    public void deleteAll() {
        new FileRepository.deleteAllFilesAsyncTask(fileDoa).execute();
    }

    public LiveData<List<InProgressFile>> getAllFiles() {
        return allFiles;
    }

    private static class InsertFileAsyncTask extends AsyncTask<InProgressFile, Void, Void> {
        private FileDoa fileDoa;

        public InsertFileAsyncTask(FileDoa fileDoa) {
            this.fileDoa = fileDoa;
        }

        @Override
        protected Void doInBackground(InProgressFile... inProgressFiles) {
            fileDoa.insert(inProgressFiles[0]);
            return null;
        }
    }

    private static class updateFileAsyncTask extends AsyncTask<InProgressFile, Void, Void> {
        private FileDoa fileDoa;

        public updateFileAsyncTask(FileDoa fileDoa) {
            this.fileDoa = fileDoa;
        }

        @Override
        protected Void doInBackground(InProgressFile... inProgressFiles) {
            fileDoa.update(inProgressFiles[0]);
            return null;
        }
    }

    private static class deleteFileByIdAsyncTask extends AsyncTask<String, Void, Void> {

        private FileDoa fileDoa;

        public deleteFileByIdAsyncTask(FileDoa fileDoa) {
            this.fileDoa = fileDoa;
        }

        @Override
        protected Void doInBackground(String... strings) {
            fileDoa.deleteById(strings[0]);
            return null;
        }
    }

    private static class updateFileByIdAsyncTask extends AsyncTask<FileUpdate, Void, Void> {

        private FileDoa fileDoa;

        public updateFileByIdAsyncTask(FileDoa fileDoa) {
            this.fileDoa = fileDoa;
        }

        @Override
        protected Void doInBackground(FileUpdate... fileUpdate) {
            fileDoa.updateById(fileUpdate[0].getId(), fileUpdate[0].getProgress(), fileUpdate[0].getTotal_size(), fileUpdate[0].getCurrent_size(), fileUpdate[0].getDownloadUrl(), fileUpdate[0].getAudioUrl());
            return null;
        }
    }

    private static class updateFileStatueByIdAsyncTask extends AsyncTask<FileStatue, Void, Void> {

        private FileDoa fileDoa;

        public updateFileStatueByIdAsyncTask(FileDoa fileDoa) {
            this.fileDoa = fileDoa;
        }

        @Override
        protected Void doInBackground(FileStatue... fileStatues) {
            fileDoa.updateFileStatueById(fileStatues[0].getVideo_id(), fileStatues[0].isPaused());
            return null;
        }
    }

    private static class updateDownloadFileIdByIdAsyncTask extends AsyncTask<FileId, Void, Void> {

        private FileDoa fileDoa;

        public updateDownloadFileIdByIdAsyncTask(FileDoa fileDoa) {
            this.fileDoa = fileDoa;
        }

        @Override
        protected Void doInBackground(FileId... fileIds) {
            fileDoa.updateDownloadFileIdById(fileIds[0].getVideo_id(), fileIds[0].getDownloadFileId());
            return null;
        }
    }

    private static class updateConvertedByIdAsyncTask extends AsyncTask<FileConverted, Void, Void> {

        private FileDoa fileDoa;

        public updateConvertedByIdAsyncTask(FileDoa fileDoa) {
            this.fileDoa = fileDoa;
        }

        @Override
        protected Void doInBackground(FileConverted... fileConverted) {
            fileDoa.updateConvertById(fileConverted[0].getVideo_id(), fileConverted[0].isConverted());
            return null;
        }
    }

    private static class updateAudioDownloadFileIdByIdAsyncTask extends AsyncTask<AudioId, Void, Void> {

        private FileDoa fileDoa;

        public updateAudioDownloadFileIdByIdAsyncTask(FileDoa fileDoa) {
            this.fileDoa = fileDoa;
        }

        @Override
        protected Void doInBackground(AudioId... audioIds) {
            fileDoa.updateAudioDownloadIdById(audioIds[0].getVideo_id(), audioIds[0].getAudioId());
            return null;
        }
    }

    private static class updateFailByIdAsyncTask extends AsyncTask<FailFile, Void, Void> {

        private FileDoa fileDoa;

        public updateFailByIdAsyncTask(FileDoa fileDoa) {
            this.fileDoa = fileDoa;
        }

        @Override
        protected Void doInBackground(FailFile... failFiles) {
            fileDoa.updateFailFileIdById(failFiles[0].getVideo_id(), failFiles[0].isFile());
            return null;
        }
    }

    private static class updateAudioFailByIdAsyncTask extends AsyncTask<AudioFail, Void, Void> {

        private FileDoa fileDoa;

        public updateAudioFailByIdAsyncTask(FileDoa fileDoa) {
            this.fileDoa = fileDoa;
        }

        @Override
        protected Void doInBackground(AudioFail... audioFails) {
            fileDoa.updateFailAudioIdById(audioFails[0].getVideo_id(), audioFails[0].isAudioFile());
            return null;
        }
    }

    private static class updateAudioProgressByIdAsyncTask extends AsyncTask<AudioProgress, Void, Void> {

        private FileDoa fileDoa;

        public updateAudioProgressByIdAsyncTask(FileDoa fileDoa) {
            this.fileDoa = fileDoa;
        }

        @Override
        protected Void doInBackground(AudioProgress... audioProgresses) {
            fileDoa.updateAudioProgressById(audioProgresses[0].getVideo_id(), audioProgresses[0].getProgress());
            return null;
        }
    }

    private static class updateDownloadStatueByIdAsyncTask extends AsyncTask<IsDownloadComplete, Void, Void> {

        private FileDoa fileDoa;

        public updateDownloadStatueByIdAsyncTask(FileDoa fileDoa) {
            this.fileDoa = fileDoa;
        }

        @Override
        protected Void doInBackground(IsDownloadComplete... isDownloadCompletes) {
            fileDoa.updateIsDownloadComplete(isDownloadCompletes[0].getVideo_id(), isDownloadCompletes[0].isDownloadComplete());
            return null;
        }
    }

    private static class deleteAllFilesAsyncTask extends AsyncTask<InProgressFile, Void, Void> {
        private FileDoa fileDoa;

        public deleteAllFilesAsyncTask(FileDoa fileDoa) {
            this.fileDoa = fileDoa;
        }

        @Override
        protected Void doInBackground(InProgressFile... inProgressFiles) {
            fileDoa.deleteAll();
            return null;
        }
    }
}
