package com.oddlyspaced.deny.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oddlyspaced.deny.R

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

    }
}