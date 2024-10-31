package com.example.buddy

import android.content.*
import android.app.*
import android.os.*

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        internal const val NOTIFICATION_ID = 123
        internal const val NOTIFICATION_CHANNEL_ID = "MyNotificationChannel"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            showNotification(context)
        }
    }

    private fun showNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Hi!")
            .setContentText("Check out your dogs!")
            .setSmallIcon(R.drawable.doglogo)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}
