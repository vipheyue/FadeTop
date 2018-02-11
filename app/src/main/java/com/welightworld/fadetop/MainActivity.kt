package com.welightworld.fadetop

import android.app.AlertDialog
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.WindowManager
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


//https://developer.android.com/training/scheduling/alarms.html
class MainActivity : AppCompatActivity() {
    private var havedStart = false
    var workState = WorkState.REST
    val vibTime: Long = 300 * 1000L
    var disposable: Disposable? = null
    lateinit var vib: Vibrator

    companion object {
        lateinit var INSTANCE: MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        INSTANCE = this
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)//屏幕常亮
        Logger.addLogAdapter(AndroidLogAdapter())
        initView()
        AlarmHelper.setAlarm(this)
    }


    private fun initView() {


//取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

//需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) window.statusBarColor = configMainBgColor

        val delegate = btn_action.delegate
        delegate.backgroundColor = configMainTiggerBgColor

        constraintlayout.setBackgroundColor(configMainBgColor)
        vib = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator

        btn_action.setOnClickListener {
            if (havedStart) {//已经开始了 想停止 转换状态
                havedStart = false
                disposable?.dispose()
                workState = WorkState.REST
                tv_time_panel.text = "00:00"
                btn_action.text = getString(R.string.start)//目前停止状态 点击启动
                tv_tip.text = getString(R.string.tip_resting)

            } else {
                havedStart = true
                workState = WorkState.WORKING
                countDown(configWorkingTime)
                btn_action.text = getString(R.string.stop)//目前启动状态 点击停止
                tv_tip.text = getString(R.string.tip_working)

            }
        }
        iv_setting.setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }

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
                tv_time_panel.text = remainMinute.toString() + ":" + remainSeconds
            }

            override fun onComplete() {
                when (workState) {
                    WorkState.WORKING -> {
                        //开始一直振动 并弹框 1.继续工作5分钟 2.开始休息
//                        sendNotify("休息一下吧!")
                        if (configSuperModel) {
                            NotifyHelper.sendNotify(this@MainActivity, getString(R.string.tip_resting), true)
                        } else {
                            vib.vibrate(vibTime)
                        }
                        chooseDialog()
                    }
                    WorkState.REST -> {
                        workState = WorkState.WORKING
                        tv_tip.text = getString(R.string.tip_working)
                        //TODO 播放开始工作的音乐 通知
//                        NotifyHelper.sendNotify(MyApplication.get(),"开始工作了,老铁")
                        NotifyHelper.sendNotify(this@MainActivity, getString(R.string.tip_working))
                        countDown(configWorkingTime)
                    }
                }
            }
        })
    }

    private fun chooseDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.tip_go_rest))
                .setNegativeButton(getString(R.string.keep_working), { dialog, which ->
                    vib.cancel()
                    NotifyHelper.cancalAll(this)
                    countDown(configRestTime)//继续工作5分钟

                })
                .setPositiveButton(getString(R.string.rest_now), { dialog, i ->
                    vib.cancel()
                    NotifyHelper.cancalAll(this)
                    workState = WorkState.REST
                    tv_tip.text = getString(R.string.tip_resting)
                    //TODO 播放开始休息的音乐 通知
                    countDown(configRestTime)
                })
                .setCancelable(false)

        builder.create().show()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        NotifyHelper.cancalAll(this)
        disposable?.dispose()
        super.onDestroy()
    }
}
