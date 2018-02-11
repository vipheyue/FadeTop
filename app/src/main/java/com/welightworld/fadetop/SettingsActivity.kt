package com.welightworld.fadetop

import android.content.Intent
import android.graphics.Color
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
        mActionBar.title = getString(R.string.setting)
    }

    private fun initView() {
        val btn_color_pinker_delegate = btn_color_pinker.getDelegate()
        btn_color_pinker_delegate.setBackgroundColor(configMainBgColor)
        val btn_color_tigger_delegate = btn_color_tigger.getDelegate()
        btn_color_tigger_delegate.setBackgroundColor(configMainTiggerBgColor)

        tv_worktime.setText((configWorkingTime / 60).toString() + getString(R.string.minutes))
        tv_resttime.setText((configRestTime / 60).toString() + getString(R.string.minutes))
        sw_everydayRemind.isChecked=configRemindEveryDay
        sw_everydayRemind.setOnCheckedChangeListener { buttonView, isChecked -> configRemindEveryDay = isChecked }
        sw_SuperModel.isChecked=configSuperModel
        sw_SuperModel.setOnCheckedChangeListener { buttonView, isChecked ->
            configSuperModel = isChecked
            toast(getString(R.string.toast_supermodel))
        }
        btn_color_pinker.setOnClickListener {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle(getString(R.string.set_main_bg))
                    .initialColor(Color.parseColor("#FFFFFF"))
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
//                    .setOnColorSelectedListener { selectedColor -> toast("onColorSelected: 0x" + Integer.toHexString(selectedColor)) }
                    .setPositiveButton(getString(R.string.ok)) { dialog, selectedColor, allColors ->
//                        toast(getString(R.string.toast_set_success))
                        configMainBgColor = selectedColor
                        val btn_color_pinker_delegate = btn_color_pinker.getDelegate()
                        btn_color_pinker_delegate.setBackgroundColor(configMainBgColor)
                        MainActivity.INSTANCE.finish()//关闭之前的页面
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }
                    .setNegativeButton(getString(R.string.cancel)) { dialog, which -> }
                    .build()
                    .show()
        }
        btn_color_tigger.setOnClickListener {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle(getString(R.string.set_tigger_bg))
                    .initialColor(Color.parseColor("#FFFFFF"))
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setPositiveButton("ok") { dialog, selectedColor, allColors ->
//                        toast(getString(R.string.toast_set_success))
                        configMainTiggerBgColor = selectedColor
                        val btn_color_tigger_delegate = btn_color_tigger.getDelegate()
                        btn_color_tigger_delegate.setBackgroundColor(configMainTiggerBgColor)
                        MainActivity.INSTANCE.finish()//关闭之前的页面
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }
                    .build()
                    .show()
        }

        iv_work_reduce.setOnClickListener {
            if (configWorkingTime > (10 * 60L)) configWorkingTime = configWorkingTime - 1 * 60L
            tv_worktime.setText((configWorkingTime / 60).toString() + getString(R.string.minutes))
        }
        iv_work_add.setOnClickListener {
            configWorkingTime = configWorkingTime + 1 * 60L
            tv_worktime.setText((configWorkingTime / 60).toString() +  getString(R.string.minutes))
        }

        iv_rest_reduce.setOnClickListener {
            if (configRestTime > (5 * 60L)) configRestTime = configRestTime - 1 * 60L
            tv_resttime.setText((configRestTime / 60).toString() + getString(R.string.minutes))
        }
        iv_rest_add.setOnClickListener {
            configRestTime = configRestTime + 1 * 60L
            tv_resttime.setText((configRestTime / 60).toString() +  getString(R.string.minutes))
        }
        btn_feedback.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/html"
            intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.email))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.suggest)+getString(R.string.app_name))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.suggest))
            startActivity(Intent.createChooser(intent, "Send Email"))
            toast("请加入QQ群:469859289 email: "+getString(R.string.email))

        }
        btn_share.setOnClickListener {
            var textIntent = Intent(Intent.ACTION_SEND)
            textIntent.setType("text/plain");
            textIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content))
            startActivity(Intent.createChooser(textIntent, getString(R.string.app_name)))
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
