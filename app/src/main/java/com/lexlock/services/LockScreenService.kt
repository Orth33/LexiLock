package com.lexlock.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.lexlock.ui.lockscreen.LockScreenActivity

class LockScreenService : Service() {

    private val screenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Triggering on SCREEN_OFF prepares the activity so it's ready when the screen turns ON.
            if (intent.action == Intent.ACTION_SCREEN_OFF || intent.action == Intent.ACTION_SCREEN_ON) {
                val lockIntent = Intent(context, LockScreenActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                context.startActivity(lockIntent)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
        }
        registerReceiver(screenReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(screenReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, "LexLockServiceChannel")
            .setContentTitle("LexLock is active")
            .setContentText("Listening for lock screen events")
            .setSmallIcon(android.R.drawable.ic_lock_idle_lock) // Uses default android icon for now
            .build()

        startForeground(1, notification)
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "LexLockServiceChannel",
                "LexLock Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}
