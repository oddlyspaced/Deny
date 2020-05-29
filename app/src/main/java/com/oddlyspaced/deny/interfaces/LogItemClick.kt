package com.oddlyspaced.deny.interfaces

import android.content.Context
import androidx.fragment.app.FragmentManager

interface LogItemClick {

    fun onClick(fragmentManager: FragmentManager, packageName: String)

}