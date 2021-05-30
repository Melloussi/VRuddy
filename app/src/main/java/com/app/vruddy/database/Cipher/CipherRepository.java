package com.app.vruddy.database.Cipher;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import java.util.List;

public class CipherRepository {
    private CipherDao cipherDao;
    private LiveData<List<Cipher>> getAll;

    public CipherRepository(Application application) {
        CipherDatabase db = CipherDatabase.getInstance(application);
        cipherDao = db.cipherDao();
        getAll    = cipherDao.getAllVars();
    }

    public LiveData<List<Cipher>> getAll() {
        return getAll;
    }

    public void insert(Cipher cipher){
        new InsertCipherAsyncTask(cipherDao).execute(cipher);
    }

    public void update(String A, String B){
        new updateCipherAsyncTask(cipherDao).execute(A, B);
    }

    private static class InsertCipherAsyncTask extends AsyncTask<Cipher, Void, Void> {
        private CipherDao cipherDao;

        public InsertCipherAsyncTask(CipherDao cipherDao) {
            this.cipherDao = cipherDao;
        }

        @Override
        protected Void doInBackground(Cipher... ciphers) {
            cipherDao.insert(ciphers[0]);
            return null;
        }
    }

    private static class updateCipherAsyncTask extends AsyncTask<String, Void, Void> {
        private CipherDao cipherDao;

        public updateCipherAsyncTask(CipherDao cipherDao) {
            this.cipherDao = cipherDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            cipherDao.update(strings[0], strings[1]);
            return null;
        }
    }
}
