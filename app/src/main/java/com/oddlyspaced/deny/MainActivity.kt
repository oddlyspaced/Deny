package com.oddlyspaced.deny

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getPackageList()
    }

    private fun getPackageList() {
        val packageManage = packageManager.getInstalledPackages(0)
        Log.e("packages", packageManage.toString())

        val perms = packageManager.getPackageInfo("com.instagram.android", PackageManager.GET_PERMISSIONS)
        for (perm in perms.requestedPermissions) {
            Log.e("eeee", perm.toString())
        }
    }
}
