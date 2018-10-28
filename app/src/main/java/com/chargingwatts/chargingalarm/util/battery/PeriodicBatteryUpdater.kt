package com.chargingwatts.chargingalarm.util.battery

import android.arch.lifecycle.LiveData
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkStatus
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.db.BatteryProfileDao
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val BATTERY_WORKER_REQUEST_TAG = "battery_worker_request_tag";


@Singleton
object PeriodicBatteryUpdater {

    private var mBatteryProfileDao: BatteryProfileDao? = null
    private var mAppExecutors: AppExecutors? = null
    private var mPeriodicWorkRequest: PeriodicWorkRequest? = null
    private var mTag: String? = null
    private var mWorkStatusLiveData: LiveData<WorkStatus>? = null


//    val mWorkStatusObserver = Observer<WorkStatus> { workStatus ->
//
//        workStatus?.let {
//            val isWorkRunning = workStatus.state == State.RUNNING
//            if (isWorkRunning) {
//                val batteryProfileData = workStatus.outputData
//                val batteryProfileMap = batteryProfileData.keyValueMap
//                val batteryProfile = BatteryProfileUtils.convertMapToBatteryProfile(batteryProfileMap)
//
//                mAppExecutors?.diskIO()?.execute {
//                    mBatteryProfileDao?.insert(batteryProfile)
//                }
//            }
//        }
//
//    }

//    fun initiate(batteryProfileDao: BatteryProfileDao,
//                 appExecutors: AppExecutors): PeriodicBatteryUpdater {
//        mBatteryProfileDao = batteryProfileDao;
//        mAppExecutors = appExecutors
//        return this
//
//    }

//    fun checkIfInitialized(): Boolean {
//        return !(mBatteryProfileDao == null || mAppExecutors == null)
//    }

    fun startPeriodicBatteryUpdate(repeatIntervalInMillis: Long, tag: String) {
//        if (!checkIfInitialized()) {
//            throw Exception(" Please call initialize method before using instance of ${PeriodicBatteryUpdater::class.qualifiedName}")
//        }
        if (repeatIntervalInMillis < PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS) {
            throw IllegalArgumentException("repeatIntervalInMillis = $repeatIntervalInMillis which is less than PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS} = ${PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS} is not allowed")
        }

        mTag?.let {
            WorkManager.getInstance().cancelAllWorkByTag(it)
//            removeBatteryUpdateObserver()
            stopPeriodicBatteryUpdate()

        }
        mTag = tag

        mTag?.let {
            mPeriodicWorkRequest = createPeriodicRequest(repeatIntervalInMillis, it);
            mPeriodicWorkRequest?.let { periodicRequest ->
                WorkManager.getInstance().enqueueUniquePeriodicWork(it,ExistingPeriodicWorkPolicy.REPLACE,periodicRequest)
//                setBatteryUpdateObserver()
            }
        }
    }

    fun stopPeriodicBatteryUpdate() {
        mTag?.let {
//            removeBatteryUpdateObserver()
            WorkManager.getInstance().cancelAllWorkByTag(it)
        }
    }


    fun createPeriodicRequest(repeatIntervalInMillis: Long, tag: String): PeriodicWorkRequest =
            PeriodicWorkRequest.Builder(BatteryUpdateWorker::class.java, repeatIntervalInMillis
                    , TimeUnit.MILLISECONDS).addTag(tag).build()

//    fun setBatteryUpdateObserver() {
//        if (!checkIfInitialized()) {
//            throw Exception(" Please call initialize method before using instance of ${PeriodicBatteryUpdater::class.qualifiedName}")
//        }
//        mPeriodicWorkRequest?.let {
//            mWorkStatusLiveData =
//                    WorkManager.getInstance().getStatusByIdLiveData(it.id)
//            mWorkStatusLiveData?.observeForever(mWorkStatusObserver)
//        }
//
//    }

//    fun removeBatteryUpdateObserver() {
//        mWorkStatusLiveData?.removeObserver(mWorkStatusObserver)
//    }


}





