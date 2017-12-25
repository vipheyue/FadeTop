package com.vipheyue.fadetop

import android.graphics.Color

/**
 * Created by heyue on 2017/12/25.
 */
var configMainBgColor by Preference(MyApplication.get(), "configMainBgColor", Color.parseColor("#dc3486"))
var configMainTiggerBgColor by Preference(MyApplication.get(), "configMainTiggerBgColor", Color.parseColor("#da04f9dc"))
var configWorkingTime by Preference(MyApplication.get(), "configWorkingTime", 30 * 60L)
var configRestTime by Preference(MyApplication.get(), "configRestTime", 5 * 60L)
