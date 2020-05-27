package com.oddlyspaced.deny

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log

class PackageListManager(val context: Context) {

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

}