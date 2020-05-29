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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.oddlyspaced.deny.util.LogManager
import com.oddlyspaced.deny.R
import com.oddlyspaced.deny.adapter.LogAdapter
import com.oddlyspaced.deny.interfaces.LogItemClick
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
        val adapter = LogAdapter(
            logManager.readLog(),
            context!!,
            AdapterItemClick()
        )
        rvLog.layoutManager = LinearLayoutManager(context)
        rvLog.setHasFixedSize(true)
        rvLog.setItemViewCacheSize(15)
        rvLog.adapter = adapter
    }

    class AdapterItemClick: LogItemClick {
        override fun onClick(context: Context, packageName: String) {
            val writer = PrintWriter(BufferedWriter(FileWriter(File(context.getExternalFilesDir(null).toString() + "/revokeperms"))))
            val pkgManager = PackageListManager(context)
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