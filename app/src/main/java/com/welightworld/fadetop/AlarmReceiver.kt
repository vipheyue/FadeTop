package com.welightworld.fadetop

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.orhanobut.logger.Logger

/**
 * Created by heyue on 2018/1/3.
 */
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
     Logger.d("AlarmReceiver")
        Toast.makeText(context,"xxx",Toast.LENGTH_SHORT).show()
        //收到每日提醒 判断是否要每日提醒 发送通知栏
        if (configRemindEveryDay) {
            AlarmHelper.setAlarm(context)
        }
        NotifyHelper.sendNotify(context, "每日作息提醒,可在设置中关闭")
    }

}