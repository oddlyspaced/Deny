package com.oddlyspaced.deny.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log

class PackageListManager(private val context: Context) {

    private val permBodySensor = arrayListOf("android.permission.BODY_SENSORS")
    private val permCalendar = arrayListOf("android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR")
    private val permCallLogs = arrayListOf("android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG", "android.permission.PROCESS_OUTGOING_CALLS")
    private val permCamera = arrayListOf("android.permission.CAMERA")
    private val permContacts = arrayListOf("android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.GET_ACCOUNTS")
    private val permLocation = arrayListOf("android.permission.ACCESS_BACKGROUND_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS")
    private val permMic = arrayListOf("android.permission.RECORD_AUDIO")
    private val permActivityRecognition = arrayListOf("android.permission.ACTIVITY_RECOGNITION")
    private val permSMS = arrayListOf("android.permission.READ_SMS", "android.permission.RECEIVE_SMS", "android.permission.SEND_SMS", "android.permission.RECEIVE_MMS", "android.permission.RECEIVE_WAP_PUSH")
    private val permStorage = arrayListOf("android.permission.ACCESS_MEDIA_LOCATION", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")
    private val permTelephone = arrayListOf("android.permission.ANSWER_PHONE_CALLS", "android.permission.CALL_PHONE", "android.permission.READ_PHONE_STATE", "android.permission.USE_SIP")
    private val all = permBodySensor + permCalendar + permCallLogs + permCamera + permContacts + permLocation + permMic + permActivityRecognition + permSMS + permStorage + permTelephone
    private val groups = arrayListOf("body sensor", "calendar", "call logs", "camera", "contacts", "location", "microphone", "physical activity", "sms", "storage", "telephone")

    fun getPackageList(): ArrayList<PackageInfo> {
        return ArrayList(context.packageManager.getInstalledPackages(0))
    }

    fun getPackageInfo(packageName: String): PackageInfo {
        for (pkg in getPackageList()) {
            if (pkg.packageName == packageName)
                return pkg
        }
        return PackageInfo()
    }

    fun getPermissions(packageName: String): ArrayList<String> {
        val perms = (context.packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS))
        if (perms.requestedPermissions == null) {
            Log.e("null", packageName)
            return ArrayList()
        }
        val allPermissions = perms.requestedPermissions.toList()
        val remainingPermissions = allPermissions.subtract(all)
        return ArrayList(allPermissions.subtract(remainingPermissions).toList())
    }

    fun getGrantedPermissions(packageName: String): ArrayList<String> {
        val perms = (context.packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS))
        val listPermission = getPermissions(packageName)
        val grantedPermissions = ArrayList<String>()
        for (perm in listPermission) {
            if ((perms.requestedPermissionsFlags[perms.requestedPermissions.indexOf(perm)] and PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                grantedPermissions.add(perm)
            }
        }
        return grantedPermissions
    }

    fun getGrantedGroups(packageName: String): ArrayList<String> {
        val perms = getGrantedPermissions(packageName)
        val listPermissions = ArrayList<String>()
        for (perm in perms) {
            val grp = getPermGroupName(checkPermGroup(perm))
            if (!listPermissions.contains(grp))
                listPermissions.add(grp)
        }
        return listPermissions
    }

    fun checkGroupNumber(group: String): Int {
        return groups.indexOf(group.toLowerCase())+1
    }

    fun checkPermGroup(permission: String): Int {
        return when {
            permBodySensor.contains(permission) -> GROUP_BODY_SENSORS
            permCalendar.contains(permission) -> GROUP_CALENDAR
            permCallLogs.contains(permission) -> GROUP_CALL_LOGS
            permCamera.contains(permission) -> GROUP_CAMERA
            permContacts.contains(permission) -> GROUP_CONTACTS
            permLocation.contains(permission) -> GROUP_LOCATION
            permMic.contains(permission) -> GROUP_MICROPHONE
            permActivityRecognition.contains(permission) -> GROUP_PHYSICAL_ACTIVITY
            permSMS.contains(permission) -> GROUP_SMS
            permStorage.contains(permission) -> GROUP_STORAGE
            permTelephone.contains(permission) -> GROUP_TELEPHONE
            else -> -1
        }
    }

    fun getPermGroupName(permission: Int): String {
        return when(permission) {
            GROUP_BODY_SENSORS -> "Body Sensors"
            GROUP_CALENDAR -> "Calendar"
            GROUP_CALL_LOGS -> "Call Logs"
            GROUP_CAMERA -> "Camera"
            GROUP_CONTACTS -> "Contacts"
            GROUP_LOCATION -> "Location"
            GROUP_MICROPHONE -> "Microphone"
            GROUP_PHYSICAL_ACTIVITY -> "Physical Activity"
            GROUP_SMS -> "SMS"
            GROUP_STORAGE -> "Storage"
            GROUP_TELEPHONE -> "Telephone"
            else -> ""
        }
    }

    companion object Constants {
        const val GROUP_BODY_SENSORS = 1
        const val GROUP_CALENDAR = 2
        const val GROUP_CALL_LOGS = 3
        const val GROUP_CAMERA = 4
        const val GROUP_CONTACTS = 5
        const val GROUP_LOCATION = 6
        const val GROUP_MICROPHONE = 7
        const val GROUP_PHYSICAL_ACTIVITY = 8
        const val GROUP_SMS = 9
        const val GROUP_STORAGE = 10
        const val GROUP_TELEPHONE = 11
    }

}