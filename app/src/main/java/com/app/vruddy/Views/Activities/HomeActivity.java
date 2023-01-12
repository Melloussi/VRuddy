package com.app.vruddy.Views.Activities;

import static com.app.vruddy.Data.Global.GlobalConstantVariables.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.app.vruddy.R;
import com.app.vruddy.Views.Fragment.DownloadedFragment;
import com.app.vruddy.Views.Fragment.FavoriteFragment;
import com.app.vruddy.Views.Fragment.HomeFragment;
import com.app.vruddy.Views.Fragment.WatchFragment;
import com.app.vruddy.Data.database.InProgress.FileHandler;
import com.app.vruddy.Data.database.InProgress.InProgressFile;
import com.app.vruddy.Data.database.InProgress.Updaters.AudioFail;
import com.app.vruddy.Data.database.InProgress.Updaters.AudioId;
import com.app.vruddy.Data.database.InProgress.Updaters.FailFile;
import com.app.vruddy.Data.database.InProgress.Updaters.FileConverted;
import com.app.vruddy.Data.database.InProgress.Updaters.IsDownloadComplete;
import com.app.vruddy.Data.database.InProgress.Updaters.FileUpdate;
import com.app.vruddy.Data.database.InProgress.FileViewModel;
import com.app.vruddy.Data.database.InProgress.Updaters.UpDater;
import com.app.vruddy.Data.database.InProgress.Updaters.AudioProgress;
import com.app.vruddy.Data.database.InProgress.Updaters.FileId;
import com.app.vruddy.Data.Background.Services.testService;
import com.app.vruddy.Views.ViewManager.Manager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements FileHandler {

    private static FileViewModel fileViewModel;
    private boolean isSearchFragmentOpen = false;
    private boolean isWatchFragmentOpen = false;
    private static List<InProgressFile> fileList;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    private Context context;
    private Dialog permissionDialog;
    private TextView okBtn;
    public static boolean isPermission = false;
    public Manager manager;

    public boolean isSearchFragmentOpen() {
        return isSearchFragmentOpen;
    }

    public void setSearchFragmentOpen(boolean searchFragmentOpen) {
        isSearchFragmentOpen = searchFragmentOpen;
    }

    public boolean isWatchFragmentOpen() {
        return isWatchFragmentOpen;
    }

    public void setWatchFragmentOpen(boolean watchFragmentOpen) {
        isWatchFragmentOpen = watchFragmentOpen;
    }

    @Override
    public void onBackPressed() {

        if (isWatchFragmentOpen()) {
//            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.watchFragment);
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.remove(fragment);
//            fragmentTransaction.commit();
            WatchFragment fragment = new WatchFragment();
            fragment.minimize();
            isWatchFragmentOpen = false;
            System.out.println("isWatchFragmentOpen  <--------");
        } else {
            if (isSearchFragmentOpen()) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.searchFragment);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
                isSearchFragmentOpen = false;
                System.out.println("isSearchFragmentOpen  <--------");
            } else {
                // super.onBackPressed();
                System.out.println("else  <--------");
            }
        }

        //function to exit from the app
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Tap back button twice in order to exit", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_home);
        changeTopBarColor();

        createNotificationChannel();
        context = getApplicationContext();
        manager = new Manager(this);

        //This Part Should not Executed here, don't executed until the user ask for downloading a file
        okBtn = findViewById(R.id.Pok);
        permissionDialog = new Dialog(HomeActivity.this);
        Pchcker();
        ////

        manager.fragmentManager(HOME_FRAGMENT);

        BottomNavigationView bottomNavigationView = findViewById(R.id.myNav);
        //navFunction();
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        //viewModel
        fileViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(FileViewModel.class);
        fileViewModel.getAllFiles().observe(this, new Observer<List<InProgressFile>>() {
            @Override
            public void onChanged(List<InProgressFile> inProgressFiles) {
                //
                fileList = inProgressFiles;
            }
        });
    }

    private void changeTopBarColor() {
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.mainRedColor));
    }

    public void Pchcker() {
        boolean check = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        if (check) {
            permissionDialog();
        }else {
            isPermission = true;
        }
    }

    public void frameLayoutVisibility() {
        manager.manageFrameLayoutVisibility();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.favorite:
                            selectedFragment = new FavoriteFragment();
                            break;
                        case R.id.downloadedFile:
                            selectedFragment = new DownloadedFragment();
                            break;
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.homeFragment, selectedFragment)
                            .commit();
                    return true;
                }
            };

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Downloading";
            String description = "Downloading Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("x-18", name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void update(FileUpdate fileUpdate) {
        //fileViewModel.updateFileById(fileUpdate);
        UpDater upDater = new UpDater(getApplication());
        upDater.execute(fileUpdate);
    }

    @Override
    public void updateDownloadFileId(FileId fileId) {
        fileViewModel.updateDownloadFileIdById(fileId);
    }

    @Override
    public void updateAudioFileId(AudioId audioId) {
        fileViewModel.updateAudioFileId(audioId);
    }

    @Override
    public void updateAudioProgressById(AudioProgress audioProgress) {
        fileViewModel.updateAudioProgressById(audioProgress);
    }

    @Override
    public void updateDownloadStatue(IsDownloadComplete isDownloadComplete) {
        fileViewModel.updateDownloadStatueById(isDownloadComplete);
    }

    @Override
    public void updateConvertById(FileConverted fileConverted) {
        fileViewModel.updateConvertById(fileConverted);
    }

    @Override
    public void updateFailFile(FailFile failFile) {
        fileViewModel.updateFailFileIdById(failFile);
    }

    @Override
    public void updateAudioFail(AudioFail audioFail) {
        fileViewModel.updateAudioFail(audioFail);
    }

    @Override
    public void deleteFileById(String video_id) {
        fileViewModel.deleteFileById(video_id);
    }

    public void disconnected() {
        for (int index = 0; index > fileList.size(); index++) {
            System.out.println("----------- [" + index + "] is not pause: " + !fileList.get(index).isPaused());
            System.out.println("----------- [" + index + "] is not fail: " + !fileList.get(index).isFileFail());
            System.out.println("----------- [" + index + "] is audio not fail: " + !fileList.get(index).isAudioFail());
            if (!fileList.get(index).isPaused() && !fileList.get(index).isFileFail() && !fileList.get(index).isAudioFail()) {
                System.out.println("----------- disconnected called ---------");
                testService testService = new testService();
                testService.pause(fileList.get(index));
            }
        }
    }

    public void reconnected() {
        if (fileList != null) {
            for (int index = 0; index > fileList.size(); index++) {
                if (!fileList.get(index).isPaused() && !fileList.get(index).isFileFail() && !fileList.get(index).isAudioFail()) {
                    System.out.println("----------- reconnected called ---------");
                    testService testService = new testService();
                    testService.resume(fileList.get(index));
                }
            }
        }
    }


    //---------------- Storage Permission -----------------
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            askForPermission();
        } else {
            //
            isPermission = true;
        }
    }

    public void askForPermission() {
        // You can directly ask for the permission.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , 1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println("-------------------------");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            System.out.println("------------- Value: Allow");
            isPermission = true;
        } else {
            System.out.println("------------- Value: Deny");
            isPermission = false;

        }
    }

    public void permissionDialog(){
        permissionDialog.show();
        permissionDialog.setContentView(R.layout.ask_for_permission);
        permissionDialog.setCanceledOnTouchOutside(false);
    }

    public void permissionOk(View view) {
        permissionDialog.cancel();
        checkPermission();
    }
}