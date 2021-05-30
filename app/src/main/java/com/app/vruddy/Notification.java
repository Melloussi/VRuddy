package com.app.vruddy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Notification extends AppCompatActivity {
    private static NotificationManagerCompat nmc;
    private static NotificationCompat.Builder builder;
    private static RemoteViews remoteViews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        builder = new NotificationCompat.Builder(this, "HD-17");
        remoteViews = new RemoteViews(getPackageName(), R.layout.download_notification);
        nmc = NotificationManagerCompat.from(getApplicationContext());


    }

    public void notifyMe(Context context, String packageName, String title, String thumbnail, int notificationId){

        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "HD-17");
            builder.setSmallIcon(R.drawable.ic_download);
            builder.setProgress(100, 0, false);
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            builder.setCustomContentView(remoteViews);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                NotificationChannel notificationChannel = new NotificationChannel("V-M", "Download", NotificationManager.IMPORTANCE_HIGH);
//                notificationChannel.setDescription("Hi there this's my description :)");
//
//                NotificationManager nm = getSystemService(NotificationManager.class);
//                nm.createNotificationChannel(notificationChannel);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("HD-17", "Downloads", importance);
                channel.setDescription("This Channel for notify with downloads files");
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }






            //builder = new NotificationCompat.Builder(context, "V-M0607");
            //builder.setContentTitle("Download");
            //builder.setContentText("Hello I hope that you liked ths notification isn't :)");
//            builder.setSmallIcon(R.drawable.ic_download);
//            builder.setProgress(100, 0, false);
//            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
//            builder.setCustomContentView(remoteViews);
            //builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
            remoteViews.setTextViewText(R.id.notification_content, title);
            nmc.notify(notificationId, builder.build());

        }catch (Exception e){
            System.out.println("Error in Notify me");
            e.printStackTrace();
        }

    }
    public void update(String packageName, Context context, String totalSize, String DownloadSize, int ProgressStatue, String type , int notificationId){
        //Update notification with current value progress
        builder.setProgress(100,ProgressStatue, false);
        remoteViews.setProgressBar(R.id.notification_download_progress, 100, ProgressStatue, false);
        remoteViews.setTextViewText(R.id.notification_file_size, DownloadSize + "MB/");
        remoteViews.setTextViewText(R.id.notification_file_total_size, totalSize + "MB");
        nmc.notify(notificationId, builder.build());
        if (ProgressStatue == 100) {
            //nmc.cancel(10);
            //downloadCompleteNotify(context, packageName);
            if(type.equals("video")){
                System.out.println("--- Converting mode ---");
                convertingFileNotify(packageName, context, totalSize, notificationId);
            }else {
                nmc.cancel(notificationId);
            }
        }
    }
    public void downloadCompleteNotify(Context context, String packageName, String title, String thumbnail, int notificationId){

        nmc.cancel(notificationId);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("V-M0607", "Download", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Hi there this's my description :)");

            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(notificationChannel);
        }

        RemoteViews remoteViews = new RemoteViews(packageName, R.layout.notification_download_complet);
        //remoteViews.setTextViewText(R.id.notification_content, "How to Open an Activity from a Notification - Android Studio Tutorial");
        remoteViews.setTextViewText(R.id.notification_content, title);
        Uri uri = Uri.parse(thumbnail);
//        remoteViews.setImageViewUri(R.id.thumbnail_download_file, uri);
//        remoteViews
        //Picasso.get().load(imageUri).into();
        //try this
//        try {
//            System.out.println("image url passed: "+thumbnail);
//            Bitmap bitmap = Glide.with(context)
//                    .asBitmap()
//                    .load(thumbnail)
//                    .submit(15, 15)
//                    .get();
//            remoteViews.setImageViewBitmap(R.id.thumbnail_download_file, bitmap);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "V-M0607");
        //builder.setContentTitle("Download");
        //builder.setContentText("Hello I hope that you liked ths notification isn't :)");
        builder.setSmallIcon(R.drawable.ic_download);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setCustomContentView(remoteViews);
        //builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        NotificationManagerCompat nmc = NotificationManagerCompat.from(context);
        nmc.notify(notificationId, builder.build());


    }
    public void convertingFileNotify(String packageName, Context context, String totalSize, int notificationId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("V-M0607", "Download", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Hi there this's my description :)");

            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(notificationChannel);
        }

        RemoteViews remoteViews = new RemoteViews(packageName, R.layout.download_notification);
        remoteViews.setTextViewText(R.id.notification_content, "Converting InProgressFile...");


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "V-M0607");
        //builder.setContentTitle("Download");
        //builder.setContentText("Hello I hope that you liked ths notification isn't :)");
        builder.setSmallIcon(R.drawable.ic_download);
        builder.setProgress(100, 100, true);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setCustomContentView(remoteViews);
        //builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        NotificationManagerCompat nmc = NotificationManagerCompat.from(context);
        nmc.notify(notificationId, builder.build());

        //Update notification with current value progress
        builder.setProgress(100,0, true);
        remoteViews.setViewVisibility(R.id.notification_download_progress, View.INVISIBLE);
        remoteViews.setViewVisibility(R.id.notification_convert_progress, View.VISIBLE);
        //remoteViews.setProgressBar(R.id.notification_convert_progress, 100, 0, false);
        remoteViews.setTextViewText(R.id.notification_file_size, totalSize + "MB/");
        remoteViews.setTextViewText(R.id.notification_file_total_size, totalSize + "MB");
        nmc.notify(notificationId, builder.build());
    }
   // public  void
}
