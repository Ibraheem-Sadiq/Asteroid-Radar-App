package com.udacity.asteroidradar.datasource.database

import androidx.room.*
import androidx.room.Dao
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay

@Dao
interface DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPic(item:PictureOfDay)

    @Query("SELECT * from PictureOfDay")
    fun getPics(): PictureOfDay

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<Asteroid>)

    @Query("SELECT * from Asteroid")
    fun getItems():List<Asteroid>

    @Query("SELECT * from Asteroid  where closeApproachDate =:date")
    fun getTodayItems(date: String):List<Asteroid>
}