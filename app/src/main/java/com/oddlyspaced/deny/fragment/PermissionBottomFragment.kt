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
import kotlinx.android.synthetic.main.fragment_bottom_permission.*

class PermissionBottomFragment: BottomSheetDialogFragment() {

    fun newInstance(): PermissionBottomFragment {
        return PermissionBottomFragment()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_permission, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val list = ArrayList<PermissionItem>()
        list.add(PermissionItem("Body Activity", 1))
        list.add(PermissionItem("Body Activity", 1))
        list.add(PermissionItem("Body Activity", 1))
        list.add(PermissionItem("Body Activity", 1))
        list.add(PermissionItem("Body Activity", 1))
        list.add(PermissionItem("Body Activity", 1))
        rvPermissions.setHasFixedSize(true)
        rvPermissions.layoutManager = LinearLayoutManager(context)
        rvPermissions.adapter = AppPermissionAdapter(list)
    }
}