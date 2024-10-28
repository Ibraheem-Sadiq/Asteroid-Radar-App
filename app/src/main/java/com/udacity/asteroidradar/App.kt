package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        var constraints =Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        request(constraints)
    }

    fun request(constraints: Constraints) {
       val worker = PeriodicWorkRequestBuilder<SyncWorker>(1,TimeUnit.DAYS)
            .setConstraints(constraints)
           .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("UniqDownloaderFOrTHisApp"
       ,ExistingPeriodicWorkPolicy.KEEP,worker)
    }
}