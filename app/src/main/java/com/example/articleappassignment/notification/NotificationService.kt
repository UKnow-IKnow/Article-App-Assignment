package com.example.articleappassignment.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.articleappassignment.R
import com.example.articleappassignment.ui.MainActivity
import com.example.articleappassignment.utils.Constants

class NotificationService {

    companion object {

        private const val packageName = "com.example.articleappassignment.notification"


        fun notificationBuilder(context: Context, title: String, description: String) {

            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // Create a PendingIntent to launch the MainActivity
            val pendingIntent = PendingIntent.getActivity(context,
                Constants.NOTIFICATION_REQUEST_CODE,intent, PendingIntent.FLAG_IMMUTABLE)

            // Build the notification using NotificationCompat
            val builder = NotificationCompat.Builder(context.applicationContext, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setVibrate(longArrayOf(2000))
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setContent(generateNotificationView(context,title,description))

            // Get the NotificationManager
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Create a notification channel for devices with API level 26 and above
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
                val notificationChannel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID,Constants.NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(notificationChannel)
            }

            // Notify with the built notification
            notificationManager.notify(Constants.NOTIFICATION_REQUEST_CODE,builder.build())
        }


        @SuppressLint("RemoteViewLayout")
        fun generateNotificationView (context: Context, title : String, description : String) : RemoteViews {
            val remoteView = RemoteViews(packageName, R.layout.layout_notification)
            remoteView.setTextViewText(R.id.noti_title,title)
            remoteView.setTextViewText(R.id.noti_title,description)
            remoteView.setImageViewResource(R.id.noti_imageView,R.drawable.ic_notification)
            return remoteView
        }

    }

}