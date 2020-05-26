package com.oddlyspaced.deny

import android.content.pm.PackageManager
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val animations = arrayOf(R.drawable.avd_body_calendar, R.drawable.avd_calendar_call, R.drawable.avd_call_camera, R.drawable.avd_camera_contact, R.drawable.avd_contact_location, R.drawable.avd_location_mic, R.drawable.avd_mic_activity, R.drawable.avd_activity_sms, R.drawable.avd_sms_storage, R.drawable.avd_storage_telephone, R.drawable.avd_telephone_body)
    private var animCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //getPackageList()
        animate()
    }

    private fun animate() {
        Handler().postDelayed({
            if (animCounter == animations.size)
                animCounter = 0
            val avd = AnimatedVectorDrawableCompat.create(this, animations[animCounter])
            animCounter++
            imgAnimMain.setImageDrawable(avd)
            val animatable = imgAnimMain.drawable as Animatable
            animatable.start()
            animate()
        }, 1000)
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
