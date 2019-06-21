package com.tanos.skiptool

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent


class AutoSkipAccessibilityService : AccessibilityService() {

    companion object {
        val KEY_SKIP_AD = "skipad"
        val KEY_MUTE_AD = "mutepad"
        var skipad: Boolean = false
        var mutead: Boolean = false
    }

    override fun onCreate() {
        super.onCreate()
        skipad = PreferenceUtil.getInstance(applicationContext).getValue(KEY_SKIP_AD,false)
        mutead = PreferenceUtil.getInstance(applicationContext).getValue(KEY_MUTE_AD,false)
    }

    override fun onInterrupt() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
//            EventUtil.getInstance().parseNodeInfo(it.source)
            if (skipad) {
                EventUtil.getInstance().findViewByID(this, "com.google.android.youtube:id/skip_ad_button")?.let {
                    EventUtil.getInstance().performViewClick(it)
                    //com.google.android.youtube:id/skip_ad_button_text
                    //com.google.android.youtube:id/skip_ad_button_icon
                }
            }

            if (mutead) {
                //TODO
            }


        }

    }
}