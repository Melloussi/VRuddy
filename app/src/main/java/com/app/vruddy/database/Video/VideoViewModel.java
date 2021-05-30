package com.app.vruddy.database.Video;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import java.util.List;

public class VideoViewModel extends AndroidViewModel {
    private VideoRepository videoRepository;
    private LiveData<List<Video>> allVideo;

    public VideoViewModel(@NonNull Application application) {
        super(application);
        videoRepository = new VideoRepository(application);
        allVideo = videoRepository.getAllFiles();
    }

    public void insert(Video video){
        videoRepository.insert(video);
    }

    public void deleteAll(){
        videoRepository.deleteAll();
    }

    public void deleteById(String id){
        videoRepository.deleteById(id);
    }

    public LiveData<List<Video>> getAllVideos(){return allVideo;}
}
