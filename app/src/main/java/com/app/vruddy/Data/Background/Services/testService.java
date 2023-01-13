package com.app.vruddy.Data.Background.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.app.vruddy.Data.Background.Timer;
import com.app.vruddy.Data.Background.DownloadThreadPool;
import com.app.vruddy.R;
import com.app.vruddy.Views.Activities.HomeActivity;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
//import com.example.fastmedia.database.InProgress.File;
import com.app.vruddy.Models.BroadcastReceiver.InternetConnectionBroadcastReceiver;
import com.app.vruddy.Views.Fragment.HomeFragment;
import com.app.vruddy.Models.Muxer.Mux;
import com.app.vruddy.Data.database.InProgress.FileHandler;
import com.app.vruddy.Data.database.InProgress.FileRepository;
import com.app.vruddy.Data.database.InProgress.Updaters.AudioFail;
import com.app.vruddy.Data.database.InProgress.Updaters.AudioId;
import com.app.vruddy.Data.database.InProgress.Updaters.FailFile;
import com.app.vruddy.Data.database.InProgress.Updaters.IsDownloadComplete;
import com.app.vruddy.Data.database.InProgress.Updaters.FileUpdate;
import com.app.vruddy.Data.database.InProgress.InProgressFile;
import com.app.vruddy.Data.database.InProgress.Updaters.UpDater;
import com.app.vruddy.Data.database.InProgress.Updaters.AudioProgress;
import com.app.vruddy.Data.database.InProgress.Updaters.FileId;
import com.app.vruddy.Data.database.Music.Music;
import com.app.vruddy.Data.database.Music.MusicRepository;
import com.app.vruddy.Data.database.Video.Video;
import com.app.vruddy.Data.database.Video.VideoRepository;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class testService extends Service {
    private static InternetConnectionBroadcastReceiver connectionReceiver = new InternetConnectionBroadcastReceiver();
    private static Notification notification;
    private static NotificationCompat.Builder notificationBuilder;
    private static NotificationManagerCompat notificationManager;
    private static RemoteViews remoteViews;
    private static PendingIntent pendingIntent;
    private static FileRepository fileRepository;
    private static FileUpdate fileUpdate;
    private static UpDater upDater;
    private static FileHandler fileHandler;
    private static PRDownloaderConfig config;
    private static DownloadThreadPool downloadThreadPool;

    private static Timer myTimer = null;
    public testService() {
    }

    private static Context context;
    private static String packageName;
    private static String video_id = "";
    private static String download_Link = null;
    private static String file_name = "";
    private static String thumbnail = "";
    private static String time_line = "";
    private static String by = "";
    private static String type = "";
    private static String download_audio_link = null;
    private static final int progressStatue = 0;
    private static int notifyId = 0;
    private static int Mid = 0;
    private static int downloadId = 0;
    private static final String totalSize = "0";
    private static final String downloadedSize ="0";
    private static float Total;
    private static boolean isServiceCreated = false;

    //Getter and Setters


    public static boolean isIsServiceCreated() {
        return isServiceCreated;
    }

    public static void setIsServiceCreated(boolean isServiceCreated) {
        testService.isServiceCreated = isServiceCreated;
    }

    public static int getNotifyId() {
        return notifyId;
    }

    public static void setNotifyId(int notifyId) {
        testService.notifyId = notifyId;
    }

    public static String getDownload_Link() {
        return download_Link;
    }

    public static void setDownload_Link(String download_Link) {
        testService.download_Link = download_Link;
    }

    public static String getFile_name() {
        return file_name;
    }

    public static void setFile_name(String file_name) {
        testService.file_name = file_name;
    }

    public static String getDownload_audio_link() {
        return download_audio_link;
    }

    public static void setDownload_audio_link(String download_audio_link) {
        testService.download_audio_link = download_audio_link;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        testService.type = type;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectionReceiver);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myTimer = Timer.getInstance();
        myTimer.doInBackground();
        fileHandler = new HomeActivity();
        downloadThreadPool = new DownloadThreadPool();
        isServiceCreated = true;
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);
        System.out.println("------------ On Create Start");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //get data value
        Intent intent1 = new Intent(this, HomeFragment.class);
        pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
        context = getApplicationContext();
        video_id = intent.getStringExtra("video_id");
        setType(intent.getStringExtra("download_type"));
        setDownload_Link(intent.getStringExtra("download_video_link"));
        setDownload_audio_link(intent.getStringExtra("download_audio_link"));
        setFile_name(intent.getStringExtra("file_name"));
        by = intent.getStringExtra("by");
        time_line = intent.getStringExtra("time_line");
        thumbnail = intent.getStringExtra("thumbnail");

        Random random = new Random();
        setNotifyId(random.nextInt(9999));
        Mid = random.nextInt(100);
        createNotification(getFile_name(), Mid);
        context = this;
        packageName = getPackageName();

        try {
            if(video_id != null || !video_id.isEmpty()){
                //add to database
                InProgressFile inProgressFile = new InProgressFile(0, 0, R.drawable.ic_pause, R.drawable.ic_download_withe_24, R.drawable.ic_replay_24, false, false, false, false, false, false, 0, 0, 0, type, file_name, by, time_line, thumbnail, video_id, download_Link, download_audio_link);
                fileRepository = new FileRepository(getApplication());
                fileRepository.insert(inProgressFile);
                //FileHandler fileHandler = new HomeActivity();
                //fileHandler.update(inProgressFile);
                config();
                //Execute Download task in Thread Pool
                downloadThreadPool.executeThreadPool(myTask(Mid, download_Link, download_audio_link, file_name, by, time_line, thumbnail, video_id, getType()));
            }else {
                config();
            }
        }catch (Exception ignored){}

        return START_NOT_STICKY;
    }

    private void config() {
        // Enabling database for resume support even after the application is killed:
        config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .setReadTimeout(10000)
                .setConnectTimeout(10000)
                .build();
        PRDownloader.initialize(this, config);
    }

    private void createNotification(String title, int id) {
        //Create RemoteView
        remoteViews = new RemoteViews(getPackageName(), R.layout.download_notification);
        remoteViews.setTextViewText(R.id.notification_content, title);
        remoteViews.setTextViewText(R.id.notification_file_size, "Papering File...");
        remoteViews.setTextViewText(R.id.notification_file_total_size, "");

        notificationBuilder = new NotificationCompat.Builder(this, "x-18")
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle("App is running in background")
                .setProgress(100, 0, false)
                .setCategory(Notification.CATEGORY_PROGRESS)
                .setCustomContentView(remoteViews)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //Show The Notification
        notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(id, notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationWithChannelConfig(String title, int id) {
        remoteViews = new RemoteViews(getPackageName(), R.layout.download_notification);
        remoteViews.setTextViewText(R.id.notification_content, title);
        remoteViews.setTextViewText(R.id.notification_file_size, "Papering File...");
        remoteViews.setTextViewText(R.id.notification_file_total_size, "");
        notificationBuilder = new NotificationCompat.Builder(this, "x-18")

                //.setOngoing(true)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle("App is running in background")
                .setProgress(100, 0, false)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_PROGRESS)
                .setCustomContentView(remoteViews);
        //Show The Notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(id, notificationBuilder.build());
    }
    private void updateNotify(String type, String title, String totalValue, String currentValue, int progressStatue, int id) {

            try {

                //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                //remoteViews = new RemoteViews(getPackageName(), R.layout.download_notification);
                remoteViews.setProgressBar(R.id.notification_download_progress, 100, progressStatue, false);
                remoteViews.setTextViewText(R.id.notification_content, title);
                remoteViews.setTextViewText(R.id.notification_file_size, currentValue + "MB/");
                remoteViews.setTextViewText(R.id.notification_file_total_size, totalValue + "MB");

                //startForeground(id, notification);
                notificationManager.notify(id,  notificationBuilder.build());

                if(progressStatue == 100){
                    notificationManager.cancel(id);
                    if(type.equals("video")){
                        notifyConvertFile(id, title, 0);
                    }else {
                        notifyComplete(title, id);
                    }
                }

            }catch (Exception e){
                System.out.println("Error in updateNotify to handel!!");
                e.printStackTrace();
            }

    }

    private void notifyComplete(String title, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID = "VM-"+3;
            String channelName = "Downloaded Media";
            //NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.GREEN);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_download_complet);
            remoteViews.setTextViewText(R.id.notification_content, title);

            Notification notification = notificationBuilder
                    //.setOngoing(true)
                    .setSmallIcon(R.drawable.ic_download)
                    //.setContentTitle("App is running in background")
                    //.setProgress(100, 0, false)
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_REMINDER)
                    .setCustomContentView(remoteViews)
                    .build();
            //startForeground(id, notification);
            notificationManager.notify(3, notification);
        }else{
            String NOTIFICATION_CHANNEL_ID = "VM-"+3;
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_download_complet);
            remoteViews.setTextViewText(R.id.notification_content, title);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder
                    //.setOngoing(true)
                    .setSmallIcon(R.drawable.ic_download)
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setCustomContentView(remoteViews)
                    .build();
            //startForeground(id, notification);
            notificationManager.notify(3, notification);
        }
    }
    private void notifyConvertFile(int id, String title, int progressStatue){
        System.out.println("Converting Msg");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
//            String channelName = "Downloaded Media";
//
//            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
//            chan.setLightColor(Color.GREEN);
//            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            assert manager != null;
//            manager.createNotificationChannel(chan);
//
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.download_notification);
//            remoteViews.setTextViewText(R.id.notification_content, title);
//            remoteViews.setViewVisibility(R.id.notification_download_progress, View.INVISIBLE);
//            remoteViews.setViewVisibility(R.id.notification_convert_progress, View.VISIBLE);
//            remoteViews.setTextViewText(R.id.notification_file_size, "Converting File...");
//            remoteViews.setTextViewText(R.id.notification_file_total_size, progressStatue+"%");
//
//            notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//            notification = notificationBuilder.setOngoing(true)
//                    .setSmallIcon(R.drawable.ic_download)
//                    .setPriority(NotificationManager.IMPORTANCE_MIN)
//                    .setCategory(Notification.CATEGORY_SERVICE)
//                    .setCustomContentView(remoteViews)
//                    .build();
//            //startForeground(id, notification);
//            notificationManager.notify(id, notification);
//        }else{
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.download_notification);
//            remoteViews.setTextViewText(R.id.notification_content, title);
//            remoteViews.setViewVisibility(R.id.notification_download_progress, View.INVISIBLE);
//            remoteViews.setViewVisibility(R.id.notification_convert_progress, View.VISIBLE);
//            remoteViews.setTextViewText(R.id.notification_file_size, "Converting File...");
//            remoteViews.setTextViewText(R.id.notification_file_total_size, progressStatue+"%");
//
//            Notification notification = notificationBuilder
//                    //.setOngoing(true)
//                    .setSmallIcon(R.drawable.ic_download)
//                    .setPriority(NotificationManager.IMPORTANCE_MIN)
//                    .setCategory(Notification.CATEGORY_SERVICE)
//                    .setCustomContentView(remoteViews)
//                    .build();
//            notificationManager.notify(id, notification);
//            if(progressStatue == 100){
//                notificationManager.cancel(id);
//            }
//        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        RemoteViews remoteViews = new RemoteViews(packageName, R.layout.download_notification);
        remoteViews.setTextViewText(R.id.notification_content, title);
        remoteViews.setViewVisibility(R.id.notification_download_progress, View.INVISIBLE);
        remoteViews.setViewVisibility(R.id.notification_convert_progress, View.VISIBLE);
        remoteViews.setTextViewText(R.id.notification_file_size, "Converting File...");
        remoteViews.setTextColor(R.id.notification_file_size, Color.parseColor("#00a308"));
        remoteViews.setTextViewText(R.id.notification_file_total_size, progressStatue+"%");
        remoteViews.setTextColor(R.id.notification_file_total_size, Color.parseColor("#00a308"));

        Notification notification = notificationBuilder
                //.setOngoing(true)
                .setSmallIcon(R.drawable.ic_download)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_PROGRESS)
                .setCustomContentView(remoteViews)
                .build();
        notificationManager.notify(id, notification);
        if(progressStatue == 100){
            notificationManager.cancel(id);
        }
    }

    public void testPRDownloader(int notifyId, String url, String audioUrl, String progressTitleName, String by, String time_line, String thumbnail, String video_id_, String video_type){
                String format = ".mp4";

                try {
                    boolean[] isFail = {false};
                    //Prepare where to save download file

                    File folder = new File(Environment.getExternalStorageDirectory(), "/Vital/trash");
                    if (!folder.exists()) {
                        folder.mkdir();
                    }
                    if(video_type.equals("audio")){
                        format = ".mp3";
                    }
                    //File dirPath = new File(folder, fileName);

                    //prepare downloading
                    //PRDownloader.initialize(this);

                    // Enabling database for resume support even after the application is killed:
//                    PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
//                            .setDatabaseEnabled(true)
//                            // Setting timeout globally for the download network requests:
//                            .setReadTimeout(30_000)
//                            .setConnectTimeout(30_000)
//                            .build();
//                    PRDownloader.initialize(this, config);

                    //Make a download request
                    downloadId = PRDownloader.download(url, folder.getAbsolutePath(), progressTitleName + format)
                            .build()
                            .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                @Override
                                public void onStartOrResume() {
                                    System.out.println("Download Started: " + progressTitleName);
                                }
                            })
                            .setOnPauseListener(new OnPauseListener() {
                                @Override
                                public void onPause() {

                                }
                            })
                            .setOnCancelListener(new OnCancelListener() {
                                @Override
                                public void onCancel() {
                                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                                    notificationManager.cancel(notifyId);
                                }
                            })
                            .setOnProgressListener(new OnProgressListener() {
                                @Override
                                public void onProgress(Progress progress) {

                                    //Calculate Percentage
                                    float currentValue = progress.currentBytes;
                                    float totalValue = progress.totalBytes;
                                    float myProgress = (currentValue / totalValue) * 100;
                                    int progressValue = Math.round(myProgress);

                                    System.out.println("File Name: " + progressTitleName + "\n" + "Progress: " + progressValue + "%");

                                    //Calculate MB Size
                                    double mb = 1048576;
                                    double Total = totalValue / mb;

                                    long size = progress.currentBytes;
                                    double downloadedSize = size / mb;
                                    System.out.println(String.format("%.2f", downloadedSize)+"MB/" +String.format("%.2f", Total)+"MB");

//                                    if(progressValue == 100){
//                                        notifyConvertFile(notifyId, progressTitleName);
//                                    }
                                    System.out.println("-------- config: "+config);
                                    System.out.println("-------- video url: "+url);
                                    System.out.println("-------- audio url: "+audioUrl);

                                    if(myTimer == null){
                                        myTimer = Timer.getInstance();
                                        myTimer.doInBackground();
                                    }
                                    if(Timer.getSeconds() > 1 || progressValue == 100){
                                        //updateNotify(progressTitleName, String.format("%.2f", Total), String.format("%.2f", downloadedSize), progressValue, notifyId);
                                        System.out.println("notifyId: "+notifyId);

                                        long TotalLong = (long) Total;
                                        long downloadedSizeLong = (long) downloadedSize;
                                        //update Notification
                                        if(notifyId != 0) {
                                            updateNotify(video_type, progressTitleName, String.format("%.2f", Total), String.format("%.2f", downloadedSize), progressValue, Mid);
                                        }else {
                                            updateNotify(video_type, progressTitleName, String.format("%.2f", Total), String.format("%.2f", downloadedSize), progressValue, notifyId);
                                        }
                                        //update Ui (InprogressFragment)
                                        FileUpdate fileUpdate = new FileUpdate(video_id_, progressValue, Total, downloadedSize, url, audioUrl);
                                        if(isFail[0]){
                                            fileHandler.updateFailFile(new FailFile(false, video_id_));
                                            isFail[0] = false;
                                        }
                                        System.out.println("----------------- video id: "+video_id_);
                                        //fileRepository.updateById(fileUpdate);
                                        //upDater = new UpDater(getApplication());
                                        //upDater.execute(fileUpdate);

                                        fileHandler.update(fileUpdate);


                                        Timer.setSeconds(0);
                                    }
//                                    long TotalLong = (long) Total;
//                                    long downloadedSizeLong = (long) downloadedSize;
//                                    //update Notification
//                                    updateNotify(progressTitleName, String.format("%.2f", Total), String.format("%.2f", downloadedSize), progressValue, notifyId);
//                                    //update Ui (InprogressFragment)
//                                    fileUpdate = new FileUpdate(video_id, progressValue, Total, downloadedSize);
//                                    //fileRepository.updateById(fileUpdate);
//                                    //upDater = new UpDater(getApplication());
//                                    //upDater.execute(fileUpdate);
//                                    FileHandler fileHandler = new HomeActivity();
//                                    fileHandler.update(fileUpdate);



                                    //notification.update(packageName, context, String.format("%.2f", Total), String.format("%.2f", downloadedSize), progressValue, getType(), notifyId);
                                }
                            })
                            .start(new OnDownloadListener() {
                                @Override
                                public void onDownloadComplete() {
                                    System.out.println("Download Video Complete :)");
                                    fileHandler.updateDownloadStatue(new IsDownloadComplete(video_id_, true));

                                    if (video_type.equals("video")) {/*Check if it type video to start downloading audio to add it to the video*/
                                        if(audioUrl == null){//notification.convertingFileNotify(packageName, context, String.format("%.2f", getTotal()), notifyId);
                                            downloadAudioFile(notifyId, getDownload_audio_link(), progressTitleName, by, time_line, thumbnail, video_id_);
                                        }else {
                                            downloadAudioFile(notifyId, audioUrl, progressTitleName, by, time_line, thumbnail, video_id_);
                                        }
                                    }else {
                                        //add audio to music db
                                        fileHandler.deleteFileById(video_id);

                                        //
                                        File from = new File(Environment.getExternalStorageDirectory(), "/Vital/trash/"+progressTitleName+".mp3");
                                        File to = new File(Environment.getExternalStorageDirectory(), "/Vital/result/"+progressTitleName+".mp3");

                                        if(!new File(Environment.getExternalStorageDirectory(), "/Vital/result/").exists()){
                                            new File(Environment.getExternalStorageDirectory(), "/Vital/result/").mkdir();
                                        }

                                        from.renameTo(to);

                                        MusicRepository musicRepository = new MusicRepository(getApplication());
                                        musicRepository.insert(new Music(false,progressTitleName,by,time_line,thumbnail,video_id));
                                    }
                                }

                                @Override
                                public void onError(com.downloader.Error error) {
                                    System.out.println("There's an error!" + "\n" + error.getServerErrorMessage() + "\n" + error.getConnectionException());
                                    System.out.println("isConnectionError: "+error.isConnectionError());
                                    System.out.println("isServerError: "+error.isServerError());
                                    //
                                    if(!error.isConnectionError()){
                                    fileHandler.updateFailFile(new FailFile(true, video_id_));
                                    isFail[0] = true;
                                    }else {
                                        HomeActivity homeActivity = new HomeActivity();
                                        homeActivity.disconnected();
                                    }
                                }
                            });

                    fileHandler.updateDownloadFileId(new FileId(video_id_,downloadId));

                }catch (Exception e){
                    System.out.println("Below An Error to Handel!");
                    e.printStackTrace();
                }



    }
    public void downloadAudioFile(int notifyId, String url, String fileName, String by, String time_line, String thumbnail, String video_id){
        try {
            boolean[] isAudioFile = {false};
            //Prepare where to save download file
            File folder = new File(Environment.getExternalStorageDirectory(), "/Vital/trash");
            if (!folder.exists()) {
                folder.mkdir();
            }
            //Make a download request
            int audioId = PRDownloader.download(url, folder.getAbsolutePath(), fileName + ".aac")
                    .build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {
                            System.out.println("Audio Start Downloading: " + fileName);
                        }
                    })
                    .setOnPauseListener(new OnPauseListener() {
                        @Override
                        public void onPause() {

                        }
                    })
                    .setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel() {

                        }
                    })
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {

                            //Calculate Percentage
                            float currentValue = progress.currentBytes;
                            float totalValue = progress.totalBytes;
                            float myProgress = (currentValue / totalValue) * 100;
                            int progressValue = Math.round(myProgress);
                            System.out.println("Audio Progress: " + progressValue + "%");
                            if (Timer.getSeconds() > 1) {
                                    notifyConvertFile(notifyId, fileName, progressValue);
                                    System.out.println("+-+-+-+-+-+-+-+-+ Timer: "+Timer.getSeconds()+" -+-+-+-+-+-+-");
                                    //update Ui
                                    FileHandler fileHandler = new HomeActivity();
                                    fileHandler.updateAudioProgressById(new AudioProgress(video_id, progressValue));
                                    if(isAudioFile[0]){
                                        isAudioFile[0] = false;
                                        fileHandler.updateAudioFail(new AudioFail(false, video_id));
                                    }
                                    Timer.setSeconds(0);
                            }
                        }
                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            System.out.println("Download Audio Complete :)");

                            //When Audio Downloaded start to convert the audio with the video
                            //if (!isConverted) {

                            System.out.println("I'm ready for Converting");
                            File resultFolder = new File(Environment.getExternalStorageDirectory(), "/Vital/result");
                            File trashFolder = new File(Environment.getExternalStorageDirectory(), "/Vital/trash");
                            if (!resultFolder.exists() || !trashFolder.exists()) {
                                System.out.println("Make Folders");
                                resultFolder.mkdir();
                                trashFolder.mkdir();
                            }

                            File FilePath = new File(trashFolder, fileName);
                            File outPath = new File(resultFolder, fileName);
                            Mux convert = new Mux();
                            try {
                                convert.doSomething(video_id, FilePath.getAbsolutePath() + ".mp4"
                                        , FilePath.getAbsolutePath() + ".aac", outPath.getAbsolutePath() + ".mp4");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (Mux.isComplete()) {
                                System.out.println("Convert File is Completed: " + fileName);
                                Notification notification = new Notification();
                                //notification.downloadCompleteNotify(context, packageName, fileName, "", notifyId);
                                notifyComplete(fileName, notifyId);
                                FilePath.delete();

                                fileHandler.deleteFileById(video_id);

                                VideoRepository videoRepository = new VideoRepository(getApplication());
                                videoRepository.insert(new Video(false,fileName,by,time_line,thumbnail,video_id));


                            }
                        }
                        //}

                        @Override
                        public void onError(com.downloader.Error error) {
                            System.out.println("There's an error!" + "\n" + error.getServerErrorMessage() + "\n" + error.getConnectionException());
                            isAudioFile[0] = true;
                            fileHandler.updateAudioFail(new AudioFail(true, video_id));
                        }
                    });
            //
            fileHandler.updateAudioFileId(new AudioId(video_id, audioId));
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
    public Runnable myTask(int id, String videoLink, String audioLink, String fileName, String by, String time_line, String thumbnail, String videoId, String video_type){
        return new Runnable() {
            @Override
            public void run() {
                //
                testPRDownloader(id, videoLink, audioLink, fileName, by, time_line, thumbnail, videoId, video_type);
            }
        };
    }

    public void cancel(int id){
        PRDownloader.cancel(id);
    }
    public void pause(InProgressFile file){
        /*this part here it a bit complicated, so I'll try to explain it to you.
        * in the total I made these conditions to pause the right file, because when
        * you download a video file it doesn't download a one file it download a tow files audio and video separately
        * so when you want to pause a file we have to know which file we should pause is audio or video,
        * so we check if the file is an video with audio or just a one audio file, if it only a one audio file I just pause it, but
        * if it video with an audio, I check if the video complete downloading or not yet, if not complete yet, and doesn't fail I pause it
        * if it the video complete I check the audio if doesn't fail and I pause it*/
        System.out.println("------- Pause Called --------");
        if(file != null) {
            if (file.getFile_type().equals("video")) {
                if (!file.isFileCompleted() && !file.isFileFail()) {
                    if (!file.isPaused()) {
                        System.out.println("--------- File Paused ---------");
                        PRDownloader.pause(file.getDownloadFileId());
                    }
                } else {
                    if (!file.isAudioFail()) {
                        PRDownloader.pause(file.getAudioFileId());
                    }
                }
            } else {
                if (!file.isPaused() && !file.isFileFail()) {
                    PRDownloader.pause(file.getDownloadFileId());
                }
            }
        }
    }
    public void resume(InProgressFile file){
        System.out.println("------- Resume Called --------");
        if(file != null) {
            if (file.getFile_type().equals("video")) {
                setDownload_audio_link(file.getAudioUrl());//re-pass the audio link in case the app killed and re-opened
                if (!file.isFileCompleted()) {
                    if (!file.isFileFail()) {
                        System.out.println("Statue: "+PRDownloader.getStatus(file.getDownloadFileId()));
                        PRDownloader.resume(file.getDownloadFileId());
//                        if (PRDownloader.getStatus(file.getDownloadFileId()) == Status.UNKNOWN) {
//                            //Execute Download task in Thread Pool
//                            DownloadThreadPool downloadThreadPool = new DownloadThreadPool();
//                            downloadThreadPool.executeThreadPool(myTask(0, file.getDownloadUrl(), file.getAudioUrl(), file.getTitle(), file.getVideo_id(), file.getFile_type()));
//                        } else {
//                            PRDownloader.resume(file.getDownloadFileId());
//                        }
                    }
                } else {
                    if (!file.isAudioFail()) {
                        PRDownloader.resume(file.getAudioFileId());
                    }
                }
            } else {
                if (!file.isFileFail()) {
                    PRDownloader.resume(file.getDownloadFileId());
                }
            }
        }
//        if(inProgressFile != null) {
//            setDownload_audio_link(inProgressFile.getAudioUrl());
//            if (PRDownloader.getStatus(id) == Status.UNKNOWN) {
//                //Execute Download task in Thread Pool
//                DownloadThreadPool downloadThreadPool = new DownloadThreadPool();
//                downloadThreadPool.executeThreadPool(myTask(0, inProgressFile.getDownloadUrl(), inProgressFile.getAudioUrl(), inProgressFile.getTitle(), inProgressFile.getVideo_id(), inProgressFile.getFile_type()));
//            } else {
//                PRDownloader.resume(id);
//            }
//        }else {
//            PRDownloader.resume(id);
//        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
