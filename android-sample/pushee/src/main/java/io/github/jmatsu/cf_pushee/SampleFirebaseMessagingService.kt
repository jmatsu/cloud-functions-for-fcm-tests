package io.github.jmatsu.cf_pushee

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class SampleFirebaseMessagingService : FirebaseMessagingService() {
    private val notificationManager by lazy { NotificationManagerCompat.from(this) }

    override fun onMessageReceived(message: RemoteMessage) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(ACTION_RECEIVED_DATA_MESSAGE).putExtra(KEY_DATA, message.dataBundle))

        if (message.data.isNotEmpty()) {
            Log.d(TAG, "Received a message which contains data")

            notificationManager.notify("data", 0x01, buildNotification("data") {
                setContentText("Hello, I'm a data message")
                setSubText("Can you see me?")
            })
        } else {
            Log.d(TAG, "Received a message which does not contain any data")

            notificationManager.notify("zero-data", 0x02, buildNotification("zero-data") {
                setContentText("Hello, I'm a zero-data message")
                setSubText("Can you see me?")
            })
        }

        if (message.notification != null) {
            Log.d(TAG, "Received a message which contains a notification")
        }
    }

    private fun buildNotification(channelId: String, builder: NotificationCompat.Builder.() -> Unit) =
            NotificationCompat.Builder(this, channelId).apply {
                setWhen(System.currentTimeMillis())
                setShowWhen(true)
                setSmallIcon(R.drawable.ic_notification)
                setAutoCancel(true)
            }.apply(builder).build()

    private val RemoteMessage.dataBundle: Bundle
        get() = Bundle().apply { data.forEach { key, value -> putString(key, value) } }
}