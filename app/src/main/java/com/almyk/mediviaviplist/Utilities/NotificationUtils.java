package com.almyk.mediviaviplist.Utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, LOGIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(Constants.NOTIFICATION_TITLE)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[0])
                .setContentIntent(getPendingIntent(context));

        // Show the notification
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build());
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
    }

    public NotificationUtils() {}
}
