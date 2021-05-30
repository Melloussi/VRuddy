package com.app.vruddy.database.Music;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;


public class MusicRepository {
    private MusicDoa musicDoa;
    private LiveData<List<Music>> allMusic;

    public MusicRepository(Application application) {
        MusicDatabase database = MusicDatabase.getInstance(application);
        musicDoa = database.musicDoa();
        allMusic = musicDoa.getAllMusic();
    }

    public void insert(Music music) {
        new InsertMusicAsyncTask(musicDoa).execute(music);
    }

    public void deleteAll() {
        new deleteAllMusicAsyncTask(musicDoa).execute();
    }

    public void deleteById(String id) {
        new deleteById(musicDoa).execute(id);
    }

    public LiveData<List<Music>> getAllFiles() {
        return allMusic;
    }

    private class InsertMusicAsyncTask extends AsyncTask<Music, Void, Void> {
        private MusicDoa musicDoa;

        public InsertMusicAsyncTask(MusicDoa musicDoa) {
            this.musicDoa = musicDoa;
        }

        @Override
        protected Void doInBackground(Music... music) {
            musicDoa.insert(music[0]);
            return null;
        }
    }

    private class deleteAllMusicAsyncTask extends AsyncTask<Void, Void, Void> {
        private MusicDoa musicDoa;

        public deleteAllMusicAsyncTask(MusicDoa musicDoa) {
            this.musicDoa = musicDoa;
        }

        @Override
        protected Void doInBackground(Void... music) {
            musicDoa.deleteAll();
            return null;
        }
    }

    private class deleteById extends AsyncTask<String, Void, Void> {
        MusicDoa musicDoa;

        public deleteById(MusicDoa musicDoa) {
            this.musicDoa = musicDoa;
        }

        @Override
        protected Void doInBackground(String... strings) {
            musicDoa.deleteById(strings[0]);
            return null;
        }
    }
}
