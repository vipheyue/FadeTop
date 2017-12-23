package com.vipheyue.fadetop

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private var havedStart = false
    var workState = WorkState.REST
    val workingTime = 30 * 60L
    private val restTime = 5 * 60L
    val vibTime: Long = 300 * 1000L
    lateinit var disposable: Disposable
    lateinit var vib: Vibrator
    lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Logger.addLogAdapter(AndroidLogAdapter())
        initView()
    }

    private fun initView() {
        vib = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        btn_action.setOnClickListener {
            if (havedStart) {//已经开始了 想停止 转换状态
                havedStart = false
                disposable.dispose()
                btn_action.setText("启动")//目前停止状态 点击启动
            } else {
                havedStart = true
                workState = WorkState.WORKING
                countDown(workingTime)
                btn_action.setText("停止")//目前启动状态 点击停止
            }
        }
    }

    fun countDown(count: Long) {
        val observable = Observable.interval(0, 1, TimeUnit.SECONDS)//每秒发射一个数字出来
                .take(count + 1)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
//TODO  可以在这儿只做倒计时  直接返回 observable 后面的东西各自处理

        observable.subscribe(object : Observer<Long> {

            override fun onSubscribe(disposable: Disposable) {
                this@MainActivity.disposable = disposable
            }

            override fun onError(e: Throwable) {
            }

            override fun onNext(t: Long) {
                val remainTime = count - t
                val remainMinute = remainTime / 60
                val remainSeconds = remainTime % 60
                tv_time_panel.setText(remainMinute.toString() + ":" + remainSeconds)
            }

            override fun onComplete() {

                when (workState) {
                    WorkState.WORKING -> {
                        //开始一直振动 并弹框 1.继续工作5分钟 2.开始休息
//                        sendNotify("休息一下吧!")
                        vib.vibrate(vibTime)
                        chooseDialog()
                    }
                    WorkState.REST -> {
                        workState = WorkState.WORKING
                        tv_tip.setText("工作中 稍后将提醒你 注意休息")
                        //TODO 播放开始工作的音乐 通知
                        sendNotify("开始工作了,老铁")
                        countDown(workingTime)
                    }
                }
            }
        })
    }

    private fun chooseDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("你已经努力工作了30分钟了,休息一下吧!")
                .setNegativeButton("再工作5分钟", { dialog, which ->
                    vib.cancel()
                    countDown(restTime)//继续工作5分钟
                })
                .setPositiveButton("立即休息", { dialog, i ->
                    vib.cancel()
                    workState = WorkState.REST
                    tv_tip.setText("休息中 放松菊花人人有责 起来喝杯水吧")
                    //TODO 播放开始休息的音乐 通知
                    countDown(restTime)
                })

        builder.create().show()
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun sendNotify(content: String) {
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


        val mBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(content)
                .setContentText("")
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission

        val resultIntent = Intent(this, MainActivity::class.java)
        val resultPendingIntent =
                PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        mBuilder.setContentIntent(resultPendingIntent)
//        mBuilder.setFullScreenIntent(resultPendingIntent,true)

//        notificationManager.flags = Notification.FLAG_INSISTENT

        notificationManager.notify(NOTIFICATION_ID, mBuilder.build())
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        disposable?.dispose()
        notificationManager.cancelAll()
        super.onDestroy()
    }
}
