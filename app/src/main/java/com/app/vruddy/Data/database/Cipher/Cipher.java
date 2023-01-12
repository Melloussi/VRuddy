package com.app.vruddy.Data.database.Cipher;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cipher_table")
public class Cipher {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String varA;
    private String varB;

    public Cipher(String varA, String varB) {
        this.varA = varA;
        this.varB = varB;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getVarA() {
        return varA;
    }

    public String getVarB() {
        return varB;
    }
}
