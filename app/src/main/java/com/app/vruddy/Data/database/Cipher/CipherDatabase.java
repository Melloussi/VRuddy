package com.app.vruddy.Data.database.Cipher;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Cipher.class, version = 1)
public abstract class CipherDatabase extends RoomDatabase {
    private static CipherDatabase instance;
    public abstract CipherDao cipherDao();

    public static synchronized CipherDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CipherDatabase.class, "cipher_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
