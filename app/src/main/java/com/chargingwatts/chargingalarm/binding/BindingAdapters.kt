package com.chargingwatts.chargingalarm.binding

import android.view.View
import androidx.databinding.BindingAdapter
import com.chargingwatts.chargingalarm.ui.circleprogress.CircleProgressView

/**
 * Data Binding adapters specific to the app.
 */
object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("cpv_value")
    fun bindBatteryLevel(circleProgressView: CircleProgressView, value:Float){
        circleProgressView.setValue(value)
    }
}
