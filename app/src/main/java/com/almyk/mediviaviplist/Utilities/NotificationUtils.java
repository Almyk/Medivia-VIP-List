package com.almyk.mediviaviplist.Utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.UI.MainActivity;

import static com.almyk.mediviaviplist.Utilities.Constants.LOGIN_CHANNEL_ID;

public class NotificationUtils{

    public static void makeStatusNotification(String message, Context context, String server) {
        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = Constants.LOGIN_NOTIFICATION_CHANNEL_NAME;
            String description = Constants.LOGIN_NOTIFICATION_CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                    new NotificationChannel(LOGIN_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setLightColor(Color.GREEN);

            // Add the channel
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Create the notification
        int NOTIFICATION_ID = 0;
        switch(server) {
            case "Pendulum": NOTIFICATION_ID = 1; break;
            case "Legacy": NOTIFICATION_ID = 2; break;
            case "Destiny": NOTIFICATION_ID = 3; break;
            case "Prophecy": NOTIFICATION_ID = 4; break;
        }
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, LOGIN_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(Constants.LOGIN_NOTIFICATION_TITLE + server.toUpperCase())
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[0])
                .setSound(alarmSound)
                .setOnlyAlertOnce(true)
                .setContentIntent(getPendingIntent(context, "vip"));

        // Show the notification
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build());
    }

    private static PendingIntent getPendingIntent(Context context, String menuFragment) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("menuFragment", menuFragment);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public NotificationUtils() {}

    public static void makeBedmageNotification(String name, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence channelName = Constants.BEDMAGE_NOTIFICATION_CHANNEL_NAME;
            String description = Constants.BEDMAGE_NOTIFICATION_CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                    new NotificationChannel(Constants.BEDMAGE_CHANNEL_ID, channelName, importance);
            channel.setDescription(description);
            channel.setLightColor(Color.GREEN);

            // Add the channel
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Create the notification
        int NOTIFICATION_ID = name.hashCode();
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.BEDMAGE_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_bed_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_bed_black_24dp))
                .setContentTitle(Constants.BEDMAGE_NOTIFICATION_TITLE)
                .setContentText("Bedmage " + name + " is ready to login")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[0])
                .setSound(alarmSound)
                .setOnlyAlertOnce(true)
                .setContentIntent(getPendingIntent(context, "bedmage"));

        // Show the notification
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build());
    }
}
