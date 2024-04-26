package com.example.articleappassignment.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Service class for handling Firebase Cloud Messaging (FCM) notifications.
 * This class extends `FirebaseMessagingService` and provides methods for handling token refresh
 * and receiving messages from Firebase Cloud Messaging.
 */
class FirebaseNotificationService : FirebaseMessagingService() {

    /**
     * Called when a new token for the device is generated.
     * @param token The new token generated for the device.
     */
    override fun onNewToken(token: String) {
        Log.d("device token", token)
        super.onNewToken(token)
    }

    /**
     * Called when a message is received from Firebase Cloud Messaging.
     * @param message The received RemoteMessage containing the FCM message data.
     */
    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("message received", message.data.toString())
        // Build and display a notification based on the received FCM message
        NotificationService.notificationBuilder(
            this,
            message.notification?.title ?: "",
            message.notification?.body ?: ""
        )
    }


}