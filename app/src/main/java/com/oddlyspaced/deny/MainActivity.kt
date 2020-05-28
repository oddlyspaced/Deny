package com.oddlyspaced.deny

import android.content.Intent
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val animations = arrayOf(R.drawable.avd_body_calendar, R.drawable.avd_calendar_call, R.drawable.avd_call_camera, R.drawable.avd_camera_contact, R.drawable.avd_contact_location, R.drawable.avd_location_mic, R.drawable.avd_mic_activity, R.drawable.avd_activity_sms, R.drawable.avd_sms_storage, R.drawable.avd_storage_telephone, R.drawable.avd_telephone_body)
    private var animCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*var intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = (Uri.parse("package:com.oddlyspaced.deny"))
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)*/
        animateIcon()
        setupOnTouch()
        Log.e("time", Calendar.getInstance().get(Calendar.HOUR).toString())
        //getPackageList()
    }

    private fun animateIcon() {
        Handler().postDelayed({
            if (animCounter == animations.size)
                animCounter = 0
            val avd = AnimatedVectorDrawableCompat.create(this, animations[animCounter])
            animCounter++
            imgAnimMain.setImageDrawable(avd)
            val animatable = imgAnimMain.drawable as Animatable
            animatable.start()
            animateIcon()
        }, 1000)
    }

    private fun setupOnTouch() {
        viewAccessibiltyTouch.setOnClickListener {
            startActivity(Intent(this, TabbedActivity::class.java))
        }
    }

    private fun getPackageList() {

        val pkgManager = PackageListManager(this)

        pkgManager.getGrantedPermissions("com.instagram.android")

        //for (pkg in pkgManager.getPackageList())
          //  Log.e("pack", pkg.applicationInfo.loadLabel(packageManager).toString())


        //val perms = packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_PERMISSIONS)
        /*for (counter in 0 until perms.requestedPermissions.size) {
            if ((perms.requestedPermissionsFlags[counter] and PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                Log.e("perm", perms.requestedPermissions[counter])
            }
            //Log.e("eeee", perm.toString())
        }*/
        //Log.e("sss", packageManager.queryPermissionsByGroup("android.permission-group.ACTIVITY_RECOGNITION", 0).toString())
    }

}
