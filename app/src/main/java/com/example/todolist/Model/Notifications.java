package com.example.todolist.Model;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;


public class Notifications {


    String channelId = "channel1";
    String channelName = "channelName1";

    //Must create channel before you create a notification
    public void createNotificationChannel(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public Notification createNotification(String title, String content, int image, Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel1")
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(image)
                .setAutoCancel(true);
        return builder.build();
    }

    public void scheduleNotification(Notification notification, Calendar calendar, Context context) {
        Intent intent = new Intent(context, NotificationsPublisher.class);
        intent.putExtra(NotificationsPublisher.NOTIFICATION_ID, 1);
        intent.putExtra(NotificationsPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
