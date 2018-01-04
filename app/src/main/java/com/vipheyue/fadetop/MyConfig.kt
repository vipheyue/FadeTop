package com.vipheyue.fadetop

import android.graphics.Color

/**
 * Created by heyue on 2017/12/25.
 */
var configMainBgColor by Preference(MyApplication.get(), "configMainBgColor", Color.parseColor("#dc3486"))
var configMainTiggerBgColor by Preference(MyApplication.get(), "configMainTiggerBgColor", Color.parseColor("#da04f9dc"))
var configWorkingTime by Preference(MyApplication.get(), "configWorkingTime", 30 * 60L)
//var configWorkingTime by Preference(MyApplication.get(), "configWorkingTime",  10L)
//var configRestTime by Preference(MyApplication.get(), "configRestTime", 6L)
var configRestTime by Preference(MyApplication.get(), "configRestTime", 5 * 60L)
var configRemindEveryDay by Preference(MyApplication.get(), "configRemindEveryDay", true)
//超强模式 持续响铃 必须用户手动停止
var configSuperModel by Preference(MyApplication.get(), "configSuperModel", false)
