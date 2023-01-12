package com.app.vruddy.Data.database.Favorite;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FavoriteRepository {
    private FavoriteDao favoriteDao;
    private LiveData<List<Favorite>> allFavorites;
    public FavoriteRepository(Application application) {
        FavoriteDatabase database = FavoriteDatabase.getInstance(application);
        favoriteDao = database.favoriteDao();
        allFavorites = favoriteDao.getAllFavorites();
    }

    public void insert(Favorite favorite) {
        new InsertFavoriteAsyncTask(favoriteDao).execute(favorite);
    }

    public void update(Favorite favorite) {
        new updateFavoriteAsyncTask(favoriteDao).execute(favorite);
    }

    public void delete(Favorite favorite) {
        new deleteFavoriteAsyncTask(favoriteDao).execute(favorite);
    }

    public void deleteById(String id) {
        new deleteContactByIdAsyncTask(favoriteDao).execute(id);
    }

    public void deleteAll() {
        new deleteAllFavoriteAsyncTask(favoriteDao).execute();
    }

    public LiveData<List<Favorite>> getAllFavorites() {
        return allFavorites;
    }

    //private static class InsertContactAsyncTask extends AsyncTask<Contact, Void, Void>{
    private static class InsertFavoriteAsyncTask extends AsyncTask<Favorite, Void, Void> {
        private FavoriteDao favoriteDao;

        public InsertFavoriteAsyncTask(FavoriteDao favoriteDao) {
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected Void doInBackground(Favorite... favorites) {
            favoriteDao.insert(favorites[0]);
            return null;
        }
    }
    private static class updateFavoriteAsyncTask extends AsyncTask<Favorite, Void, Void> {
        private FavoriteDao favoriteDao;

        public updateFavoriteAsyncTask(FavoriteDao favoriteDao) {
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected Void doInBackground(Favorite... favorites) {
            favoriteDao.update(favorites[0]);
            return null;
        }
    }
    private static class deleteFavoriteAsyncTask extends AsyncTask<Favorite, Void, Void> {
        private FavoriteDao favoriteDao;

        public deleteFavoriteAsyncTask(FavoriteDao favoriteDao) {
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected Void doInBackground(Favorite... favorites) {
            favoriteDao.delete(favorites[0]);
            return null;
        }
    }
    private static class deleteContactByIdAsyncTask extends AsyncTask<String, Void, Void>{

        private FavoriteDao favoriteDao;
        public deleteContactByIdAsyncTask(FavoriteDao favoriteDao) {
            this.favoriteDao = favoriteDao;
        }
        @Override
        protected Void doInBackground(String... strings) {
            favoriteDao.deleteById(strings[0]);
            return null;
        }
    }
    private static class deleteAllFavoriteAsyncTask extends AsyncTask<Favorite, Void, Void> {
        private FavoriteDao favoriteDao;

        public deleteAllFavoriteAsyncTask(FavoriteDao favoriteDao) {
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected Void doInBackground(Favorite... favorites) {
            favoriteDao.deleteAll();
            return null;
        }
    }
}
