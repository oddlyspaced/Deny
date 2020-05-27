package com.oddlyspaced.deny

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class TabbedAdapter(activity: AppCompatActivity, val totalItems: Int): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return totalItems
    }

    override fun createFragment(position: Int): Fragment {
        if (position == 0)
            return LogFragment.getInstance(position)
        return ItemFragment.getInstance(position)
    }


}