package com.oddlyspaced.deny.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class LogAdapter(private val fragmentManager: FragmentManager, private val list: ArrayList<LogItem>, private val click: LogItemClick): RecyclerView.Adapter<LogAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pkgManager = PackageListManager(context)
        val item = list[position]

        if (item.status == 0) {
            holder.logDate.text = item.time
            holder.logDate.visibility = View.VISIBLE
            holder.container.visibility = View.GONE
            return
        }

        when (item.status) {
            1 -> holder.status.setBackgroundColor(context.getColor(R.color.colorGreen)) // granted
            2 -> holder.status.setBackgroundColor(context.getColor(R.color.colorRed)) // revoked
        }

        holder.iconApp.setImageDrawable(item.icon)

        var logText = when (item.status) {
            1 -> "Granted:"
            2 -> "Revoked:"
            else -> ""
        }
        logText = when (item.permission) {
            1 -> "$logText Body sensors"
            2 -> "$logText Calendar"
            3 -> "$logText Call logs"
            4 -> "$logText Camera"
            5 -> "$logText Contacts"
            6 -> "$logText Location"
            7 -> "$logText Microphone"
            8 -> "$logText Physical activity"
            9 -> "$logText SMS"
            10 -> "$logText Storage"
            11 -> "$logText Telephone"
            else -> ""
        }
        logText = if (item.status == 2)
            "$logText from ${pkgManager.getPackageInfo(item.packageName).applicationInfo.loadLabel(context.packageManager)}"
        else
            "$logText to ${pkgManager.getPackageInfo(item.packageName).applicationInfo.loadLabel(context.packageManager)}"

        holder.logText.text = logText

        when(item.permission) {
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

        holder.touch.setOnClickListener {
            click.onClick(fragmentManager, item.packageName)
        }

        holder.permTime.text = item.time

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val status: View = itemView.findViewById(R.id.viewPermStatus)
        val iconApp: ImageView = itemView.findViewById(R.id.imgPackageIcon)
        val logText: TextView = itemView.findViewById(R.id.txStatus)
        val permTime: TextView = itemView.findViewById(R.id.txPermissionTime)
        val imgPerm: ImageView = itemView.findViewById(R.id.imgPermission)
        val logDate: TextView = itemView.findViewById(R.id.txLogDate)
        val container: ConstraintLayout = itemView.findViewById(R.id.consPermission)
        val touch: View = itemView.findViewById(R.id.viewLogTouch)
    }

}