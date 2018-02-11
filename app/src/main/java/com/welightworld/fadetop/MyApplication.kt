package com.welightworld.fadetop

import android.app.Application
import com.tencent.bugly.Bugly

/**
 * Created by heyue on 2017/12/25.
 */

class MyApplication : Application() {
    override fun onCreate() {
        INSTANCE = this
        super.onCreate()
        Bugly.init(getApplicationContext(), "81a1bab3b8", false);
    }

    companion object {
        private lateinit var INSTANCE: MyApplication

        fun get(): MyApplication = INSTANCE
    }
}
