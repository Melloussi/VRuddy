package com.app.vruddy.Data.database.Cipher;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CipherViewModel extends AndroidViewModel {
    private CipherRepository repository;
    private LiveData<List<Cipher>> getAll;

    public CipherViewModel(Application application){
        super(application);
        repository = new CipherRepository(application);
        getAll = repository.getAll();
    }

    public void insert(Cipher cipher){
        repository.insert(cipher);
    }

    public void update(String A, String B){
        repository.update(A, B);
    }

    public LiveData<List<Cipher>> getGetAll(){
        return getAll;
    };
}
