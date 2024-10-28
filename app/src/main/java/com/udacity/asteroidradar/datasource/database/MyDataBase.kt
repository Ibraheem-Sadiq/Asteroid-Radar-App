package com.udacity.asteroidradar.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay

@Database(entities = [Asteroid::class,PictureOfDay::class], version = 1)
abstract class MyDataBase:RoomDatabase() {
    abstract fun dao(): DAO
    companion object{
        lateinit  var database: MyDataBase
        fun get( contect:Context): MyDataBase?{

            if (!Companion::database.isInitialized){
            database =    Room.databaseBuilder(contect,
                MyDataBase::class.java,"data")
                .fallbackToDestructiveMigration()
                .build()
            }
            return database
        }

    }
}