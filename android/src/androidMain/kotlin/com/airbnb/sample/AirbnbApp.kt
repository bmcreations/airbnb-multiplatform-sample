package com.airbnb.sample

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class AirbnbApp: Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}