package com.oddlyspaced.deny.util

import android.content.Context
import com.oddlyspaced.deny.modal.LogItem
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class LogManager(private val context: Context) {

    fun addLog(status: Int, packageName: String, permission: Int) {
        // status 1 for granted, 0 for revoked
        val writer = PrintWriter(BufferedWriter(FileWriter(File(context.getExternalFilesDir(null).toString() + "/activitylog"), true)))
        // time should be hour:minutes ampm
        // date should be day:month
        val calendar = Calendar.getInstance()

        val time = "${String.format("%02d", calendar.get(Calendar.HOUR))}:${String.format("%02d", calendar.get(Calendar.MINUTE))} ${when(calendar.get(Calendar.AM_PM)) {
            0 -> "AM"
            1 -> "PM"
            else -> "" 
        }}"
        val date = "${calendar.get(Calendar.DAY_OF_MONTH)} ${calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH)}"
        writer.println("$status;$packageName;$time;$permission;$date")
        writer.close()
    }

    fun readLog(): ArrayList<LogItem> {
        val list = ArrayList<LogItem>()
        val reader = BufferedReader(FileReader(File(context.getExternalFilesDir(null).toString() + "/activitylog")))
        for (line in reader.readLines()) {
            val tokenizer = StringTokenizer(line, ";")
            list.add(
                LogItem(
                    tokenizer.nextToken().toInt(),
                    tokenizer.nextToken(),
                    tokenizer.nextToken(),
                    tokenizer.nextToken().toInt()
                )
            )
        }
        reader.close()
        list.reverse()
        return list
    }


}