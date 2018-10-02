package com.chargingwatts.chargingalarm.util.logging

import android.util.Log

/**
 *
 */
object LoggingHelper {

    fun d(tag: String, message: String) = Log.d(tag, message)

    fun e(tag: String, message: String) = Log.e(tag, message)

    fun v(tag: String, message: String) = Log.v(tag, message)

    fun w(tag: String, message: String) = Log.w(tag, message)

}