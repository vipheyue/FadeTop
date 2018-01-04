package com.vipheyue.fadetop

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by heyue on 2018/1/4.
 */
object AlarmHelper {
    fun setAlarm(mContext: Context) {
        var alarmMgr: AlarmManager? = null
        alarmMgr = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent = Intent(mContext, AlarmReceiver::class.java);
        var alarmIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Set the alarm to start at 9:30 a.m.
            val calendar = Calendar.getInstance()
//            calendar.timeInMillis = System.currentTimeMillis()
            calendar.set(Calendar.HOUR_OF_DAY, 9)
            calendar.set(Calendar.MINUTE, 30)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1)//下一天 9:30

            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)


            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var format = sdf.format(calendar.getTime())

//            alarmMgr?.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+7*1000, alarmIntent)
//            alarmMgr?.setWindow(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+7*1000,10*1000, alarmIntent)
//            alarmMgr?.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+7*1000,10*1000, alarmIntent)
        }
    }
}
