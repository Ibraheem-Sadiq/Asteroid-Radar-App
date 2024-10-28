package com.udacity.asteroidradar.datasource

import android.content.Context
import android.os.Build
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.*

open class MyViewModel() : ViewModel() {
    private var repository: Repository? = null
    var asteroidList: MutableLiveData<List<Asteroid>> = MutableLiveData()
    var pic: MutableLiveData<PictureOfDay?> = MutableLiveData()
    var loading: MutableLiveData<Boolean> = MutableLiveData(false)
    fun loadLive() {
        viewModelScope.launch(Dispatchers.IO) {
            val value = repository?.asteroidList
            val picture = repository?.pic
            withContext(Dispatchers.Main) {
                asteroidList.value = value ?: listOf()
                pic.value=picture
                toggle()
            }
        }

    }

    fun refesh(internet:Boolean) {
        toggle()
        viewModelScope.launch(Dispatchers.IO) {
            if (internet)  repository?.updateDatabase()
            val picture = repository?.pic
            val value = repository?.asteroidList
            withContext(Dispatchers.Main){
                asteroidList.value = value ?: listOf()
                toggle()
                pic.value=picture

            }
        }

    }

    fun loadToday() {
        viewModelScope.launch(Dispatchers.IO) {
            val value = repository?.asteroidListforOnDay
            withContext(Dispatchers.Main) {
                asteroidList.value = value ?: listOf()
                toggle()
            }
        }

    }

    fun setAndInitialize(context: Context) {
        repository = Repository.build(context!!)

    }

    fun toggle() {
        loading.value = loading.value?.not()
    }
}
