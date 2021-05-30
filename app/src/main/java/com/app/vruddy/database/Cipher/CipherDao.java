package com.app.vruddy.database.Cipher;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CipherDao {
    @Insert
    void insert(Cipher cipher);

    @Query("UPDATE cipher_table SET varA = :A, varB = :B WHERE id = id")
    void update(String A, String B);

    @Query("SELECT * FROM cipher_table")
    LiveData<List<Cipher>> getAllVars();
}
