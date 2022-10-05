package com.canerture.firebaseexamples.service

import android.util.Log
import com.canerture.firebaseexamples.common.showNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, "Refreshed token :: $token")
        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.i(TAG, "Message : $message")

        if (message.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${message.data}")
        }

        message.notification?.let {
            Log.d(TAG, "Notification: ${it.body.orEmpty()}")
            showNotification(
                applicationContext,
                "Notification Title",
                it.body.orEmpty()
            )
        }
    }

    private fun sendRegistrationToServer(token: String) {
        //send token to your server
    }

    companion object {
        private const val TAG = "FirebaseMessagingService"
    }
}