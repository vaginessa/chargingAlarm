package com.chargingwatts.chargingalarm.ui.batteryprofile

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.work.PeriodicWorkRequest
import com.chargingwatts.chargingalarm.BaseFragment
import com.chargingwatts.chargingalarm.R
import com.chargingwatts.chargingalarm.databinding.FragmentBatteryProfileBinding
import com.chargingwatts.chargingalarm.util.autoCleared
import com.chargingwatts.chargingalarm.util.battery.*
import com.chargingwatts.chargingalarm.util.ringtonepicker.RingtonePickerDialog
import com.chargingwatts.chargingalarm.util.ringtonepicker.RingtonePickerListener
import com.chargingwatts.chargingalarm.util.settings.SettingsManager
import com.chargingwatts.chargingalarm.util.ui.UIHelper
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import javax.inject.Inject


class BatteryProfileFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var periodicBatteryUpdater: PeriodicBatteryUpdater
    @Inject
    lateinit var batteryChangeReciever: BatteryChangeReciever
    @Inject
    lateinit var uiHelper: UIHelper
    @Inject
    lateinit var batteryAlarmManager: BatteryAlarmManager
    @Inject
    lateinit var settingsManager: SettingsManager

    private lateinit var batteryProfileViewModel: BatteryProfileViewModel

    var binding by autoCleared<FragmentBatteryProfileBinding>()

    var mediaPlayer: MediaPlayer? = null
    var mBatteryProfile: BatteryProfile? = null
    val callback = object : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.ll_start_stop_service -> {
                    val earlierPreference = batteryProfileViewModel.userAlarmPreferenceLiveData.value
                            ?: false
                    val newPreference = !earlierPreference
                    batteryProfileViewModel.setUserAlarmPreference(newPreference)

                    if (newPreference) {
                        startAlarm()
                    } else {
                        stopAlarm()
                    }
                }

                R.id.ll_settings -> {
                    openRingtonPickerDialog()
                }

                else -> {

                }
            }
        }

    }


    fun startAlarm() {
        context?.let { lContext ->
            mBatteryProfile?.isCharging?.apply {
                if (true){
                    BatteryMonitoringService.startInForeground(lContext)
                    periodicBatteryUpdater.startPeriodicBatteryUpdate(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, BATTERY_WORKER_REQUEST_TAG)
                }
            }
            uiHelper.showToast(R.string.toast_alarm_start, Toast.LENGTH_SHORT)

        }
    }

    fun stopAlarm() {
        context?.let { lContext ->
            BatteryMonitoringService.stopService(lContext)
            uiHelper.showToast(R.string.toast_alarm_stop, Toast.LENGTH_SHORT)
            batteryAlarmManager.stopAllAlarms()

        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        batteryProfileViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(BatteryProfileViewModel::class.java)
        binding = FragmentBatteryProfileBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        val lBatteryProfileLiveData = batteryProfileViewModel.batteryProfileLiveData
        val lUserAlarmPreferenceLiveData = batteryProfileViewModel.userAlarmPreferenceLiveData
        lBatteryProfileLiveData.observe(this, Observer { lBatteryProfile ->
            binding.batteryProfile = lBatteryProfile
            mBatteryProfile = lBatteryProfile
        })

        lUserAlarmPreferenceLiveData.observe(this, Observer { lUserAlarmPreference ->

            binding.userAlarmPreference = lUserAlarmPreference ?: false

        })
        binding.clickListener = callback


        mediaPlayer = MediaPlayer.create(context, R.raw.ultra_alarm)
        mediaPlayer?.isLooping = true

        mediaPlayer?.setOnCompletionListener {
            MediaPlayer.OnCompletionListener {
                mediaPlayer?.stop()
            }
        }

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        periodicBatteryUpdater.startPeriodicBatteryUpdate(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, BATTERY_WORKER_REQUEST_TAG)
        context?.let {
            try {
                batteryChangeReciever.registerReciever(it)
            } catch (exception: Exception) {

            }
        }

    }

    override fun onPause() {
        super.onPause()
//        periodicBatteryUpdater.stopPeriodicBatteryUpdate()
        context?.let {
            try {
                batteryChangeReciever.unregisterReciever(it)

            } catch (exception: Exception) {

            }
        }
    }


    fun openRingtonPickerDialog() {
        if (ActivityCompat.checkSelfPermission(context!!,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            val ringtonePickerBuilder = RingtonePickerDialog.Builder(getContext()!!, activity!!.getSupportFragmentManager())

                    //Set title of the dialog.
                    //If set null, no title will be displayed.
                    .setTitle("Select ringtone")

                    //set the currently selected uri, to mark that ringtone as checked by default.
                    //If no ringtone is currently selected, pass null.
//                    .setCurrentRingtoneUri(mCurrentSelectedUri)

                    //Allow user to select default ringtone set in phone settings.
                    .displayDefaultRingtone(true)

                    //Allow user to select silent (i.e. No ringtone.).
                    .displaySilentRingtone(true)

                    //set the text to display of the positive (ok) button.
                    //If not set OK will be the default text.
                    .setPositiveButtonText("SET RINGTONE")

                    //set text to display as negative button.
                    //If set null, negative button will not be displayed.
                    .setCancelButtonText("CANCEL")

                    //Set flag true if you want to play the sample of the clicked tone.
                    .setPlaySampleWhileSelection(true)

                    //Set the callback listener.
                    .setListener(object : RingtonePickerListener {
                        override fun OnRingtoneSelected(ringtoneName: String, ringtoneUri: Uri?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    })


            //Add the desirable ringtone types.
//            if (musicCb.isChecked())
//                ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_MUSIC)
//            if (notificationCb.isChecked())
//                ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_NOTIFICATION)
//            if (ringtoneCb.isChecked())
//                ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_RINGTONE)
//            if (alarmCb.isChecked())
            ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_ALARM)

            //Display the dialog.
            ringtonePickerBuilder.show()
        } else {
            ActivityCompat.requestPermissions(activity!!,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    123)
        }
    }

}
