
package com.chargingwatts.chargingalarm.binding

import android.databinding.DataBindingComponent
import android.support.v4.app.Fragment
import com.chargingwatts.chargingalarm.binding.FragmentBindingAdapters

/**
 * A Data Binding Component implementation for fragments.
 */
class FragmentDataBindingComponent(fragment: Fragment) : DataBindingComponent {
    private val adapter = FragmentBindingAdapters(fragment)

    override fun getFragmentBindingAdapters() = adapter
}
