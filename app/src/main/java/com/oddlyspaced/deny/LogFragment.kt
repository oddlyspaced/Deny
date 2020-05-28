package com.oddlyspaced.deny

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        val logManager = LogManager(context!!)
        val adapter = LogAdapter(logManager.readLog(), context!!, AdapterItemClick())
        rvLog.layoutManager = LinearLayoutManager(context)
        rvLog.setHasFixedSize(true)
        rvLog.setItemViewCacheSize(15)
        rvLog.adapter = adapter
    }

    class AdapterItemClick: LogAdapter.ItemClick {
        override fun onClick(ctx: Context) {
            Toast.makeText(ctx, "o0o0o0", Toast.LENGTH_LONG).show()
        }
    }
}