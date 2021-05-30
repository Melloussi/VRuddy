package com.app.vruddy.database.Video;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import java.util.List;

public class VideoRepository {
    private VideoDoa videoDoa;
    private LiveData<List<Video>> allVideos;

    public VideoRepository(Application application) {
        VideoDatabase database = VideoDatabase.getInstance(application);
        videoDoa = database.videoDoa();
        allVideos = videoDoa.getAllVideos();
    }

    public void insert(Video video) {
        new InsertVideoAsyncTask(videoDoa).execute(video);
    }

    public void deleteAll() {
        new deleteAllVideoAsyncTask(videoDoa).execute();
    }

    public void deleteById(String id) {
        new deleteById(videoDoa).execute(id);
    }

    public LiveData<List<Video>> getAllFiles() {
        return allVideos;
    }

    private class InsertVideoAsyncTask extends AsyncTask<Video, Void, Void> {
        private VideoDoa videoDoa;

        public InsertVideoAsyncTask(VideoDoa videoDoa) {
            this.videoDoa = videoDoa;
        }

        @Override
        protected Void doInBackground(Video... videos) {
            videoDoa.insert(videos[0]);
            return null;
        }
    }

    private class deleteAllVideoAsyncTask extends AsyncTask<Void, Void, Void> {
        private VideoDoa videoDoa;

        public deleteAllVideoAsyncTask(VideoDoa videoDoa) {
            this.videoDoa = videoDoa;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            videoDoa.deleteAll();
            return null;
        }
    }

    private class deleteById extends AsyncTask<String, Void, Void> {
        private VideoDoa videoDoa;

        public deleteById(VideoDoa videoDoa) {
            this.videoDoa = videoDoa;
        }

        @Override
        protected Void doInBackground(String... strings) {
            videoDoa.deleteById(strings[0]);
            return null;
        }
    }
}
