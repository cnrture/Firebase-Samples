package com.canerture.firebaseexamples.service

import com.canerture.firebaseexamples.common.showLogDebug
import com.canerture.firebaseexamples.common.showNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.data.isNotEmpty()) {
            showLogDebug(TAG, "Data: ${message.data}")
        }

        message.notification?.let {
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