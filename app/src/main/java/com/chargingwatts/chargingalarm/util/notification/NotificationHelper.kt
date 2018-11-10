package com.chargingwatts.chargingalarm.util.notification


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.chargingwatts.chargingalarm.HomeActivity
import com.chargingwatts.chargingalarm.R
import com.chargingwatts.chargingalarm.util.battery.BatteryProfileUtils
import com.chargingwatts.chargingalarm.util.logging.EventLogger
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Helper class to manage notification channels, and create notifications.
 */
@Singleton
class NotificationHelper @Inject constructor(context: Context) : ContextWrapper(context) {

    private var manager: NotificationManagerCompat? = null


    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationManager = getSystemService(NotificationManager::class.java)


            val batteryHighChannel = NotificationChannel(BATTERY_LEVEL_HIGH_CHANNEL,
                    getString(R.string.noti_channel_high_battery), NotificationManager.IMPORTANCE_HIGH)
            batteryHighChannel.description = getString(R.string.BATTERY_LEVEL_HIGH_CHANNEL_DESCRIPTION)
            batteryHighChannel.lightColor = Color.GREEN
            batteryHighChannel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(batteryHighChannel)


            val batteryLevelChannel = NotificationChannel(BATTERY_LEVEL_CHANNEL,
                    getString(R.string.noti_channel_battery_level), NotificationManager.IMPORTANCE_DEFAULT)
            batteryLevelChannel.description = getString(R.string.BATTERY_LEVEL_CHANNEL_DESCRIPTION)
            batteryLevelChannel.lightColor = Color.BLUE
            batteryLevelChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            batteryLevelChannel.enableVibration(false)
            notificationManager.createNotificationChannel(batteryLevelChannel)

            val batteryLevelLowChannel = NotificationChannel(BATTERY_LEVEL_LOW_CHANNEL,
                    getString(R.string.noti_channel_battery_level), NotificationManager.IMPORTANCE_HIGH)
            batteryLevelLowChannel.description = getString(R.string.BATTERY_LEVEL_LOW_CHANNEL_DESCRIPTION)
            batteryLevelLowChannel.lightColor = Color.RED
            batteryLevelLowChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(batteryLevelLowChannel)
        }
    }

    /**
     * Get the notification manager.
     *
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private fun getManager(): NotificationManagerCompat {
        if (manager == null) {
            manager = NotificationManagerCompat.from(this)
        }
        return manager as NotificationManagerCompat
    }

    /**
     * Get the small icon for this app
     *
     * @return The small icon resource id
     */
    private val smallIcon: Int
        get() = android.R.drawable.ic_lock_idle_low_battery

    /**
     * Get a notification of type 1
     *
     * Provide the builder rather than the notification it's self as useful for making notification
     * changes.
     *
     * @param title the title of the notification
     * @param body the body text for the notification
     * @return the builder as it keeps a reference to the notification (since API 24)
     */
    fun getHighBatteryNotificationBuilder(title: String, body: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, BATTERY_LEVEL_HIGH_CHANNEL)
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setContentText(body)
//                .setStyle(NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setProgress(100, batteryLevel, false)
                .setContentIntent(getTapIntent())
                .setAutoCancel(true)

    }


    fun getBatteryLevelNotificationBuilder(title: String, body: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, BATTERY_LEVEL_CHANNEL)
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setContentText(body)
//                .setStyle(NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setProgress(100, batteryLevel, false)
                .setContentIntent(getTapIntent())
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)

    }

    fun getLowBatteryNotificationBuilder(title: String, body: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, BATTERY_LEVEL_LOW_CHANNEL)
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setContentText(body)
//                .setStyle(NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(getTapIntent())
                .setAutoCancel(true)
    }


    fun getTapIntent(): PendingIntent {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return PendingIntent.getActivity(this, 0, intent, 0)
    }


    /**
     * Send a notification.
     *
     * @param id The ID of the notification
     * @param notification The notification object
     */
    fun notify(id: Int, notificationBuilder: NotificationCompat.Builder) {
        getManager().notify(id, notificationBuilder.build())
    }


    /**
     * Send Intent to load system Notification Settings for this app.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun goToNotificationSettings() {
        val i = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        i.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        if (i.resolveActivity(packageManager) != null) {
            startActivity(i)
        }
    }

    /**
     * Send intent to load system Notification Settings UI for a particular channel.
     *
     * @param channel Name of channel to configure
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun goToNotificationSettings(channel: String) {
        val i = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
        i.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        i.putExtra(Settings.EXTRA_CHANNEL_ID, channel)
        if (i.resolveActivity(packageManager) != null) {
            startActivity(i)
        }
    }


    companion object {
        @JvmStatic
        val BATTERY_LEVEL_CHANNEL = "battery_level_channel"
        @JvmStatic
        val BATTERY_LEVEL_LOW_CHANNEL = "battery_level_low_channel"
        @JvmStatic
        val BATTERY_LEVEL_HIGH_CHANNEL = "battery_level_high_channel"

        @JvmStatic
        val BATTERY_LEVEL_CHANNEL_NOTIFICATION_ID = 111
        @JvmStatic
        val BATTERY_LEVEL_LOW_CHANNEL_NOTIFICATION_ID = 222
        @JvmStatic
        val BATTERY_LEVEL_HIGH_CHANNEL_NOTIFICATION_ID = 333


        @JvmStatic
        fun createBatteryNotificationTitleString(context: Context, batteryProfile: BatteryProfile?): String {
            batteryProfile?.let {
                EventLogger.logBatteryNotificationUpdatedEvent(it)
            }

            var bodyString: String = context.getString(R.string.default_notification_body)

            batteryProfile?.let {
                bodyString = "Battery Status"

                batteryProfile.remainingPercent?.let { batteryPercent ->
                    bodyString = "$bodyString - $batteryPercent%"
                }

                batteryProfile.recentBatteryTemperature?.let { batteryTemperature ->
                    bodyString = "$bodyString ($batteryTemperature\u2103)"
                }

                batteryProfile.batteryStatusType?.let { batteryStatusType ->

                    BatteryProfileUtils.getBatteryStatusString(context, batteryStatusType)?.let { batteryStatusString ->
                        if (batteryStatusString != context.getString(R.string.BATTERY_STATUS_UNKNOWN)) {
                            bodyString = "$bodyString - $batteryStatusString"
                        }
                    }
                }
            }

            return bodyString
        }
    }
}