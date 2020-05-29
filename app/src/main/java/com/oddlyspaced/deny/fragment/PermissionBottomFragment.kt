package com.oddlyspaced.deny.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oddlyspaced.deny.R
import com.oddlyspaced.deny.adapter.AppPermissionAdapter
import com.oddlyspaced.deny.modal.PermissionItem
import com.oddlyspaced.deny.util.PackageListManager
import kotlinx.android.synthetic.main.fragment_bottom_permission.*

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
        val list = ArrayList<PermissionItem>()
        for (item in pkgManager.getGroups(packageName)) {
            list.add(PermissionItem(item, pkgManager.checkGroupNumber(item)))
        }
        rvPermissions.setHasFixedSize(true)
        rvPermissions.layoutManager = LinearLayoutManager(context)
        rvPermissions.adapter = AppPermissionAdapter(list)
    }
}