package com.oddlyspaced.deny.modal

import android.graphics.drawable.Drawable

class LogItem(val status: Int, val packageName: String, val time: String, val permission: Int) {

    lateinit var icon: Drawable

}