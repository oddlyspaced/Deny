package com.oddlyspaced.deny

import android.accessibilityservice.AccessibilityService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.scaleMatrix
import java.lang.Exception
import kotlin.random.Random


class DenyService : AccessibilityService() {

    private val notificationChannelId = "10001"

    private val tag = "DenyService"
    private lateinit var pkgManager: PackageListManager
    private val whitelistedPackages = arrayListOf(
        "com.android.systemui",
        "com.google.android.packageinstaller",
        "com.android.settings",
        "com.android.packageinstaller",
        "com.google.android.permissioncontroller",
        "com.android.permissioncontroller"
    )

    override fun onServiceConnected() {
        Log.d(tag, "Service Connected")
        pkgManager = PackageListManager(applicationContext)
        createNotificationChannel()
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }

    private var packageInContext = ""
    private var grantedPermissions = ArrayList<String>()

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.packageName == null)
            return
        if (event.packageName.toString() != packageInContext && !whitelistedPackages.contains(event.packageName.toString())) {
            packageInContext = event.packageName.toString()
            grantedPermissions = pkgManager.getGrantedPermissions(packageInContext)
            checkPerms()
        }
    }

    private fun checkPerms() {
        Handler().postDelayed({
            try {
                val pp = pkgManager.getGrantedPermissions(packageInContext)
                if (pp != grantedPermissions) {
                    val diff = pp.subtract(grantedPermissions).toList()[0]
                    createNotification(pkgManager.checkPermGroup(diff))
                    grantedPermissions = pp
                }
            } catch (e: Exception) {
                Log.e("Error", e.toString())
            }
            checkPerms()
        }, 100)
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.app_name)
        val descriptionText = getString(R.string.app_name)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(notificationChannelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(permission: Int) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, notificationChannelId)
            .setSmallIcon(when(permission) {
                PackageListManager.GROUP_BODY_SENSORS -> R.drawable.ic_perm_body_sensors
                PackageListManager.GROUP_CALENDAR -> R.drawable.ic_perm_calendar
                PackageListManager.GROUP_CALL_LOGS -> R.drawable.ic_perm_call_logs
                PackageListManager.GROUP_CAMERA -> R.drawable.ic_perm_camera
                PackageListManager.GROUP_CONTACTS -> R.drawable.ic_perm_contacts
                PackageListManager.GROUP_LOCATION -> R.drawable.ic_perm_location
                PackageListManager.GROUP_MICROPHONE -> R.drawable.ic_perm_mic
                PackageListManager.GROUP_PHYSICAL_ACTIVITY -> R.drawable.ic_perm_physical_activity
                PackageListManager.GROUP_SMS -> R.drawable.ic_perm_sms
                PackageListManager.GROUP_STORAGE -> R.drawable.ic_perm_storage
                PackageListManager.GROUP_TELEPHONE -> R.drawable.ic_perm_telephone
                else -> R.drawable.ic_launcher_foreground
            })
            .setContentTitle(pkgManager.getPermGroupName(permission))
            .setContentText(pkgManager.getPackageInfo(packageInContext).applicationInfo.loadLabel(applicationContext.packageManager).toString() + " was granted " + pkgManager.getPermGroupName(permission))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setWhen(System.currentTimeMillis())

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(permission, builder.build())
        }
    }

}