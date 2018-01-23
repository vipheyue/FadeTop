package com.welightworld.fadetop

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import kotlinx.android.synthetic.main.activity_setting.*
import org.jetbrains.anko.toast


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initView()

        val mActionBar = supportActionBar
        mActionBar!!.setHomeButtonEnabled(true)
        mActionBar.setDisplayHomeAsUpEnabled(true)
        mActionBar.title = "设置"
    }

    private fun initView() {
        val btn_color_pinker_delegate = btn_color_pinker.getDelegate()
        btn_color_pinker_delegate.setBackgroundColor(configMainBgColor)
        val btn_color_tigger_delegate = btn_color_tigger.getDelegate()
        btn_color_tigger_delegate.setBackgroundColor(configMainTiggerBgColor)

        tv_worktime.setText((configWorkingTime / 60).toString() + "分钟")
        tv_resttime.setText((configRestTime / 60).toString() + "分钟")
        sw_everydayRemind.isChecked=configRemindEveryDay
        sw_everydayRemind.setOnCheckedChangeListener { buttonView, isChecked -> configRemindEveryDay = isChecked }
        sw_SuperModel.isChecked=configSuperModel
        sw_SuperModel.setOnCheckedChangeListener { buttonView, isChecked ->
            configSuperModel = isChecked
            toast("超级模式会持续响铃,必须手动点击才会停止")
        }
        btn_color_pinker.setOnClickListener {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("设置主页背景")
                    .initialColor(Color.parseColor("#FFFFFF"))
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
//                    .setOnColorSelectedListener { selectedColor -> toast("onColorSelected: 0x" + Integer.toHexString(selectedColor)) }
                    .setPositiveButton("ok") { dialog, selectedColor, allColors ->
                        toast("设置成功,下次启动 APP 时生效")
                        configMainBgColor = selectedColor
                        val btn_color_pinker_delegate = btn_color_pinker.getDelegate()
                        btn_color_pinker_delegate.setBackgroundColor(configMainBgColor)
                    }
                    .setNegativeButton("cancel") { dialog, which -> }
                    .build()
                    .show()
        }
        btn_color_tigger.setOnClickListener {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("设置启动器背景")
                    .initialColor(Color.parseColor("#FFFFFF"))
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setPositiveButton("ok") { dialog, selectedColor, allColors ->
                        toast("设置成功,下次启动 APP 时生效")
                        configMainTiggerBgColor = selectedColor
                        val btn_color_tigger_delegate = btn_color_tigger.getDelegate()
                        btn_color_tigger_delegate.setBackgroundColor(configMainTiggerBgColor)
                    }
                    .build()
                    .show()
        }

        iv_work_reduce.setOnClickListener {
            if (configWorkingTime > (10 * 60L)) configWorkingTime = configWorkingTime - 1 * 60L
            tv_worktime.setText((configWorkingTime / 60).toString() + "分钟")
        }
        iv_work_add.setOnClickListener {
            configWorkingTime = configWorkingTime + 1 * 60L
            tv_worktime.setText((configWorkingTime / 60).toString() + "分钟")
        }

        iv_rest_reduce.setOnClickListener {
            if (configRestTime > (5 * 60L)) configRestTime = configRestTime - 1 * 60L
            tv_resttime.setText((configRestTime / 60).toString() + "分钟")
        }
        iv_rest_add.setOnClickListener {
            configRestTime = configRestTime + 1 * 60L
            tv_resttime.setText((configRestTime / 60).toString() + "分钟")
        }
        btn_feedback.setOnClickListener {
            try {
                val url = "https://jq.qq.com/?_wv=1027&k=5mvN2Tr"
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                toast("请加入QQ群:469859289")
            } catch (e: Exception) {
                toast("请加入QQ群:469859289")
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
