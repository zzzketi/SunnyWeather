package com.sunnyweather.android

import android.app.Application
import android.content.Context

class SunnyWeatherApplication:Application() {


    companion object{
        const val TOKEN = "ua1F2GDAYmJzDfJN"
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}