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

/**
 * Service class for handling notifications.
 * This class provides methods for building and displaying notifications.
 */
class NotificationService {

    companion object {
        // Package name for the application
        private const val packageName = "com.example.articleappassignment.notification"

        /**
         * Builds and displays a notification.
         * @param context The context in which the notification should be displayed.
         * @param title The title of the notification.
         * @param description The description of the notification.
         */
        fun notificationBuilder(context: Context, title: String, description: String) {

            // Create an intent to launch the MainActivity
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // Create a PendingIntent to launch the MainActivity
            val pendingIntent = PendingIntent.getActivity(
                context,
                Constants.NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE
            )

            // Build the notification using NotificationCompat
            val builder = NotificationCompat.Builder(
                context.applicationContext,
                Constants.NOTIFICATION_CHANNEL_ID
            )
                .setSmallIcon(R.drawable.ic_notification)
                .setVibrate(longArrayOf(2000))
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setContent(generateNotificationView(context, title, description))

            // Get the NotificationManager
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Create a notification channel for devices with API level 26 and above
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    Constants.NOTIFICATION_CHANNEL_ID, Constants.NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(notificationChannel)
            }

            // Notify with the built notification
            notificationManager.notify(Constants.NOTIFICATION_REQUEST_CODE, builder.build())
        }

        /**
         * Generates the custom layout for the notification.
         * @param context The context in which the notification view should be generated.
         * @param title The title of the notification.
         * @param description The description of the notification.
         * @return The RemoteViews object representing the custom notification view.
         */
        @SuppressLint("RemoteViewLayout")
        fun generateNotificationView(
            context: Context,
            title: String,
            description: String
        ): RemoteViews {
            val remoteView = RemoteViews(packageName, R.layout.layout_notification)
            remoteView.setTextViewText(R.id.noti_title, title)
            remoteView.setTextViewText(R.id.noti_title, description)
            remoteView.setImageViewResource(R.id.noti_imageView, R.drawable.ic_notification)
            return remoteView
        }

    }

}