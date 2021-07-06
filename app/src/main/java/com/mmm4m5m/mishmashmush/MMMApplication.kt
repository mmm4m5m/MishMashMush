package com.mmm4m5m.mishmashmush

import android.app.Application
import timber.log.Timber

class MMMApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (PRJTST?.USE_Timber == true) Timber.plant(Timber.DebugTree())
    }
}
