package com.mmm4m5m.mishmashmush

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MMMLifecycle(lifecycle: Lifecycle) : LifecycleObserver {
    private var handler = Handler(Looper.getMainLooper())

    lateinit var countDownListener: () -> Unit
    lateinit var countDownDelay: () -> Long
    private var countDownRunnable: Runnable? = null
    private var countDownRunning = false

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart() {
        PRJTST?.Log({this})
        if (countDownRunning) countDown()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() {
        PRJTST?.Log({this})
        if (countDownRunnable != null) countDownRunning = true
        countDownStop()
    }

    fun countDown() {
        PRJTST?.Log({this})
        if (countDownRunnable != null) return
        countDownRunnable = Runnable(::onCountDown)
        handler.postDelayed(countDownRunnable!!, countDownDelay())
    }

    fun countDownStop() {
        PRJTST?.Log({this})
        if (countDownRunnable == null) return
        handler.removeCallbacks(countDownRunnable!!)
        countDownRunnable = null
    }

    private fun onCountDown() {
        countDownListener.invoke()
        if (countDownRunnable == null) return
        handler.postDelayed(countDownRunnable!!, countDownDelay())
    }
}
