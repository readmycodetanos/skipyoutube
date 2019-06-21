package com.tanos.skiptool

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        item1?.setOnClickListener {
            //skip ads
            if (isAccessbilityServiceOn()) {
                mIdSkipSwitch.isChecked = !mIdSkipSwitch.isChecked
                PreferenceUtil.getInstance(applicationContext)
                    .setValue(AutoSkipAccessibilityService.KEY_SKIP_AD, mIdSkipSwitch.isChecked)
                AutoSkipAccessibilityService.skipad = mIdSkipSwitch.isChecked
            }
        }
        textView4?.setOnClickListener {

            startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:readmycodetanos@gmail.com")))
        }

        item2?.setOnClickListener {
            //mute ads
            if (isAccessbilityServiceOn()) {
                mIdMuteSwitch.isChecked = !mIdMuteSwitch.isChecked
                PreferenceUtil.getInstance(applicationContext)
                    .setValue(AutoSkipAccessibilityService.KEY_MUTE_AD, mIdMuteSwitch.isChecked)
                AutoSkipAccessibilityService.mutead = mIdMuteSwitch.isChecked
            }
        }
        button2?.setOnClickListener {
            Utilities().goAccess(this)
        }
        button?.setOnClickListener {
            packageManager.getLaunchIntentForPackage("com.google.android.youtube")?.let {
                startActivity(it)
            } ?: Toast.makeText(this, getString(R.string.youtube_not_install), Toast.LENGTH_LONG).show()
        }

        mIdSkipSwitch.isChecked =
            PreferenceUtil.getInstance(applicationContext).getValue(AutoSkipAccessibilityService.KEY_SKIP_AD, false)
        mIdMuteSwitch.isChecked =
            PreferenceUtil.getInstance(applicationContext).getValue(AutoSkipAccessibilityService.KEY_MUTE_AD, false)
        AutoSkipAccessibilityService.skipad = mIdSkipSwitch.isChecked
        AutoSkipAccessibilityService.mutead = mIdMuteSwitch.isChecked
    }

    override fun onResume() {
        super.onResume()
        Utilities().isAccessibilitySettingsOn(this,AutoSkipAccessibilityService::class.java).apply {
            mIdStateGroup.visibility = if(this) View.GONE else View.VISIBLE
        }
    }

    fun isAccessbilityServiceOn(): Boolean {
        return Utilities().isAccessibilitySettingsOn(this, AutoSkipAccessibilityService::class.java).also {
            if (!it) {
                AlertDialog.Builder(this).setTitle(R.string.permission_title).setMessage(R.string.permission_reason)
                    .setPositiveButton(R.string.move,
                        DialogInterface.OnClickListener { dialog, which ->
                            Utilities().goAccess(this)

                        }).setNegativeButton(R.string.cancel, null).create().show()

            }
        }
    }


}
