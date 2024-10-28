package com.udacity.asteroidradar.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
@Parcelize @Entity
data class Asteroid(@PrimaryKey  val id: Long, val codename: String, val closeApproachDate: String,
                    val absoluteMagnitude: Double, val estimatedDiameter: Double,
                    val relativeVelocity: Double, val distanceFromEarth: Double,
                    val isPotentiallyHazardous: Boolean,
                    ) : Parcelable{
    fun b():String {return "codename $codename closeApproachDate $closeApproachDate click for more details "}
    fun isHDDescription():String {return if (isPotentiallyHazardous)  "the asteroid  is isPotentiallyHazardous " else "the asteroid  is Not Hazardous "}

}