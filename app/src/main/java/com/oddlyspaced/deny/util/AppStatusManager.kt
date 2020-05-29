package com.oddlyspaced.deny.util

import android.content.Context
import com.oddlyspaced.deny.modal.AppStatusItem
import com.oddlyspaced.deny.modal.PermissionItem
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class AppStatusManager(private val context: Context) {

    fun save(packageName: String, item: AppStatusItem) {
        val writer = PrintWriter(BufferedWriter(FileWriter(File(context.getExternalFilesDir(null).toString() + "/$packageName"))))
        writer.println("${if(item.autoGrant) 1 else 0};${if(item.autoRevoke) 1 else 0}")
        for (perm in item.permissions) {
            writer.println("${perm.permNum};${if(perm.granted) 1 else 0}")
        }
        writer.close()
    }

    fun read(packageName: String): AppStatusItem {
        val file = File(context.getExternalFilesDir(null).toString() + "/$packageName")
        if (file.exists()) {
            val perms = ArrayList<PermissionItem>()
            val reader = BufferedReader(FileReader(File(context.getExternalFilesDir(null).toString() + "/$packageName")))
            var tokens = StringTokenizer(reader.readLine(), ";")
            val grant = tokens.nextToken().toInt() == 1
            val revoke = tokens.nextToken().toInt() == 1
            val pkgManager = PackageListManager(context)
            while (true) {
                val line = reader.readLine() ?: break
                tokens = StringTokenizer(line, ";")
                val pp = tokens.nextToken().toInt()
                perms.add(PermissionItem(pkgManager.getPermGroupName(pp), pp, tokens.nextToken().toInt() == 1))
            }
            return AppStatusItem(grant, revoke, perms)
        }
        else {
            return AppStatusItem(false, false, ArrayList())
        }
    }

}