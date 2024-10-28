package com.udacity.asteroidradar.datasource

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NetworkUtils
import com.udacity.asteroidradar.api.RetAPI
import com.udacity.asteroidradar.datasource.database.MyDataBase
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Repository(var context: Context) {
    companion object {
        var repository: Repository? = null
        fun build(context: Context): Repository {
            if (repository == null) {
                repository = Repository(context)
            }
            return repository!!
        }
    }

    private var db: MyDataBase
    var asteroidList: List<Asteroid>?
    get() = db.dao().getItems()
    var asteroidListforOnDay: List<Asteroid>?
    get() {  return filterByDate(db.dao().getItems())}
    var pic: PictureOfDay?=null
    get() {return  db.dao().getPics()}
    private var net: RetAPI

    init {
        net = RetAPI.getRetrofit()
        db = MyDataBase.get(context)!!
        asteroidList = listOf()
        asteroidListforOnDay = listOf()
    }

    suspend fun downloadpicOfTheDay() {
        cashPic(net.loadIPic(Constants.KEY).await())
    }

    suspend fun downloadItems() {
        var start = ""
        var end = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            start = LocalDate.now()
                .format(DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT))
            end = LocalDate.now().minusDays(Constants.DEFAULT_END_DATE_DAYS.toLong())
                .format(DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT))
        } else {

        }


        var string = net.loadItems(end, start, Constants.KEY)
        var date: List<Asteroid> = listOf()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            date = NetworkUtils().parseAsteroidsJsonResult(JSONObject(string))
        }
        cashItems(date)
    }


    private fun cashPic(pic: PictureOfDay?) {
        if (pic != null)
            db.dao().insertPic(pic)
    }

    private fun cashItems(items: List<Asteroid>) {
        db.dao().insertAll(items)
    }

    suspend fun updateDatabase() {
       withContext(Dispatchers.IO) {
            launch { downloadpicOfTheDay() }
            launch { downloadItems() }
        }
    }



    fun filterByDate(list: List<Asteroid>?): List<Asteroid> {
        var newList: MutableList<Asteroid> = mutableListOf()
        if (list == null)
            return newList
        val start = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
                .format(DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT))
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        for (item in list) {
            if (item.closeApproachDate.equals(start))
                newList.add(item)
        }
        return newList
    }

}