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
import com.chargingwatts.chargingalarm.util.settings.SettingsProfile
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Helper class to manage notification channels, and create notifications.
 */
@Singleton
class NotificationHelper @Inject constructor(context: Context) : ContextWrapper(context.applicationContext) {

    private var manager: NotificationManagerCompat? = null


    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationManager = getSystemService(NotificationManager::class.java)


            val batteryHighChannel = NotificationChannel(BATTERY_LEVEL_HIGH_CHANNEL,
                    getString(R.string.noti_channel_high_battery), NotificationManager.IMPORTANCE_HIGH)
            batteryHighChannel.description = getString(R.string.BATTERY_LEVEL_HIGH_CHANNEL_DESCRIPTION)
            batteryHighChannel.lightColor = Color.GREEN
            batteryHighChannel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            batteryHighChannel.setSound(null, null)
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
       //     batteryLevelLowChannel.setSound(null, null)
            notificationManager.createNotificationChannel(batteryLevelLowChannel)


            val batteryTempHighChannel = NotificationChannel(BATTERY_TEMPERATURE_HIGH_CHANNEL,
                    getString(R.string.noti_channel_battery_level), NotificationManager.IMPORTANCE_HIGH)
            batteryTempHighChannel.description = getString(R.string.BATTERY_TEMPERATURE_LOW_CHANNEL_DESCRIPTION)
            batteryTempHighChannel.lightColor = Color.RED
            batteryTempHighChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
           // batteryTempHighChannel.setSound(null, null)
            notificationManager.createNotificationChannel(batteryTempHighChannel)
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
        get() = R.drawable.ic_charger

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
                .setVibrate(LongArray(1){0L})



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
                .setVibrate(LongArray(1){0L})
                .setOnlyAlertOnce(true)


    }

    fun getBatteryHighTempNotificationBuilder(title: String, body: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, BATTERY_TEMPERATURE_HIGH_CHANNEL)
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setContentText(body)
//                .setStyle(NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(getTapIntent())
                .setAutoCancel(true)
                .setVibrate(LongArray(1){0L})
                .setOnlyAlertOnce(true)


    }


    fun getTapIntent(): PendingIntent {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return PendingIntent.getActivity(this, 0, intent, 0)
    }

    fun cancelNotification(notificationId:Int){
        getManager().cancel(notificationId)

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
        val BATTERY_TEMPERATURE_HIGH_CHANNEL = "battery_temperature_high_channel"

        @JvmStatic
        val BATTERY_LEVEL_CHANNEL_NOTIFICATION_ID = 1111
        @JvmStatic
        val BATTERY_LEVEL_LOW_CHANNEL_NOTIFICATION_ID = 2121
        @JvmStatic
        val BATTERY_LEVEL_HIGH_CHANNEL_NOTIFICATION_ID = 3131
        @JvmStatic
        val BATTERY_TEMPERATURE_HIGH_CHANNEL_NOTIFICATION_ID = 4141

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

                    BatteryProfileUtils.getBatteryStatusString( batteryStatusType,context)?.let { batteryStatusString ->
                        if (batteryStatusString != context.getString(R.string.BATTERY_STATUS_UNKNOWN)) {
                            bodyString = "$bodyString - $batteryStatusString"
                        }
                    }
                }
            }

            return bodyString
        }

        @JvmStatic
        fun createHighBatteryAlarmNotificationTitleString(context: Context, batteryProfile: BatteryProfile?, settingsProfile: SettingsProfile): String{

            val bodyString: String = context.getString(R.string.battery_full_notification_title) + " : " +
                    batteryProfile?.remainingPercent + context.getString(R.string.show_percent)
            return bodyString

        }

        @JvmStatic
        fun createHighBatteryAlarmNotificationBodyString(context: Context, batteryProfile: BatteryProfile?, settingsProfile: SettingsProfile): String{

            val bodyString: String = context.getString(R.string.battery_full_notification_body) + " : " +
                    settingsProfile.batteryHighLevelPercent + context.getString(R.string.show_percent)
            return bodyString

        }

        @JvmStatic
        fun createLowBatteryAlarmNotificationTitleString(context: Context, batteryProfile: BatteryProfile?, settingsProfile: SettingsProfile): String{

            val bodyString: String = context.getString(R.string.battery_low_notification_title) + " : " +
                    batteryProfile?.remainingPercent + context.getString(R.string.show_percent)
            return bodyString

        }

        @JvmStatic
        fun createLowBatteryAlarmNotificationBodyString(context: Context, batteryProfile: BatteryProfile?, settingsProfile: SettingsProfile): String{

            val bodyString: String = context.getString(R.string.battery_low_notification_body) + " : " +
                    settingsProfile.batteryLowLevelPercent + context.getString(R.string.show_percent)
            return bodyString

        }

        @JvmStatic
        fun createHighTempAlarmNotificationTitleString(context: Context, batteryProfile: BatteryProfile?, settingsProfile: SettingsProfile): String{

            val bodyString: String = context.getString(R.string.battery_high_temperature_notification_title) + " : " +
                    batteryProfile?.recentBatteryTemperature + context.getString(R.string.show_degree)
            return bodyString

        }

        @JvmStatic
        fun createHighTempAlarmNotificationBodyString(context: Context, batteryProfile: BatteryProfile?, settingsProfile: SettingsProfile): String{

            val bodyString: String = context.getString(R.string.battery_high_temperature_notification_body) + " : " +
                    settingsProfile.batteryHighTemperature + context.getString(R.string.show_degree)
            return bodyString

        }
    }
}