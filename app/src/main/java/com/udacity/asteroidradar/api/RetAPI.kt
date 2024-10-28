package com.udacity.asteroidradar.api


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface RetAPI {
    companion object {
        fun getRetrofit(): RetAPI {
            var net: RetAPI? = null
            if (net == null) {
                var moshi: Moshi
                var retrofit: Retrofit
                moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                var client: OkHttpClient = OkHttpClient.Builder().callTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build()
                retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
                 //   .callAdapter(RxJava)
                net = retrofit.create(RetAPI::class.java)
            }
            return net!!
        }

    }

    @GET("/neo/rest/v1/feed")
  suspend  fun loadItems(
        @Query("start_date") sd: String,
        @Query("end_date") ed: String,
        @Query("api_key") id: String
    ) : String

    @GET("planetary/apod")
    fun loadIPic(@Query("api_key") id: String): Call<PictureOfDay>

}