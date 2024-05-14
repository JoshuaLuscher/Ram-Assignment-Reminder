package com.wcupa.assignmentNotifier.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.wcupa.assignmentNotifier.R

private const val CHANNEL_ID = "Ram Assignment Reminder"
private const val VERBOSE_NOTIFICATION_CHANNEL_NAME = "Verbose Assignment Reminder Notifications"
private const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications when due date is approaching"

//Sends out Notifications
@SuppressLint("MissingPermission")
fun sendNotification(context: Context, assignmentName: String, courseName: String, id: Int) {
    //Creates Channel

    //Create the NotificationChannel
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(
        CHANNEL_ID,
        VERBOSE_NOTIFICATION_CHANNEL_NAME,
        importance
    )
    channel.description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

    notificationManager?.createNotificationChannel(channel)


    //Build Notification
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Assignment Reminder")
        .setContentText("Due date approaching: $assignmentName for $courseName")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))
        .setAutoCancel(true)

    //Sends out Notifications. Will not function if notifications aren't enabled, but causes no issues otherwise
    NotificationManagerCompat.from(context).notify(id, builder.build())
}