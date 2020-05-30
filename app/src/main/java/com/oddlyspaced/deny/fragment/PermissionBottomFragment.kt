package com.oddlyspaced.deny.fragment

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oddlyspaced.deny.R
import com.oddlyspaced.deny.adapter.AppPermissionAdapter
import com.oddlyspaced.deny.modal.PermissionItem
import com.oddlyspaced.deny.util.PackageListManager
import kotlinx.android.synthetic.main.fragment_bottom_permission.*
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

class PermissionBottomFragment: BottomSheetDialogFragment() {

    fun newInstance(): PermissionBottomFragment {
        return PermissionBottomFragment()
    }

    private var packageName: String = ""
    private lateinit var pkgManager: PackageListManager

    // we use tag as package name
    override fun show(manager: FragmentManager, pkg: String?) {
        packageName = pkg!!
        super.show(manager, packageName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_permission, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pkgManager = PackageListManager(context!!)

        viewRevokeAll.setOnClickListener{
            val writer = PrintWriter(BufferedWriter(FileWriter(File(context!!.getExternalFilesDir(null).toString() + "/revokeperms"))))
            val pkgManager = PackageListManager(context!!)
            writer.println(packageName)
            for (perm in pkgManager.getGrantedGroups(packageName)) {
                writer.println(perm)
            }
            writer.close()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = (Uri.parse("package:$packageName"))
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            context!!.startActivity(intent)
        }

        txPermissionAppName.text = pkgManager.getPackageInfo(packageName).applicationInfo.loadLabel(context!!.packageManager).toString().toUpperCase()
        val dr = RoundedBitmapDrawableFactory.create(context!!.resources, pkgManager.getPackageInfo(packageName).applicationInfo.loadIcon(context!!.packageManager).toBitmap())
        dr.cornerRadius = 10.0F
        imgPermissionIcon.setImageDrawable(dr)
        pkgManager = PackageListManager(context!!)
        val list = ArrayList<PermissionItem>()
        ///////
        val listAll = pkgManager.getGroups(packageName)
        val listGranted = pkgManager.getGrantedGroups(packageName)
        val listDenied = ArrayList(listAll.subtract(listGranted))

        for (item in listDenied) {
            list.add(PermissionItem(item, pkgManager.checkGroupNumber(item), false))
        }

        for (item in listGranted) {
            list.add(PermissionItem(item, pkgManager.checkGroupNumber(item), true))
        }
        rvPermissions.setHasFixedSize(true)
        rvPermissions.layoutManager = LinearLayoutManager(context)
        rvPermissions.adapter = AppPermissionAdapter(list)
    }
}