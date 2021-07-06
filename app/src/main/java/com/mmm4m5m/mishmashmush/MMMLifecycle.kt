package com.mmm4m5m.mishmashmush

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MMMLifecycle(lifecycle: Lifecycle) : LifecycleObserver {
    private val WORK_INTERVAL_SECS: Long = 5

    private lateinit var runnable: Runnable
    private var handler = Handler(Looper.getMainLooper())

    var timerSeconds: Long = 0 // seconds since last start

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        runnable = Runnable(::onRunTimer)
        handler.postDelayed(runnable, WORK_INTERVAL_SECS*1000)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        handler.removeCallbacks(runnable)
    }

    fun onRunTimer() {
        try {
            timerSeconds += WORK_INTERVAL_SECS
            //PRJTST?.Log({this}, "Working... $timerSeconds seconds") //??? todo pending
        } finally {
            handler.postDelayed(runnable, WORK_INTERVAL_SECS*1000)
        }
    }
}
