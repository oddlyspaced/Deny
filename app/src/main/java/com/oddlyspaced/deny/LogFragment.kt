package com.oddlyspaced.deny

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_log.*

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
        val list = ArrayList<LogItem>()
        list.add(LogItem(0, "", "Today", 0))
        list.add(LogItem(1, "com.instagram.android", "12:03 AM", 9))
        list.add(LogItem(2, "com.whatsapp", "12:34 AM", 10))
        list.add(LogItem(3, "com.spotify.music", "6:54: PM", 2))
        val adapter = LogAdapter(list, context!!)
        rvLog.layoutManager = LinearLayoutManager(context)
        rvLog.adapter = adapter
    }
}