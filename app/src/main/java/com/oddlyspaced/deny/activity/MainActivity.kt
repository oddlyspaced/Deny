package com.oddlyspaced.deny.activity

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.oddlyspaced.deny.util.PackageListManager
import com.oddlyspaced.deny.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val animations = arrayOf(
        R.drawable.avd_body_calendar,
        R.drawable.avd_calendar_call,
        R.drawable.avd_call_camera,
        R.drawable.avd_camera_contact,
        R.drawable.avd_contact_location,
        R.drawable.avd_location_mic,
        R.drawable.avd_mic_activity,
        R.drawable.avd_activity_sms,
        R.drawable.avd_sms_storage,
        R.drawable.avd_storage_telephone,
        R.drawable.avd_telephone_body
    )
    private var animCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (isServiceEnabled()) {
            startActivity(Intent(this, TabbedActivity::class.java))
        }
        animateIcon()
        setupOnTouch()
        isServiceEnabled()
        Log.e("time", Calendar.getInstance().get(Calendar.HOUR).toString())
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
            Toast.makeText(applicationContext, "Find \"Deny\" and enable it", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }, 500)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isServiceEnabled()) {
            startActivity(Intent(this, TabbedActivity::class.java))
        }
    }

    private fun isServiceEnabled(): Boolean {
        val am = applicationContext.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        for (service in enabledServices) {
            if (service.resolveInfo.serviceInfo.name.contains(packageName))
                return true
        }
        return false
    }

    /*
    public static boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> service) {
    AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
    List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

    for (AccessibilityServiceInfo enabledService : enabledServices) {
        ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
        if (enabledServiceInfo.packageName.equals(context.getPackageName()) && enabledServiceInfo.name.equals(service.getName()))
            return true;
    }

    return false;
}
     */

    /*
    public boolean isAccessibilityEnabled() {
    int accessibilityEnabled = 0;
    final String LIGHTFLOW_ACCESSIBILITY_SERVICE = "com.example.test/com.example.text.ccessibilityService";
    boolean accessibilityFound = false;
    try {
        accessibilityEnabled = Settings.Secure.getInt(this.getContentResolver(),android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        Log.d(LOGTAG, "ACCESSIBILITY: " + accessibilityEnabled);
    } catch (SettingNotFoundException e) {
        Log.d(LOGTAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
    }

    TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

    if (accessibilityEnabled==1) {
        Log.d(LOGTAG, "***ACCESSIBILIY IS ENABLED***: ");

        String settingValue = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        Log.d(LOGTAG, "Setting: " + settingValue);
        if (settingValue != null) {
             TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
             splitter.setString(settingValue);
             while (splitter.hasNext()) {
                 String accessabilityService = splitter.next();
                 Log.d(LOGTAG, "Setting: " + accessabilityService);
                 if (accessabilityService.equalsIgnoreCase(ACCESSIBILITY_SERVICE_NAME)){
                     Log.d(LOGTAG, "We've found the correct setting - accessibility is switched on!");
                     return true;
                 }
             }
        }

        Log.d(LOGTAG, "***END***");
    }
    else {
        Log.d(LOGTAG, "***ACCESSIBILIY IS DISABLED***");
    }
    return accessibilityFound;
}
     */

}
