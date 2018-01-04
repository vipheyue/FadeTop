package com.vipheyue.fadetop

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat

/**
 * Created by heyue on 2018/1/4.
 */
object NotifyHelper {
    @TargetApi(Build.VERSION_CODES.O)
    fun sendNotify(mContext: Context, content: String,superModel:Boolean=false) {
        var notificationManager: NotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val NOTIFICATION_ID = 1
        val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
        val pattern = longArrayOf(0, 100, 1000, 300, 200, 100, 500, 200, 100)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "久坐提醒", NotificationManager.IMPORTANCE_DEFAULT)
            // Configure the notification channel.
            notificationChannel.description = "我是久坐提醒 APP 的描述"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = pattern
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }


        val mBuilder = NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(content)
                .setContentText("")
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission

        val resultIntent = Intent(mContext, MainActivity::class.java)
        val resultPendingIntent =
                PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        mBuilder.setContentIntent(resultPendingIntent)
//        mBuilder.setFullScreenIntent(resultPendingIntent,true)

        var build = mBuilder.build()
        if (superModel) {
            build.flags = Notification.FLAG_INSISTENT
        } else {
            build.flags = Notification.FLAG_AUTO_CANCEL
        }
        notificationManager.notify(NOTIFICATION_ID, build)
    }

    fun cancalAll(mContext: Context) {
        var notificationManager: NotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()

    }
}