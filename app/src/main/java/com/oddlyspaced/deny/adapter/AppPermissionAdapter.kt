package com.oddlyspaced.deny.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.oddlyspaced.deny.modal.LogItem
import com.oddlyspaced.deny.util.PackageListManager
import com.oddlyspaced.deny.R
import com.oddlyspaced.deny.interfaces.LogItemClick
import com.oddlyspaced.deny.modal.PermissionItem
import com.oddlyspaced.deny.util.AppStatusManager

class AppPermissionAdapter(val list: ArrayList<PermissionItem>): RecyclerView.Adapter<AppPermissionAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var appStatusManager: AppStatusManager
    var packageName = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        appStatusManager = AppStatusManager(context)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_permission, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.txPerm.text = item.perm
        when(item.permNum) {
            1 -> holder.imgPerm.setImageDrawable(context.getDrawable(R.drawable.ic_perm_body_sensors))
            2 -> holder.imgPerm.setImageDrawable(context.getDrawable(R.drawable.ic_perm_calendar))
            3 -> holder.imgPerm.setImageDrawable(context.getDrawable(R.drawable.ic_perm_call_logs))
            4 -> holder.imgPerm.setImageDrawable(context.getDrawable(R.drawable.ic_perm_camera))
            5 -> holder.imgPerm.setImageDrawable(context.getDrawable(R.drawable.ic_perm_contacts))
            6 -> holder.imgPerm.setImageDrawable(context.getDrawable(R.drawable.ic_perm_location))
            7 -> holder.imgPerm.setImageDrawable(context.getDrawable(R.drawable.ic_perm_mic))
            8 -> holder.imgPerm.setImageDrawable(context.getDrawable(R.drawable.ic_perm_physical_activity))
            9 -> holder.imgPerm.setImageDrawable(context.getDrawable(R.drawable.ic_perm_sms))
            10 -> holder.imgPerm.setImageDrawable(context.getDrawable(R.drawable.ic_perm_storage))
            11 -> holder.imgPerm.setImageDrawable(context.getDrawable(R.drawable.ic_perm_telephone))
        }
        holder.cbPerm.isChecked = item.granted
        holder.cbPerm.setOnCheckedChangeListener { _: CompoundButton, checked: Boolean ->
            list[position] = PermissionItem(item.perm, item.permNum, checked)
            val appItem = appStatusManager.read(packageName)
            appItem.permissions = list
            appStatusManager.save(packageName, appItem)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txPerm: TextView = itemView.findViewById(R.id.txPermText)
        val imgPerm: ImageView = itemView.findViewById(R.id.imgPermIcon)
        val cbPerm: CheckBox = itemView.findViewById(R.id.cbPerm)
    }

}