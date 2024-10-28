package com.udacity.asteroidradar

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker

import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.NetworkUtils
import com.udacity.asteroidradar.api.RetAPI
import com.udacity.asteroidradar.datasource.database.MyDataBase
import org.json.JSONObject
import retrofit2.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SyncWorker(val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun doWork(): Result {

        val retrofit = RetAPI.getRetrofit()
        val database = MyDataBase.get(context)
        var start = ""
        var end = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            start = LocalDate.now()
                .format(DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT))
            end = LocalDate.now().minusDays(7)
                .format(DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT))
        } else {

        }
        try {
            database?.dao()?.insertAll(
                NetworkUtils().parseAsteroidsJsonResult(
                    JSONObject(
                        retrofit.loadItems(
                            end,
                            start,
                            Constants.KEY
                        )
                    )
                )
            )
            return  Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }

}
}