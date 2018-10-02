package com.chargingwatts.chargingalarm.util.preference

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.chargingwatts.chargingalarm.ChargingAlarmApp
import javax.inject.Inject


class PreferenceHelper @Inject constructor(
        private val application: ChargingAlarmApp, private val appPreferenceName: String =
                "in.cashify.logistic"
) {

    private var preferences: SharedPreferences

    init {
        preferences = application.applicationContext.getSharedPreferences(
                appPreferenceName,
                Context.MODE_PRIVATE
        )
        Log.d("spef", "spef loaded")
    }

    fun getNewSharedPreference(preferenceName: String) =
            application.applicationContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)


    //--------------------------APP LEVEL SHARED PREFERENCES --------------------------------------//


    //=============================================String==========================================/

    fun getString(key: String, defaultValue: String) = preferences.getString(key, defaultValue)


    @SuppressLint("CommitPrefEdits")
    fun putString(key: String, value: String) {
        with(preferences.edit()) {
            putString(key, value)
            apply()
        }
    }
    //==END========================================String==========================================/


    //=============================================Boolean=========================================/

    fun getBoolean(key: String): Boolean = preferences.getBoolean(key, false)

    fun getBoolean(key: String, defaultValue: Boolean): Boolean = preferences.getBoolean(
            key,
            defaultValue
    )

    @SuppressLint("CommitPrefEdits")
    fun putBoolean(key: String, value: Boolean) {
        with(preferences.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    //==END========================================Boolean=========================================/


    //===============================================Int===========================================/

    fun getInt(key: String): Int = preferences.getInt(key, 0)

    fun getInt(key: String, defaultValue: Int): Int = preferences.getInt(key, defaultValue)

    @SuppressLint("CommitPrefEdits")
    fun putInt(key: String, value: Int) {
        with(preferences.edit()) {
            putInt(key, value)
            apply()
        }
    }

    //==END=========================================Int============================================/


    //============================================Long=============================================/

    fun getLong(key: String): Long = preferences.getLong(key, 0)

    fun getLong(key: String, defaultValue: Long): Long = preferences.getLong(key, defaultValue)

    @SuppressLint("CommitPrefEdits")
    fun putLong(key: String, value: Long) {
        with(preferences.edit()) {
            putLong(key, value)
            apply()
        }
    }

    //==END======================================Long============================================/


    //======================================Set<String>=============================================/

    fun getStringSet(key: String, defaultValue: Set<String>): Set<String> =
            preferences.getStringSet(key, defaultValue)


    @SuppressLint("CommitPrefEdits")
    fun putStringSet(key: String, value: MutableSet<String>) {
        with(preferences.edit()) {
            putStringSet(key, value)
            apply()
        }
    }
    //==END==================================Set<String>============================================/


    //--------------------------------ACTIVITY LEVEL PREFERENCES ---------------------------------//

    //=============================================String==========================================/

    @SuppressLint("CommitPrefEdits")
    fun putString(context: Activity, key: String, value: String) {
        with(context.getPreferences(Context.MODE_PRIVATE).edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getString(context: Activity, key: String, defaultValue: String?): String? =
            context.getPreferences(Context.MODE_PRIVATE).getString(key, defaultValue)

    //==END========================================String==========================================/


    //============================================Long=============================================/

    @SuppressLint("CommitPrefEdits")
    fun putLong(context: Activity, key: String, value: Long) {
        with(context.getPreferences(Context.MODE_PRIVATE).edit()) {
            putLong(key, value)
            apply()
        }
    }

    fun getLong(context: Activity, key: String, defaultValue: Long): Long =
            context.getPreferences(Context.MODE_PRIVATE).getLong(key, defaultValue)

    fun getLong(context: Activity, key: String): Long = getLong(context, key, 0)

    //==END======================================Long==============================================/


    //===================CLEAR DATA IN ACTIVITY PREFERENCES========================================/

    @SuppressLint("CommitPrefEdits")
    fun clearData(context: Activity) {
        with(context.getPreferences(Context.MODE_PRIVATE).edit())
        {
            clear()
            apply()
        }
    }


}