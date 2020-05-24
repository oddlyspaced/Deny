package com.oddlyspaced.deny

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class DenyService: AccessibilityService() {

    private val tag = "DenyService"

    override fun onServiceConnected() {
        Log.d(tag, "Service Connected")
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        TODO("Not yet implemented")
    }

}