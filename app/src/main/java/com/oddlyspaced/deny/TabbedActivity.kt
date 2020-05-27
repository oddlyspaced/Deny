package com.oddlyspaced.deny

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_tabbed.*

class TabbedActivity : AppCompatActivity() {

    private val tabNames = arrayOf("Activity", "Manage", "Settings", "Help")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabbed)

        // layouts should be
        // log
        // manage apps
        // settings
        // help
        // maybe ~ about
        val adapter = TabbedAdapter(this, 4)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            //To get the first name of doppelganger celebrities
            tab.text = tabNames[position]
        }.attach()
    }
}
