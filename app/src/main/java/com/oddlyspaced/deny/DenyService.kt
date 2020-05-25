package com.oddlyspaced.deny

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import java.lang.Exception


class DenyService : AccessibilityService() {

    private val tag = "DenyService"

    override fun onServiceConnected() {
        Log.d(tag, "Service Connected")

        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = (Uri.parse("package:com.whatsapp"))
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

        Log.e("why", "no u come")
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }

    var currentScreen = -1
    var perms = arrayOf("Call logs", "Camera", "Contacts", "Location", "Microphone", "SMS", "Telephone", "Storage")
    private var permcounter = 0

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event != null) {
            if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                try {
                    if (currentScreen == -1) {
                        // app info screen
                        val res =
                            event.source.findAccessibilityNodeInfosByText("Permissions")[0].parent
                        res.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        currentScreen = 0
                    } else if (currentScreen == 0) {
                        // app permission screen
                        event.source.refresh()
                        if (permcounter == perms.size) {
                            currentScreen = 5
                            return
                        }
                        val perm =
                            event.source.findAccessibilityNodeInfosByText(perms[permcounter])[0].parent
                        permcounter++
                        perm.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        currentScreen = 1
                    } else if (currentScreen == 1) {
                        event.source.refresh()
                        // allow deny page
                        val deny = event.source.findAccessibilityNodeInfosByText("Deny")[0]
                        deny.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        performGlobalAction(GLOBAL_ACTION_BACK)
                        currentScreen = 0
                    }
                }
                catch (e: Exception) {
                    Log.e("Hit", "or miss")
                }
            }
        }
    }

}