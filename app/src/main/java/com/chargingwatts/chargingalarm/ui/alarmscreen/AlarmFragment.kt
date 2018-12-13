package com.chargingwatts.chargingalarm.ui.alarmscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chargingwatts.chargingalarm.BaseFragment
import com.chargingwatts.chargingalarm.R


class AlarmFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = getActivity()!!.getLayoutInflater().inflate(R.layout.fragment_alarm, container, false)


        return view
    }


    companion object {

        private val PAGE = "page"

        fun newInstance(): AlarmFragment {
            val frag = AlarmFragment()
            return frag
        }
    }

}