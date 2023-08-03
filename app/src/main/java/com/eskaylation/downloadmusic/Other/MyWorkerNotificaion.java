package com.eskaylation.downloadmusic.Other;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.ui.activity.plash.StartActivity;


public class MyWorkerNotificaion extends Worker {
    Context context;

    public MyWorkerNotificaion(Context context2, WorkerParameters workerParameters) {
        super(context2, workerParameters);
        this.context = context2;
    }

    public ListenableWorker.Result doWork() {
        displayNotification();
        return ListenableWorker.Result.success();
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void displayNotification() {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService("notification");
        if (Build.VERSION.SDK_INT >= 26) {
            notificationManager.createNotificationChannel(new NotificationChannel("musicapp_three", "mp3_music", 3));
        }
        PendingIntent activity;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            activity = PendingIntent.getActivity(this.context, 0, new Intent(this.context, StartActivity.class), PendingIntent.FLAG_IMMUTABLE);
        } else {
            activity = PendingIntent.getActivity(this.context, 0, new Intent(this.context, StartActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        }
        NotificationCompat.Builder smallIcon = new NotificationCompat.Builder(getApplicationContext(), "musicapp_three").setContentTitle(this.context.getString(R.string.app_name)).setContentText("All Musics Play & Download for free.").setSmallIcon(R.mipmap.ic_launcher);
        smallIcon.setContentIntent(activity);
        notificationManager.notify(1, smallIcon.build());
    }
}
