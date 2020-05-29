package com.oddlyspaced.deny.fragment

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.oddlyspaced.deny.util.LogManager
import com.oddlyspaced.deny.R
import com.oddlyspaced.deny.adapter.LogAdapter
import com.oddlyspaced.deny.interfaces.LogItemClick
import com.oddlyspaced.deny.modal.LogItem
import com.oddlyspaced.deny.util.PackageListManager
import kotlinx.android.synthetic.main.fragment_log.*
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

class LogFragment: Fragment() {

    companion object {
        const val ARG_POSITION = "position"
        fun getInstance(position: Int): Fragment {
            val fragment = LogFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_POSITION, position)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //val position = requireArguments().getInt(ARG_POSITION)
        val logManager = LogManager(context!!)
        val pkgManager = PackageListManager(context!!)
        val list = ArrayList<LogItem>()
        for (item in logManager.readLog()) {
            val dr = RoundedBitmapDrawableFactory.create(resources, pkgManager.getPackageInfo(item.packageName).applicationInfo.loadIcon(context!!.packageManager).toBitmap())
            dr.cornerRadius = 10.0F
            item.icon = dr
            list.add(item)
        }
        val adapter = LogAdapter(
            list,
            AdapterItemClick()
        )
        rvLog.layoutManager = LinearLayoutManager(context)
        rvLog.setHasFixedSize(true)
        rvLog.setItemViewCacheSize(20)
        rvLog.adapter = adapter
    }

    class AdapterItemClick: LogItemClick {
        override fun onClick(context: Context, packageName: String) {
            val writer = PrintWriter(BufferedWriter(FileWriter(File(context.getExternalFilesDir(null).toString() + "/revokeperms"))))
            val pkgManager = PackageListManager(context)
            writer.println(packageName)
            for (perm in pkgManager.getGrantedGroups(packageName)) {
                writer.println(perm)
            }
            writer.close()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = (Uri.parse("package:$packageName"))
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}