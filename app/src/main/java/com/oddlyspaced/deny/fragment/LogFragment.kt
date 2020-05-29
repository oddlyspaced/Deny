package com.oddlyspaced.deny.fragment

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlin.math.log

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

    private lateinit var asyncLoader: AsyncLoader

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //val position = requireArguments().getInt(ARG_POSITION)
        animateLoading()
        asyncLoader = AsyncLoader(context!!, rvLog, fragmentManager!!)
        asyncLoader.execute()
    }

    class AdapterItemClick: LogItemClick {
        //override fun onClick(fragmentManager: FragmentManager: Context, packageName: String) {
            /*val writer = PrintWriter(BufferedWriter(FileWriter(File(context.getExternalFilesDir(null).toString() + "/revokeperms"))))
            val pkgManager = PackageListManager(context)
            writer.println(packageName)
            for (perm in pkgManager.getGrantedGroups(packageName)) {
                writer.println(perm)
            }
            writer.close()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = (Uri.parse("package:$packageName"))
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)*/

            //frag.show(fragmentManager, "rrr")

            /*
            AddPhotoBottomDialogFragment addPhotoBottomDialogFragment =
        AddPhotoBottomDialogFragment.newInstance();
addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
        "add_photo_dialog_fragment");
             */
     //   }

        override fun onClick(fragmentManager: FragmentManager, packageName: String) {
            val frag = PermissionBottomFragment().newInstance()
            frag.show(fragmentManager, packageName)
        }
    }

    private fun animateLoading() {
        Handler().postDelayed({
            var tx = txLoading.text
            tx = if (tx.length == 13) {
                "Loading"
            } else {
                " $tx."
            }
            txLoading.text = tx
            if (asyncLoader.isLoading)
                animateLoading()
        }, 250)
    }


    class AsyncLoader(private val context: Context, private val recyclerView: RecyclerView, private val fragmentManager: FragmentManager): AsyncTask<Void, Void, Void>() {

        private lateinit var logManager: LogManager
        private lateinit var pkgManager: PackageListManager
        private lateinit var adapter: LogAdapter
        var isLoading = true

        override fun doInBackground(vararg params: Void?): Void? {
            val list = ArrayList<LogItem>()
            pkgManager = PackageListManager(context)
            logManager = LogManager(context)
            for (item in logManager.readLog()) {
                val dr = RoundedBitmapDrawableFactory.create(context.resources, pkgManager.getPackageInfo(item.packageName).applicationInfo.loadIcon(context!!.packageManager).toBitmap())
                dr.cornerRadius = 10.0F
                item.icon = dr
                list.add(item)
            }
            adapter = LogAdapter(
                fragmentManager,
                list,
                AdapterItemClick()
            )
            return null
        }

        override fun onPostExecute(result: Void?) {
            isLoading = false
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.setHasFixedSize(true)
            recyclerView.setItemViewCacheSize(300)
            recyclerView.adapter = adapter
        }
    }
}