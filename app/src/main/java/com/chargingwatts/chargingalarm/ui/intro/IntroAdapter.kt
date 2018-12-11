package com.chargingwatts.chargingalarm.ui.intro

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class IntroAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return IntroFragment.newInstance( position)
    }

    override fun getCount(): Int {
        return 3
    }

}