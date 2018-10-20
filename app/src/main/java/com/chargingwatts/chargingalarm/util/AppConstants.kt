@file:JvmName("AppConstants")


import android.support.design.widget.Snackbar
import android.widget.Toast

object AppConstants {
    //--------------------------SNACKBAR CONSTANTS------------------------------------------------//
    @JvmField
    val SNACKBAR_DURATION_SHORT = Snackbar.LENGTH_SHORT
    @JvmField
    val SNACKBAR_DURATION_LONG = Snackbar.LENGTH_LONG
    @JvmField
    val SNACKBAR_DURATION_INDEFINITE = Snackbar.LENGTH_INDEFINITE


    //--------------------------TOAST CONSTANTS---------------------------------------------------//
    @JvmField
    val TOAST_DURATION_SHORT = Toast.LENGTH_SHORT
    @JvmField
    val TOAST_DURATION_LONG = Toast.LENGTH_LONG


    //--------------------------PERMISSION CONSTANTS----------------------------------------------//
    @JvmField
    val PERMISSION_CAMERA_REQUEST_CODE: Int = 2

    //--------------------------START ACTIVITY FOR INTENT REQUEST CODES---------------------------//
    @JvmField
    val START_SETTINGS_ACTIVITY_REQUEST_CODE: Int = 101

    //--------------------------------LOG CONSTANTS----------------------------------------------//
    @JvmField
    val LOG_CHARGING_ALARM = "LOG_CHARGING_ALARM"



}