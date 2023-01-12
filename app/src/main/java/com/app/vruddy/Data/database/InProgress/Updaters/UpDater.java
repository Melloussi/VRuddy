package com.app.vruddy.Data.database.InProgress.Updaters;

import android.app.Application;
import android.os.AsyncTask;

import com.app.vruddy.Data.database.InProgress.FileRepository;

public class UpDater extends AsyncTask<FileUpdate, Void, Void> {
    private FileRepository fileRepository;
    private Application application;

    public UpDater(Application application) {
        this.application = application;
    }

    @Override
    protected Void doInBackground(FileUpdate... fileUpdates) {
        fileRepository = new FileRepository(application);
        fileRepository.updateById(fileUpdates[0]);
        return null;
    }
}
