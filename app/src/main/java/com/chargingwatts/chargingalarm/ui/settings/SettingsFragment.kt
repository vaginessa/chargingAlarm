package com.chargingwatts.chargingalarm.ui.settings

import AppConstants
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.preference.*
import com.chargingwatts.chargingalarm.R
import com.chargingwatts.chargingalarm.di.Injectable
import com.chargingwatts.chargingalarm.util.AlarmMediaManager
import com.chargingwatts.chargingalarm.util.settings.DEFAULT_BATTERY_HIGH_LEVEL
import com.chargingwatts.chargingalarm.util.settings.DEFAULT_BATTERY_HIGH_TEMPERATURE
import com.chargingwatts.chargingalarm.util.settings.DEFAULT_BATTERY_LOW_LEVEL
import com.chargingwatts.chargingalarm.util.settings.SettingsManager
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class SettingsFragment : PreferenceFragmentCompat(), Injectable {

    @Inject
    lateinit var settingsManager: SettingsManager

    @Inject
    lateinit var alarmMediaManager: AlarmMediaManager

    private var lContext: Context? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)

    }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        lContext = preferenceManager.context
        val screen = preferenceManager.createPreferenceScreen(lContext)


        screen.addPreference(createVibrationModePreference())
        screen.addPreference(createSoundPreference())


        //Battery Threshold level category
        val thresholdPrefCategory = createBatteryThresholdPrefCategory()
        screen.addPreference(thresholdPrefCategory)

        val batteryHighLevelPreference = createBatteryHighLevelPref(startValue = 0,
                endValue = 100, stepSize = 5, defaultValue = DEFAULT_BATTERY_HIGH_LEVEL)
        thresholdPrefCategory.addPreference(batteryHighLevelPreference)

        val batteryLowLevelPreference = createBatteryLowLevelPref(startValue = 0,
                endValue = 100, stepSize = 5, defaultValue = DEFAULT_BATTERY_LOW_LEVEL)
        thresholdPrefCategory.addPreference(batteryLowLevelPreference)

        val batteryHighTempPreference = createBatteryHighTempPref(startValue = 25,
                endValue = 45, stepSize = 1, defaultValue = DEFAULT_BATTERY_HIGH_TEMPERATURE)
        thresholdPrefCategory.addPreference(batteryHighTempPreference)


        // Silent Mode Category
        val silentModePrefCategory = createSilentModePrefCategory()
        screen.addPreference(silentModePrefCategory)

        silentModePrefCategory.addPreference(createRingOnSilentModePreference())
        silentModePrefCategory.addPreference(createVibrateOnSilentModePreference())


        // Alarm Tone Settings Category
        val alarmTonePrefCategory = createAlarmTonePrefCategory()
        screen.addPreference(alarmTonePrefCategory)

        alarmTonePrefCategory.addPreference(createAlarmTonePreference())


        // Alarm Volume Settings Category
        val alarmVolumePrefCategory = createAlarmVolumeSettingPrefCategory()
        screen.addPreference(alarmVolumePrefCategory)

        alarmVolumePrefCategory.addPreference(createAlarmVolumeSettingsPref())


        preferenceScreen = screen


    }

    //--------------------------CREATE THRESHOLD CATEGORY------------------------------------------//

    private fun createBatteryThresholdPrefCategory(): PreferenceCategory {
        val thresholdPrefCategory = PreferenceCategory(lContext)
        thresholdPrefCategory.key = AppConstants.KEY_BATTERY_THRESHOLD_PREF_CATEGORY
        thresholdPrefCategory.title = getString(R.string.pref_category_battery_threshold_label)
        return thresholdPrefCategory

    }

    //--------------------------CREATE THRESHOLD CATEGORY------------------------------------------//

    private fun createSilentModePrefCategory(): PreferenceCategory {
        val silentModePrefCategory = PreferenceCategory(lContext)
        silentModePrefCategory.key = AppConstants.KEY_SILENT_MODE_PREF_CATEGORY
        silentModePrefCategory.title = getString(R.string.pref_category_silent_mode_label)
        return silentModePrefCategory

    }

    //--------------------------CREATE VOLUME SETTING CATEGORY------------------------------------//

    private fun createAlarmVolumeSettingPrefCategory(): PreferenceCategory {
        val alarmVolumePrefCategory = PreferenceCategory(lContext)
        alarmVolumePrefCategory.key = AppConstants.KEY_VOLUME_SETTING_PREF_CATEGORY
        alarmVolumePrefCategory.title = getString(R.string.pref_category_alarm_volume_setting_label)
        return alarmVolumePrefCategory

    }

    //--------------------------CREATE ALARM TONE CATEGORY----------------------------------------//

    private fun createAlarmTonePrefCategory(): PreferenceCategory {
        val alarmTonePrefCategory = PreferenceCategory(lContext)
        alarmTonePrefCategory.key = AppConstants.KEY_ALARM_TONE_PREF_CATEGORY
        alarmTonePrefCategory.title = getString(R.string.pref_category_alarm_tone_setting_label)
        return alarmTonePrefCategory

    }

    //--------------------------CREATE BATTERY HIGH LEVEL PREFERENCE -----------------------------//

    private fun createBatteryHighLevelPref(startValue: Int, endValue: Int, stepSize: Int, defaultValue: Int): ListPreference {
        val highLevelListPreference = ListPreference(context)
        highLevelListPreference.key = AppConstants.BATTERY_HIGH_LEVEL_PREF
        highLevelListPreference.title = context?.getString(R.string.label_battery_high_level)
        highLevelListPreference.dialogTitle = context?.getString(R.string.label_battery_high_level)
        highLevelListPreference.setDefaultValue(defaultValue.toString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            highLevelListPreference.icon = context?.getDrawable(R.drawable.ic_setting_full_battery)
        }
        val noOfSteps = (endValue - startValue) / stepSize + 1


        val entryValueArray: Array<String> = Array(noOfSteps) { i ->
            Integer.toString(i * stepSize + startValue)
        }


        val defaultValueIndex = checkAndFindIfValueInArray(entryValueArray, defaultValue)

        val entryArray: Array<String> = Array(noOfSteps) { i ->
            if (defaultValueIndex < 0 && i == (noOfSteps - 1)) {
                endValue.toString() + " %" + " (" + context?.getString(R.string.show_default) + ")"
            } else if (defaultValueIndex > 0 && defaultValueIndex == i) {
                Integer.toString(i * stepSize + startValue) + " %" + " (" + context?.getString(R.string.show_default) + ")"

            } else {
                Integer.toString(i * stepSize + startValue) + " %"
            }
        }

        highLevelListPreference.entries = entryArray
        highLevelListPreference.entryValues = entryValueArray

        val selectedPrefIndex = checkAndFindIfValueInArray(entryValueArray, settingsManager.getBatteryHighLevelPercentPreference())

        if (selectedPrefIndex < 0) {
            highLevelListPreference.setValueIndex(noOfSteps - 1)

        } else {
            highLevelListPreference.setValueIndex(selectedPrefIndex)

        }

        highLevelListPreference.summary =
                createBatteryHighLevelListPrefSummary(highLevelListPreference.value)
        highLevelListPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            settingsManager.setBatteryHighLevelPercentPreference((newValue as String).toInt()
            )
            preference.summary = createBatteryHighLevelListPrefSummary(newValue.toString())
            true
        }

        return highLevelListPreference
    }

    private fun checkAndFindIfValueInArray(entryArray: Array<String>, searchValue: Int): Int {
        for (entryIndex in entryArray.indices) {
            if (entryArray[entryIndex].toInt() == searchValue) {
                return entryIndex
            }
        }
        return -1

    }

    private fun createBatteryHighLevelListPrefSummary(currentSelectedBatteryLevel: String) =
            context?.getString(R.string.summary_battery_high_level) + " : " +
                    currentSelectedBatteryLevel + "%%"

    //--------------------------CREATE BATTERY LOW LEVEL PREFERENCE ------------------------------//

    private fun createBatteryLowLevelPref(startValue: Int, endValue: Int, stepSize: Int, defaultValue: Int): ListPreference {
        val lowLevelListPreference = ListPreference(context)
        lowLevelListPreference.key = AppConstants.BATTERY_LOW_LEVEL_PREF
        lowLevelListPreference.title = context?.getString(R.string.label_battery_low_level)
        lowLevelListPreference.dialogTitle = context?.getString(R.string.label_battery_low_level)
        lowLevelListPreference.setDefaultValue(defaultValue.toString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lowLevelListPreference.icon = context?.getDrawable(R.drawable.ic_setting_low_battery)
        }
        val noOfSteps = (endValue - startValue) / stepSize + 1

        val entryValueArray: Array<String> = Array(noOfSteps) { i ->
            Integer.toString(i * stepSize + startValue)
        }

        val defaultValueIndex = checkAndFindIfValueInArray(entryValueArray, defaultValue)

        val entryArray: Array<String> = Array(noOfSteps) { i ->
            if (defaultValueIndex < 0 && i == (noOfSteps - 1)) {
                endValue.toString() + " %" + " (" + context?.getString(R.string.show_default) + ")"
            } else if (defaultValueIndex > 0 && defaultValueIndex == i) {
                Integer.toString(i * stepSize + startValue) + " %" + " (" + context?.getString(R.string.show_default) + ")"

            } else {
                Integer.toString(i * stepSize + startValue) + " %"
            }
        }



        lowLevelListPreference.entries = entryArray
        lowLevelListPreference.entryValues = entryValueArray

        val selectedPrefIndex = checkAndFindIfValueInArray(entryValueArray, settingsManager.getBatteryLowLevelPercentPreference())

        if (selectedPrefIndex < 0) {
            lowLevelListPreference.setValueIndex(0)

        } else {
            lowLevelListPreference.setValueIndex(selectedPrefIndex)

        }



        lowLevelListPreference.summary =
                createBatteryLowLevelListPrefSummary(lowLevelListPreference.value)
        lowLevelListPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            settingsManager.setBatteryLowLevelPercentPreference((newValue as String).toInt()
            )
            preference.summary = createBatteryLowLevelListPrefSummary(newValue.toString())
            true
        }

        return lowLevelListPreference
    }

    private fun createBatteryLowLevelListPrefSummary(currentSelectedBatteryLevel: String) =
            context?.getString(R.string.summary_battery_low_level) + " : " +
                    currentSelectedBatteryLevel + "%%"


    //--------------------------CREATE BATTERY HIGH TEMP PREFERENCE ------------------------------//

    private fun createBatteryHighTempPref(startValue: Int, endValue: Int, stepSize: Int, defaultValue: Float): ListPreference {
        val highTempListPreference = ListPreference(context)
        highTempListPreference.key = AppConstants.BATTERY_HIGH_TEMPERATURE_PREF
        highTempListPreference.title = context?.getString(R.string.label_battery_high_temperature)
        highTempListPreference.dialogTitle = context?.getString(R.string.label_battery_high_temperature)
        highTempListPreference.setDefaultValue(defaultValue.toString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            highTempListPreference.icon = context?.getDrawable(R.drawable.ic_setting_temperature)
        }
        val noOfSteps = (endValue - startValue) / stepSize + 1

        val entryValueArray: Array<String> = Array(noOfSteps) { i ->
            Integer.toString(i * stepSize + startValue)
        }

        val defaultValueIndex = checkAndFindIfValueInArray(entryValueArray, defaultValue.toInt())

        val entryArray: Array<String> = Array(noOfSteps) { i ->
            if (defaultValueIndex < 0 && i == (noOfSteps - 1)) {
                endValue.toString() + context?.getString(R.string.show_degree) + " (" + context?.getString(R.string.show_default) + ")"
            } else if (defaultValueIndex > 0 && defaultValueIndex == i) {
                Integer.toString(i * stepSize + startValue) + context?.getString(R.string.show_degree) + " (" + context?.getString(R.string.show_default) + ")"

            } else {
                Integer.toString(i * stepSize + startValue) + context?.getString(R.string.show_degree)
            }
        }

        highTempListPreference.entries = entryArray
        highTempListPreference.entryValues = entryValueArray

        val selectedPrefIndex = checkAndFindIfValueInArray(entryValueArray, settingsManager.getBatteryHighTemperaturePreference().toInt())

        if (selectedPrefIndex < 0) {
            highTempListPreference.setValueIndex(noOfSteps - 1)

        } else {
            highTempListPreference.setValueIndex(selectedPrefIndex)

        }



        highTempListPreference.summary =
                createBatteryHighTempListPrefSummary(highTempListPreference.value)
        highTempListPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener {
            preference, newValue ->
            settingsManager.setBatteryHighTemperaturePreference((newValue as String).toFloat()
            )
            preference.summary = createBatteryHighTempListPrefSummary(newValue.toString())
            true
        }

        return highTempListPreference
    }

    private fun createBatteryHighTempListPrefSummary(curentSelectedTemp: String) =
            context?.getString(R.string.summary_battery_high_temperature) + " : " +
                    curentSelectedTemp + context?.getString(R.string.show_degree)


    //--------------------------CREATE VIBRATION MODE PREFERENCE ---------------------------------//

    private fun createVibrationModePreference(): CheckBoxPreference {
        val vibrationPref = CheckBoxPreference(context)
        vibrationPref.key = AppConstants.IS_VIBRATION_ENABLED_PREF
        vibrationPref.title = getString(R.string.label_activate_vibration_mode)
        vibrationPref.setDefaultValue(settingsManager.getVibrationPreference())
        vibrationPref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener {
            preference, newValue ->
            settingsManager.setVibrationPreference(newValue as Boolean)
            true
        }
        return vibrationPref
    }

    //--------------------------CREATE SOUND PREFERENCE ------------------------------------------//

    private fun createSoundPreference(): CheckBoxPreference {
        val soundPref = CheckBoxPreference(context)
        soundPref.key = AppConstants.IS_SOUND_ENABLED_PREF
        soundPref.title = getString(R.string.label_activate_sound_mode)
        soundPref.setDefaultValue(settingsManager.getVibrationPreference())
        soundPref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener {
            preference, newValue ->
            settingsManager.setSoundPreference(newValue as Boolean)
            true
        }
        return soundPref
    }

    //--------------------------CREATE SILENT MODE RING PREFERENCE --------------------------------//

    private fun createRingOnSilentModePreference(): CheckBoxPreference {
        val silentModeRingPref = CheckBoxPreference(context)
        silentModeRingPref.key = AppConstants.RING_ON_SILENT_MODE_PREF
        silentModeRingPref.title = getString(R.string.label_ring_on_silent_mode)
        silentModeRingPref.setDefaultValue(settingsManager.getRingOnSilentModePreference())
        silentModeRingPref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener {
            preference, newValue ->
            settingsManager.setRingOnSilentModePreference(newValue as Boolean)
            true
        }
        return silentModeRingPref
    }

    //--------------------------CREATE SILENT MODE VIBRATE PREFERENCE -----------------------------//

    private fun createVibrateOnSilentModePreference(): CheckBoxPreference {
        val silentModeVibratePref = CheckBoxPreference(context)
        silentModeVibratePref.key = AppConstants.VIBRATE_ON_SILENT_MODE_PREF
        silentModeVibratePref.title = getString(R.string.label_vibrate_on_silent_mode)
        silentModeVibratePref.setDefaultValue(settingsManager.getVibrateOnSilentModePreference())
        silentModeVibratePref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener {
            preference, newValue ->
            settingsManager.setVibrateOnSilentModePreference(newValue as Boolean)
            true
        }
        return silentModeVibratePref
    }

    //--------------------------CREATE ALARM VOLUME PREFERENCE -----------------------------------//

    private fun createAlarmVolumeSettingsPref(): ListPreference {
        val noOfSteps = 11
        val maxVolume = alarmMediaManager.getMaxVoulume()
        val defaultValue = maxVolume
        val alarmVolumeLevelListPref = ListPreference(context)
        alarmVolumeLevelListPref.key = AppConstants.ALARM_VOLUME_PREF
        alarmVolumeLevelListPref.title = context?.getString(R.string.label_alarm_volume)
        alarmVolumeLevelListPref.dialogTitle = context?.getString(R.string.label_alarm_volume)
//        alarmLevelListPref.setDefaultValue(maxVolume)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alarmVolumeLevelListPref.icon = context?.getDrawable(R.drawable.ic_volume_settings)
        }
        val stepSize = maxVolume / noOfSteps


        val entryValueArray: Array<String> = Array(noOfSteps) { i ->
            Integer.toString(i * stepSize)
        }


        val defaultValueIndex = checkAndFindIfValueInArray(entryValueArray, defaultValue)

        val entryArray: Array<String> = Array(noOfSteps) { i ->
            if (defaultValueIndex < 0 && i == (noOfSteps - 1)) {
                Integer.toString(i) + " (" + context?.getString(R.string.show_default) + ")"
            } else if (defaultValueIndex > 0 && defaultValueIndex == i) {
                Integer.toString(i) + " (" + context?.getString(R.string.show_default) + ")"

            } else {
                Integer.toString(i)
            }
        }

        alarmVolumeLevelListPref.entries = entryArray
        alarmVolumeLevelListPref.entryValues = entryValueArray

        val selectedVolumeLevelIndex = getSelectedVolumeLevelIndex(entryValueArray,settingsManager.getAlarmVolumePreference())

        alarmVolumeLevelListPref.setValueIndex(selectedVolumeLevelIndex)


        alarmVolumeLevelListPref.summary =
                createAlarmVolumeSettingsPrefSummary(selectedVolumeLevelIndex.toString())
        alarmVolumeLevelListPref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            settingsManager.setAlarmVolumePreference((newValue as String).toInt()
            )
            preference.summary = createAlarmVolumeSettingsPrefSummary(getSelectedVolumeLevelIndex(entryValueArray,newValue.toInt()).toString())
            true
        }

        return alarmVolumeLevelListPref
    }

    private fun getSelectedVolumeLevelIndex(entryValueArray: Array<String>, alarmPref: Int): Int {
        val tempPrefIndex = checkAndFindIfValueInArray(entryValueArray, alarmPref)
        val selectedPrefIndex: Int
        selectedPrefIndex = if (tempPrefIndex < 0) {
            entryValueArray.size - 1

        } else {
            tempPrefIndex

        }
        return selectedPrefIndex

    }

    private fun createAlarmVolumeSettingsPrefSummary(currentSelectedVolumeLevel: String) =
            context?.getString(R.string.summary_alarm_volume) + " : " +
                    currentSelectedVolumeLevel


    //----------------------------CREATE ALARM TONE PREFERENCE -----------------------------------//


    private fun createAlarmTonePreference(): Preference? {
        val ringtonPreference = Preference(context)
        ringtonPreference.key = AppConstants.ALARM_TONE_PREF
        ringtonPreference.title = context?.getString(R.string.label_alarm_tone)
        ringtonPreference.summary = context?.getString(R.string.summary_alarm_tone)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ringtonPreference.icon = context?.getDrawable(R.drawable.ic_alarm_tone_settings)
        }
        ringtonPreference.setOnPreferenceClickListener { preference ->
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            onPrepareRingtonePickerIntent(intent, getSelectedAlarmTone(), RingtoneManager.TYPE_ALL)
            startActivityForResult(intent, AppConstants.REQUEST_CODE_ALARM_TONE_SELECTOR)
            true
        }
        return ringtonPreference
    }

    private fun onPrepareRingtonePickerIntent(ringtonePickerIntent: Intent, existingAlarmTone: Uri,
                                              ringtoneType: Int, showDefault: Boolean = true,
                                              showSilent: Boolean = false) {

        ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
                existingAlarmTone)
        ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, showDefault)
        if (showDefault) {
            ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI,
                    getDefaulAppAlarmTone())
        }
        ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, showSilent)
        ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, ringtoneType)
        ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Choose Alarm Tone")

    }

    private fun getSelectedAlarmTone(): Uri {
        val uriString = settingsManager.getAlarmTonePreference()
        return Uri.parse(uriString)
    }

    private fun getDefaulAppAlarmTone(): Uri {
        val uriString = settingsManager.DEFAULT_ALARM_TONE_RINGTONE
        return Uri.parse(uriString)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AppConstants.REQUEST_CODE_ALARM_TONE_SELECTOR) {
            val selectedAlarmToneUri = data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)

            if (selectedAlarmToneUri != null) {
                settingsManager.setAlarmTonePreference(selectedAlarmToneUri.toString())
                Log.d("ringtoneuri", selectedAlarmToneUri.toString())
            }
        }
    }
}
