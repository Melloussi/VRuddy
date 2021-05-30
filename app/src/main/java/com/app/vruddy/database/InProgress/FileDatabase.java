package com.app.vruddy.database.InProgress;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = InProgressFile.class, version = 10)
public abstract class FileDatabase extends RoomDatabase {
    private static FileDatabase instance;
    public abstract FileDoa fileDoa();

    public static synchronized FileDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FileDatabase.class, "downloading_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
