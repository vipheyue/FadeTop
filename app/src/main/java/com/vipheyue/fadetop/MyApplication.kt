package com.vipheyue.fadetop

import android.app.Application
import com.tencent.bugly.crashreport.CrashReport

/**
 * Created by heyue on 2017/12/25.
 */

class MyApplication : Application() {
    override fun onCreate() {
        INSTANCE = this
        super.onCreate()
        CrashReport.initCrashReport(getApplicationContext(), "3b083ab1ce", false);
    }

    companion object {
        private lateinit var INSTANCE: MyApplication

        fun get(): MyApplication = INSTANCE
    }
}
