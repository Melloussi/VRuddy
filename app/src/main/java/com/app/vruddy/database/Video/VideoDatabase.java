package com.app.vruddy.database.Video;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = Video.class, version = 1)
public abstract class VideoDatabase extends RoomDatabase {
    private static VideoDatabase instance;
    public abstract VideoDoa videoDoa();

    public static synchronized VideoDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    VideoDatabase.class, "video_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
