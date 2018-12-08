@file:JvmName("AppConstants")


import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

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

    //--------------------------------LOG CONSTANTS-----------------------------------------------//
    @JvmField
    val LOG_CHARGING_ALARM = "LOG_CHARGING_ALARM"

    //---------------------------SHARED PREFERENCE CONSTANTS--------------------------------------//

    @JvmField
    val USER_ALARM_PREFERENCE = "user_alarm_preference_pref"

    @JvmField
    val IS_CHARGING_PREFERENCE = "is_charging_preference_pref"

    @JvmField
    val BATTERY_HIGH_LEVEL_PREF = "battery_high_level_pref"

    @JvmField
    val BATTERY_LOW_LEVEL_PREF = "battery_low_level_pref"

    @JvmField
    val BATTERY_HIGH_TEMPERATURE_PREF = "battery_high_Temperature_pref"

    @JvmField
    val IS_VIBRATION_ENABLED_PREF = "is_vibration_enabled_pref"

    @JvmField
    val IS_SOUND_ENABLED_PREF = "is_sound_enabled_pref"

    @JvmField
    val RING_ON_SILENT_MODE_PREF = "ring_on_silent_mode_pref"

    @JvmField
    val VIBRATE_ON_SILENT_MODE_PREF = "vibrate_on_silent_mode_pref"

    @JvmField
    val ALARM_VOLUME_PREF = "alarm_volume_pref"


    //---------------------------SETTINGS FRAGMENT CONSTANTS--------------------------------------//


    @JvmField
    val KEY_BATTERY_THRESHOLD_PREF_CATEGORY = "key_threshold_pref_category"


    @JvmField
    val KEY_SILENT_MODE_PREF_CATEGORY = "key_silent_mode_pref_category"

    @JvmField
    val KEY_VOLUME_SETTING_PREF_CATEGORY = "key_volume_setting_pref_category"
}