package com.chargingwatts.chargingalarm.util.ui

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class UIHelper @Inject constructor(private val context: Context) {

    val applicationContext = context.applicationContext

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