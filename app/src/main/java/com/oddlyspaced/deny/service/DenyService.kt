package com.oddlyspaced.deny.service

import android.accessibilityservice.AccessibilityService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_MAIN
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.oddlyspaced.deny.util.LogManager
import com.oddlyspaced.deny.util.PackageListManager
import com.oddlyspaced.deny.R
import com.oddlyspaced.deny.util.AppStatusManager
import java.io.*
import java.lang.Exception


class DenyService : AccessibilityService() {

    private val notificationChannelId = "111"
    private val tag = "DenyService"
    private lateinit var pkgManager: PackageListManager
    private lateinit var logManager: LogManager
    private lateinit var appStatusManager: AppStatusManager
    private var packageHome: String = ""
    private val whitelistedPackages = arrayListOf("com.android.systemui", "com.google.android.packageinstaller", "com.android.settings", "com.android.packageinstaller", "com.google.android.permissioncontroller", "com.android.permissioncontroller")

    override fun onServiceConnected() {
        Log.d(tag, "Service Connected")
        pkgManager = PackageListManager(applicationContext)
        logManager = LogManager(applicationContext)
        appStatusManager = AppStatusManager(applicationContext)
        createNotificationChannel()
        // add launcher to ignore list
        val intent = Intent(ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME);
        val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        packageHome = resolveInfo?.activityInfo?.packageName!!
        whitelistedPackages.add(packageHome)
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }

