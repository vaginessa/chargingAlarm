package com.chargingwatts.chargingalarm.di.module


import android.app.Application
import android.arch.persistence.room.Room
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.R
import com.chargingwatts.chargingalarm.api.AlarmApiService
import com.chargingwatts.chargingalarm.db.BatteryProfileDao
import com.chargingwatts.chargingalarm.db.ChargingAlarmDb
import com.chargingwatts.chargingalarm.db.UserDetailDao
import com.chargingwatts.chargingalarm.util.LiveDataCallAdapterFactory
import com.chargingwatts.chargingalarm.util.battery.PeriodicBatteryUpdater
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideFEService(@Named baseApiUrl: String): AlarmApiService {
        return Retrofit.Builder()
                .baseUrl(baseApiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create(AlarmApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(application: Application): ChargingAlarmDb {
        return Room.databaseBuilder(application, ChargingAlarmDb::class.java, "logistics.db")
                .fallbackToDestructiveMigration()
                .build()

    }

    @Singleton
    @Provides
    @Named
    fun provideApiBaseUrl(application: Application) =
            application.getString(R.string.base_api_url)


    @Singleton
    @Provides
    fun provideFeProfileDao(db: ChargingAlarmDb): UserDetailDao {
        return db.userDetailDao()
    }

    @Singleton
    @Provides
    fun provideBatteryProfileDao(db: ChargingAlarmDb): BatteryProfileDao {
        return db.batteryProfileDao()
    }

    @Singleton
    @Provides
    fun providePeriodicBatteryUpdater(batteryProfileDao: BatteryProfileDao, appExecutors: AppExecutors): PeriodicBatteryUpdater{
        return PeriodicBatteryUpdater.initiate(batteryProfileDao = batteryProfileDao, appExecutors = appExecutors)
    }




}