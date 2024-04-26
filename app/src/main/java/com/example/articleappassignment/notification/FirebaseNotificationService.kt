package com.example.articleappassignment.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseNotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("device token", token)
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("message received", message.data.toString())
        // Build and display a notification based on the received FCM message
        NotificationService.notificationBuilder(this, message.notification?.title?:"", message.notification?.body?:"")
    }


}