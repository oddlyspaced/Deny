package com.oddlyspaced.deny.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.oddlyspaced.deny.fragment.ItemFragment
import com.oddlyspaced.deny.fragment.LogFragment


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