package com.chargingwatts.chargingalarm.util.ui

import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast
import com.chargingwatts.chargingalarm.ChargingAlarmApp
import javax.inject.Inject


class UIHelper @Inject constructor(private val application: ChargingAlarmApp) {

    val applicationContext = application.applicationContext

    //--------------------------TOAST HELPER FUNCTIONS -------------------------------------------//

    fun showToast(msgResId: Int, duration: Int = Toast.LENGTH_SHORT) =
            Toast.makeText(applicationContext, applicationContext.getText(msgResId), duration).show()


    //--------------------------SNACKBAR HELPER FUNCTIONS ----------------------------------------//

    fun showSnackbar(rootView: View, msgResId: Int, duration: Int = Snackbar.LENGTH_SHORT) =
            Snackbar.make(rootView, msgResId, duration).show()

    fun showSnackbar(
            rootView: View, msgResId: Int, duration: Int = Snackbar.LENGTH_SHORT,
            btnResId: Int, listener: (View) -> Unit
    ) =
            Snackbar.make(rootView, msgResId, duration).setAction(btnResId, listener).show()

    //-------------------------PIXEL - DP CONVERSIONS --------------------------------------------//
    fun convertDpToPixel(dp: Float): Int {
        val resources = applicationContext.resources
        val metrics = resources.displayMetrics
        return (dp * (metrics.densityDpi / 160f)).toInt()
    }

    fun convertPixelToDp(pixel: Float): Int {
        val resources = applicationContext.resources
        val metrics = resources.displayMetrics
        return ((pixel / (metrics.densityDpi / 160f)).toInt())
    }

}