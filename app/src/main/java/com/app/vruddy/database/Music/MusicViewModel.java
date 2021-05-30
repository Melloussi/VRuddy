package com.app.vruddy.database.Music;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;


public class MusicViewModel extends AndroidViewModel {
    private MusicRepository musicRepository;
    private LiveData<List<Music>> allMusic;

    public MusicViewModel(@NonNull Application application) {
        super(application);
        musicRepository = new MusicRepository(application);
        allMusic = musicRepository.getAllFiles();
    }

    public void insert(Music music){
        musicRepository.insert(music);
    }

    public void deleteAll(){
        musicRepository.deleteAll();
    }

    public void deleteById(String id){
        musicRepository.deleteById(id);
    }

    public LiveData<List<Music>> getAllMusic(){return allMusic;}
}