    private var packageInContext = ""
    private var grantedPermissions = ArrayList<String>()
    private var isPermissionBeingRevoked = false
    private var permissionsToRevoke = ArrayList<String>()
    private var packageRevoked = ""
    private var shouldExit = false
    private var revoketemp = ""

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Log.e("ttt", packageInContext)
        if (event?.packageName == null)
            return
        // ignore notification and toast
        if (event.parcelableData is Notification || event.parcelableData is Toast)
            return
        if (shouldExit && event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            Log.e("EXYE", "EEEEE")
            performGlobalAction(GLOBAL_ACTION_HOME)
            shouldExit = false
            return
        }
        if (event.packageName == packageHome && packageInContext != "" && File(applicationContext.getExternalFilesDir(null).toString() + "/$packageInContext").exists() && pkgManager.getGrantedGroups(packageInContext).subtract(appStatusManager.read(packageInContext).permissions).isNotEmpty() && revoketemp != packageInContext) {
            Log.e("REVOKE111", packageInContext)
            Log.e("GRANTEd", packageInContext + ";;;" + pkgManager.getGrantedGroups(packageInContext).toString())
            revoketemp = packageInContext
            notify("Revoking Permissions", "Revoking Permissions from " + pkgManager.getPackageInfo(revoketemp).applicationInfo.loadLabel(applicationContext.packageManager).toString() + " in 3 seconds...")
            Handler().postDelayed({
                notify("Revoking", "Revoking Permissions from " + pkgManager.getPackageInfo(revoketemp).applicationInfo.loadLabel(applicationContext.packageManager).toString() + " now")
                val writer = PrintWriter(BufferedWriter(FileWriter(File(applicationContext.getExternalFilesDir(null).toString() + "/revokeperms"))))
                val pkgManager = PackageListManager(applicationContext)
                writer.println(revoketemp)
                val pp = appStatusManager.read(packageInContext).permissions
                for (perm in pp) {
                    if (perm.granted) {
                        writer.println(perm.perm)
                    }
                }
                writer.close()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = (Uri.parse("package:$revoketemp"))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                applicationContext.startActivity(intent)
            }, 3000)
        }
        if (whitelistedPackages.contains(event.packageName.toString())) {
            // revoke perms
            if (!isPermissionBeingRevoked) {
                isPermissionBeingRevoked = checkForRevoking()
            }
            if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && isPermissionBeingRevoked && event.source != null) {
                Log.e("rec", "REVOKe")
                event.source.refresh()
                revokePermissions(event)
            }
        }
        else if (event.packageName.toString() != packageInContext && !whitelistedPackages.contains(event.packageName.toString()) && event.packageName.toString() != applicationContext.packageName) {
            packageInContext = event.packageName.toString()
            grantedPermissions = pkgManager.getGrantedPermissions(packageInContext)
            checkPerms()
        }
    }

    private fun checkForRevoking(): Boolean {
        val permFile = File(applicationContext.getExternalFilesDir(null).toString() + "/revokeperms")
        if (permFile.exists()) {
            val reader = BufferedReader(FileReader(permFile))
            val lines = ArrayList(reader.readLines())
            packageRevoked = lines[0]
            lines.removeAt(0)
            for (line in lines)
                permissionsToRevoke.add(line)
            Log.e("TTTt", permissionsToRevoke.toString())
            return true
        }
        return false
    }

    private var isOnMainScreen = true
    private var isOnPermissionListScreen = false
    private var isOnPermissionScreen = false

    private fun revokePermissions(event: AccessibilityEvent) {
        try {
            if (isOnMainScreen) {
                Log.e("OK", "WHY THO")
                val checkItem = event.source.findAccessibilityNodeInfosByText("Permissions")
                if (checkItem.size > 0) {
                    val item = checkItem[0].parent
                    item.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    isOnMainScreen = false
                    isOnPermissionListScreen = true
                    //return
                }
            }
            else if (isOnPermissionListScreen) {
                if (permissionsToRevoke.size > 0) {
                    Log.e("OK", "WEIRd")
                    val checkItem = event.source.findAccessibilityNodeInfosByText(permissionsToRevoke[0])
                    if (checkItem.size > 0) {
                        Log.e("OK", "CLICK")
                        val item = checkItem[0].parent
                        Log.e("ITEM", checkItem[0].parent.toString())
                        item.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        isOnPermissionListScreen = false
                        isOnPermissionScreen = true
                        logManager.addLog(2, packageRevoked, pkgManager.checkGroupNumber(permissionsToRevoke[0]))
                        permissionsToRevoke.removeAt(0)
                    }
                }
                else {
                    // all permissions revoked
                    val permFile = File(applicationContext.getExternalFilesDir(null).toString() + "/revokeperms")
                    permFile.delete()
                    performGlobalAction(GLOBAL_ACTION_BACK)
                    isPermissionBeingRevoked = false
                    isOnMainScreen = true
                    shouldExit = true
                    revoketemp = ""
                }
            }
            else if (isOnPermissionScreen) {
                Log.e("OK", "OK")
                val checkItem = event.source.findAccessibilityNodeInfosByText("Deny")
                if (checkItem.size > 0) {
                    Log.e("DENY", checkItem.size.toString())
                    val item = checkItem[0]
                    item.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    performGlobalAction(GLOBAL_ACTION_BACK)
                    isOnPermissionListScreen = true
                    isOnPermissionScreen = false
                }

            }
        }
        catch (e: Exception) {
            Log.e("ssss", e.toString())
        }
    }

    private fun checkPerms() {
        //Log.e("called", "called")
        Handler().postDelayed({
            try {
                val pp = pkgManager.getGrantedPermissions(packageInContext)
                if (pp != grantedPermissions) {
                    val diff = pp.subtract(grantedPermissions).toList()
                    if (diff.isNotEmpty()) {
                        val item = diff[0]
                        val permGroup = pkgManager.checkPermGroup(item)
                        Log.e("diff", pp.subtract(grantedPermissions).toString())

                        createNotification(permGroup)
                        logManager.addLog(1, packageInContext, permGroup)
                        grantedPermissions = pp
                    }
                }
            } catch (e: Exception) {
                Log.e("Error", e.toString())
            }
            checkPerms()
        }, 500)
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
            .setContentTitle(pkgManager.getPermGroupName(permission) + " Granted")
            .setContentText(pkgManager.getPermGroupName(permission))
            .setStyle(NotificationCompat.BigTextStyle().bigText(pkgManager.getPackageInfo(packageInContext).applicationInfo.loadLabel(applicationContext.packageManager).toString() + " was granted " + pkgManager.getPermGroupName(permission)))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setWhen(System.currentTimeMillis())

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(permission, builder.build())
        }
    }

    private fun notify(title: String, text: String) {
        val builder = NotificationCompat.Builder(this, notificationChannelId)
            .setSmallIcon(R.drawable.ic_perm_body_sensors)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setWhen(System.currentTimeMillis())

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }
    }

}