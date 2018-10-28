package com.chargingwatts.chargingalarm.util.logging

import android.content.Intent
import android.os.Bundle
import android.util.Log


/**
 *
 */
object LoggingHelper {

    fun d(tag: String, message: String) = Log.d(tag, message)

    fun e(tag: String, message: String) = Log.e(tag, message)

    fun v(tag: String, message: String) = Log.v(tag, message)

    fun w(tag: String, message: String) = Log.w(tag, message)


    private val TAG = "IntentLogger"

    fun logFullIntentContent(intent: Intent) {

        //        Log.v(TAG, "Intent.toString(): " + intent.toString());
        //        Log.v(TAG, "Intent.toUri(): " + intent.toUri(URI_INTENT_SCHEME));

        Log.v(TAG, "Package: " + intent.getPackage()!!)

        Log.v(TAG, "Action: " + intent.action!!)

        Log.v(TAG, "Type: " + intent.type!!)

        printCategories(intent.categories)

        Log.v(TAG, "Component: " + intent.component!!)

        Log.v(TAG, "Data String: " + intent.dataString!!)

        logIntentExtras(intent.extras)
    }

    private fun logIntentExtras(extras: Bundle?) {

        if (extras == null) {

            Log.v(TAG, "Extras: null")

        } else {

            if (extras.isEmpty) {

                Log.v(TAG, "Extras: not null, but empty")

            } else {

                for (extraKey in extras.keySet()) {
                    Log.v(TAG, "Extra: " + extraKey + ": " + extras.get(extraKey))
                }
            }
        }
    }

    private fun printCategories(categories: Set<String>?) {

        if (categories == null) {

            Log.v(TAG, "Categories: null")

        } else {

            if (categories.isEmpty()) {

                Log.v(TAG, "Categories: not null, but empty")

            } else {

                for (category in categories) {
                    Log.v(TAG, "Category: $category")
                }
            }
        }
    }

}