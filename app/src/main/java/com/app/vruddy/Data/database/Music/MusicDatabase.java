package com.app.vruddy.Data.database.Music;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;



@Database(entities = Music.class, version = 1)
public abstract class MusicDatabase extends RoomDatabase{
    private static MusicDatabase instance;
    public abstract MusicDoa musicDoa();

    public static synchronized MusicDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MusicDatabase.class, "music_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
