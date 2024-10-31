    package com.example.buddy

    import android.annotation.SuppressLint
    import android.app.NotificationChannel
    import android.app.NotificationManager
    import android.app.PendingIntent
    import android.content.BroadcastReceiver
    import android.content.Context
    import android.content.Intent
    import android.graphics.BitmapFactory
    import android.os.Build
    import android.os.PowerManager
    import android.util.Log
    import android.widget.RemoteViews
    import androidx.core.app.NotificationCompat
    import androidx.core.app.NotificationManagerCompat
    import androidx.core.content.ContextCompat
    import androidx.core.content.ContextCompat.getSystemService



    class AlarmReceiver : BroadcastReceiver() {

        companion object {
            private var notificationIdCounter = 0
        }

        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("Alarm", "Alarm received")
            val title = intent.getStringExtra("title")
            val note = intent.getStringExtra("note")
            val dialogIntent = Intent(context, calendar::class.java)
            dialogIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, dialogIntent,
                PendingIntent.FLAG_IMMUTABLE)
            val channelId = "event_reminder_channel"
            val notificationId = notificationIdCounter++ // Increment the counter for each notification
            val bigIcon = BitmapFactory.decodeResource(context.resources, R.drawable.notif_mc)
            val notification = NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(note)
                .setSmallIcon(R.drawable.notif_icon)
                .setLargeIcon(bigIcon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelName = "Event Reminders"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(channelId, channelName, importance).apply {
                    description = "Channel for event reminders"
                }

                // Register the channel with the system
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            // Show notification
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManagerCompat.notify(notificationId, notification)
            Log.d("Alarm", "Alarm notification sent")
        }
    }